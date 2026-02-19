package eu.decentsoftware.holograms.nms.v1_17_R1;

import com.mojang.datafixers.util.Pair;
import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_17_R1.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


class EntityPacketsBuilder {

    private static final Constructor<?> destroyPacketConstructor = getTheOtherFuckingDestroyPacketConstructor();

    private static Constructor<?> getTheOtherFuckingDestroyPacketConstructor() {
        Constructor<?> constructor;
        try {
            // This constructor is available in "1.17" only.
            constructor = PacketPlayOutEntityDestroy.class.getConstructor(int.class);
        } catch (NoSuchMethodException | SecurityException e) {
            constructor = null;
        }
        return constructor;
    }

    private final List<Packet<?>> packets;

    private EntityPacketsBuilder() {
        this.packets = new ArrayList<>();
    }

    void sendTo(Player player) {
        for (Packet<?> packet : packets) {
            sendPacket(player, packet);
        }
    }

    EntityPacketsBuilder withSpawnEntityLivingOrObject(int entityId, EntityType type, DecentPosition position) {
        if (type.isAlive()) {
            return withSpawnEntityLiving(entityId, type, position);
        } else {
            return withSpawnEntity(entityId, type, position);
        }
    }

    EntityPacketsBuilder withSpawnEntity(int entityId, EntityType type, DecentPosition position) {
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(
                entityId,
                UUID.randomUUID(),
                position.getX(),
                position.getY(),
                position.getZ(),
                position.getYaw(),
                position.getPitch(),
                EntityTypeRegistry.findEntityTypes(type),
                type == EntityType.DROPPED_ITEM ? 1 : 0,
                Vec3D.a
        );

        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withSpawnEntityLiving(int entityId, EntityType type, DecentPosition position) {
        PacketDataSerializerWrapper serializer = PacketDataSerializerWrapper.getInstance();
        serializer.writeVarInt(entityId);
        serializer.writeUuid(UUID.randomUUID());
        serializer.writeByte(EntityTypeRegistry.getEntityTypeId(type));
        serializer.writeDouble(position.getX());
        serializer.writeDouble(position.getY());
        serializer.writeDouble(position.getZ());
        serializer.writeByte(MathHelper.d(position.getYaw() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(position.getPitch() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(position.getYaw() * 256.0F / 360.0F));
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeShort(0);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(serializer.getSerializer());

        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withEntityMetadata(int entityId, List<DataWatcher.Item<?>> watchableObjects) {
        PacketDataSerializerWrapper serializer = PacketDataSerializerWrapper.getInstance();
        serializer.writeVarInt(entityId);
        serializer.writeWatchableObjects(watchableObjects);

        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(serializer.getSerializer());

        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withHelmet(int entityId, ItemStack itemStack) {
        Pair<EnumItemSlot, net.minecraft.world.item.ItemStack> equipmentPair = new Pair<>(
                CraftEquipmentSlot.getNMS(EquipmentSlot.HEAD),
                itemStackToNms(itemStack)
        );
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(
                entityId,
                Collections.singletonList(equipmentPair)
        );
        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withTeleportEntity(int entityId, DecentPosition position) {
        PacketDataSerializerWrapper serializer = PacketDataSerializerWrapper.getInstance();
        serializer.writeVarInt(entityId);
        serializer.writeDouble(position.getX());
        serializer.writeDouble(position.getY());
        serializer.writeDouble(position.getZ());
        serializer.writeByte((byte) ((int) (position.getYaw() * 256.0F / 360.0F)));
        serializer.writeByte((byte) ((int) (position.getPitch() * 256.0F / 360.0F)));
        serializer.writeBoolean(false); // onGround

        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(serializer.getSerializer());
        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withPassenger(int entityId, int passenger) {
        return updatePassenger(entityId, passenger);
    }

    EntityPacketsBuilder withRemovePassenger(int entityId) {
        return updatePassenger(entityId, -1);
    }

    private EntityPacketsBuilder updatePassenger(int entityId, int... passengers) {
        PacketDataSerializerWrapper serializer = PacketDataSerializerWrapper.getInstance();
        serializer.writeVarInt(entityId);
        serializer.writeIntArray(passengers);

        PacketPlayOutMount packet = new PacketPlayOutMount(serializer.getSerializer());
        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withRemoveEntity(int entityId) {
        PacketPlayOutEntityDestroy packet;
        try {
            packet = new PacketPlayOutEntityDestroy(entityId);
        } catch (NoSuchMethodError e) {
            // So it seems that ONLY "1.17" has this constructor.
            // The NMS version is not even different from "1.17.1", which has the same constructor as all the other versions.
            // Basically, one of the "v1_17_R1" versions is different from the other.
            // Working with minecraft is so much fun.
            try {
                if (destroyPacketConstructor == null) {
                    // what do you want from me then? is there a third constructor?
                    throw new DecentHologramsNmsException("Failed to find the constructor for PacketPlayOutEntityDestroy.");
                }
                packet = (PacketPlayOutEntityDestroy) destroyPacketConstructor.newInstance(entityId);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                throw new DecentHologramsNmsException("Failed to remove entity with id " + entityId + " using the 1.17 constructor.", ex);
            }
        }
        packets.add(packet);
        return this;
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().b.sendPacket(packet);
    }

    private net.minecraft.world.item.ItemStack itemStackToNms(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    static EntityPacketsBuilder create() {
        return new EntityPacketsBuilder();
    }

}

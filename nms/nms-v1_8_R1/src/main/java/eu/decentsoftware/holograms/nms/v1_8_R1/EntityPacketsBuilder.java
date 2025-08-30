package eu.decentsoftware.holograms.nms.v1_8_R1;

import eu.decentsoftware.holograms.shared.DecentPosition;
import eu.decentsoftware.holograms.shared.reflect.ReflectField;
import net.minecraft.server.v1_8_R1.DataWatcher;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.Packet;
import net.minecraft.server.v1_8_R1.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R1.WatchableObject;
import org.bukkit.craftbukkit.v1_8_R1.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

class EntityPacketsBuilder {

    private static final ReflectField<DataWatcher> SPAWN_ENTITY_LIVING_PACKET_DATA_WATCHER_FIELD = new ReflectField<>(
            PacketPlayOutSpawnEntityLiving.class, "l");
    private final List<Packet> packets;
    private final DataWatcher emptyDataWatcher;

    private EntityPacketsBuilder() {
        this.packets = new ArrayList<>();
        this.emptyDataWatcher = DataWatcherBuilder.create()
                .withEmptyEntityProperties()
                .toDataWatcher();
    }

    void sendTo(Player player) {
        for (Packet packet : packets) {
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
        PacketDataSerializerWrapper serializer = prepareSpawnEntityData(entityId, type, position);
        serializer.writeInt(type == EntityType.DROPPED_ITEM ? 1 : 0);
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeShort(0);

        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
        serializer.writeToPacket(packet);

        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withSpawnEntityLiving(int entityId, EntityType type, DecentPosition position) {
        return withSpawnEntityLiving(entityId, type, position, emptyDataWatcher);
    }

    EntityPacketsBuilder withSpawnEntityLiving(int entityId, EntityType type, DecentPosition position, DataWatcher dataWatcher) {
        PacketDataSerializerWrapper serializer = prepareSpawnEntityData(entityId, type, position);
        serializer.writeByte(MathHelper.d(position.getYaw() * 256.0F / 360.0F));
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeShort(0);
        serializer.writeByte(127);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
        serializer.writeToPacket(packet);
        SPAWN_ENTITY_LIVING_PACKET_DATA_WATCHER_FIELD.set(packet, dataWatcher);

        packets.add(packet);
        return this;
    }

    private PacketDataSerializerWrapper prepareSpawnEntityData(int entityId, EntityType type, DecentPosition position) {
        PacketDataSerializerWrapper serializer = PacketDataSerializerWrapper.getInstance();
        serializer.writeVarInt(entityId);
        serializer.writeByte(EntityTypeRegistry.getEntityTypeId(type));
        serializer.writeInt(MathHelper.floor(position.getX() * 32));
        serializer.writeInt(MathHelper.floor(position.getY() * 32));
        serializer.writeInt(MathHelper.floor(position.getZ() * 32));
        serializer.writeByte(MathHelper.d(position.getYaw() * 256.0F / 360.0F));
        serializer.writeByte(MathHelper.d(position.getPitch() * 256.0F / 360.0F));
        return serializer;
    }

    EntityPacketsBuilder withEntityMetadata(int entityId, List<WatchableObject> watchableObjects) {
        PacketDataSerializerWrapper serializer = PacketDataSerializerWrapper.getInstance();
        serializer.writeVarInt(entityId);
        serializer.writeWatchableObjects(watchableObjects);

        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
        serializer.writeToPacket(packet);

        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withHelmet(int entityId, ItemStack itemStack) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(
                entityId,
                CraftEquipmentSlot.getSlotIndex(EquipmentSlot.HEAD),
                itemStackToNms(itemStack)
        );
        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withTeleportEntity(int entityId, DecentPosition position) {
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(
                entityId,
                MathHelper.floor(position.getX() * 32.0),
                MathHelper.floor(position.getY() * 32.0),
                MathHelper.floor(position.getZ() * 32.0),
                (byte) ((int) (position.getYaw() * 256.0F / 360.0F)),
                (byte) ((int) (position.getPitch() * 256.0F / 360.0F)),
                false // onGround
        );
        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withEntityHeadLook(int entityId, float yaw) {
        PacketDataSerializerWrapper serializer = PacketDataSerializerWrapper.getInstance();
        serializer.writeVarInt(entityId);
        serializer.writeByte((byte) ((int) (yaw * 256.0F / 360.0F)));

        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation();
        serializer.writeToPacket(packet);

        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder attachEntity(int attachedEntityId, int vehicleEntityId) {
        return updatePassenger(attachedEntityId, vehicleEntityId);
    }

    EntityPacketsBuilder unattachEntity(int attachedEntityId) {
        return updatePassenger(attachedEntityId, -1);
    }

    private EntityPacketsBuilder updatePassenger(int attachedEntityId, int vehicleEntityId) {
        PacketDataSerializerWrapper serializer = PacketDataSerializerWrapper.getInstance();
        serializer.writeInt(attachedEntityId);
        serializer.writeInt(vehicleEntityId);
        serializer.writeByte(0); // Leash

        PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity();
        serializer.writeToPacket(packet);

        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withRemoveEntity(int entityId) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
        packets.add(packet);
        return this;
    }

    private void sendPacket(Player player, Packet packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private net.minecraft.server.v1_8_R1.ItemStack itemStackToNms(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    static EntityPacketsBuilder create() {
        return new EntityPacketsBuilder();
    }

}

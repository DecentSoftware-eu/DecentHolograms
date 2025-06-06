package eu.decentsoftware.holograms.nms.v1_19_R3;

import com.mojang.datafixers.util.Pair;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_19_R3.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class EntityPacketsBuilder {

    private final List<Packet<?>> packets;

    private EntityPacketsBuilder() {
        this.packets = new ArrayList<>();
    }

    void sendTo(Player player) {
        for (Packet<?> packet : packets) {
            sendPacket(player, packet);
        }
    }

    EntityPacketsBuilder withSpawnEntity(int entityId, EntityType type, DecentPosition position) {
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(
                entityId,
                UUID.randomUUID(),
                position.getX(),
                position.getY(),
                position.getZ(),
                position.getPitch(),
                position.getYaw(),
                EntityTypeRegistry.findEntityTypes(type),
                type == EntityType.DROPPED_ITEM ? 1 : 0,
                Vec3D.b,
                position.getYaw()
        );
        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withEntityMetadata(int entityId, List<DataWatcher.Item<?>> items) {
        List<DataWatcher.b<?>> bs = new ArrayList<>();
        for (DataWatcher.Item<?> item : items) {
            bs.add(item.e());
        }

        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entityId, bs);
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
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
        packets.add(packet);
        return this;
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().b.a(packet);
    }

    private net.minecraft.world.item.ItemStack itemStackToNms(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    static EntityPacketsBuilder create() {
        return new EntityPacketsBuilder();
    }

}

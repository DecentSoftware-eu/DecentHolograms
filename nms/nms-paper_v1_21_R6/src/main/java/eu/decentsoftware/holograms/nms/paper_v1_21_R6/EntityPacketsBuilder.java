package eu.decentsoftware.holograms.nms.paper_v1_21_R6;

import com.mojang.datafixers.util.Pair;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
        ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(
                entityId,
                UUID.randomUUID(),
                position.getX(),
                position.getY(),
                position.getZ(),
                position.getPitch(),
                position.getYaw(),
                EntityTypeRegistry.findEntityTypes(type),
                type == EntityType.ITEM ? 1 : 0,
                Vec3.ZERO,
                position.getYaw()
        );

        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withEntityMetadata(int entityId, List<SynchedEntityData.DataItem<?>> items) {
        List<SynchedEntityData.DataValue<?>> cs = new ArrayList<>();
        for (SynchedEntityData.DataItem<?> item : items) {
            cs.add(item.value());
        }

        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(entityId, cs);
        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withHelmet(int entityId, ItemStack itemStack) {
        Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack> equipmentPair = new Pair<>(
                CraftEquipmentSlot.getNMS(EquipmentSlot.HEAD),
                itemStackToNms(itemStack)
        );
        ClientboundSetEquipmentPacket packet = new ClientboundSetEquipmentPacket(
                entityId,
                Collections.singletonList(equipmentPair)
        );
        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withTeleportEntity(int entityId, DecentPosition position) {
        Vec3 locationVec3 = new Vec3(position.getX(), position.getY(), position.getZ());
        Vec3 zeroVec3 = new Vec3(0, 0, 0);
        ClientboundTeleportEntityPacket packet = new ClientboundTeleportEntityPacket(
                entityId,
                new PositionMoveRotation(locationVec3, zeroVec3, position.getYaw(), position.getPitch()),
                Set.of(),
                false
        );
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
        FriendlyByteBufWrapper serializer = FriendlyByteBufWrapper.getInstance();
        serializer.writeVarInt(entityId);
        serializer.writeIntArray(passengers);

        ClientboundSetPassengersPacket packet = ClientboundSetPassengersPacket.STREAM_CODEC.decode(serializer.getSerializer());
        packets.add(packet);
        return this;
    }

    EntityPacketsBuilder withRemoveEntity(int entityId) {
        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(entityId);
        packets.add(packet);
        return this;
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    private net.minecraft.world.item.ItemStack itemStackToNms(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    static EntityPacketsBuilder create() {
        return new EntityPacketsBuilder();
    }

}

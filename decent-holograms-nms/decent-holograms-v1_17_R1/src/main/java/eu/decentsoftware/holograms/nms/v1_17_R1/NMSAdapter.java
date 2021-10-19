package eu.decentsoftware.holograms.nms.v1_17_R1;

import com.mojang.datafixers.util.Pair;
import eu.decentsoftware.holograms.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.utils.reflect.ReflectField;
import io.netty.buffer.Unpooled;
import lombok.NonNull;
import net.minecraft.core.IRegistry;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SuppressWarnings("unused")
public class NMSAdapter implements eu.decentsoftware.holograms.api.nms.NMSAdapter {

	private static final Random random;
	private static final DataWatcherObject<Optional<IChatBaseComponent>> customName;
	private static final DataWatcherObject<Boolean> customNameVisible;
	private static final DataWatcherObject<Byte> entityData;
	private static final DataWatcherObject<Byte> armorStandData;
	private static final DataWatcherObject<net.minecraft.world.item.ItemStack> item;

	static {
		random = new Random();
		entityData = new ReflectField<DataWatcherObject<Byte>>(Entity.class, "Z").getValue(null);
		customName = new ReflectField<DataWatcherObject<Optional<IChatBaseComponent>>>(Entity.class, "aJ").getValue(null);
		customNameVisible = new ReflectField<DataWatcherObject<Boolean>>(Entity.class, "aK").getValue(null);
		armorStandData = new ReflectField<DataWatcherObject<Byte>>(EntityArmorStand.class, "bG").getValue(null);
		item = new ReflectField<DataWatcherObject<net.minecraft.world.item.ItemStack>>(EntityItem.class, "c").getValue(null);
	}

	/*
	 *	General Methods
	 */

	@Override
	public void sendPacket(@NonNull final Player player, @NonNull final Object packet) {
		if (!(packet instanceof Packet<?>)) return;
		((CraftPlayer) player).getHandle().b.sendPacket((Packet<?>) packet);
	}

	@Override
	public int getEntityTypeId(@NonNull final EntityType type) {
		if (!DecentEntityType.isAllowed(type)) return -1;
		Optional<EntityTypes<?>> entityTypes = EntityTypes.a(type.getKey().getKey().toLowerCase());
		return entityTypes.map(IRegistry.Y::getId).orElse(-1);
	}

	@Override
	public float getEntityHeigth(@NonNull final EntityType type) {
		if (!DecentEntityType.isAllowed(type)) return 0.0f;
		Optional<EntityTypes<?>> entityTypes = EntityTypes.a(type.getKey().getKey().toLowerCase());
		return entityTypes.map(types -> types.m().b).orElse(0.0f);
	}

	/*
	 *	Fake Entity Methods
	 */

	@Override
	public void showFakeEntity(@NonNull final Player player, @NonNull final Location location, final int entityId, final int entityTypeId) {
		PacketPlayOutSpawnEntity spawn = new PacketPlayOutSpawnEntity(
				entityId,
				MathHelper.a(random),
				location.getX(),
				location.getY(),
				location.getZ(),
				location.getYaw(),
				location.getPitch(),
				IRegistry.Y.fromId(entityTypeId),
				0,
				Vec3D.a
		);
		sendPacket(player, spawn);
		teleportFakeEntity(player, location, entityId);
	}

	@Override
	public void showFakeItem(@NonNull final Player player, @NonNull final Location location, final int entityId, @NonNull final ItemStack itemStack) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.register(item, CraftItemStack.asNMSCopy(itemStack));
		showFakeEntity(player, location, entityId, 41);
		sendPacket(player, new PacketPlayOutEntityMetadata(entityId, dataWatcher, true));
		teleportFakeEntity(player, location, entityId);
	}

	@Override
	public void showFakeEntityLiving(@NonNull final Player player, @NonNull final Location location, final int entityId, final int entityTypeId) {
		PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.buffer());
		packetDataSerializer.d(entityId);
		packetDataSerializer.a(MathHelper.a(random));
		packetDataSerializer.d(entityTypeId);
		packetDataSerializer.writeDouble(location.getX());
		packetDataSerializer.writeDouble(location.getY());
		packetDataSerializer.writeDouble(location.getZ());
		packetDataSerializer.writeByte((byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		packetDataSerializer.writeByte((byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
		packetDataSerializer.writeByte((byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		packetDataSerializer.writeShort(0);
		packetDataSerializer.writeShort(0);
		packetDataSerializer.writeShort(0);

		PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(packetDataSerializer);
		sendPacket(player, spawn);
	}

	@Override
	public void showFakeArmorStand(@NonNull final Player player, @NonNull final Location location, final int entityId, final boolean small) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.register(entityData, (byte) 0x20);
		dataWatcher.register(armorStandData, (byte) (small ? 0x19 : 0x18));
		showFakeEntityLiving(player, location, entityId, 1);
		sendPacket(player, new PacketPlayOutEntityMetadata(entityId, dataWatcher, true));
	}

	@Override
	public void updateFakeEntityName(@NonNull final Player player, final int entityId, @NonNull final String name) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.register(customName, Optional.ofNullable(CraftChatMessage.fromStringOrNull(name)));
		dataWatcher.register(customNameVisible, !ChatColor.stripColor(name).isEmpty());
		sendPacket(player, new PacketPlayOutEntityMetadata(entityId, dataWatcher, true));
	}

	@Override
	public void helmetFakeEntity(@NonNull final Player player, final int entityId, @NonNull final ItemStack itemStack) {
		List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> items = new ArrayList<>();
		items.add(Pair.of(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
		sendPacket(player, new PacketPlayOutEntityEquipment(entityId, items));
	}

	@Override
	public void teleportFakeEntity(@NonNull final Player player, @NonNull final Location location, final int entityId) {
		PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.buffer());
		packetDataSerializer.d(entityId);
		packetDataSerializer.writeDouble(location.getX());
		packetDataSerializer.writeDouble(location.getY());
		packetDataSerializer.writeDouble(location.getZ());
		packetDataSerializer.writeByte((byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		packetDataSerializer.writeByte((byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
		packetDataSerializer.writeBoolean(true);

		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(packetDataSerializer);
		sendPacket(player, packet);
	}

	@Override
	public void attachFakeEnity(@NonNull final Player player, final int vehicleId, final int entityId) {
		PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.buffer());
		packetDataSerializer.d(vehicleId);
		packetDataSerializer.a(new int[] {entityId});
		PacketPlayOutMount mount = new PacketPlayOutMount(packetDataSerializer);
		sendPacket(player, mount);
	}

	@Override
	public void hideFakeEntity(@NonNull final Player player, final int entityId) {
		try {
			for (Constructor<?> c : PacketPlayOutEntityDestroy.class.getConstructors()) {
				if (c.getParameterTypes()[0].equals(int[].class)) {
					sendPacket(player, c.newInstance(new int[] {entityId, entityId + 1}));
				} else if (c.getParameterTypes()[0].equals(int.class)) {
					sendPacket(player, c.newInstance(entityId));
					sendPacket(player, c.newInstance(entityId + 1));
				}
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}

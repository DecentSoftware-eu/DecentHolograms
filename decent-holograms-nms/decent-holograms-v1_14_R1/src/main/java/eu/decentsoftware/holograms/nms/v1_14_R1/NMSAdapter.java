package eu.decentsoftware.holograms.nms.v1_14_R1;

import eu.decentsoftware.holograms.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.utils.reflect.ReflectField;
import eu.decentsoftware.holograms.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.utils.reflect.ReflectionUtil;
import lombok.NonNull;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Random;

@SuppressWarnings("unused")
public class NMSAdapter implements eu.decentsoftware.holograms.api.nms.NMSAdapter {

	private static final Random random;
	private static final DataWatcherObject<Optional<IChatBaseComponent>> customName;
	private static final DataWatcherObject<Boolean> customNameVisible;
	private static final DataWatcherObject<Byte> entityData;
	private static final DataWatcherObject<Byte> armorStandData;
	private static final DataWatcherObject<ItemStack> item;
	private static ReflectMethod ENTITY_SIZE_GETTER = null;

	static {
		random = new Random();
		entityData = new ReflectField<DataWatcherObject<Byte>>(Entity.class, "W").getValue(null);
		customName = new ReflectField<DataWatcherObject<Optional<IChatBaseComponent>>>(Entity.class, "az").getValue(null);
		customNameVisible = new ReflectField<DataWatcherObject<Boolean>>(Entity.class, "aA").getValue(null);
		armorStandData = new ReflectField<DataWatcherObject<Byte>>(EntityArmorStand.class, "b").getValue(null);
		item = new ReflectField<DataWatcherObject<ItemStack>>(EntityItem.class, "ITEM").getValue(null);

		for (Method method : EntityTypes.class.getMethods()) {
			if (method.getReturnType().getName().contains("EntitySize")) {
				ENTITY_SIZE_GETTER = new ReflectMethod(EntityTypes.class, method.getName());
			}
		}

	}

	/*
	 *	General Methods
	 */

	@Override
	public void sendPacket(@NonNull final Player player, @NonNull final Object packet) {
		if (!(packet instanceof Packet<?>)) return;
		((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
	}

	@Override
	public int getEntityTypeId(@NonNull final EntityType type) {
		if (!DecentEntityType.isAllowed(type)) return -1;
		Optional<EntityTypes<?>> entityTypes = EntityTypes.a(type.getKey().getKey().toLowerCase());
		return entityTypes.map(IRegistry.ENTITY_TYPE::a).orElse(-1);
	}

	@Override
	public float getEntityHeigth(final @NonNull EntityType type) {
		if (!DecentEntityType.isAllowed(type)) return -1;
		Optional<EntityTypes<?>> entityTypes = EntityTypes.a(type.getKey().getKey().toLowerCase());
		if (ENTITY_SIZE_GETTER != null) {
			return entityTypes.map(types -> ((EntitySize) ENTITY_SIZE_GETTER.invoke(types)).height).orElse(0.0f);
		}
		return 0.0f;
	}

	/*
	 *	Fake Entity Methods
	 */

	@Override
	public void showFakeEntity(@NonNull final Player player, @NonNull final Location location, final int entityId, final int entityTypeId) {
		PacketPlayOutSpawnEntity spawn = new PacketPlayOutSpawnEntity();
		ReflectionUtil.setFieldValue(spawn, "a", entityId);
		ReflectionUtil.setFieldValue(spawn, "b", MathHelper.a(random));
		ReflectionUtil.setFieldValue(spawn, "c", location.getX());
		ReflectionUtil.setFieldValue(spawn, "d", location.getY());
		ReflectionUtil.setFieldValue(spawn, "e", location.getZ());
		ReflectionUtil.setFieldValue(spawn, "i", MathHelper.d(location.getPitch() * 256.0F / 360.0F));
		ReflectionUtil.setFieldValue(spawn, "j", MathHelper.d(location.getYaw() * 256.0F / 360.0F));
		ReflectionUtil.setFieldValue(spawn, "k", (EntityTypes<?>) IRegistry.ENTITY_TYPE.fromId(entityTypeId));
		sendPacket(player, spawn);
	}

	@Override
	public void showFakeItem(@NonNull final Player player, @NonNull final Location location, final int entityId, @NonNull final org.bukkit.inventory.ItemStack itemStack) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.register(item, CraftItemStack.asNMSCopy(itemStack));
		showFakeEntity(player, location, entityId, 34);
		sendPacket(player, new PacketPlayOutEntityMetadata(entityId, dataWatcher, true));
		teleportFakeEntity(player, location, entityId);
	}

	private void showFakeEntityLiving(@NonNull Player player, @NonNull Location location, int entityId, int entityTypeId, @NonNull DataWatcher dataWatcher) {
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
		ReflectionUtil.setFieldValue(packet, "a", entityId);
		ReflectionUtil.setFieldValue(packet, "b", MathHelper.a(random));
		ReflectionUtil.setFieldValue(packet, "c", entityTypeId);
		ReflectionUtil.setFieldValue(packet, "d", location.getX());
		ReflectionUtil.setFieldValue(packet, "e", location.getY());
		ReflectionUtil.setFieldValue(packet, "f", location.getZ());
		ReflectionUtil.setFieldValue(packet, "j", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(packet, "k", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(packet, "l", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(packet, "m", dataWatcher);
		sendPacket(player, packet);
	}

	@Override
	public void showFakeEntityLiving(@NonNull final Player player, @NonNull final Location location, final int entityId, final int entityTypeId) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.register(entityData, (byte) 0);
		showFakeEntityLiving(player, location, entityId, entityTypeId, dataWatcher);
	}

	@Override
	public void showFakeArmorStand(@NonNull final Player player, @NonNull final Location location, final int entityId, final boolean small) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.register(entityData, (byte) 0x20); // Invisible
		dataWatcher.register(armorStandData, (byte) (small ? 0x19 : 0x18)); // Marker, Remove Base Plate, Small
		showFakeEntityLiving(player, location, entityId, 1, dataWatcher);
	}

	@Override
	public void updateFakeEntityName(@NonNull final Player player, final int entityId, @NonNull final String name) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.register(customName, Optional.ofNullable(CraftChatMessage.fromStringOrNull(name))); // Custom Name
		dataWatcher.register(customNameVisible, !ChatColor.stripColor(name).isEmpty()); // Custom Name Visible
		sendPacket(player, new PacketPlayOutEntityMetadata(entityId, dataWatcher, true));
	}

	@Override
	public void teleportFakeEntity(@NonNull final Player player, @NonNull final Location location, final int entityId) {
		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
		ReflectionUtil.setFieldValue(packet, "a", entityId);
		ReflectionUtil.setFieldValue(packet, "b", location.getX());
		ReflectionUtil.setFieldValue(packet, "c", location.getY());
		ReflectionUtil.setFieldValue(packet, "d", location.getZ());
		ReflectionUtil.setFieldValue(packet, "e", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(packet, "f", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(packet, "g", true);
		sendPacket(player, packet);
	}

	@Override
	public void hideFakeEntity(@NonNull final Player player, final int entityId) {
		sendPacket(player, new PacketPlayOutEntityDestroy(entityId, entityId + 1));
	}

	@Override
	public void helmetFakeEntity(@NonNull final Player player, final int entityId, @NonNull final org.bukkit.inventory.ItemStack itemStack) {
		sendPacket(player, new PacketPlayOutEntityEquipment(entityId, EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack)));
	}

	@Override
	public void attachFakeEnity(@NonNull final Player player, final int vehicleId, final int entityId) {
		PacketPlayOutMount mount = new PacketPlayOutMount();
		ReflectionUtil.setFieldValue(mount, "a", vehicleId);
		ReflectionUtil.setFieldValue(mount, "b", new int[] {entityId});
		sendPacket(player, mount);
	}
}

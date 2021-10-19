package eu.decentsoftware.holograms.nms.v1_11_R1;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.utils.objects.Pair;
import eu.decentsoftware.holograms.utils.reflect.ReflectField;
import eu.decentsoftware.holograms.utils.reflect.ReflectionUtil;
import lombok.NonNull;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Random;

@SuppressWarnings("unused")
public class NMSAdapter implements eu.decentsoftware.holograms.api.nms.NMSAdapter {

	private static final Random random;
	private static final DataWatcherObject<String> customName;
	private static final DataWatcherObject<Boolean> customNameVisible;
	private static final DataWatcherObject<Byte> entityData;
	private static final DataWatcherObject<Byte> armorStandData;
	private static final DataWatcherObject<ItemStack> item;

	/**
	 * Allowed entity line entity types.
	 */
	private static final Map<EntityType, Pair<Integer, Float>> mapEntityTypes = Maps.newHashMap();

	static {
		random = new Random();
		entityData = new ReflectField<DataWatcherObject<Byte>>(Entity.class, "Z").getValue(null);
		customName = new ReflectField<DataWatcherObject<String>>(Entity.class, "aA").getValue(null);
		customNameVisible = new ReflectField<DataWatcherObject<Boolean>>(Entity.class, "aB").getValue(null);
		armorStandData = new ReflectField<DataWatcherObject<Byte>>(EntityArmorStand.class, "a").getValue(null);
		item = new ReflectField<DataWatcherObject<ItemStack>>(EntityItem.class, "c").getValue(null);

		// Entities
		mapEntityTypes.put(EntityType.ELDER_GUARDIAN, new Pair<>(4, 1.8f));
		mapEntityTypes.put(EntityType.WITHER_SKELETON, new Pair<>(5, 1.8f));
		mapEntityTypes.put(EntityType.STRAY, new Pair<>(6, 1.8f));
		mapEntityTypes.put(EntityType.HUSK, new Pair<>(23, 1.8f));
		mapEntityTypes.put(EntityType.ZOMBIE_VILLAGER, new Pair<>(27, 1.8f));
		mapEntityTypes.put(EntityType.SKELETON_HORSE, new Pair<>(28, 1.8f));
		mapEntityTypes.put(EntityType.ZOMBIE_HORSE, new Pair<>(29, 1.8f));
		mapEntityTypes.put(EntityType.DONKEY, new Pair<>(31, 1.8f));
		mapEntityTypes.put(EntityType.MULE, new Pair<>(32, 1.8f));
		mapEntityTypes.put(EntityType.EVOKER, new Pair<>(34, 1.8f));
		mapEntityTypes.put(EntityType.VEX, new Pair<>(35, 1.8f));
		mapEntityTypes.put(EntityType.VINDICATOR, new Pair<>(36, 1.8f));
		mapEntityTypes.put(EntityType.CREEPER, new Pair<>(50, 1.8f));
		mapEntityTypes.put(EntityType.SKELETON, new Pair<>(51, 1.8f));
		mapEntityTypes.put(EntityType.SPIDER, new Pair<>(52, 0.9f));
		mapEntityTypes.put(EntityType.ZOMBIE, new Pair<>(54, 1.8f));
		mapEntityTypes.put(EntityType.SLIME, new Pair<>(55, 0.51000005f));
		mapEntityTypes.put(EntityType.PIG_ZOMBIE, new Pair<>(57, 1.8f));
		mapEntityTypes.put(EntityType.ENDERMAN, new Pair<>(58, 2.9f));
		mapEntityTypes.put(EntityType.CAVE_SPIDER, new Pair<>(59, 0.5f));
		mapEntityTypes.put(EntityType.SILVERFISH, new Pair<>(60, 0.3f));
		mapEntityTypes.put(EntityType.BLAZE, new Pair<>(61, 1.8f));
		mapEntityTypes.put(EntityType.MAGMA_CUBE, new Pair<>(62, 0.51000005f));
		mapEntityTypes.put(EntityType.BAT, new Pair<>(65, 0.9f));
		mapEntityTypes.put(EntityType.WITCH, new Pair<>(66, 1.8f));
		mapEntityTypes.put(EntityType.ENDERMITE, new Pair<>(67, 0.3f));
		mapEntityTypes.put(EntityType.GUARDIAN, new Pair<>(68, 0.85f));
		mapEntityTypes.put(EntityType.PIG, new Pair<>(90, 0.9f));
		mapEntityTypes.put(EntityType.SHEEP, new Pair<>(91, 1.3f));
		mapEntityTypes.put(EntityType.COW, new Pair<>(92, 1.3f));
		mapEntityTypes.put(EntityType.CHICKEN, new Pair<>(93, 0.7f));
		mapEntityTypes.put(EntityType.SQUID, new Pair<>(94, 0.95f));
		mapEntityTypes.put(EntityType.WOLF, new Pair<>(95, 0.8f));
		mapEntityTypes.put(EntityType.MUSHROOM_COW, new Pair<>(96, 1.3f));
		mapEntityTypes.put(EntityType.SNOWMAN, new Pair<>(97, 1.9f));
		mapEntityTypes.put(EntityType.OCELOT, new Pair<>(98, 0.7f));
		mapEntityTypes.put(EntityType.IRON_GOLEM, new Pair<>(99, 2.9f));
		mapEntityTypes.put(EntityType.HORSE, new Pair<>(100, 1.6f));
		mapEntityTypes.put(EntityType.RABBIT, new Pair<>(101, 0.7f));
		mapEntityTypes.put(EntityType.POLAR_BEAR, new Pair<>(102, 1.8f));
		mapEntityTypes.put(EntityType.LLAMA, new Pair<>(103, 1.8f));
		mapEntityTypes.put(EntityType.VILLAGER, new Pair<>(120, 1.8f));

		// Objects
		mapEntityTypes.put(EntityType.ENDER_CRYSTAL, new Pair<>(51, 2.0f));
		mapEntityTypes.put(EntityType.ARROW, new Pair<>(60, 2.0f));
		mapEntityTypes.put(EntityType.SNOWBALL, new Pair<>(61, 0.25f));
		mapEntityTypes.put(EntityType.EGG, new Pair<>(62, 0.25f));
		mapEntityTypes.put(EntityType.FIREBALL, new Pair<>(63, 1.0f));
		mapEntityTypes.put(EntityType.SMALL_FIREBALL, new Pair<>(64, 0.3125f));
		mapEntityTypes.put(EntityType.ENDER_PEARL, new Pair<>(65, 0.25f));
		mapEntityTypes.put(EntityType.ENDER_SIGNAL, new Pair<>(72, 0.25f));
		mapEntityTypes.put(EntityType.FIREWORK, new Pair<>(76, 2.0f));
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
	public int getEntityTypeId(final @NonNull EntityType type) {
		if (mapEntityTypes.containsKey(type)) {
			return mapEntityTypes.get(type).getKey();
		}
		return -1;
	}

	@Override
	public float getEntityHeigth(final @NonNull EntityType type) {
		if (mapEntityTypes.containsKey(type)) {
			return mapEntityTypes.get(type).getValue();
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
		ReflectionUtil.setFieldValue(spawn, "k", entityTypeId);
		sendPacket(player, spawn);
	}

	@Override
	public void showFakeItem(@NonNull final Player player, @NonNull final Location location, final int entityId, @NonNull final org.bukkit.inventory.ItemStack itemStack) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.register(item, CraftItemStack.asNMSCopy(itemStack));
		showFakeEntity(player, location, entityId, 2);
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
		showFakeEntityLiving(player, location, entityId, 30, dataWatcher);
	}

	@Override
	public void updateFakeEntityName(@NonNull final Player player, final int entityId, @NonNull final String name) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.register(customName, name); // Custom Name
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

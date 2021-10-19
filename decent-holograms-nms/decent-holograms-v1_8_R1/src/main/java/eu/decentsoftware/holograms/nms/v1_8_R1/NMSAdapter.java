package eu.decentsoftware.holograms.nms.v1_8_R1;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.utils.objects.Pair;
import eu.decentsoftware.holograms.utils.reflect.ReflectionUtil;
import lombok.NonNull;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@SuppressWarnings("unused")
public class NMSAdapter implements eu.decentsoftware.holograms.api.nms.NMSAdapter {

	/**
	 * Allowed entity line entity types.
	 */
	private static final Map<EntityType, Pair<Integer, Float>> mapEntityTypes = Maps.newHashMap();

	static {
		// Entities
		mapEntityTypes.put(EntityType.CREEPER, new Pair<>(50, 1.8f));
		mapEntityTypes.put(EntityType.SKELETON, new Pair<>(51, 1.95f));
		mapEntityTypes.put(EntityType.SPIDER, new Pair<>(52, 0.9f));
		mapEntityTypes.put(EntityType.ZOMBIE, new Pair<>(54, 1.8f));
		mapEntityTypes.put(EntityType.SLIME, new Pair<>(55, 0.51000005f));
		mapEntityTypes.put(EntityType.PIG_ZOMBIE, new Pair<>(57, 1.8f));
		mapEntityTypes.put(EntityType.ENDERMAN, new Pair<>(58, 2.9f));
		mapEntityTypes.put(EntityType.CAVE_SPIDER, new Pair<>(59, 0.5f));
		mapEntityTypes.put(EntityType.SILVERFISH, new Pair<>(60, 0.3f));
		mapEntityTypes.put(EntityType.BLAZE, new Pair<>(61, 1.8f));
		mapEntityTypes.put(EntityType.MAGMA_CUBE, new Pair<>(62, 0.51000005f));
		mapEntityTypes.put(EntityType.WITHER, new Pair<>(64, 3.5f));
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
		mapEntityTypes.put(EntityType.OCELOT, new Pair<>(98, 0.8f));
		mapEntityTypes.put(EntityType.IRON_GOLEM, new Pair<>(99, 2.9f));
		mapEntityTypes.put(EntityType.HORSE, new Pair<>(100, 1.6f));
		mapEntityTypes.put(EntityType.RABBIT, new Pair<>(101, 0.7f));
		mapEntityTypes.put(EntityType.VILLAGER, new Pair<>(120, 1.8f));

		// Objects
		mapEntityTypes.put(EntityType.ENDER_CRYSTAL, new Pair<>(51, 2.0f));
		mapEntityTypes.put(EntityType.ARROW, new Pair<>(60, 0.5f));
		mapEntityTypes.put(EntityType.SNOWBALL, new Pair<>(61, 0.25f));
		mapEntityTypes.put(EntityType.EGG, new Pair<>(62, 0.25f));
		mapEntityTypes.put(EntityType.FIREBALL, new Pair<>(63, 1.0f));
		mapEntityTypes.put(EntityType.SMALL_FIREBALL, new Pair<>(64, 0.3125f));
		mapEntityTypes.put(EntityType.ENDER_PEARL, new Pair<>(65, 0.25f));
		mapEntityTypes.put(EntityType.ENDER_SIGNAL, new Pair<>(72, 0.25f));
		mapEntityTypes.put(EntityType.FIREWORK, new Pair<>(76, 0.25f));
	}

	/*
	 *	General Methods
	 */

	@Override
	public void sendPacket(@NonNull final Player player, @NonNull final Object packet) {
		if (!(packet instanceof Packet)) return;
		((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) packet);
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
		spawn.a(MathHelper.floor(location.getX() * 32.0D));
		spawn.b(MathHelper.floor(location.getY() * 32.0D));
		spawn.c(MathHelper.floor(location.getZ() * 32.0D));
		ReflectionUtil.setFieldValue(spawn, "h", MathHelper.d(location.getPitch() * 256.0F / 360.0F));
		ReflectionUtil.setFieldValue(spawn, "i", MathHelper.d(location.getYaw() * 256.0F / 360.0F));
		ReflectionUtil.setFieldValue(spawn, "j", entityTypeId);
		sendPacket(player, spawn);
	}

	@Override
	public void showFakeItem(@NonNull final Player player, @NonNull final Location location, final int entityId, @NonNull final ItemStack itemStack) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.a(10, CraftItemStack.asNMSCopy(itemStack));
		showFakeEntity(player, location, entityId, 2);
		sendPacket(player, new PacketPlayOutEntityMetadata(entityId, dataWatcher, true));
		teleportFakeEntity(player, location, entityId);
	}

	private void showFakeEntityLiving(@NonNull Player player, @NonNull Location location, int entityId, int entityTypeId, @NonNull DataWatcher dataWatcher) {
		PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving();
		ReflectionUtil.setFieldValue(spawn, "a", entityId);
		ReflectionUtil.setFieldValue(spawn, "b", entityTypeId);
		ReflectionUtil.setFieldValue(spawn, "c", MathHelper.floor(location.getX() * 32.0D));
		ReflectionUtil.setFieldValue(spawn, "d", MathHelper.floor(location.getY() * 32.0D));
		ReflectionUtil.setFieldValue(spawn, "e", MathHelper.floor(location.getZ() * 32.0D));
		ReflectionUtil.setFieldValue(spawn, "i", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(spawn, "j", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(spawn, "k", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(spawn, "l", dataWatcher);
		sendPacket(player, spawn);
	}

	@Override
	public void showFakeEntityLiving(@NonNull final Player player, @NonNull final Location location, final int entityId, final int entityTypeId) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.a(15, (byte) 1); // No AI
		showFakeEntityLiving(player, location, entityId, entityTypeId, dataWatcher);
	}

	@Override
	public void showFakeArmorStand(@NonNull final Player player, @NonNull final Location location, final int entityId, final boolean small) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.a(0, (byte) 0x20); // Invisible
		dataWatcher.a(10, (byte) (small ? 0x19 : 0x18)); // Marker, Remove Base Plate, Small
		showFakeEntityLiving(player, location, entityId, 30, dataWatcher);
	}

	@Override
	public void updateFakeEntityName(@NonNull final Player player, final int entityId, @NonNull final String name) {
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.a(2, name); // Custom Name
		dataWatcher.a(3, (byte) (ChatColor.stripColor(name).isEmpty() ? 0 : 1)); // Custom Name Visible
		sendPacket(player, new PacketPlayOutEntityMetadata(entityId, dataWatcher, true));
	}

	@Override
	public void teleportFakeEntity(@NonNull final Player player, @NonNull final Location location, final int entityId) {
		PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport();
		ReflectionUtil.setFieldValue(teleport, "a", entityId);
		ReflectionUtil.setFieldValue(teleport, "b", MathHelper.floor(location.getX() * 32.0D));
		ReflectionUtil.setFieldValue(teleport, "c", MathHelper.floor(location.getY() * 32.0D));
		ReflectionUtil.setFieldValue(teleport, "d", MathHelper.floor(location.getZ() * 32.0D));
		ReflectionUtil.setFieldValue(teleport, "e", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(teleport, "f", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
		ReflectionUtil.setFieldValue(teleport, "g", true);
		sendPacket(player, teleport);
	}

	@Override
	public void hideFakeEntity(@NonNull final Player player, final int entityId) {
		sendPacket(player, new PacketPlayOutEntityDestroy(entityId, entityId + 1));
	}

	@Override
	public void helmetFakeEntity(@NonNull final Player player, final int entityId, @NonNull final org.bukkit.inventory.ItemStack itemStack) {
		sendPacket(player, new PacketPlayOutEntityEquipment(entityId, 4, CraftItemStack.asNMSCopy(itemStack)));
	}

	@Override
	public void attachFakeEnity(@NonNull final Player player, final int vehicleId, final int entityId) {
		PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity();
		ReflectionUtil.setFieldValue(packet, "a", 0);
		ReflectionUtil.setFieldValue(packet, "b", entityId);
		ReflectionUtil.setFieldValue(packet, "c", vehicleId);
		sendPacket(player, packet);
	}

}

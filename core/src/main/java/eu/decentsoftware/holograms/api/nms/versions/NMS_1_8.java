package eu.decentsoftware.holograms.api.nms.versions;

import eu.decentsoftware.holograms.api.nms.NMS;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectConstructor;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectField;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated For removal.
 */
@Deprecated
@SuppressWarnings("unused")
public class NMS_1_8 extends NMS {

    // UTILITY
    private static final Class<?> ENTITY_CLASS;
    private static final Class<?> ITEM_STACK_CLASS;
    private static final ReflectMethod CRAFT_ITEM_NMS_COPY_METHOD;
    // DATA WATCHER
    private static final Class<?> DATA_WATCHER_CLASS;
    private static final ReflectConstructor DATA_WATCHER_CONSTRUCTOR;
    private static final ReflectMethod DATA_WATCHER_A_METHOD;
    // MATH HELPER
    private static final Class<?> MATH_HELPER_CLASS;
    private static final ReflectMethod MATH_HELPER_FLOOR_METHOD;
    private static final ReflectMethod MATH_HELPER_D_METHOD;
    // PACKETS
    private static final ReflectConstructor PACKET_SPAWN_ENTITY_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_METADATA_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_TELEPORT_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ATTACH_ENTITY_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_DESTROY_CONSTRUCTOR;

    private static final ReflectField<Integer> ENTITY_COUNTER_FIELD;

    static {
        // UTILITY
        ENTITY_CLASS = ReflectionUtil.getNMSClass("Entity");
        ITEM_STACK_CLASS = ReflectionUtil.getNMSClass("ItemStack");
        CRAFT_ITEM_NMS_COPY_METHOD = new ReflectMethod(ReflectionUtil.getObcClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class);
        // DATA WATCHER
        DATA_WATCHER_CLASS = ReflectionUtil.getNMSClass("DataWatcher");
        DATA_WATCHER_CONSTRUCTOR = new ReflectConstructor(DATA_WATCHER_CLASS, ENTITY_CLASS);
        DATA_WATCHER_A_METHOD = new ReflectMethod(DATA_WATCHER_CLASS, "a", int.class, Object.class);
        // MATH HELPER
        MATH_HELPER_CLASS = ReflectionUtil.getNMSClass("MathHelper");
        MATH_HELPER_FLOOR_METHOD = new ReflectMethod(MATH_HELPER_CLASS, "floor", double.class);
        MATH_HELPER_D_METHOD = new ReflectMethod(MATH_HELPER_CLASS, "d", float.class);
        // PACKETS
        PACKET_SPAWN_ENTITY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutSpawnEntity"));
        PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutSpawnEntityLiving"));
        PACKET_ENTITY_METADATA_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityMetadata"), int.class, DATA_WATCHER_CLASS, boolean.class);
        PACKET_ENTITY_TELEPORT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityTeleport"));
        PACKET_ATTACH_ENTITY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutAttachEntity"));
        PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityEquipment"), int.class, int.class, ITEM_STACK_CLASS);
        PACKET_ENTITY_DESTROY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityDestroy"), int[].class);

        ENTITY_COUNTER_FIELD = new ReflectField<>(ENTITY_CLASS, "entityCount");
    }

    @Override
    public int getFreeEntityId() {
        int entityCount = ENTITY_COUNTER_FIELD.getValue(null);
        ENTITY_COUNTER_FIELD.setValue(null, entityCount + 1);
        return entityCount;
    }

    @Override
    public void showFakeEntity(Player player, Location location, EntityType entityType, int entityId) {
        Validate.notNull(entityType);
        showFakeEntity(player, location, getEntityTypeId(entityType), entityId);
    }

    @Override
    public void showFakeEntityLiving(Player player, Location location, EntityType entityType, int entityId) {
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 15, (byte) 0);
        showFakeEntityLiving(player, location, getEntityTypeId(entityType), entityId, dataWatcher);
    }

    @Override
    public void showFakeEntityArmorStand(Player player, Location location, int entityId, boolean invisible, boolean small, boolean clickable) {
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 0, (byte) (invisible ? 0x20 : 0x00)); // Invisible
        byte data = 0x08;
        if (small) data += 0x01;
        if (!clickable) data += 0x10;
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 10, data);
        showFakeEntityLiving(player, location, 30, entityId, dataWatcher);
    }

    @Override
    public void showFakeEntityItem(Player player, Location location, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        Validate.notNull(itemStack);

        Object nmsItemStack = CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack);
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        if (nmsItemStack == null || dataWatcher == null) return;
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 10, nmsItemStack);
        showFakeEntity(player, location, 2, entityId);
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
        teleportFakeEntity(player, location, entityId);
    }

    @Override
    public void updateFakeEntityCustomName(Player player, String name, int entityId) {
        Validate.notNull(player);
        Validate.notNull(name);

        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 2, name); // Custom Name
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 3, (byte) (ChatColor.stripColor(name).isEmpty() ? 0 : 1)); // Custom Name Visible
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
    }

    @Override
    public void updateFakeEntityItem(Player player, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(itemStack);

        Object nmsItemStack = CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack);
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        if (nmsItemStack == null || dataWatcher == null) return;
        DATA_WATCHER_A_METHOD.invoke(dataWatcher, 10, nmsItemStack);
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
    }

    @Override
    public void teleportFakeEntity(Player player, Location location, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object teleport = PACKET_ENTITY_TELEPORT_CONSTRUCTOR.newInstance();
        if (teleport == null) return;
        ReflectionUtil.setFieldValue(teleport, "a", entityId);
        ReflectionUtil.setFieldValue(teleport, "b", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getX() * 32.0D));
        ReflectionUtil.setFieldValue(teleport, "c", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getY() * 32.0D));
        ReflectionUtil.setFieldValue(teleport, "d", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getZ() * 32.0D));
        ReflectionUtil.setFieldValue(teleport, "e", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(teleport, "f", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(teleport, "g", false);
        sendPacket(player, teleport);
    }

    @Override
    public void helmetFakeEntity(Player player, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(itemStack);
        Object nmsItemStack = CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack);
        if (nmsItemStack == null) return;
        Object packet = PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR.newInstance(entityId, 4, nmsItemStack);
        if (packet == null) return;
        sendPacket(player, packet);
    }

    @Override
    public void attachFakeEntity(Player player, int vehicleId, int entityId) {
        Validate.notNull(player);
        Object packet = PACKET_ATTACH_ENTITY_CONSTRUCTOR.newInstance();
        if (packet == null) return;
        ReflectionUtil.setFieldValue(packet, "a", 0);
        ReflectionUtil.setFieldValue(packet, "b", entityId);
        ReflectionUtil.setFieldValue(packet, "c", vehicleId);
        sendPacket(player, packet);
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public void hideFakeEntities(Player player, int... entityIds) {
        Validate.notNull(player);
        sendPacket(player, PACKET_ENTITY_DESTROY_CONSTRUCTOR.newInstance((Object) entityIds));
    }

    public void showFakeEntity(Player player, Location location, int entityTypeId, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object spawn = PACKET_SPAWN_ENTITY_CONSTRUCTOR.newInstance();
        if (spawn == null) return;
        ReflectionUtil.setFieldValue(spawn, "a", entityId);
        ReflectionUtil.setFieldValue(spawn, "b", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getX() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "c", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getY() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "d", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getZ() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "h", MATH_HELPER_D_METHOD.invokeStatic(location.getPitch() * 256.0F / 360.0F));
        ReflectionUtil.setFieldValue(spawn, "i", MATH_HELPER_D_METHOD.invokeStatic(location.getYaw() * 256.0F / 360.0F));
        ReflectionUtil.setFieldValue(spawn, "j", entityTypeId);
        sendPacket(player, spawn);
    }

    private void showFakeEntityLiving(Player player, Location location, int entityTypeId, int entityId, Object dataWatcher) {
        Validate.notNull(player);
        Validate.notNull(location);
        if (dataWatcher == null || !DATA_WATCHER_CLASS.isAssignableFrom(dataWatcher.getClass())) return;

        Object spawn = PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR.newInstance();
        if (spawn == null) return;
        ReflectionUtil.setFieldValue(spawn, "a", entityId);
        ReflectionUtil.setFieldValue(spawn, "b", entityTypeId);
        ReflectionUtil.setFieldValue(spawn, "c", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getX() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "d", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getY() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "e", MATH_HELPER_FLOOR_METHOD.invokeStatic(location.getZ() * 32.0D));
        ReflectionUtil.setFieldValue(spawn, "i", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(spawn, "j", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(spawn, "k", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(spawn, "l", dataWatcher);
        sendPacket(player, spawn);
    }

}

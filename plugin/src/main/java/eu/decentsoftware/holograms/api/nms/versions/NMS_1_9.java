package eu.decentsoftware.holograms.api.nms.versions;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.nms.NMS;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectConstructor;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectField;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @deprecated For removal.
 */
@Deprecated
public class NMS_1_9 extends NMS {

    private static final int ARMOR_STAND_ID = Version.before(13) ? 30 : 1;
    private static int DROPPED_ITEM_ID = 2;
    // UTILITY
    private static final Class<?> ENTITY_CLASS;
    private static final Class<?> ITEM_STACK_CLASS;
    private static final Class<?> ENTITY_ARMOR_STAND_CLASS;
    private static final Class<?> ENTITY_ITEM_CLASS;
    private static final Class<?> ENUM_ITEM_SLOT_CLASS;
    private static final ReflectMethod ENUM_ITEM_SLOT_FROM_NAME_METHOD;
    private static final ReflectMethod CRAFT_ITEM_NMS_COPY_METHOD;
    private static final ReflectMethod CRAFT_CHAT_MESSAGE_FROM_STRING_METHOD;
    private static final ReflectMethod PAIR_OF_METHOD;
    // DATA WATCHER
    private static final Class<?> DATA_WATCHER_CLASS;
    private static final ReflectConstructor DATA_WATCHER_CONSTRUCTOR;
    private static final ReflectMethod DATA_WATCHER_REGISTER_METHOD;
    // MATH HELPER
    private static final Class<?> MATH_HELPER_CLASS;
    private static final ReflectMethod MATH_HELPER_D_METHOD;
    private static final ReflectMethod MATH_HELPER_A_METHOD;
    // PACKETS
    private static final ReflectConstructor PACKET_SPAWN_ENTITY_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_METADATA_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_TELEPORT_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_MOUNT_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR;
    private static final ReflectConstructor PACKET_ENTITY_DESTROY_CONSTRUCTOR;
    // DATA WATCHER OBJECT
    private static final Class<?> DWO_CLASS;
    private static final Object DWO_CUSTOM_NAME;
    private static final Object DWO_CUSTOM_NAME_VISIBLE;
    private static final Object DWO_ENTITY_DATA;
    private static final Object DWO_ARMOR_STAND_DATA;
    private static final Object DWO_ITEM;
    // ENTITY TYPES
    private static Class<?> ENTITY_TYPES_CLASS;
    private static ReflectField<?> I_REGISTRY_ENTITY_TYPE_FIELD;
    private static ReflectMethod ENTITY_TYPES_A_METHOD;
    private static ReflectMethod ENTITY_TYPE_GET_KEY_METHOD;
    private static ReflectMethod REGISTRY_BLOCKS_GET_ID_METHOD;
    private static ReflectMethod REGISTRY_BLOCKS_FROM_ID_METHOD;
    private static ReflectMethod NAMESPACED_KEY_GET_KEY_METHOD;
    private static ReflectMethod ENTITY_TYPES_GET_SIZE_METHOD;
    private static ReflectField<Float> ENTITY_SIZE_HEIGHT_FIELD;

    private static final ReflectField<Object> ENTITY_COUNTER_FIELD;

    static {
        DWO_CLASS = ReflectionUtil.getNMSClass("DataWatcherObject");
        // UTILITY
        ENTITY_CLASS = ReflectionUtil.getNMSClass("Entity");
        ITEM_STACK_CLASS = ReflectionUtil.getNMSClass("ItemStack");
        ENTITY_ARMOR_STAND_CLASS = ReflectionUtil.getNMSClass("EntityArmorStand");
        ENTITY_ITEM_CLASS = ReflectionUtil.getNMSClass("EntityItem");
        ENUM_ITEM_SLOT_CLASS = ReflectionUtil.getNMSClass("EnumItemSlot");
        ENUM_ITEM_SLOT_FROM_NAME_METHOD = new ReflectMethod(ENUM_ITEM_SLOT_CLASS, Version.afterOrEqual(14) ? "fromName" : "a", String.class);
        CRAFT_ITEM_NMS_COPY_METHOD = new ReflectMethod(ReflectionUtil.getObcClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class);
        CRAFT_CHAT_MESSAGE_FROM_STRING_METHOD = new ReflectMethod(ReflectionUtil.getObcClass("util.CraftChatMessage"), "fromStringOrNull", String.class);
        // DATA WATCHER
        DATA_WATCHER_CLASS = ReflectionUtil.getNMSClass("DataWatcher");
        DATA_WATCHER_CONSTRUCTOR = new ReflectConstructor(DATA_WATCHER_CLASS, ENTITY_CLASS);
        DATA_WATCHER_REGISTER_METHOD = new ReflectMethod(DATA_WATCHER_CLASS, "register", DWO_CLASS, Object.class);
        // MATH HELPER
        MATH_HELPER_CLASS = ReflectionUtil.getNMSClass("MathHelper");
        MATH_HELPER_D_METHOD = new ReflectMethod(MATH_HELPER_CLASS, "d", float.class);
        MATH_HELPER_A_METHOD = new ReflectMethod(MATH_HELPER_CLASS, "a", Random.class);
        // PACKETS
        PACKET_SPAWN_ENTITY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutSpawnEntity"));
        PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutSpawnEntityLiving"));
        PACKET_ENTITY_METADATA_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityMetadata"), int.class, DATA_WATCHER_CLASS, boolean.class);
        PACKET_ENTITY_TELEPORT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityTeleport"));
        PACKET_MOUNT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutMount"));
        if (Version.afterOrEqual(16)) {
            PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityEquipment"), int.class, List.class);
            PAIR_OF_METHOD = new ReflectMethod(ReflectionUtil.getClass("com.mojang.datafixers.util.Pair"), "of", Object.class, Object.class);
        } else {
            PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityEquipment"), int.class, ENUM_ITEM_SLOT_CLASS, ITEM_STACK_CLASS);
            PAIR_OF_METHOD = null;
        }
        PACKET_ENTITY_DESTROY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMSClass("PacketPlayOutEntityDestroy"), int[].class);
        // DATA WATCHER OBJECT
        switch (Version.CURRENT) {
            case v1_9_R1:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "ax").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "az").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aA").getValue(null);
                break;
            case v1_9_R2:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "ay").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aA").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aB").getValue(null);
                break;
            case v1_10_R1:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "aa").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aA").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aB").getValue(null);
                break;
            case v1_11_R1:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "Z").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aA").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aB").getValue(null);
                break;
            case v1_12_R1:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "Z").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aB").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aC").getValue(null);
                break;
            case v1_13_R1:
            case v1_13_R2:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "ac").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aE").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aF").getValue(null);
                DROPPED_ITEM_ID = 34;
                break;
            case v1_14_R1:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "W").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "az").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aA").getValue(null);
                DROPPED_ITEM_ID = 34;
                break;
            case v1_15_R1:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "T").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "az").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aA").getValue(null);
                DROPPED_ITEM_ID = 35;
                break;
            case v1_16_R1:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "T").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "ax").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "ay").getValue(null);
                DROPPED_ITEM_ID = 37;
                break;
            case v1_16_R2:
            case v1_16_R3:
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "S").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aq").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "ar").getValue(null);
                DROPPED_ITEM_ID = 37;
                break;
            default:
                DWO_ENTITY_DATA = null;
                DWO_CUSTOM_NAME = null;
                DWO_CUSTOM_NAME_VISIBLE = null;
        }

        if (Version.before(13)) {
            DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "a").getValue(null);
            DWO_ITEM = new ReflectField<>(ENTITY_ITEM_CLASS, "c").getValue(null);
        } else if (Version.before(14)) {
            DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "a").getValue(null);
            DWO_ITEM = new ReflectField<>(ENTITY_ITEM_CLASS, "b").getValue(null);
        } else {
            DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "b").getValue(null);
            DWO_ITEM = new ReflectField<>(ENTITY_ITEM_CLASS, "ITEM").getValue(null);
            // ENTITY TYPES
            Class<?> registryBlocksClass = ReflectionUtil.getNMSClass("RegistryBlocks");
            ENTITY_TYPES_CLASS = ReflectionUtil.getNMSClass("EntityTypes");
            I_REGISTRY_ENTITY_TYPE_FIELD = new ReflectField<>(ReflectionUtil.getNMSClass("IRegistry"), "ENTITY_TYPE");
            ENTITY_TYPES_A_METHOD = new ReflectMethod(ENTITY_TYPES_CLASS, "a", String.class);
            ENTITY_TYPE_GET_KEY_METHOD = new ReflectMethod(EntityType.class, "getKey");
            REGISTRY_BLOCKS_GET_ID_METHOD = new ReflectMethod(registryBlocksClass, "a", Object.class);
            REGISTRY_BLOCKS_FROM_ID_METHOD = new ReflectMethod(registryBlocksClass, "fromId", int.class);
            NAMESPACED_KEY_GET_KEY_METHOD = new ReflectMethod(ReflectionUtil.getClass("org.bukkit.NamespacedKey"), "getKey");
            for (Method method : ENTITY_TYPES_CLASS.getMethods()) {
                if (method.getReturnType().getName().contains("EntitySize")) {
                    ENTITY_TYPES_GET_SIZE_METHOD = new ReflectMethod(ENTITY_TYPES_CLASS, method.getName());
                }
            }
            ENTITY_SIZE_HEIGHT_FIELD = new ReflectField<>(ReflectionUtil.getNMSClass("EntitySize"), "height");
        }

        ENTITY_COUNTER_FIELD = new ReflectField<>(ENTITY_CLASS, "entityCount");
    }

    @Override
    public int getFreeEntityId() {
        Object entityCounter = ENTITY_COUNTER_FIELD.getValue(null);
        if (entityCounter instanceof AtomicInteger) {
            return ((AtomicInteger) ENTITY_COUNTER_FIELD.getValue(null)).addAndGet(1);
        }
        ENTITY_COUNTER_FIELD.setValue(null, (int) entityCounter + 1);
        return (int) entityCounter;
    }

    @Override
    public int getEntityTypeId(EntityType type) {
        if (Version.before(14)) {
            return super.getEntityTypeId(type);
        }

        if (type == null) return -1;
        Object namespacedKey = ENTITY_TYPE_GET_KEY_METHOD.invoke(type);
        String key = NAMESPACED_KEY_GET_KEY_METHOD.invoke(namespacedKey);
        java.util.Optional<?> entityTypes = ENTITY_TYPES_A_METHOD.invokeStatic(key.toLowerCase());
        return entityTypes.map(entityType -> {
            Object registryBlocks = I_REGISTRY_ENTITY_TYPE_FIELD.getValue(null);
            return REGISTRY_BLOCKS_GET_ID_METHOD.<Integer>invoke(registryBlocks, entityType);
        }).orElse(-1);
    }

    @Override
    public float getEntityHeight(EntityType type) {
        if (Version.before(14)) {
            return super.getEntityHeight(type);
        }

        if (type == null) return 0.0f;
        Object namespacedKey = ENTITY_TYPE_GET_KEY_METHOD.invoke(type);
        String key = NAMESPACED_KEY_GET_KEY_METHOD.invoke(namespacedKey);
        java.util.Optional<?> entityTypes = ENTITY_TYPES_A_METHOD.invokeStatic(key.toLowerCase());
        return entityTypes.map(entityType -> {
            Object entitySize = ENTITY_TYPES_GET_SIZE_METHOD.invoke(entityType);
            return ENTITY_SIZE_HEIGHT_FIELD.getValue(entitySize);
        }).orElse(0.0f);
    }

    @Override
    public void showFakeEntity(Player player, Location location, EntityType entityType, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        Validate.notNull(entityType);
        showFakeEntity(player, location, getEntityTypeId(entityType), entityId);
    }

    @Override
    public void showFakeEntityLiving(Player player, Location location, EntityType entityType, int entityId) {
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ENTITY_DATA, (byte) 0);
        showFakeEntityLiving(player, location, getEntityTypeId(entityType), entityId, dataWatcher);
    }

    @Override
    public void showFakeEntityArmorStand(Player player, Location location, int entityId, boolean invisible, boolean small, boolean clickable) {
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ENTITY_DATA, (byte) (invisible ? 0x20 : 0x00)); // Invisible
        byte data = 0x08;
        if (small) data += 0x01;
        if (!clickable) data += 0x10;
        DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ARMOR_STAND_DATA, data);
        showFakeEntityLiving(player, location, ARMOR_STAND_ID, entityId, dataWatcher);
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
    }

    @Override
    public void showFakeEntityItem(Player player, Location location, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        Validate.notNull(itemStack);

        Object nmsItemStack = CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack);
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        if (nmsItemStack == null || dataWatcher == null) return;
        if (Version.before(11)) {
            DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ITEM, Optional.fromNullable(nmsItemStack));
        } else {
            DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ITEM, nmsItemStack);
        }
        showFakeEntity(player, location, DROPPED_ITEM_ID, entityId);
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
        teleportFakeEntity(player, location, entityId);
    }

    @Override
    public void updateFakeEntityCustomName(Player player, String name, int entityId) {
        Validate.notNull(player);
        Validate.notNull(name);

        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        if (Version.before(13)) {
            DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_CUSTOM_NAME, name); // Custom Name
        } else {
            DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_CUSTOM_NAME, java.util.Optional.ofNullable(CRAFT_CHAT_MESSAGE_FROM_STRING_METHOD.invokeStatic(name))); // Custom Name
        }
        DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_CUSTOM_NAME_VISIBLE, !ChatColor.stripColor(name).isEmpty()); // Custom Name Visible
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
    }

    @Override
    public void updateFakeEntityItem(Player player, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(itemStack);

        Object nmsItemStack = CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack);
        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        if (nmsItemStack == null || dataWatcher == null) return;
        if (Version.before(11)) {
            DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ITEM, Optional.fromNullable(nmsItemStack));
        } else {
            DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ITEM, nmsItemStack);
        }
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
    }

    @Override
    public void teleportFakeEntity(Player player, Location location, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object teleport = PACKET_ENTITY_TELEPORT_CONSTRUCTOR.newInstance();
        if (teleport == null) return;
        ReflectionUtil.setFieldValue(teleport, "a", entityId);
        ReflectionUtil.setFieldValue(teleport, "b", location.getX());
        ReflectionUtil.setFieldValue(teleport, "c", location.getY());
        ReflectionUtil.setFieldValue(teleport, "d", location.getZ());
        ReflectionUtil.setFieldValue(teleport, "e", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(teleport, "f", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(teleport, "g", false);
        sendPacket(player, teleport);
    }

    private static Object ENUM_ITEM_SLOT_HEAD;

    @Override
    public void helmetFakeEntity(Player player, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(itemStack);

        if (ENUM_ITEM_SLOT_HEAD == null) {
            ENUM_ITEM_SLOT_HEAD = ENUM_ITEM_SLOT_FROM_NAME_METHOD.invokeStatic("head");
        }
        Object nmsItemStack = CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack);
        Object packet;
        if (Version.afterOrEqual(16)) {
            Object pair = PAIR_OF_METHOD.invokeStatic(ENUM_ITEM_SLOT_HEAD, nmsItemStack);
            packet = PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR.newInstance(entityId, Lists.newArrayList(pair));
        } else {
            packet = PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR.newInstance(entityId, ENUM_ITEM_SLOT_HEAD, nmsItemStack);
        }
        sendPacket(player, packet);
    }

    @Override
    public void attachFakeEntity(Player player, int vehicleId, int entityId) {
        Validate.notNull(player);
        Object packet = PACKET_MOUNT_CONSTRUCTOR.newInstance();
        if (packet == null) return;
        ReflectionUtil.setFieldValue(packet, "a", vehicleId);
        ReflectionUtil.setFieldValue(packet, "b", new int[]{entityId});
        sendPacket(player, packet);
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public void hideFakeEntities(Player player, int... entityIds) {
        Validate.notNull(player);
        sendPacket(player, PACKET_ENTITY_DESTROY_CONSTRUCTOR.newInstance((Object) entityIds));
    }

    private void showFakeEntityLiving(Player player, Location location, int entityTypeId, int entityId, Object dataWatcher) {
        Validate.notNull(player);
        Validate.notNull(location);
        if (dataWatcher == null || !DATA_WATCHER_CLASS.isAssignableFrom(dataWatcher.getClass())) return;

        if (entityTypeId == -1) return;
        Object spawn = PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR.newInstance();
        if (spawn == null) return;
        ReflectionUtil.setFieldValue(spawn, "a", entityId);
        ReflectionUtil.setFieldValue(spawn, "b", MATH_HELPER_A_METHOD.invokeStatic(ThreadLocalRandom.current()));
        ReflectionUtil.setFieldValue(spawn, "c", entityTypeId);
        ReflectionUtil.setFieldValue(spawn, "d", location.getX());
        ReflectionUtil.setFieldValue(spawn, "e", location.getY());
        ReflectionUtil.setFieldValue(spawn, "f", location.getZ());
        ReflectionUtil.setFieldValue(spawn, "j", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(spawn, "k", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(spawn, "l", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        ReflectionUtil.setFieldValue(spawn, "m", dataWatcher);
        sendPacket(player, spawn);
    }

    public void showFakeEntity(Player player, Location location, int entityTypeId, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        if (entityTypeId == -1) return;
        Object spawn = PACKET_SPAWN_ENTITY_CONSTRUCTOR.newInstance();
        if (spawn == null) return;
        ReflectionUtil.setFieldValue(spawn, "a", entityId);
        ReflectionUtil.setFieldValue(spawn, "b", MATH_HELPER_A_METHOD.invokeStatic(ThreadLocalRandom.current()));
        ReflectionUtil.setFieldValue(spawn, "c", location.getX());
        ReflectionUtil.setFieldValue(spawn, "d", location.getY());
        ReflectionUtil.setFieldValue(spawn, "e", location.getZ());
        ReflectionUtil.setFieldValue(spawn, "i", MATH_HELPER_D_METHOD.invokeStatic(location.getPitch() * 256.0F / 360.0F));
        ReflectionUtil.setFieldValue(spawn, "j", MATH_HELPER_D_METHOD.invokeStatic(location.getYaw() * 256.0F / 360.0F));
        ReflectionUtil.setFieldValue(spawn, "k", Version.afterOrEqual(14) ?
                ENTITY_TYPES_CLASS.cast(REGISTRY_BLOCKS_FROM_ID_METHOD.invoke(I_REGISTRY_ENTITY_TYPE_FIELD.getValue(null), entityTypeId)) :
                entityTypeId);
        sendPacket(player, spawn);
    }

}

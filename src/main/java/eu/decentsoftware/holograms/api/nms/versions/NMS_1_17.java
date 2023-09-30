package eu.decentsoftware.holograms.api.nms.versions;

import eu.decentsoftware.holograms.api.nms.NMS;
import eu.decentsoftware.holograms.api.utils.RandomUtils;
import eu.decentsoftware.holograms.api.utils.reflect.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NMS_1_17 extends NMS {

    // UTILITY
    private static final Class<?> ENTITY_CLASS;
    private static final Class<?> ENTITY_ARMOR_STAND_CLASS;
    private static final Class<?> ENTITY_ITEM_CLASS;
    private static final Class<?> ENUM_ITEM_SLOT_CLASS;
    private static final Class<?> ENTITY_TYPES_CLASS;
    private static final Class<?> VEC_3D_CLASS;
    private static final ReflectField<?> ENTITY_TYPES_REGISTRY_FIELD;
    private static final ReflectMethod REGISTRY_BLOCKS_FROM_ID_METHOD;
    private static final ReflectMethod ENUM_ITEM_SLOT_FROM_NAME_METHOD;
    private static final ReflectMethod CRAFT_ITEM_NMS_COPY_METHOD;
    private static final ReflectMethod CRAFT_CHAT_MESSAGE_FROM_STRING_METHOD;
    private static final ReflectMethod PAIR_OF_METHOD;
    // MATH HELPER
    private static final Class<?> MATH_HELPER_CLASS;
    private static final ReflectMethod MATH_HELPER_A_METHOD;
    // PACKET DATA SERIALIZER
    private static final Class<?> PACKET_DATA_SERIALIZER_CLASS;
    private static final ReflectConstructor PACKET_DATA_SERIALIZER_CONSTRUCTOR;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_WRITE_INT_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_WRITE_UUID_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_WRITE_INTS_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_WRITE_SHORT_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_WRITE_BOOLEAN_METHOD;
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
    private static final Class<?> DWS_CLASS;
    private static final Class<?> DWI_CLASS;
    private static final Object DWO_CUSTOM_NAME;
    private static final Object DWO_CUSTOM_NAME_VISIBLE;
    private static final Object DWO_ENTITY_DATA;
    private static final Object DWO_ARMOR_STAND_DATA;
    private static final Object DWO_ITEM;
    private static final ReflectConstructor DATA_WATCHER_ITEM_CONSTRUCTOR;
    // ENTITY TYPES
    private static final ReflectMethod ENTITY_TYPES_A_METHOD;
    private static final ReflectMethod ENTITY_TYPE_GET_KEY_METHOD;
    private static final ReflectMethod REGISTRY_BLOCKS_GET_ID_METHOD;
    private static final ReflectMethod NAMESPACED_KEY_GET_KEY_METHOD;
    private static final ReflectMethod ENTITY_TYPES_GET_SIZE_METHOD;
    private static final ReflectField<Float> ENTITY_SIZE_HEIGHT_FIELD;

    private static final ReflectField<AtomicInteger> ENTITY_COUNTER_FIELD;
    private static final Object VEC_3D_A;

    static {
        DWO_CLASS = ReflectionUtil.getNMClass("network.syncher.DataWatcherObject");
        DWS_CLASS = ReflectionUtil.getNMClass("network.syncher.DataWatcherSerializer");
        DWI_CLASS = ReflectionUtil.getNMClass("network.syncher.DataWatcher$Item");
        // UTILITY
        ENTITY_CLASS = ReflectionUtil.getNMClass("world.entity.Entity");
        ENTITY_ARMOR_STAND_CLASS = ReflectionUtil.getNMClass("world.entity.decoration.EntityArmorStand");
        ENTITY_ITEM_CLASS = ReflectionUtil.getNMClass("world.entity.item.EntityItem");
        ENUM_ITEM_SLOT_CLASS = ReflectionUtil.getNMClass("world.entity.EnumItemSlot");
        ENTITY_TYPES_CLASS = ReflectionUtil.getNMClass("world.entity.EntityTypes");
        VEC_3D_CLASS = ReflectionUtil.getNMClass("world.phys.Vec3D");
        CRAFT_ITEM_NMS_COPY_METHOD = new ReflectMethod(ReflectionUtil.getObcClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class);
        CRAFT_CHAT_MESSAGE_FROM_STRING_METHOD = new ReflectMethod(ReflectionUtil.getObcClass("util.CraftChatMessage"), "fromStringOrNull", String.class);
        PAIR_OF_METHOD = new ReflectMethod(ReflectionUtil.getClass("com.mojang.datafixers.util.Pair"), "of", Object.class, Object.class);
        // DATA WATCHER
        DATA_WATCHER_ITEM_CONSTRUCTOR = new ReflectConstructor(DWI_CLASS, DWO_CLASS, Object.class);
        if (Version.afterOrEqual(18)) {
            if (Version.afterOrEqual(Version.v1_19_R2)) {
                ENTITY_TYPES_REGISTRY_FIELD = new ReflectField<>(ReflectionUtil.getNMClass("core.registries.BuiltInRegistries"), "h");
            } else if (Version.afterOrEqual(19)) {
                ENTITY_TYPES_REGISTRY_FIELD = new ReflectField<>(ReflectionUtil.getNMClass("core.IRegistry"), "X");
            } else if (Version.is(Version.v1_18_R2)) {
                ENTITY_TYPES_REGISTRY_FIELD = new ReflectField<>(ReflectionUtil.getNMClass("core.IRegistry"), "W");
            } else {
                ENTITY_TYPES_REGISTRY_FIELD = new ReflectField<>(ReflectionUtil.getNMClass("core.IRegistry"), "Z");
            }
            REGISTRY_BLOCKS_FROM_ID_METHOD = new ReflectMethod(ReflectionUtil.getNMClass("core.RegistryBlocks"), "a", int.class);
            ENUM_ITEM_SLOT_FROM_NAME_METHOD = new ReflectMethod(ENUM_ITEM_SLOT_CLASS, "a", String.class);
        } else {
            ENTITY_TYPES_REGISTRY_FIELD = new ReflectField<>(ReflectionUtil.getNMClass("core.IRegistry"), "Y");
            REGISTRY_BLOCKS_FROM_ID_METHOD = new ReflectMethod(ReflectionUtil.getNMClass("core.RegistryBlocks"), "fromId", int.class);
            ENUM_ITEM_SLOT_FROM_NAME_METHOD = new ReflectMethod(ENUM_ITEM_SLOT_CLASS, "fromName", String.class);
        }
        // MATH HELPER
        MATH_HELPER_CLASS = ReflectionUtil.getNMClass("util.MathHelper");
        MATH_HELPER_A_METHOD = new ReflectMethod(MATH_HELPER_CLASS, "a", Random.class);
        // PACKET DATA SERIALIZER
        PACKET_DATA_SERIALIZER_CLASS = ReflectionUtil.getNMClass("network.PacketDataSerializer");
        PACKET_DATA_SERIALIZER_CONSTRUCTOR = new ReflectConstructor(PACKET_DATA_SERIALIZER_CLASS, ByteBuf.class);
        if (Version.afterOrEqual(Version.v1_20_R2)) {
            PACKET_DATA_SERIALIZER_WRITE_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "c", int.class);
        } else {
            PACKET_DATA_SERIALIZER_WRITE_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "d", int.class);
        }
        PACKET_DATA_SERIALIZER_WRITE_UUID_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "a", UUID.class);
        PACKET_DATA_SERIALIZER_WRITE_INTS_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "a", int[].class);
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "writeDouble", double.class);
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "writeByte", int.class);
        PACKET_DATA_SERIALIZER_WRITE_SHORT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "writeShort", int.class);
        PACKET_DATA_SERIALIZER_WRITE_BOOLEAN_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "writeBoolean", boolean.class);
        // PACKETS
        if (Version.before(19)) {
            PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutSpawnEntityLiving"),
                    PACKET_DATA_SERIALIZER_CLASS);
            PACKET_SPAWN_ENTITY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutSpawnEntity"),
                    int.class, UUID.class, double.class, double.class, double.class, float.class, float.class, ENTITY_TYPES_CLASS, int.class, VEC_3D_CLASS);
        } else {
            PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR = null;
            PACKET_SPAWN_ENTITY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutSpawnEntity"),
                    int.class, UUID.class, double.class, double.class, double.class, float.class, float.class, ENTITY_TYPES_CLASS, int.class, VEC_3D_CLASS, double.class);
        }
        PACKET_ENTITY_METADATA_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutEntityMetadata"),
                PACKET_DATA_SERIALIZER_CLASS);
        PACKET_ENTITY_TELEPORT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutEntityTeleport"),
                PACKET_DATA_SERIALIZER_CLASS);
        PACKET_MOUNT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutMount"),
                PACKET_DATA_SERIALIZER_CLASS);
        PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutEntityEquipment"),
                int.class, List.class);
        PACKET_ENTITY_DESTROY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutEntityDestroy"),
                int[].class);
        // DATA WATCHER OBJECT
        if (Version.afterOrEqual(18)) {
            if (Version.afterOrEqual(Version.v1_20_R2)) {
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "ao").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aU").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aV").getValue(null);
                DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "bC").getValue(null);
            } else if (Version.afterOrEqual(Version.v1_20_R1)) {
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "an").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aU").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aV").getValue(null);
                DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "bC").getValue(null);
            } else if (Version.afterOrEqual(Version.v1_19_R3)) {
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "an").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aR").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aS").getValue(null);
                DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "bB").getValue(null);
            } else if (Version.afterOrEqual(Version.v1_18_R2)) {
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "Z").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aM").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aN").getValue(null);
                DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "bG").getValue(null);
            } else {
                DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "aa").getValue(null);
                DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aL").getValue(null);
                DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aM").getValue(null);
                DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "bH").getValue(null);
            }
        } else {
            DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "Z").getValue(null);
            DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aJ").getValue(null);
            DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aK").getValue(null);
            DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "bG").getValue(null);
        }
        DWO_ITEM = new ReflectField<>(ENTITY_ITEM_CLASS, "c").getValue(null);
        // ENTITY TYPES
        ENTITY_TYPES_A_METHOD = new ReflectMethod(ENTITY_TYPES_CLASS, "a", String.class);
        ENTITY_TYPE_GET_KEY_METHOD = new ReflectMethod(EntityType.class, "getKey");
        if (Version.afterOrEqual(18)) {
            REGISTRY_BLOCKS_GET_ID_METHOD = new ReflectMethod(ReflectionUtil.getNMClass("core.Registry"), "a", Object.class);
        } else {
            REGISTRY_BLOCKS_GET_ID_METHOD = new ReflectMethod(ReflectionUtil.getNMClass("core.Registry"), "getId", Object.class);
        }
        NAMESPACED_KEY_GET_KEY_METHOD = new ReflectMethod(ReflectionUtil.getClass("org.bukkit.NamespacedKey"), "getKey");
        ENTITY_TYPES_GET_SIZE_METHOD = new ReflectMethod(ENTITY_TYPES_CLASS, Version.afterOrEqual(Version.v1_19_R2) ? "n" : "m");
        ENTITY_SIZE_HEIGHT_FIELD = new ReflectField<>(ReflectionUtil.getNMClass("world.entity.EntitySize"), "b");

        if (Version.afterOrEqual(Version.v1_19_R3)) {
            ENTITY_COUNTER_FIELD = new ReflectField<>(ENTITY_CLASS, "d");
        } else if (Version.CURRENT.equals(Version.v1_18_R2) || Version.afterOrEqual(19)) {
            ENTITY_COUNTER_FIELD = new ReflectField<>(ENTITY_CLASS, "c");
        } else {
            ENTITY_COUNTER_FIELD = new ReflectField<>(ENTITY_CLASS, "b");
        }
        VEC_3D_A = new ReflectField<>(VEC_3D_CLASS, Version.afterOrEqual(19) ? "b" : "a").getValue(null);
    }

    @Override
    public int getFreeEntityId() {
        return ENTITY_COUNTER_FIELD.getValue(null).addAndGet(1);
    }

    @Override
    public int getEntityTypeId(EntityType type) {
        if (type == null) return -1;
        Object namespacedKey = ENTITY_TYPE_GET_KEY_METHOD.invoke(type);
        String key = NAMESPACED_KEY_GET_KEY_METHOD.invoke(namespacedKey);
        Optional<?> entityTypes = ENTITY_TYPES_A_METHOD.invokeStatic(key.toLowerCase());
        return entityTypes.map(entityType -> {
            Object registryBlocks = ENTITY_TYPES_REGISTRY_FIELD.getValue(null);
            return REGISTRY_BLOCKS_GET_ID_METHOD.<Integer>invoke(registryBlocks, entityType);
        }).orElse(-1);
    }

    @Override
    public float getEntityHeight(EntityType type) {
        if (type == null) return 0.0f;
        Object namespacedKey = ENTITY_TYPE_GET_KEY_METHOD.invoke(type);
        String key = NAMESPACED_KEY_GET_KEY_METHOD.invoke(namespacedKey);
        Optional<?> entityTypes = ENTITY_TYPES_A_METHOD.invokeStatic(key.toLowerCase());
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

        int entityTypeId = getEntityTypeId(entityType);
        if (entityTypeId == -1) return;
        showFakeEntity(player, location, entityTypeId, entityId);
    }

    private void showFakeEntity(Player player, Location location, int entityTypeId, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        if (Version.afterOrEqual(19)) {
            sendPacket(player, PACKET_SPAWN_ENTITY_CONSTRUCTOR.newInstance(
                    entityId,
                    UUID.randomUUID(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getYaw(),
                    location.getPitch(),
                    REGISTRY_BLOCKS_FROM_ID_METHOD.invoke(ENTITY_TYPES_REGISTRY_FIELD.getValue(null), entityTypeId),
                    0,
                    VEC_3D_A,
                    location.getYaw()
            ));
        } else {
            sendPacket(player, PACKET_SPAWN_ENTITY_CONSTRUCTOR.newInstance(
                    entityId,
                    UUID.randomUUID(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getYaw(),
                    location.getPitch(),
                    REGISTRY_BLOCKS_FROM_ID_METHOD.invoke(ENTITY_TYPES_REGISTRY_FIELD.getValue(null), entityTypeId),
                    0,
                    VEC_3D_A
            ));
        }
        teleportFakeEntity(player, location, entityId);
    }

    @Override
    public void showFakeEntityLiving(Player player, Location location, EntityType entityType, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        Validate.notNull(entityType);

        int entityTypeId = getEntityTypeId(entityType);
        if (entityTypeId == -1) return;
        if (Version.afterOrEqual(19)) {
            showFakeEntity(player, location, entityTypeId, entityId);
        } else {
            showFakeEntityLiving(player, location, entityTypeId, entityId);
        }
    }

    private void showFakeEntityLiving(Player player, Location location, int entityTypeId, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
        PACKET_DATA_SERIALIZER_WRITE_INT_METHOD.invoke(packetDataSerializer, entityId);
        PACKET_DATA_SERIALIZER_WRITE_UUID_METHOD.invoke(packetDataSerializer, MATH_HELPER_A_METHOD.<UUID>invokeStatic(RandomUtils.RANDOM));
        PACKET_DATA_SERIALIZER_WRITE_INT_METHOD.invoke(packetDataSerializer, entityTypeId);
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD.invoke(packetDataSerializer, location.getX());
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD.invoke(packetDataSerializer, location.getY());
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD.invoke(packetDataSerializer, location.getZ());
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD.invoke(packetDataSerializer, (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD.invoke(packetDataSerializer, (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD.invoke(packetDataSerializer, (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        PACKET_DATA_SERIALIZER_WRITE_SHORT_METHOD.invoke(packetDataSerializer, 0);
        PACKET_DATA_SERIALIZER_WRITE_SHORT_METHOD.invoke(packetDataSerializer, 0);
        PACKET_DATA_SERIALIZER_WRITE_SHORT_METHOD.invoke(packetDataSerializer, 0);
        sendPacket(player, PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR.newInstance(packetDataSerializer));
    }

    private static final Class<?> DWR_CLASS = ReflectionUtil.getNMClass("network.syncher.DataWatcherRegistry");
    private static final ReflectMethod DWI_GET_OBJECT_METHOD = new ReflectMethod(DWI_CLASS, "a");
    private static final ReflectMethod DWI_GET_VALUE_METHOD = new ReflectMethod(DWI_CLASS, "b");
    private static final ReflectMethod DWO_GET_SERIALIZER_METHOD = new ReflectMethod(DWO_CLASS, "b");
    private static final ReflectMethod DWO_GET_INDEX_METHOD = new ReflectMethod(DWO_CLASS, "a");
    private static final ReflectMethod DWS_GET_TYPE_ID_METHOD = new ReflectMethod(DWR_CLASS, "b", DWS_CLASS);
    private static final ReflectMethod DWS_SERIALIZE_METHOD = new ReflectMethod(DWS_CLASS, "a",
            PACKET_DATA_SERIALIZER_CLASS, Object.class);

    private void sendEntityMetadata(Player player, int entityId, List<Object> items) {
        Validate.notNull(player);
        Validate.notNull(items);

        Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
        PACKET_DATA_SERIALIZER_WRITE_INT_METHOD.invoke(packetDataSerializer, entityId);
        for (Object item : items) {
            if (!item.getClass().isAssignableFrom(DWI_CLASS)) {
                continue;
            }

            Object object = DWI_GET_OBJECT_METHOD.invoke(item);
            Object value = DWI_GET_VALUE_METHOD.invoke(item);
            Object serializer = DWO_GET_SERIALIZER_METHOD.invoke(object);
            int serializerIndex = DWO_GET_INDEX_METHOD.invoke(object);
            int serializerTypeId = DWS_GET_TYPE_ID_METHOD.invokeStatic(serializer);

            PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD.invoke(packetDataSerializer, (byte) serializerIndex);
            PACKET_DATA_SERIALIZER_WRITE_INT_METHOD.invoke(packetDataSerializer, serializerTypeId);
            DWS_SERIALIZE_METHOD.invoke(serializer, packetDataSerializer, value);

        }
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD.invoke(packetDataSerializer, 0xFF);
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(packetDataSerializer));
    }

    @Override
    public void showFakeEntityArmorStand(Player player, Location location, int entityId, boolean invisible, boolean small, boolean clickable) {
        Validate.notNull(player);
        Validate.notNull(location);

        List<Object> dataWatcherItems = new ArrayList<>();
        dataWatcherItems.add(DATA_WATCHER_ITEM_CONSTRUCTOR.newInstance(DWO_ENTITY_DATA, (byte) (invisible ? 0x20 : 0)));
        dataWatcherItems.add(DATA_WATCHER_ITEM_CONSTRUCTOR.newInstance(DWO_ARMOR_STAND_DATA, (byte) (0x08 | (small ? 0x01 : 0) | (clickable ? 0 : 0x10))));
        showFakeEntityLiving(player, location, EntityType.ARMOR_STAND, entityId);
        sendEntityMetadata(player, entityId, dataWatcherItems);
    }

    @Override
    public void showFakeEntityItem(Player player, Location location, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        Validate.notNull(itemStack);

        List<Object> dataWatcherItems = new ArrayList<>();
        dataWatcherItems.add(DATA_WATCHER_ITEM_CONSTRUCTOR.newInstance(DWO_ITEM, CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack)));
        showFakeEntity(player, location, getEntityTypeId(EntityType.DROPPED_ITEM), entityId);
        sendEntityMetadata(player, entityId, dataWatcherItems);
        teleportFakeEntity(player, location, entityId);
    }

    @Override
    public void updateFakeEntityCustomName(Player player, String name, int entityId) {
        Validate.notNull(player);
        Validate.notNull(name);

        List<Object> dataWatcherItems = new ArrayList<>();
        dataWatcherItems.add(DATA_WATCHER_ITEM_CONSTRUCTOR.newInstance(DWO_CUSTOM_NAME, Optional.ofNullable(CRAFT_CHAT_MESSAGE_FROM_STRING_METHOD.invokeStatic(name))));
        dataWatcherItems.add(DATA_WATCHER_ITEM_CONSTRUCTOR.newInstance(DWO_CUSTOM_NAME_VISIBLE, !ChatColor.stripColor(name).isEmpty()));
        sendEntityMetadata(player, entityId, dataWatcherItems);
    }

    @Override
    public void teleportFakeEntity(Player player, Location location, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
        PACKET_DATA_SERIALIZER_WRITE_INT_METHOD.invoke(packetDataSerializer, entityId);
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD.invoke(packetDataSerializer, location.getX());
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD.invoke(packetDataSerializer, location.getY());
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD.invoke(packetDataSerializer, location.getZ());
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD.invoke(packetDataSerializer, (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD.invoke(packetDataSerializer, (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        PACKET_DATA_SERIALIZER_WRITE_BOOLEAN_METHOD.invoke(packetDataSerializer, false);
        sendPacket(player, PACKET_ENTITY_TELEPORT_CONSTRUCTOR.newInstance(packetDataSerializer));
    }

    @Override
    public void helmetFakeEntity(Player player, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(itemStack);

        List<Object> items = new ArrayList<>();
        items.add(PAIR_OF_METHOD.invokeStatic(ENUM_ITEM_SLOT_FROM_NAME_METHOD.invokeStatic("head"), CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack)));
        sendPacket(player, PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR.newInstance(entityId, items));
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public void attachFakeEntity(Player player, int vehicleId, int entityId) {
        Validate.notNull(player);
        Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
        PACKET_DATA_SERIALIZER_WRITE_INT_METHOD.invoke(packetDataSerializer, vehicleId);
        PACKET_DATA_SERIALIZER_WRITE_INTS_METHOD.invoke(packetDataSerializer, (Object) new int[] {entityId});
        sendPacket(player, PACKET_MOUNT_CONSTRUCTOR.newInstance(packetDataSerializer));
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public void hideFakeEntities(Player player, int... entityIds) {
        Validate.notNull(player);
        sendPacket(player, PACKET_ENTITY_DESTROY_CONSTRUCTOR.newInstance((Object) entityIds));
    }

}

package eu.decentsoftware.holograms.api.nms.versions;

import eu.decentsoftware.holograms.api.nms.NMS;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.RandomUtils;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectConstructor;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectField;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
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
    private static final ReflectField<?> I_REGISTRY_Y_FIELD;
    private static final ReflectMethod REGISTRY_BLOCKS_FROM_ID_METHOD;
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
    private static final ReflectMethod MATH_HELPER_A_METHOD;
    // PACKET DATA SERIALIZER
    private static final Class<?> PACKET_DATA_SERIALIZER_CLASS;
    private static final ReflectConstructor PACKET_DATA_SERIALIZER_CONSTRUCTOR;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_D_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_A_UUID_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_A_INTS_METHOD;
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
    private static final Object DWO_CUSTOM_NAME;
    private static final Object DWO_CUSTOM_NAME_VISIBLE;
    private static final Object DWO_ENTITY_DATA;
    private static final Object DWO_ARMOR_STAND_DATA;
    private static final Object DWO_ITEM;
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
        // UTILITY
        ENTITY_CLASS = ReflectionUtil.getNMClass("world.entity.Entity");
        ENTITY_ARMOR_STAND_CLASS = ReflectionUtil.getNMClass("world.entity.decoration.EntityArmorStand");
        ENTITY_ITEM_CLASS = ReflectionUtil.getNMClass("world.entity.item.EntityItem");
        ENUM_ITEM_SLOT_CLASS = ReflectionUtil.getNMClass("world.entity.EnumItemSlot");
        ENTITY_TYPES_CLASS = ReflectionUtil.getNMClass("world.entity.EntityTypes");
        VEC_3D_CLASS = ReflectionUtil.getNMClass("world.phys.Vec3D");
        I_REGISTRY_Y_FIELD = new ReflectField<>(ReflectionUtil.getNMClass("core.IRegistry"), "Y");
        REGISTRY_BLOCKS_FROM_ID_METHOD = new ReflectMethod(ReflectionUtil.getNMClass("core.RegistryBlocks"), "fromId", int.class);
        ENUM_ITEM_SLOT_FROM_NAME_METHOD = new ReflectMethod(ENUM_ITEM_SLOT_CLASS, "fromName", String.class);
        CRAFT_ITEM_NMS_COPY_METHOD = new ReflectMethod(ReflectionUtil.getObcClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class);
        CRAFT_CHAT_MESSAGE_FROM_STRING_METHOD = new ReflectMethod(ReflectionUtil.getObcClass("util.CraftChatMessage"), "fromStringOrNull", String.class);
        PAIR_OF_METHOD = new ReflectMethod(ReflectionUtil.getClass("com.mojang.datafixers.util.Pair"), "of", Object.class, Object.class);
        // DATA WATCHER
        DATA_WATCHER_CLASS = ReflectionUtil.getNMClass("network.syncher.DataWatcher");
        DATA_WATCHER_CONSTRUCTOR = new ReflectConstructor(DATA_WATCHER_CLASS, ENTITY_CLASS);
        DATA_WATCHER_REGISTER_METHOD = new ReflectMethod(DATA_WATCHER_CLASS, "register", DWO_CLASS, Object.class);
        // MATH HELPER
        MATH_HELPER_CLASS = ReflectionUtil.getNMClass("util.MathHelper");
        MATH_HELPER_A_METHOD = new ReflectMethod(MATH_HELPER_CLASS, "a", Random.class);
        // PACKET DATA SERIALIZER
        PACKET_DATA_SERIALIZER_CLASS = ReflectionUtil.getNMClass("network.PacketDataSerializer");
        PACKET_DATA_SERIALIZER_CONSTRUCTOR = new ReflectConstructor(PACKET_DATA_SERIALIZER_CLASS, ByteBuf.class);
        PACKET_DATA_SERIALIZER_D_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "d", int.class);
        PACKET_DATA_SERIALIZER_A_UUID_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "a", UUID.class);
        PACKET_DATA_SERIALIZER_A_INTS_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "a", int[].class);
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "writeDouble", double.class);
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "writeByte", int.class);
        PACKET_DATA_SERIALIZER_WRITE_SHORT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "writeShort", int.class);
        PACKET_DATA_SERIALIZER_WRITE_BOOLEAN_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "writeBoolean", boolean.class);
        // PACKETS
        PACKET_SPAWN_ENTITY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutSpawnEntity"),
                int.class, UUID.class, double.class, double.class, double.class, float.class, float.class, ENTITY_TYPES_CLASS, int.class, VEC_3D_CLASS);
        PACKET_SPAWN_ENTITY_LIVING_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutSpawnEntityLiving"),
                PACKET_DATA_SERIALIZER_CLASS);
        PACKET_ENTITY_METADATA_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutEntityMetadata"),
                int.class, DATA_WATCHER_CLASS, boolean.class);
        PACKET_ENTITY_TELEPORT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutEntityTeleport"),
                PACKET_DATA_SERIALIZER_CLASS);
        PACKET_MOUNT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutMount"),
                PACKET_DATA_SERIALIZER_CLASS);
        PACKET_ENTITY_EQUIPMENT_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutEntityEquipment"),
                int.class, List.class);
        PACKET_ENTITY_DESTROY_CONSTRUCTOR = new ReflectConstructor(ReflectionUtil.getNMClass("network.protocol.game.PacketPlayOutEntityDestroy"),
                int[].class);
        // DATA WATCHER OBJECT
        DWO_ENTITY_DATA = new ReflectField<>(ENTITY_CLASS, "Z").getValue(null);
        DWO_CUSTOM_NAME = new ReflectField<>(ENTITY_CLASS, "aJ").getValue(null);
        DWO_CUSTOM_NAME_VISIBLE = new ReflectField<>(ENTITY_CLASS, "aK").getValue(null);
        DWO_ARMOR_STAND_DATA = new ReflectField<>(ENTITY_ARMOR_STAND_CLASS, "bG").getValue(null);
        DWO_ITEM = new ReflectField<>(ENTITY_ITEM_CLASS, "c").getValue(null);
        // ENTITY TYPES
        ENTITY_TYPES_A_METHOD = new ReflectMethod(ENTITY_TYPES_CLASS, "a", String.class);
        ENTITY_TYPE_GET_KEY_METHOD = new ReflectMethod(EntityType.class, "getKey");
        REGISTRY_BLOCKS_GET_ID_METHOD = new ReflectMethod(ReflectionUtil.getNMClass("core.RegistryBlocks"), "getId", Object.class);
        NAMESPACED_KEY_GET_KEY_METHOD = new ReflectMethod(ReflectionUtil.getClass("org.bukkit.NamespacedKey"), "getKey");
        ENTITY_TYPES_GET_SIZE_METHOD = new ReflectMethod(ENTITY_TYPES_CLASS, "m");
        ENTITY_SIZE_HEIGHT_FIELD = new ReflectField<>(ReflectionUtil.getNMClass("world.entity.EntitySize"), "b");

        ENTITY_COUNTER_FIELD = new ReflectField<>(ENTITY_CLASS, "b");
        VEC_3D_A = new ReflectField<>(VEC_3D_CLASS, "a").getValue(null);
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
        return entityTypes.map((entityType) -> {
            Object registryBlocks = I_REGISTRY_Y_FIELD.getValue(null);
            return REGISTRY_BLOCKS_GET_ID_METHOD.<Integer>invoke(registryBlocks, entityType);
        }).orElse(-1);
    }

    @Override
    public float getEntityHeigth(EntityType type) {
        if (type == null) return 0.0f;
        Object namespacedKey = ENTITY_TYPE_GET_KEY_METHOD.invoke(type);
        String key = NAMESPACED_KEY_GET_KEY_METHOD.invoke(namespacedKey);
        Optional<?> entityTypes = ENTITY_TYPES_A_METHOD.invokeStatic(key.toLowerCase());
        return entityTypes.map((entityType) -> {
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

        sendPacket(player, PACKET_SPAWN_ENTITY_CONSTRUCTOR.newInstance(
                entityId,
                MATH_HELPER_A_METHOD.invokeStatic(RandomUtils.RANDOM),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                REGISTRY_BLOCKS_FROM_ID_METHOD.invoke(I_REGISTRY_Y_FIELD.getValue(null), entityTypeId),
                0,
                VEC_3D_A
        ));
        teleportFakeEntity(player, location, entityId);
    }

    @Override
    public void showFakeEntityLiving(Player player, Location location, EntityType entityType, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        Validate.notNull(entityType);

        int entityTypeId = getEntityTypeId(entityType);
        if (entityTypeId == -1) return;
        showFakeEntityLiving(player, location, entityTypeId, entityId);
    }

    private void showFakeEntityLiving(Player player, Location location, int entityTypeId, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
        PACKET_DATA_SERIALIZER_D_METHOD.invoke(packetDataSerializer, entityId);
        PACKET_DATA_SERIALIZER_A_UUID_METHOD.invoke(packetDataSerializer, MATH_HELPER_A_METHOD.<UUID>invokeStatic(RandomUtils.RANDOM));
        PACKET_DATA_SERIALIZER_D_METHOD.invoke(packetDataSerializer, entityTypeId);
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

    @Override
    public void showFakeEntityArmorStand(Player player, Location location, int entityId, boolean invisible, boolean small, boolean clickable) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ENTITY_DATA, (byte) (invisible ? 0x20 : 0x00)); // Invisible
        byte data = 0x08;
        if (small) data += 0x01;
        if (!clickable) data += 0x10;
        DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ARMOR_STAND_DATA, data);
        showFakeEntityLiving(player, location, 1, entityId);
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
    }

    @Override
    public void showFakeEntityItem(Player player, Location location, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        Validate.notNull(itemStack);

        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_ITEM, CRAFT_ITEM_NMS_COPY_METHOD.invokeStatic(itemStack));
        showFakeEntity(player, location, 41, entityId);
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
        teleportFakeEntity(player, location, entityId);
    }

    @Override
    public void updateFakeEntityCustomName(Player player, String name, int entityId) {
        Validate.notNull(player);
        Validate.notNull(name);

        Object dataWatcher = DATA_WATCHER_CONSTRUCTOR.newInstance(ENTITY_CLASS.cast(null));
        DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_CUSTOM_NAME, Optional.ofNullable(CRAFT_CHAT_MESSAGE_FROM_STRING_METHOD.invokeStatic(name)));
        DATA_WATCHER_REGISTER_METHOD.invoke(dataWatcher, DWO_CUSTOM_NAME_VISIBLE, !ChatColor.stripColor(name).isEmpty());
        sendPacket(player, PACKET_ENTITY_METADATA_CONSTRUCTOR.newInstance(entityId, dataWatcher, true));
    }

    @Override
    public void teleportFakeEntity(Player player, Location location, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
        PACKET_DATA_SERIALIZER_D_METHOD.invoke(packetDataSerializer, entityId);
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD.invoke(packetDataSerializer, location.getX());
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD.invoke(packetDataSerializer, location.getY());
        PACKET_DATA_SERIALIZER_WRITE_DOUBLE_METHOD.invoke(packetDataSerializer, location.getZ());
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD.invoke(packetDataSerializer, (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        PACKET_DATA_SERIALIZER_WRITE_BYTE_METHOD.invoke(packetDataSerializer, (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        PACKET_DATA_SERIALIZER_WRITE_BOOLEAN_METHOD.invoke(packetDataSerializer, true);
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
    public void attachFakeEnity(Player player, int vehicleId, int entityId) {
        Validate.notNull(player);
        Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
        PACKET_DATA_SERIALIZER_D_METHOD.invoke(packetDataSerializer, vehicleId);
        PACKET_DATA_SERIALIZER_A_INTS_METHOD.invoke(packetDataSerializer, (Object) new int[] {entityId});
        sendPacket(player, PACKET_MOUNT_CONSTRUCTOR.newInstance(packetDataSerializer));
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public void hideFakeEntities(Player player, int... entityIds) {
        Validate.notNull(player);
        sendPacket(player, PACKET_ENTITY_DESTROY_CONSTRUCTOR.newInstance((Object) entityIds));
    }

}

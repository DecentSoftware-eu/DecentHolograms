package eu.decentsoftware.holograms.nms.v1_16_R1;

import eu.decentsoftware.holograms.shared.reflect.ReflectUtil;
import net.minecraft.server.v1_16_R1.DataWatcher;
import net.minecraft.server.v1_16_R1.DataWatcherObject;
import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.EntityArmorStand;
import net.minecraft.server.v1_16_R1.EntityItem;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.ItemStack;

import java.util.Optional;

class EntityMetadataType<T> {

    private static final DataWatcherObject<Byte> ENTITY_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "T");
    private static final DataWatcherObject<Optional<IChatBaseComponent>> ENTITY_CUSTOM_NAME_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "ax");
    private static final DataWatcherObject<Boolean> ENTITY_CUSTOM_NAME_VISIBLE_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "ay");
    private static final DataWatcherObject<Boolean> ENTITY_SILENT_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "az");
    private static final DataWatcherObject<Boolean> ENTITY_HAS_NO_GRAVITY_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "aA");
    private static final DataWatcherObject<Byte> ARMOR_STAND_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(EntityArmorStand.class, "b");
    private static final DataWatcherObject<ItemStack> ITEM_STACK_OBJECT
            = ReflectUtil.getFieldValue(EntityItem.class, "ITEM");

    static final EntityMetadataType<Byte> ENTITY_PROPERTIES = new EntityMetadataType<>(ENTITY_PROPERTIES_OBJECT);
    static final EntityMetadataType<Optional<IChatBaseComponent>> ENTITY_CUSTOM_NAME = new EntityMetadataType<>(ENTITY_CUSTOM_NAME_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_CUSTOM_NAME_VISIBLE = new EntityMetadataType<>(ENTITY_CUSTOM_NAME_VISIBLE_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_SILENT = new EntityMetadataType<>(ENTITY_SILENT_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_HAS_NO_GRAVITY = new EntityMetadataType<>(ENTITY_HAS_NO_GRAVITY_OBJECT);
    static final EntityMetadataType<Byte> ARMOR_STAND_PROPERTIES = new EntityMetadataType<>(ARMOR_STAND_PROPERTIES_OBJECT);
    static final EntityMetadataType<ItemStack> ITEM_STACK = new EntityMetadataType<>(ITEM_STACK_OBJECT);

    private final DataWatcherObject<T> dataWatcherObject;

    private EntityMetadataType(DataWatcherObject<T> dataWatcherObject) {
        this.dataWatcherObject = dataWatcherObject;
    }

    DataWatcher.Item<T> construct(T value) {
        return new DataWatcher.Item<>(dataWatcherObject, value);
    }

}

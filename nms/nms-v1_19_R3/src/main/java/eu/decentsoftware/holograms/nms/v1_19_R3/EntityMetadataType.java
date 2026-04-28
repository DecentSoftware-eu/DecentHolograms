package eu.decentsoftware.holograms.nms.v1_19_R3;

import eu.decentsoftware.holograms.shared.reflect.ReflectUtil;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

class EntityMetadataType<T> {

    private static final DataWatcherObject<Byte> ENTITY_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "an");
    private static final DataWatcherObject<Optional<IChatBaseComponent>> ENTITY_CUSTOM_NAME_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "aR");
    private static final DataWatcherObject<Boolean> ENTITY_CUSTOM_NAME_VISIBLE_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "aS");
    private static final DataWatcherObject<Boolean> ENTITY_SILENT_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "aT");
    private static final DataWatcherObject<Boolean> ENTITY_HAS_NO_GRAVITY_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "aU");
    private static final DataWatcherObject<Byte> ARMOR_STAND_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(EntityArmorStand.class, "bB");
    private static final DataWatcherObject<ItemStack> ITEM_STACK_OBJECT
            = ReflectUtil.getFieldValue(EntityItem.class, "c");

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

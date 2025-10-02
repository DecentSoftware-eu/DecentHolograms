package eu.decentsoftware.holograms.nms.paper_v1_21_R6;

import eu.decentsoftware.holograms.shared.reflect.ReflectUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

class EntityMetadataType<T> {

    private static final EntityDataAccessor<Byte> ENTITY_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "DATA_SHARED_FLAGS_ID");
    private static final EntityDataAccessor<Optional<Component>> ENTITY_CUSTOM_NAME_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "DATA_CUSTOM_NAME");
    private static final EntityDataAccessor<Boolean> ENTITY_CUSTOM_NAME_VISIBLE_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "DATA_CUSTOM_NAME_VISIBLE");
    private static final EntityDataAccessor<Boolean> ENTITY_SILENT_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "DATA_SILENT");
    private static final EntityDataAccessor<Boolean> ENTITY_HAS_NO_GRAVITY_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "DATA_NO_GRAVITY");
    private static final EntityDataAccessor<Byte> ARMOR_STAND_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(ArmorStand.class, "DATA_CLIENT_FLAGS");
    private static final EntityDataAccessor<ItemStack> ITEM_STACK_OBJECT
            = ReflectUtil.getFieldValue(ItemEntity.class, "DATA_ITEM");

    static final EntityMetadataType<Byte> ENTITY_PROPERTIES = new EntityMetadataType<>(ENTITY_PROPERTIES_OBJECT);
    static final EntityMetadataType<Optional<Component>> ENTITY_CUSTOM_NAME = new EntityMetadataType<>(ENTITY_CUSTOM_NAME_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_CUSTOM_NAME_VISIBLE = new EntityMetadataType<>(ENTITY_CUSTOM_NAME_VISIBLE_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_SILENT = new EntityMetadataType<>(ENTITY_SILENT_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_HAS_NO_GRAVITY = new EntityMetadataType<>(ENTITY_HAS_NO_GRAVITY_OBJECT);
    static final EntityMetadataType<Byte> ARMOR_STAND_PROPERTIES = new EntityMetadataType<>(ARMOR_STAND_PROPERTIES_OBJECT);
    static final EntityMetadataType<ItemStack> ITEM_STACK = new EntityMetadataType<>(ITEM_STACK_OBJECT);

    private final EntityDataAccessor<T> EntityDataAccessor;

    private EntityMetadataType(EntityDataAccessor<T> EntityDataAccessor) {
        this.EntityDataAccessor = EntityDataAccessor;
    }

    SynchedEntityData.DataItem<T> construct(T value) {
        return new SynchedEntityData.DataItem<>(EntityDataAccessor, value);
    }

}

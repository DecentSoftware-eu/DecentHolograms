package eu.decentsoftware.holograms.nms.v26_2;

import eu.decentsoftware.holograms.shared.reflect.ReflectUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3fc;

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
    // Displays
    private static final EntityDataAccessor<Vector3fc> DISPLAY_TRANSLATION_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "DATA_TRANSLATION_ID");
    private static final EntityDataAccessor<Vector3fc> DISPLAY_SCALE_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "DATA_SCALE_ID");
    private static final EntityDataAccessor<Byte> DISPLAY_BILLBOARD_CONSTRAINTS_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "DATA_BILLBOARD_RENDER_CONSTRAINTS_ID");
    private static final EntityDataAccessor<Integer> DISPLAY_BRIGHTNESS_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "DATA_BRIGHTNESS_OVERRIDE_ID");
    private static final EntityDataAccessor<Float> DISPLAY_SHADOW_RADIUS_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "DATA_SHADOW_RADIUS_ID");
    private static final EntityDataAccessor<Float> DISPLAY_SHADOW_STRENGTH_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "DATA_SHADOW_STRENGTH_ID");
    private static final EntityDataAccessor<Integer> DISPLAY_GLOW_COLOR_OVERRIDE_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "DATA_GLOW_COLOR_OVERRIDE_ID");
    // Text Display
    private static final EntityDataAccessor<Component> TEXT_DISPLAY_TEXT_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "DATA_TEXT_ID");
    private static final EntityDataAccessor<Integer> TEXT_DISPLAY_LINE_WIDTH_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "DATA_LINE_WIDTH_ID");
    private static final EntityDataAccessor<Integer> TEXT_DISPLAY_BACKGROUND_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "DATA_BACKGROUND_COLOR_ID");
    private static final EntityDataAccessor<Byte> TEXT_DISPLAY_TEXT_OPACITY_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "DATA_TEXT_OPACITY_ID");
    private static final EntityDataAccessor<Byte> TEXT_DISPLAY_DATA_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "DATA_STYLE_FLAGS_ID");
    // Item Display
    private static final EntityDataAccessor<ItemStack> ITEM_DISPLAY_ITEM_STACK_OBJECT
            = ReflectUtil.getFieldValue(Display.ItemDisplay.class, "DATA_ITEM_STACK_ID");
    private static final EntityDataAccessor<Byte> ITEM_DISPLAY_DATA_OBJECT
            = ReflectUtil.getFieldValue(Display.ItemDisplay.class, "DATA_ITEM_DISPLAY_ID");
    // Block Display
    private static final EntityDataAccessor<BlockState> BLOCK_DISPLAY_BLOCK_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(Display.BlockDisplay.class, "DATA_BLOCK_STATE_ID");

    static final EntityMetadataType<Byte> ENTITY_PROPERTIES = new EntityMetadataType<>(ENTITY_PROPERTIES_OBJECT);
    static final EntityMetadataType<Optional<Component>> ENTITY_CUSTOM_NAME = new EntityMetadataType<>(ENTITY_CUSTOM_NAME_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_CUSTOM_NAME_VISIBLE = new EntityMetadataType<>(ENTITY_CUSTOM_NAME_VISIBLE_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_SILENT = new EntityMetadataType<>(ENTITY_SILENT_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_HAS_NO_GRAVITY = new EntityMetadataType<>(ENTITY_HAS_NO_GRAVITY_OBJECT);
    static final EntityMetadataType<Byte> ARMOR_STAND_PROPERTIES = new EntityMetadataType<>(ARMOR_STAND_PROPERTIES_OBJECT);
    static final EntityMetadataType<ItemStack> ITEM_STACK = new EntityMetadataType<>(ITEM_STACK_OBJECT);
    // Displays
    static final EntityMetadataType<Vector3fc> DISPLAY_TRANSLATION = new EntityMetadataType<>(DISPLAY_TRANSLATION_OBJECT);
    static final EntityMetadataType<Vector3fc> DISPLAY_SCALE = new EntityMetadataType<>(DISPLAY_SCALE_OBJECT);
    static final EntityMetadataType<Byte> DISPLAY_BILLBOARD_CONSTRAINTS = new EntityMetadataType<>(DISPLAY_BILLBOARD_CONSTRAINTS_OBJECT);
    static final EntityMetadataType<Integer> DISPLAY_BRIGHTNESS = new EntityMetadataType<>(DISPLAY_BRIGHTNESS_OBJECT);
    static final EntityMetadataType<Float> DISPLAY_SHADOW_RADIUS = new EntityMetadataType<>(DISPLAY_SHADOW_RADIUS_OBJECT);
    static final EntityMetadataType<Float> DISPLAY_SHADOW_STRENGTH = new EntityMetadataType<>(DISPLAY_SHADOW_STRENGTH_OBJECT);
    static final EntityMetadataType<Integer> DISPLAY_GLOW_COLOR_OVERRIDE = new EntityMetadataType<>(DISPLAY_GLOW_COLOR_OVERRIDE_OBJECT);
    // Text Display
    static final EntityMetadataType<Component> TEXT_DISPLAY_TEXT = new EntityMetadataType<>(TEXT_DISPLAY_TEXT_OBJECT);
    static final EntityMetadataType<Integer> TEXT_DISPLAY_LINE_WIDTH = new EntityMetadataType<>(TEXT_DISPLAY_LINE_WIDTH_OBJECT);
    static final EntityMetadataType<Integer> TEXT_DISPLAY_BACKGROUND = new EntityMetadataType<>(TEXT_DISPLAY_BACKGROUND_OBJECT);
    static final EntityMetadataType<Byte> TEXT_DISPLAY_TEXT_OPACITY = new EntityMetadataType<>(TEXT_DISPLAY_TEXT_OPACITY_OBJECT);
    static final EntityMetadataType<Byte> TEXT_DISPLAY_DATA = new EntityMetadataType<>(TEXT_DISPLAY_DATA_OBJECT);
    // Item Display
    static final EntityMetadataType<ItemStack> ITEM_DISPLAY_ITEM_STACK = new EntityMetadataType<>(ITEM_DISPLAY_ITEM_STACK_OBJECT);
    static final EntityMetadataType<Byte> ITEM_DISPLAY_DATA = new EntityMetadataType<>(ITEM_DISPLAY_DATA_OBJECT);
    // Block Display
    static final EntityMetadataType<BlockState> BLOCK_DISPLAY_BLOCK_PROPERTIES = new EntityMetadataType<>(BLOCK_DISPLAY_BLOCK_PROPERTIES_OBJECT);

    private final EntityDataAccessor<T> dataWatcherObject;

    private EntityMetadataType(EntityDataAccessor<T> dataWatcherObject) {
        this.dataWatcherObject = dataWatcherObject;
    }

    SynchedEntityData.DataItem<T> construct(T value) {
        return new SynchedEntityData.DataItem<>(dataWatcherObject, value);
    }
}

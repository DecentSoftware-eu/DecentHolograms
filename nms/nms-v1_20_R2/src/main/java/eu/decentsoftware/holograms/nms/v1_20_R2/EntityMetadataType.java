package eu.decentsoftware.holograms.nms.v1_20_R2;

import eu.decentsoftware.holograms.shared.reflect.ReflectUtil;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.IBlockData;
import org.joml.Vector3f;

import java.util.Optional;

class EntityMetadataType<T> {

    private static final DataWatcherObject<Byte> ENTITY_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "ao");
    private static final DataWatcherObject<Optional<IChatBaseComponent>> ENTITY_CUSTOM_NAME_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "aU");
    private static final DataWatcherObject<Boolean> ENTITY_CUSTOM_NAME_VISIBLE_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "aV");
    private static final DataWatcherObject<Boolean> ENTITY_SILENT_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "aW");
    private static final DataWatcherObject<Boolean> ENTITY_HAS_NO_GRAVITY_OBJECT
            = ReflectUtil.getFieldValue(Entity.class, "aX");
    private static final DataWatcherObject<Byte> ARMOR_STAND_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(EntityArmorStand.class, "bC");
    private static final DataWatcherObject<ItemStack> ITEM_STACK_OBJECT
            = ReflectUtil.getFieldValue(EntityItem.class, "c");
    // Displays
    private static final DataWatcherObject<Vector3f> DISPLAY_TRANSLATION_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "t");
    private static final DataWatcherObject<Vector3f> DISPLAY_SCALE_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "u");
    private static final DataWatcherObject<Byte> DISPLAY_BILLBOARD_CONSTRAINTS_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "aF");
    private static final DataWatcherObject<Integer> DISPLAY_BRIGHTNESS_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "aG");
    private static final DataWatcherObject<Float> DISPLAY_SHADOW_RADIUS_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "aI");
    private static final DataWatcherObject<Float> DISPLAY_SHADOW_STRENGTH_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "aJ");
    private static final DataWatcherObject<Integer> DISPLAY_GLOW_COLOR_OVERRIDE_OBJECT
            = ReflectUtil.getFieldValue(Display.class, "aM");
    // Text Display
    private static final DataWatcherObject<IChatBaseComponent> TEXT_DISPLAY_TEXT_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "aM");
    private static final DataWatcherObject<Integer> TEXT_DISPLAY_LINE_WIDTH_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "aN");
    private static final DataWatcherObject<Integer> TEXT_DISPLAY_BACKGROUND_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "aO");
    private static final DataWatcherObject<Byte> TEXT_DISPLAY_TEXT_OPACITY_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "aP");
    private static final DataWatcherObject<Byte> TEXT_DISPLAY_DATA_OBJECT
            = ReflectUtil.getFieldValue(Display.TextDisplay.class, "aQ");
    // Item Display
    private static final DataWatcherObject<ItemStack> ITEM_DISPLAY_ITEM_STACK_OBJECT
            = ReflectUtil.getFieldValue(Display.ItemDisplay.class, "r");
    private static final DataWatcherObject<Byte> ITEM_DISPLAY_DATA_OBJECT
            = ReflectUtil.getFieldValue(Display.ItemDisplay.class, "s");
    // Block Display
    private static final DataWatcherObject<IBlockData> BLOCK_DISPLAY_BLOCK_PROPERTIES_OBJECT
            = ReflectUtil.getFieldValue(Display.BlockDisplay.class, "q");

    static final EntityMetadataType<Byte> ENTITY_PROPERTIES = new EntityMetadataType<>(ENTITY_PROPERTIES_OBJECT);
    static final EntityMetadataType<Optional<IChatBaseComponent>> ENTITY_CUSTOM_NAME = new EntityMetadataType<>(ENTITY_CUSTOM_NAME_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_CUSTOM_NAME_VISIBLE = new EntityMetadataType<>(ENTITY_CUSTOM_NAME_VISIBLE_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_SILENT = new EntityMetadataType<>(ENTITY_SILENT_OBJECT);
    static final EntityMetadataType<Boolean> ENTITY_HAS_NO_GRAVITY = new EntityMetadataType<>(ENTITY_HAS_NO_GRAVITY_OBJECT);
    static final EntityMetadataType<Byte> ARMOR_STAND_PROPERTIES = new EntityMetadataType<>(ARMOR_STAND_PROPERTIES_OBJECT);
    static final EntityMetadataType<ItemStack> ITEM_STACK = new EntityMetadataType<>(ITEM_STACK_OBJECT);
    // Displays
    static final EntityMetadataType<Vector3f> DISPLAY_TRANSLATION = new EntityMetadataType<>(DISPLAY_TRANSLATION_OBJECT);
    static final EntityMetadataType<Vector3f> DISPLAY_SCALE = new EntityMetadataType<>(DISPLAY_SCALE_OBJECT);
    static final EntityMetadataType<Byte> DISPLAY_BILLBOARD_CONSTRAINTS = new EntityMetadataType<>(DISPLAY_BILLBOARD_CONSTRAINTS_OBJECT);
    static final EntityMetadataType<Integer> DISPLAY_BRIGHTNESS = new EntityMetadataType<>(DISPLAY_BRIGHTNESS_OBJECT);
    static final EntityMetadataType<Float> DISPLAY_SHADOW_RADIUS = new EntityMetadataType<>(DISPLAY_SHADOW_RADIUS_OBJECT);
    static final EntityMetadataType<Float> DISPLAY_SHADOW_STRENGTH = new EntityMetadataType<>(DISPLAY_SHADOW_STRENGTH_OBJECT);
    static final EntityMetadataType<Integer> DISPLAY_GLOW_COLOR_OVERRIDE = new EntityMetadataType<>(DISPLAY_GLOW_COLOR_OVERRIDE_OBJECT);
    // Text Display
    static final EntityMetadataType<IChatBaseComponent> TEXT_DISPLAY_TEXT = new EntityMetadataType<>(TEXT_DISPLAY_TEXT_OBJECT);
    static final EntityMetadataType<Integer> TEXT_DISPLAY_LINE_WIDTH = new EntityMetadataType<>(TEXT_DISPLAY_LINE_WIDTH_OBJECT);
    static final EntityMetadataType<Integer> TEXT_DISPLAY_BACKGROUND = new EntityMetadataType<>(TEXT_DISPLAY_BACKGROUND_OBJECT);
    static final EntityMetadataType<Byte> TEXT_DISPLAY_TEXT_OPACITY = new EntityMetadataType<>(TEXT_DISPLAY_TEXT_OPACITY_OBJECT);
    static final EntityMetadataType<Byte> TEXT_DISPLAY_DATA = new EntityMetadataType<>(TEXT_DISPLAY_DATA_OBJECT);
    // Item Display
    static final EntityMetadataType<ItemStack> ITEM_DISPLAY_ITEM_STACK = new EntityMetadataType<>(ITEM_DISPLAY_ITEM_STACK_OBJECT);
    static final EntityMetadataType<Byte> ITEM_DISPLAY_DATA = new EntityMetadataType<>(ITEM_DISPLAY_DATA_OBJECT);
    // Block Display
    static final EntityMetadataType<IBlockData> BLOCK_DISPLAY_BLOCK_PROPERTIES = new EntityMetadataType<>(BLOCK_DISPLAY_BLOCK_PROPERTIES_OBJECT);

    private final DataWatcherObject<T> dataWatcherObject;

    private EntityMetadataType(DataWatcherObject<T> dataWatcherObject) {
        this.dataWatcherObject = dataWatcherObject;
    }

    DataWatcher.Item<T> construct(T value) {
        return new DataWatcher.Item<>(dataWatcherObject, value);
    }

}

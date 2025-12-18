package eu.decentsoftware.holograms.nms.v1_21_R5;

import com.google.common.base.Strings;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayType;
import eu.decentsoftware.holograms.nms.api.display.data.NmsDisplayAttribute;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayAlignment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R5.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class EntityMetadataBuilder {

    private final List<DataWatcher.Item<?>> watchableObjects;

    private EntityMetadataBuilder() {
        this.watchableObjects = new ArrayList<>();
    }

    List<DataWatcher.Item<?>> toWatchableObjects() {
        return watchableObjects;
    }

    EntityMetadataBuilder withInvisible() {
        /*
         * Entity Properties:
         * 0x01 - On Fire
         * 0x02 - Crouched
         * 0x08 - Sprinting
         * 0x10 - Swimming
         * 0x20 - Invisible
         * 0x40 - Has glowing effect
         * 0x80 - If flying with an elytra
         */

        watchableObjects.add(EntityMetadataType.ENTITY_PROPERTIES.construct((byte) 0x20));
        return this;
    }

    EntityMetadataBuilder withGlowing(boolean glowing) {
        byte data = (byte) (glowing ? 0x40 : 0x00);

        watchableObjects.add(EntityMetadataType.ENTITY_PROPERTIES.construct(data));
        return this;
    }

    EntityMetadataBuilder withArmorStandProperties(boolean small, boolean marker) {
        /*
         * Armor Stand Properties:
         * 0x01 - Small
         * 0x02 - Unused
         * 0x04 - Has Arms
         * 0x08 - Remove Baseplate
         * 0x10 - Marker (Zero bounding box)
         */

        byte data = 0x08; // Always Remove Baseplate
        if (small) {
            data |= 0x01;
        }
        if (marker) {
            data |= 0x10;
        }

        watchableObjects.add(EntityMetadataType.ARMOR_STAND_PROPERTIES.construct(data));
        return this;
    }

    EntityMetadataBuilder withCustomName(String customName) {
        IChatBaseComponent iChatBaseComponent = CraftChatMessage.fromStringOrNull(customName);
        Optional<IChatBaseComponent> optionalIChatBaseComponent = Optional.ofNullable(iChatBaseComponent);
        watchableObjects.add(EntityMetadataType.ENTITY_CUSTOM_NAME.construct(optionalIChatBaseComponent));
        boolean visible = !Strings.isNullOrEmpty(customName);
        watchableObjects.add(EntityMetadataType.ENTITY_CUSTOM_NAME_VISIBLE.construct(visible));
        return this;
    }

    EntityMetadataBuilder withItemStack(ItemStack itemStack) {
        watchableObjects.add(EntityMetadataType.ITEM_STACK.construct(CraftItemStack.asNMSCopy(itemStack)));
        return this;
    }

    EntityMetadataBuilder withSilent() {
        watchableObjects.add(EntityMetadataType.ENTITY_SILENT.construct(true));
        return this;
    }

    EntityMetadataBuilder withNoGravity() {
        watchableObjects.add(EntityMetadataType.ENTITY_HAS_NO_GRAVITY.construct(true));
        return this;
    }

    EntityMetadataBuilder withDisplayTranslation(NmsDisplayAttribute<DisplayVector3f> translationAttribute) {
        if (translationAttribute == null) {
            return this;
        }

        DisplayVector3f translation = translationAttribute.getValue();
        Vector3f translationVector;
        if (translation == null) {
            translationVector = new Vector3f(0.0f, 0.0f, 0.0f);
        } else {
            translationVector = new Vector3f(translation.getX(), translation.getY(), translation.getZ());
        }

        watchableObjects.add(EntityMetadataType.DISPLAY_TRANSLATION.construct(translationVector));
        return this;
    }

    EntityMetadataBuilder withDisplayScale(NmsDisplayAttribute<DisplayVector3f> scaleAttribute) {
        if (scaleAttribute == null) {
            return this;
        }

        DisplayVector3f scale = scaleAttribute.getValue();
        Vector3f scaleVector;
        if (scale == null) {
            scaleVector = new Vector3f(1.0f, 1.0f, 1.0f);
        } else {
            scaleVector = new Vector3f(scale.getX(), scale.getY(), scale.getZ());
        }

        watchableObjects.add(EntityMetadataType.DISPLAY_SCALE.construct(scaleVector));
        return this;
    }

    EntityMetadataBuilder withDisplayBillboardConstraints(NmsDisplayAttribute<DisplayBillboardConstraints> billboardAttribute) {
        /*
         * Billboard Constraints:
         * - 0x00 - fixed
         * - 0x01 - vertical
         * - 0x02 - horizontal
         * - 0x03 - center
         */
        if (billboardAttribute == null) {
            return this;
        }

        DisplayBillboardConstraints constraints = billboardAttribute.getValue();
        if (constraints == null) {
            constraints = DisplayBillboardConstraints.FIXED;
        }
        byte data = switch (constraints) {
            case VERTICAL -> 0x01;
            case HORIZONTAL -> 0x02;
            case CENTER -> 0x03;
            default -> 0x00; // FIXED
        };

        watchableObjects.add(EntityMetadataType.DISPLAY_BILLBOARD_CONSTRAINTS.construct(data));
        return this;
    }

    EntityMetadataBuilder withDisplayBrightness(NmsDisplayAttribute<DisplayBrightness> brightnessAttribute) {
        if (brightnessAttribute == null) {
            return this;
        }

        DisplayBrightness brightness = brightnessAttribute.getValue();
        int brightnessInt;
        if (brightness == null) {
            brightnessInt = -1;
        } else {
            brightnessInt = brightness.getBlockLight() << 4 | brightness.getSkyLight() << 20;
        }

        watchableObjects.add(EntityMetadataType.DISPLAY_BRIGHTNESS.construct(brightnessInt));
        return this;
    }

    EntityMetadataBuilder withDisplayShadowRadius(NmsDisplayAttribute<Float> shadowRadius) {
        if (shadowRadius == null) {
            return this;
        }
        float shadowRadiusFloat = shadowRadius.getValue() == null ? 0.0f : shadowRadius.getValue();

        watchableObjects.add(EntityMetadataType.DISPLAY_SHADOW_RADIUS.construct(shadowRadiusFloat));
        return this;
    }

    EntityMetadataBuilder withDisplayShadowStrength(NmsDisplayAttribute<Float> shadowStrength) {
        if (shadowStrength == null) {
            return this;
        }
        float shadowStrengthFloat = shadowStrength.getValue() == null ? 1.0f : shadowStrength.getValue();

        watchableObjects.add(EntityMetadataType.DISPLAY_SHADOW_STRENGTH.construct(shadowStrengthFloat));
        return this;
    }

    EntityMetadataBuilder withDisplayGlowColorOverride(NmsDisplayAttribute<DisplayColor> glowColorOverrideAttribute) {
        if (glowColorOverrideAttribute == null) {
            return this;
        }
        DisplayColor glowColorOverride = glowColorOverrideAttribute.getValue();
        int glowColorRgb = glowColorOverride == null ? -1 : glowColorOverride.asRGB();

        watchableObjects.add(EntityMetadataType.DISPLAY_GLOW_COLOR_OVERRIDE.construct(glowColorRgb));
        return this;
    }

    EntityMetadataBuilder withTextDisplayText(String text) {
        IChatBaseComponent component;
        if (text == null) {
            component = null;
        } else {
            component = CraftChatMessage.fromString(text, true, true)[0];
        }

        watchableObjects.add(EntityMetadataType.TEXT_DISPLAY_TEXT.construct(component));
        return this;
    }

    EntityMetadataBuilder withTextDisplayLineWidth(NmsDisplayAttribute<Integer> lineWidth) {
        if (lineWidth == null) {
            return this;
        }
        int lineWidthInt = lineWidth.getValue() == null ? 200 : lineWidth.getValue();

        watchableObjects.add(EntityMetadataType.TEXT_DISPLAY_LINE_WIDTH.construct(lineWidthInt));
        return this;
    }

    EntityMetadataBuilder withTextDisplayBackground(NmsDisplayAttribute<DisplayColor> backgroundColorAttribute) {
        if (backgroundColorAttribute == null) {
            return this;
        }
        DisplayColor backgroundColor = backgroundColorAttribute.getValue();
        int backgroundColorRgb = backgroundColor == null ? 0x40000000 : backgroundColor.asARGB();

        watchableObjects.add(EntityMetadataType.TEXT_DISPLAY_BACKGROUND.construct(backgroundColorRgb));
        return this;
    }

    EntityMetadataBuilder withTextDisplayTextOpacity(NmsDisplayAttribute<Byte> textOpacity) {
        if (textOpacity == null) {
            return this;
        }
        byte textOpacityByte = textOpacity.getValue() == null ? -1 : textOpacity.getValue();

        watchableObjects.add(EntityMetadataType.TEXT_DISPLAY_TEXT_OPACITY.construct(textOpacityByte));
        return this;
    }

    EntityMetadataBuilder withTextDisplayProperties(NmsDisplayAttribute<Boolean> hasShadowAttribute,
                                                    NmsDisplayAttribute<Boolean> isSeeThroughAttribute,
                                                    NmsDisplayAttribute<TextDisplayAlignment> alignAttribute) {
        /*
         * Text Display Data:
         * 0x01 - Has shadow
         * 0x02 - Is see-through
         * 0x04	- Use default background color
         * 0x08 - Align Left
         * 0x10 - Align Right
         */

        byte data = 0x00;
        if (hasShadowAttribute != null && hasShadowAttribute.getValue()) {
            data |= 0x01;
        }
        if (isSeeThroughAttribute != null && isSeeThroughAttribute.getValue()) {
            data |= 0x02;
        }
        if (alignAttribute != null) {
            if (alignAttribute.getValue() == TextDisplayAlignment.LEFT) {
                data |= 0x08;
            } else if (alignAttribute.getValue() == TextDisplayAlignment.RIGHT) {
                data |= 0x10;
            }
        }

        watchableObjects.add(EntityMetadataType.TEXT_DISPLAY_DATA.construct(data));
        return this;
    }

    EntityMetadataBuilder withItemDisplayItemStack(ItemStack itemStack) {
        watchableObjects.add(EntityMetadataType.ITEM_DISPLAY_ITEM_STACK.construct(CraftItemStack.asNMSCopy(itemStack)));
        return this;
    }

    EntityMetadataBuilder withItemDisplayData(NmsDisplayAttribute<ItemDisplayType> displayTypeAttribute) {
        /*
         * Display type:
         * 0x00 - NONE
         * 0x01 - THIRD_PERSON_LEFT_HAND
         * 0x02 - THIRD_PERSON_RIGHT_HAND
         * 0x03 - FIRST_PERSON_LEFT_HAND
         * 0x04 - FIRST_PERSON_RIGHT_HAND
         * 0x05 - HEAD
         * 0x06 - GUI
         * 0x07 - GROUND
         * 0x08 - FIXED
         */
        if (displayTypeAttribute == null) {
            return this;
        }

        ItemDisplayType displayType = displayTypeAttribute.getValue();
        if (displayType == null) {
            displayType = ItemDisplayType.NONE;
        }
        byte data = switch (displayType) {
            case THIRD_PERSON_LEFT_HAND -> 0x01;
            case THIRD_PERSON_RIGHT_HAND -> 0x02;
            case FIRST_PERSON_LEFT_HAND -> 0x03;
            case FIRST_PERSON_RIGHT_HAND -> 0x04;
            case HEAD -> 0x05;
            case GUI -> 0x06;
            case GROUND -> 0x07;
            case FIXED -> 0x08;
            default -> 0x00; // NONE
        };

        watchableObjects.add(EntityMetadataType.ITEM_DISPLAY_DATA.construct(data));
        return this;
    }

    EntityMetadataBuilder withBlockDisplayBlockData(Material material) {
        Block block = BuiltInRegistries.e.a(MinecraftKey.a("minecraft:" + material.name().toLowerCase(), ':'));

        watchableObjects.add(EntityMetadataType.BLOCK_DISPLAY_BLOCK_PROPERTIES.construct(block.m()));
        return this;
    }

    static EntityMetadataBuilder create() {
        return new EntityMetadataBuilder();
    }

}

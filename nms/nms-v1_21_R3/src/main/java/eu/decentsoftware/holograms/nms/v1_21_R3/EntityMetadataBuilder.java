package eu.decentsoftware.holograms.nms.v1_21_R3;

import com.google.common.base.Strings;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayAlignment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class EntityMetadataBuilder {

    private static final LegacyTextFormattingParserImpl textFormattingParser = new LegacyTextFormattingParserImpl();
    private final List<DataWatcher.Item<?>> watchableObjects;

    private EntityMetadataBuilder() {
        this.watchableObjects = new ArrayList<>();
    }

    static EntityMetadataBuilder create() {
        return new EntityMetadataBuilder();
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
        IChatBaseComponent iChatBaseComponent = textFormattingParser.parseLine(customName);
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

    EntityMetadataBuilder withGlowing(boolean glowing) {
        byte data = (byte) (glowing ? 0x40 : 0x00);

        watchableObjects.add(EntityMetadataType.ENTITY_PROPERTIES.construct(data));
        return this;
    }

    EntityMetadataBuilder withDisplayTranslation(DecentVector3f translation) {
        if (translation == null) {
            return this;
        }

        Vector3f translationVector = new Vector3f(translation.getX(), translation.getY(), translation.getZ());

        watchableObjects.add(EntityMetadataType.DISPLAY_TRANSLATION.construct(translationVector));
        return this;
    }

    EntityMetadataBuilder withDisplayScale(DecentVector3f scale) {
        if (scale == null) {
            return this;
        }

        Vector3f scaleVector = new Vector3f(scale.getX(), scale.getY(), scale.getZ());

        watchableObjects.add(EntityMetadataType.DISPLAY_SCALE.construct(scaleVector));
        return this;
    }

    EntityMetadataBuilder withDisplayBillboardConstraints(DisplayBillboardConstraints billboard) {
        /*
         * Billboard Constraints:
         * - 0x00 - fixed
         * - 0x01 - vertical
         * - 0x02 - horizontal
         * - 0x03 - center
         */
        if (billboard == null) {
            return this;
        }

        byte data = switch (billboard) {
            case VERTICAL -> 0x01;
            case HORIZONTAL -> 0x02;
            case CENTER -> 0x03;
            default -> 0x00; // FIXED
        };

        watchableObjects.add(EntityMetadataType.DISPLAY_BILLBOARD_CONSTRAINTS.construct(data));
        return this;
    }

    EntityMetadataBuilder withDisplayBrightness(DisplayBrightness brightness) {
        if (brightness == null) {
            return this;
        }

        int brightnessInt = brightness.getBlockLight() << 4 | brightness.getSkyLight() << 20;

        watchableObjects.add(EntityMetadataType.DISPLAY_BRIGHTNESS.construct(brightnessInt));
        return this;
    }

    EntityMetadataBuilder withDisplayShadowRadius(float shadowRadius) {
        watchableObjects.add(EntityMetadataType.DISPLAY_SHADOW_RADIUS.construct(shadowRadius));
        return this;
    }

    EntityMetadataBuilder withDisplayShadowStrength(float shadowStrength) {
        watchableObjects.add(EntityMetadataType.DISPLAY_SHADOW_STRENGTH.construct(shadowStrength));
        return this;
    }

    EntityMetadataBuilder withDisplayGlowColorOverride(DecentColor glowColorOverride) {
        if (glowColorOverride == null) {
            return this;
        }

        watchableObjects.add(EntityMetadataType.DISPLAY_GLOW_COLOR_OVERRIDE.construct(glowColorOverride.asRGB()));
        return this;
    }

    EntityMetadataBuilder withTextDisplayText(List<String> text) {
        IChatBaseComponent component = textFormattingParser.parse(text);

        watchableObjects.add(EntityMetadataType.TEXT_DISPLAY_TEXT.construct(component));
        return this;
    }

    EntityMetadataBuilder withTextDisplayLineWidth(int lineWidth) {
        watchableObjects.add(EntityMetadataType.TEXT_DISPLAY_LINE_WIDTH.construct(lineWidth));
        return this;
    }

    EntityMetadataBuilder withTextDisplayBackground(DecentColor backgroundColor) {
        if (backgroundColor == null) {
            return this;
        }

        watchableObjects.add(EntityMetadataType.TEXT_DISPLAY_BACKGROUND.construct(backgroundColor.asARGB()));
        return this;
    }

    EntityMetadataBuilder withTextDisplayTextOpacity(int textOpacity) {
        watchableObjects.add(EntityMetadataType.TEXT_DISPLAY_TEXT_OPACITY.construct(intToByte(textOpacity)));
        return this;
    }

    private byte intToByte(int i) {
        return (byte) (i & 0xFF);
    }

    EntityMetadataBuilder withTextDisplayProperties(boolean hasShadow,
                                                    boolean isSeeThrough,
                                                    TextDisplayAlignment alignment) {
        /*
         * Text Display Data:
         * 0x01 - Has shadow
         * 0x02 - Is see-through
         * 0x04	- Use default background color
         * 0x08 - Align Left
         * 0x10 - Align Right
         */

        byte data = 0x00;
        if (hasShadow) {
            data |= 0x01;
        }
        if (isSeeThrough) {
            data |= 0x02;
        }
        if (alignment != null) {
            if (alignment == TextDisplayAlignment.LEFT) {
                data |= 0x08;
            } else if (alignment == TextDisplayAlignment.RIGHT) {
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

    EntityMetadataBuilder withItemDisplayData(ItemDisplayType displayType) {
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
        if (displayType == null) {
            return this;
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
}

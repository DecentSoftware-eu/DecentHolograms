/*
 * This file is part of DecentHolograms, licensed under the GNU GPL v3.0 License.
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.display.rendering;

import eu.decentsoftware.holograms.display.attributes.DisplayAttribute;
import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.ItemDisplay;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.nms.api.display.data.BlockDisplayData;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayData;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayData;
import eu.decentsoftware.holograms.nms.api.display.data.NmsDisplayAttribute;
import eu.decentsoftware.holograms.nms.api.display.data.NmsDisplayAttributeType;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayData;
import org.bukkit.entity.Player;

public class DisplayDataMapper {

    public TextDisplayData mapTextDisplay(TextDisplay textDisplay, Player player) {
        TextDisplayData data = new TextDisplayData();
        setCommonAttributes(textDisplay, data);
        data.setText(textDisplay.getText(player));
        data.setLineWidthAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_LINE_WIDTH, textDisplay.getLineWidthAttribute()));
        data.setBackgroundColorAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_BACKGROUND_COLOR, textDisplay.getBackgroundColorAttribute()));
        data.setTextOpacityAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_OPACITY, textDisplay.getTextOpacityAttribute()));
        data.setTextShadowAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_SHADOW, textDisplay.getTextShadowAttribute()));
        data.setSeeThroughAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_SEE_THROUGH, textDisplay.getSeeThroughAttribute()));
        data.setAlignmentAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_ALIGNMENT, textDisplay.getAlignmentAttribute()));
        return data;
    }

    public ItemDisplayData mapItemDisplay(ItemDisplay itemDisplay, Player player) {
        ItemDisplayData data = new ItemDisplayData();
        setCommonAttributes(itemDisplay, data);
        data.setDisplayedItem(itemDisplay.getDisplayedItem().parse(player));
        data.setDisplayType(mapAttribute(NmsDisplayAttributeType.ITEM_DISPLAY_TYPE, itemDisplay.getDisplayTypeAttribute()));
        data.setGlowColor(mapAttribute(NmsDisplayAttributeType.ITEM_GLOW_COLOR, itemDisplay.getGlowColorAttribute()));
        return data;
    }

    public BlockDisplayData mapBlockDisplay(BlockDisplay blockDisplay) {
        BlockDisplayData data = new BlockDisplayData();
        setCommonAttributes(blockDisplay, data);
        data.setMaterial(blockDisplay.getMaterial());
        data.setGlowColor(mapAttribute(NmsDisplayAttributeType.BLOCK_GLOW_COLOR, blockDisplay.getGlowColorAttribute()));
        return data;
    }

    private void setCommonAttributes(DisplayBase display, DisplayData data) {
        data.setTranslation(mapAttribute(NmsDisplayAttributeType.TRANSLATION, display.getTranslationAttribute()));
        data.setScale(mapAttribute(NmsDisplayAttributeType.SCALE, display.getScaleAttribute()));
        data.setBillboardConstraints(mapAttribute(NmsDisplayAttributeType.BILLBOARD, display.getBillboardAttribute()));
        data.setBrightnessOverride(mapAttribute(NmsDisplayAttributeType.BRIGHTNESS, display.getBrightnessAttribute()));
        data.setShadowRadiusAttribute(mapAttribute(NmsDisplayAttributeType.SHADOW_RADIUS, display.getShadowRadiusAttribute()));
        data.setShadowStrengthAttribute(mapAttribute(NmsDisplayAttributeType.SHADOW_STRENGTH, display.getShadowStrengthAttribute()));
    }

    private <A> NmsDisplayAttribute<A> mapAttribute(NmsDisplayAttributeType type, DisplayAttribute<A> attribute) {
        if (attribute == null) {
            return null;
        }

        return new NmsDisplayAttribute<>(type, attribute.getValue());
    }
}

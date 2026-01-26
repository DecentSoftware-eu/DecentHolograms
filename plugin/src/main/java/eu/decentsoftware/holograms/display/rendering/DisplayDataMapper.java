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

import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.ItemDisplay;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.definition.general.BillboardAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.BrightnessAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.GlowColorAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.ScaleAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.ShadowRadiusAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.ShadowStrengthAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.TranslationAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.item.ItemDisplayTypeAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextAlignmentAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextBackgroundColorAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextOpacityAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextSeeThroughAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextShadowAttributeDefinition;
import eu.decentsoftware.holograms.nms.api.display.data.BlockDisplayData;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayData;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayData;
import eu.decentsoftware.holograms.nms.api.display.data.NmsDisplayAttribute;
import eu.decentsoftware.holograms.nms.api.display.data.NmsDisplayAttributeType;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayData;
import org.bukkit.entity.Player;

public class DisplayDataMapper {

    public TextDisplayData mapTextDisplay(TextDisplay textDisplay, String text) {
        TextDisplayData data = new TextDisplayData();
        setCommonAttributes(textDisplay, data);
        data.setText(text);
        data.setBackgroundColorAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_BACKGROUND_COLOR,
                textDisplay.getAttribute(TextBackgroundColorAttributeDefinition.KEY)));
        data.setTextOpacityAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_OPACITY,
                textDisplay.getAttribute(TextOpacityAttributeDefinition.KEY)));
        data.setTextShadowAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_SHADOW,
                textDisplay.getAttribute(TextShadowAttributeDefinition.KEY)));
        data.setSeeThroughAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_SEE_THROUGH,
                textDisplay.getAttribute(TextSeeThroughAttributeDefinition.KEY)));
        data.setAlignmentAttribute(mapAttribute(NmsDisplayAttributeType.TEXT_ALIGNMENT,
                textDisplay.getAttribute(TextAlignmentAttributeDefinition.KEY)));
        return data;
    }

    public ItemDisplayData mapItemDisplay(ItemDisplay itemDisplay, Player player) {
        ItemDisplayData data = new ItemDisplayData();
        setCommonAttributes(itemDisplay, data);
        data.setDisplayedItem(itemDisplay.getDisplayedItem().parse(player));
        data.setDisplayType(mapAttribute(NmsDisplayAttributeType.ITEM_DISPLAY_TYPE,
                itemDisplay.getAttribute(ItemDisplayTypeAttributeDefinition.KEY)));
        data.setGlowColor(mapAttribute(NmsDisplayAttributeType.ITEM_GLOW_COLOR,
                itemDisplay.getAttribute(GlowColorAttributeDefinition.KEY)));
        return data;
    }

    public BlockDisplayData mapBlockDisplay(BlockDisplay blockDisplay) {
        BlockDisplayData data = new BlockDisplayData();
        setCommonAttributes(blockDisplay, data);
        data.setMaterial(blockDisplay.getMaterial());
        data.setGlowColor(mapAttribute(NmsDisplayAttributeType.BLOCK_GLOW_COLOR,
                blockDisplay.getAttribute(GlowColorAttributeDefinition.KEY)));
        return data;
    }

    private void setCommonAttributes(DisplayBase display, DisplayData data) {
        data.setTranslation(mapAttribute(NmsDisplayAttributeType.TRANSLATION,
                display.getAttribute(TranslationAttributeDefinition.KEY)));
        data.setScale(mapAttribute(NmsDisplayAttributeType.SCALE,
                display.getAttribute(ScaleAttributeDefinition.KEY)));
        data.setBillboardConstraints(mapAttribute(NmsDisplayAttributeType.BILLBOARD,
                display.getAttribute(BillboardAttributeDefinition.KEY)));
        data.setBrightnessOverride(mapAttribute(NmsDisplayAttributeType.BRIGHTNESS,
                display.getAttribute(BrightnessAttributeDefinition.KEY)));
        data.setShadowRadiusAttribute(mapAttribute(NmsDisplayAttributeType.SHADOW_RADIUS,
                display.getAttribute(ShadowRadiusAttributeDefinition.KEY)));
        data.setShadowStrengthAttribute(mapAttribute(NmsDisplayAttributeType.SHADOW_STRENGTH,
                display.getAttribute(ShadowStrengthAttributeDefinition.KEY)));
    }

    private <A> NmsDisplayAttribute<A> mapAttribute(NmsDisplayAttributeType type, DisplayAttribute<A> attribute) {
        if (attribute == null) {
            return null;
        }

        return new NmsDisplayAttribute<>(type, attribute.getValue());
    }
}

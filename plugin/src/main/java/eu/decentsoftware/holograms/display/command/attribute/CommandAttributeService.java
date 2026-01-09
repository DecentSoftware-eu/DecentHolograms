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

package eu.decentsoftware.holograms.display.command.attribute;

import com.google.common.collect.ImmutableMap;
import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.ItemDisplay;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayType;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayAlignment;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandAttributeService {

    private static final Map<String, CommandAttribute> TEXT_ATTRIBUTES;
    private static final Map<String, CommandAttribute> ITEM_ATTRIBUTES;
    private static final Map<String, CommandAttribute> BLOCK_ATTRIBUTES;

    static {
        List<CommandAttribute> generalAttributes = Arrays.asList(
                new Vector3fCommandAttribute<>("translation", -8, 8, DisplayBase::setTranslationAttribute, DisplayBase::getTranslationAttribute, DisplayBase.class),
                new Vector3fCommandAttribute<>("scale", 0, 100, DisplayBase::setScaleAttribute, DisplayBase::getScaleAttribute, DisplayBase.class),
                new EnumCommandAttribute<>("billboard", DisplayBillboardConstraints.class, DisplayBase::setBillboardAttribute, DisplayBase::getBillboardAttribute, DisplayBase.class),
                new BrightnessCommandAttribute(DisplayBase::setBrightnessAttribute, DisplayBase::getBrightnessAttribute),
                new FloatCommandAttribute<>("shadow-radius", 0, 32, DisplayBase::setShadowRadiusAttribute, DisplayBase::getShadowRadiusAttribute, DisplayBase.class),
                new FloatCommandAttribute<>("shadow-strength", 0, 32, DisplayBase::setShadowStrengthAttribute, DisplayBase::getShadowStrengthAttribute, DisplayBase.class)
        );

        Map<String, CommandAttribute> textAttributes = new HashMap<>();
        addAllAttributes(textAttributes, generalAttributes);
        // TODO: add limits, or remove and make constant
        addAttribute(textAttributes, new IntegerCommandAttribute<>("line-width", 0, Integer.MAX_VALUE, TextDisplay::setLineWidthAttribute, TextDisplay::getLineWidthAttribute, TextDisplay.class));
        addAttribute(textAttributes, new ColorCommandAttribute<>("background-color", TextDisplay::setBackgroundColorAttribute, TextDisplay::getBackgroundColorAttribute, TextDisplay.class));
        addAttribute(textAttributes, new ByteCommandAttribute<>("text-opacity", TextDisplay::setTextOpacityAttribute, TextDisplay::getTextOpacityAttribute, TextDisplay.class));
        addAttribute(textAttributes, new BooleanCommandAttribute<>("see-through", TextDisplay::setSeeThroughAttribute, TextDisplay::getSeeThroughAttribute, TextDisplay.class));
        addAttribute(textAttributes, new BooleanCommandAttribute<>("text-shadow", TextDisplay::setTextShadowAttribute, TextDisplay::getTextShadowAttribute, TextDisplay.class));
        addAttribute(textAttributes, new EnumCommandAttribute<>("alignment", TextDisplayAlignment.class, TextDisplay::setAlignmentAttribute, TextDisplay::getAlignmentAttribute, TextDisplay.class));
        TEXT_ATTRIBUTES = ImmutableMap.copyOf(textAttributes);

        Map<String, CommandAttribute> itemAttributes = new HashMap<>();
        addAllAttributes(itemAttributes, generalAttributes);
        addAttribute(itemAttributes, new EnumCommandAttribute<>("display-type", ItemDisplayType.class, ItemDisplay::setDisplayTypeAttribute, ItemDisplay::getDisplayTypeAttribute, ItemDisplay.class));
        addAttribute(itemAttributes, new ColorCommandAttribute<>("glow-color", ItemDisplay::setGlowColorAttribute, ItemDisplay::getGlowColorAttribute, ItemDisplay.class));
        ITEM_ATTRIBUTES = ImmutableMap.copyOf(itemAttributes);

        Map<String, CommandAttribute> blockAttributes = new HashMap<>();
        addAllAttributes(blockAttributes, generalAttributes);
        addAttribute(blockAttributes, new ColorCommandAttribute<>("glow-color", BlockDisplay::setGlowColorAttribute, BlockDisplay::getGlowColorAttribute, BlockDisplay.class));
        BLOCK_ATTRIBUTES = ImmutableMap.copyOf(blockAttributes);
    }

    private static void addAttribute(Map<String, CommandAttribute> map, CommandAttribute attribute) {
        map.put(attribute.getName(), attribute);
    }

    private static void addAllAttributes(Map<String, CommandAttribute> map, List<CommandAttribute> attributes) {
        attributes.forEach(attribute -> addAttribute(map, attribute));
    }

    public Map<String, CommandAttribute> getAvailableAttributes(DisplayBase display) {
        if (display instanceof TextDisplay) {
            return TEXT_ATTRIBUTES;
        } else if (display instanceof ItemDisplay) {
            return ITEM_ATTRIBUTES;
        } else if (display instanceof BlockDisplay) {
            return BLOCK_ATTRIBUTES;
        }
        return Collections.emptyMap();
    }
}

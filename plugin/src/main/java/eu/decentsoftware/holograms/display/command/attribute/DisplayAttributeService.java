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

public class DisplayAttributeService {

    private static final Map<String, DisplayAttribute> TEXT_ATTRIBUTES;
    private static final Map<String, DisplayAttribute> ITEM_ATTRIBUTES;
    private static final Map<String, DisplayAttribute> BLOCK_ATTRIBUTES;

    static {
        List<DisplayAttribute> generalAttributes = Arrays.asList(
                new Vector3fDisplayAttribute<>("translation", DisplayBase::setTranslation, DisplayBase.class),
                new Vector3fDisplayAttribute<>("scale", DisplayBase::setScale, DisplayBase.class),
                new EnumDisplayAttribute<>("billboard", DisplayBillboardConstraints.class, DisplayBase::setBillboardConstraints, DisplayBase.class),
                new FloatDisplayAttribute<>("shadow-radius", DisplayBase::setShadowRadius, DisplayBase.class),
                new FloatDisplayAttribute<>("shadow-strength", DisplayBase::setShadowStrength, DisplayBase.class)
        );

        Map<String, DisplayAttribute> textAttributes = new HashMap<>();
        addAllAttributes(textAttributes, generalAttributes);
        addAttribute(textAttributes, new IntegerDisplayAttribute<>("line-width", 0, Integer.MAX_VALUE, TextDisplay::setLineWidth, TextDisplay.class));
        addAttribute(textAttributes, new ColorDisplayAttribute<>("background-color", TextDisplay::setBackgroundColor, TextDisplay.class));
        addAttribute(textAttributes, new IntegerDisplayAttribute<>("text-opacity", 0, 255, (display, value) -> display.setTextOpacity(value.byteValue()), TextDisplay.class));
        addAttribute(textAttributes, new BooleanDisplayAttribute<>("see-through", TextDisplay::setSeeThrough, TextDisplay.class));
        addAttribute(textAttributes, new BooleanDisplayAttribute<>("text-shadow", TextDisplay::setTextShadow, TextDisplay.class));
        addAttribute(textAttributes, new EnumDisplayAttribute<>("alignment", TextDisplayAlignment.class, TextDisplay::setAlignment, TextDisplay.class));
        TEXT_ATTRIBUTES = ImmutableMap.copyOf(textAttributes);

        Map<String, DisplayAttribute> itemAttributes = new HashMap<>();
        addAllAttributes(itemAttributes, generalAttributes);
        addAttribute(itemAttributes, new EnumDisplayAttribute<>("display-type", ItemDisplayType.class, ItemDisplay::setDisplayType, ItemDisplay.class));
        addAttribute(itemAttributes, new ColorDisplayAttribute<>("glow-color", ItemDisplay::setGlowColor, ItemDisplay.class));
        ITEM_ATTRIBUTES = ImmutableMap.copyOf(itemAttributes);

        Map<String, DisplayAttribute> blockAttributes = new HashMap<>();
        addAllAttributes(blockAttributes, generalAttributes);
        addAttribute(blockAttributes, new ColorDisplayAttribute<>("glow-color", BlockDisplay::setGlowColor, BlockDisplay.class));
        BLOCK_ATTRIBUTES = ImmutableMap.copyOf(blockAttributes);
    }

    private static void addAttribute(Map<String, DisplayAttribute> map, DisplayAttribute attribute) {
        map.put(attribute.getName(), attribute);
    }

    private static void addAllAttributes(Map<String, DisplayAttribute> map, List<DisplayAttribute> attributes) {
        attributes.forEach(attribute -> addAttribute(map, attribute));
    }

    public Map<String, DisplayAttribute> getAvailableAttributes(DisplayBase display) {
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

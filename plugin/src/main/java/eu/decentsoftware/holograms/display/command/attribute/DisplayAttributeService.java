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

import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.ItemDisplay;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayType;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayAlignment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DisplayAttributeService {

    private static final List<DisplayAttribute> GENERAL_ATTRIBUTES = Arrays.asList(
            new Vector3fDisplayAttribute<>("translation", DisplayBase::setTranslation, DisplayBase.class),
            new Vector3fDisplayAttribute<>("scale", DisplayBase::setScale, DisplayBase.class),
            new EnumDisplayAttribute<>("billboard", DisplayBillboardConstraints.class, DisplayBase::setBillboardConstraints, DisplayBase.class),
            new FloatDisplayAttribute<>("shadow-radius", DisplayBase::setShadowRadius, DisplayBase.class),
            new FloatDisplayAttribute<>("shadow-strength", DisplayBase::setShadowStrength, DisplayBase.class)
    );
    private static final List<DisplayAttribute> TEXT_ATTRIBUTES = Arrays.asList(
            new IntegerDisplayAttribute<>("line-width", 0, Integer.MAX_VALUE, TextDisplay::setLineWidth, TextDisplay.class),
            new ColorDisplayAttribute<>("background-color", TextDisplay::setBackgroundColor, TextDisplay.class),
            new IntegerDisplayAttribute<>("text-opacity", 0, 255, (display, value) -> display.setTextOpacity(value.byteValue()), TextDisplay.class),
            new BooleanDisplayAttribute<>("see-through", TextDisplay::setSeeThrough, TextDisplay.class),
            new BooleanDisplayAttribute<>("text-shadow", TextDisplay::setTextShadow, TextDisplay.class),
            new EnumDisplayAttribute<>("alignment", TextDisplayAlignment.class, TextDisplay::setAlignment, TextDisplay.class)
    );
    private static final List<DisplayAttribute> ITEM_ATTRIBUTES = Arrays.asList(
            new EnumDisplayAttribute<>("display-type", ItemDisplayType.class, ItemDisplay::setDisplayType, ItemDisplay.class),
            new ColorDisplayAttribute<>("glow-color", ItemDisplay::setGlowColor, ItemDisplay.class)
    );
    private static final List<DisplayAttribute> BLOCK_ATTRIBUTES = Arrays.asList(
            new ColorDisplayAttribute<>("glow-color", BlockDisplay::setGlowColor, BlockDisplay.class)
    );

    public Map<String, DisplayAttribute> getAvailableAttributes(DisplayBase display) {
        // TODO: rewrite this
        Map<String, DisplayAttribute> attributes = new HashMap<>();
        GENERAL_ATTRIBUTES.forEach(attribute -> attributes.put(attribute.getName(), attribute));
        if (display instanceof TextDisplay) {
            attributes.putAll(TEXT_ATTRIBUTES.stream().collect(Collectors.toMap(DisplayAttribute::getName, attribute -> attribute)));
        } else if (display instanceof ItemDisplay) {
            attributes.putAll(ITEM_ATTRIBUTES.stream().collect(Collectors.toMap(DisplayAttribute::getName, attribute -> attribute)));
        } else if (display instanceof BlockDisplay) {
            attributes.putAll(BLOCK_ATTRIBUTES.stream().collect(Collectors.toMap(DisplayAttribute::getName, attribute -> attribute)));
        }
        return attributes;
    }
}

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

package eu.decentsoftware.holograms.display.render.metadata;

import eu.decentsoftware.holograms.nms.api.display.data.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayType;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayAlignment;

public final class BuiltInMetadataKeys {

    public static final MetadataKey<DisplayVector3f> TRANSLATION
            = new MetadataKey<>(DisplayMetadataType.TRANSLATION, DisplayVector3f.class);
    public static final MetadataKey<DisplayVector3f> SCALE
            = new MetadataKey<>(DisplayMetadataType.SCALE, DisplayVector3f.class);
    public static final MetadataKey<DisplayBillboardConstraints> BILLBOARD_CONSTRAINTS
            = new MetadataKey<>(DisplayMetadataType.BILLBOARD_CONSTRAINTS, DisplayBillboardConstraints.class);
    public static final MetadataKey<DisplayBrightness> BRIGHTNESS
            = new MetadataKey<>(DisplayMetadataType.BRIGHTNESS, DisplayBrightness.class);
    public static final MetadataKey<Float> SHADOW_RADIUS
            = new MetadataKey<>(DisplayMetadataType.SHADOW_RADIUS, Float.class);
    public static final MetadataKey<Float> SHADOW_STRENGTH
            = new MetadataKey<>(DisplayMetadataType.SHADOW_STRENGTH, Float.class);
    public static final MetadataKey<DisplayColor> GLOW_COLOR_OVERRIDE
            = new MetadataKey<>(DisplayMetadataType.GLOW_COLOR_OVERRIDE, DisplayColor.class);

    public static final MetadataKey<DisplayColor> TEXT_DISPLAY_BACKGROUND
            = new MetadataKey<>(DisplayMetadataType.TEXT_DISPLAY_BACKGROUND, DisplayColor.class);
    public static final MetadataKey<Integer> TEXT_DISPLAY_OPACITY
            = new MetadataKey<>(DisplayMetadataType.TEXT_DISPLAY_OPACITY, Integer.class);
    public static final MetadataKey<Boolean> TEXT_DISPLAY_HAS_SHADOW
            = new MetadataKey<>(DisplayMetadataType.TEXT_DISPLAY_HAS_SHADOW, Boolean.class);
    public static final MetadataKey<Boolean> TEXT_DISPLAY_SEE_THROUGH
            = new MetadataKey<>(DisplayMetadataType.TEXT_DISPLAY_SEE_THROUGH, Boolean.class);
    public static final MetadataKey<TextDisplayAlignment> TEXT_DISPLAY_ALIGNMENT
            = new MetadataKey<>(DisplayMetadataType.TEXT_DISPLAY_ALIGNMENT, TextDisplayAlignment.class);

    public static final MetadataKey<ItemDisplayType> ITEM_DISPLAY_TYPE
            = new MetadataKey<>(DisplayMetadataType.ITEM_DISPLAY_TYPE, ItemDisplayType.class);

    private BuiltInMetadataKeys() {
    }

}

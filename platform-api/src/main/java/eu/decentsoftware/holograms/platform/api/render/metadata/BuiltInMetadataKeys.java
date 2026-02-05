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

package eu.decentsoftware.holograms.platform.api.render.metadata;

import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayProperties;

public final class BuiltInMetadataKeys {

    public static final MetadataKey<DecentVector3f> TRANSLATION
            = new MetadataKey<>(DisplayMetadataType.TRANSLATION, DecentVector3f.class);
    public static final MetadataKey<DecentVector3f> SCALE
            = new MetadataKey<>(DisplayMetadataType.SCALE, DecentVector3f.class);
    public static final MetadataKey<DisplayBillboardConstraints> BILLBOARD_CONSTRAINTS
            = new MetadataKey<>(DisplayMetadataType.BILLBOARD_CONSTRAINTS, DisplayBillboardConstraints.class);
    public static final MetadataKey<DisplayBrightness> BRIGHTNESS
            = new MetadataKey<>(DisplayMetadataType.BRIGHTNESS, DisplayBrightness.class);
    public static final MetadataKey<Float> SHADOW_RADIUS
            = new MetadataKey<>(DisplayMetadataType.SHADOW_RADIUS, Float.class);
    public static final MetadataKey<Float> SHADOW_STRENGTH
            = new MetadataKey<>(DisplayMetadataType.SHADOW_STRENGTH, Float.class);
    public static final MetadataKey<Boolean> GLOWING
            = new MetadataKey<>(DisplayMetadataType.GLOWING, Boolean.class);
    public static final MetadataKey<DecentColor> GLOW_COLOR_OVERRIDE
            = new MetadataKey<>(DisplayMetadataType.GLOW_COLOR_OVERRIDE, DecentColor.class);

    public static final MetadataKey<DecentColor> TEXT_DISPLAY_BACKGROUND
            = new MetadataKey<>(DisplayMetadataType.TEXT_DISPLAY_BACKGROUND, DecentColor.class);
    public static final MetadataKey<Integer> TEXT_DISPLAY_OPACITY
            = new MetadataKey<>(DisplayMetadataType.TEXT_DISPLAY_OPACITY, Integer.class);
    public static final MetadataKey<TextDisplayProperties> TEXT_DISPLAY_PROPERTIES
            = new MetadataKey<>(DisplayMetadataType.TEXT_DISPLAY_PROPERTIES, TextDisplayProperties.class);

    public static final MetadataKey<ItemDisplayType> ITEM_DISPLAY_TYPE
            = new MetadataKey<>(DisplayMetadataType.ITEM_DISPLAY_TYPE, ItemDisplayType.class);

    private BuiltInMetadataKeys() {
    }

}

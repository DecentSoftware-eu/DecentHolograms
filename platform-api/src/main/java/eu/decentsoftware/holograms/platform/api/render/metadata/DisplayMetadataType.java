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

/**
 * Represents metadata types related to display entities.
 *
 * @author d0by
 * @since 2.10.0
 */
public enum DisplayMetadataType implements MetadataType {
    TRANSLATION,
    SCALE,
    BILLBOARD_CONSTRAINTS,
    BRIGHTNESS,
    SHADOW_RADIUS,
    SHADOW_STRENGTH,
    GLOWING,
    GLOW_COLOR_OVERRIDE,

    TEXT_DISPLAY_BACKGROUND,
    TEXT_DISPLAY_OPACITY,
    TEXT_DISPLAY_PROPERTIES,
    TEXT_LINE_WIDTH,

    ITEM_DISPLAY_TYPE,
}

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

package eu.decentsoftware.holograms.display.attribute.value.color;

import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.StaticCompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;

public final class RgbaValue implements AttributeValue<DecentColor> {

    private final int red;
    private final int green;
    private final int blue;
    private final Integer alpha;

    public RgbaValue(int red, int green, int blue) {
        this(red, green, blue, null);
    }

    public RgbaValue(int red, int green, int blue, Integer alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public String getTypeKey() {
        return RgbaValueType.TYPE_ID;
    }

    @Override
    public CompiledAttributeValue<DecentColor> compile(DisplayRenderContext context) {
        int alphaNotNull = alpha == null ? 255 : alpha;
        DecentColor decentColor = DecentColor.fromRGBA(red, green, blue, alphaNotNull);
        return new StaticCompiledAttributeValue<>(decentColor);
    }

    @Override
    public String toHumanReadableString() {
        String rgbString = String.format("RGBA: %s, %s, %s, %s", red, green, blue, alpha);
        return asRGBString() + rgbString;
    }

    private String asRGBString() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public Integer getAlpha() {
        return alpha;
    }
}

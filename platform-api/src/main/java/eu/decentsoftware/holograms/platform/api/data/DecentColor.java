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

package eu.decentsoftware.holograms.platform.api.data;

import java.awt.*;

public class DecentColor {

    public static final DecentColor TRANSPARENT = DecentColor.fromRGBA(0, 0, 0, 0);
    public static final DecentColor DEFAULT_BACKGROUND = DecentColor.fromRGBA(0, 0, 0, 64);
    public static final DecentColor BLACK = DecentColor.fromRGB(0x000000);
    public static final DecentColor DARK_BLUE = DecentColor.fromRGB(0x0000AA);
    public static final DecentColor DARK_GREEN = DecentColor.fromRGB(0x00AA00);
    public static final DecentColor DARK_AQUA = DecentColor.fromRGB(0x00AAAA);
    public static final DecentColor DARK_RED = DecentColor.fromRGB(0xAA0000);
    public static final DecentColor DARK_PURPLE = DecentColor.fromRGB(0xAA00AA);
    public static final DecentColor GOLD = DecentColor.fromRGB(0xFFAA00);
    public static final DecentColor GRAY = DecentColor.fromRGB(0xAAAAAA);
    public static final DecentColor DARK_GRAY = DecentColor.fromRGB(0x555555);
    public static final DecentColor BLUE = DecentColor.fromRGB(0x5555FF);
    public static final DecentColor GREEN = DecentColor.fromRGB(0x55FF55);
    public static final DecentColor AQUA = DecentColor.fromRGB(0x55FFFF);
    public static final DecentColor RED = DecentColor.fromRGB(0xFF5555);
    public static final DecentColor LIGHT_PURPLE = DecentColor.fromRGB(0xFF55FF);
    public static final DecentColor YELLOW = DecentColor.fromRGB(0xFFFF55);
    public static final DecentColor WHITE = DecentColor.fromRGB(0xFFFFFF);

    private final int alpha;
    private final int red;
    private final int green;
    private final int blue;

    private DecentColor(int red, int green, int blue, int alpha) {
        if (red < 0 || red > 255) {
            throw new IllegalArgumentException("Red must be between 0 and 255");
        }
        if (green < 0 || green > 255) {
            throw new IllegalArgumentException("Green must be between 0 and 255");
        }
        if (blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Blue must be between 0 and 255");
        }
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("Alpha must be between 0 and 255");
        }

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public static DecentColor fromRGBA(int rgba) {
        return new DecentColor((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF);
    }

    public static DecentColor fromRGB(int rgb) {
        return new DecentColor((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, 255);
    }

    public static DecentColor fromRGBA(int red, int green, int blue, int alpha) {
        return new DecentColor(red, green, blue, alpha);
    }

    public static DecentColor fromRGB(int red, int green, int blue) {
        return new DecentColor(red, green, blue, 255);
    }

    public static DecentColor fromHsv(float hue, float saturation, float value) {
        int rgb = Color.HSBtoRGB(hue / 360f, saturation / 100f, value / 100f);
        return DecentColor.fromRGB(rgb);
    }

    public static DecentColor fromHsva(float hue, float saturation, float value, int alpha) {
        int rgb = Color.HSBtoRGB(hue / 360f, saturation / 100f, value / 100f);
        return DecentColor.fromRGBA((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, alpha);
    }

    public int asARGB() {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public int asRGB() {
        return (red << 16) | (green << 8) | blue;
    }

    public int getAlpha() {
        return alpha;
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

    public String asRGBString() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }
}

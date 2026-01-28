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

package eu.decentsoftware.holograms.nms.api.display.data;

import com.google.common.base.Preconditions;

import java.awt.*;

public class DisplayColor {

    public static final DisplayColor TRANSPARENT = DisplayColor.fromRGBA(0, 0, 0, 0);
    public static final DisplayColor DEFAULT_BACKGROUND = DisplayColor.fromRGBA(0, 0, 0, 64);
    public static final DisplayColor BLACK = DisplayColor.fromRGB(0x000000);
    public static final DisplayColor DARK_BLUE = DisplayColor.fromRGB(0x0000AA);
    public static final DisplayColor DARK_GREEN = DisplayColor.fromRGB(0x00AA00);
    public static final DisplayColor DARK_AQUA = DisplayColor.fromRGB(0x00AAAA);
    public static final DisplayColor DARK_RED = DisplayColor.fromRGB(0xAA0000);
    public static final DisplayColor DARK_PURPLE = DisplayColor.fromRGB(0xAA00AA);
    public static final DisplayColor GOLD = DisplayColor.fromRGB(0xFFAA00);
    public static final DisplayColor GRAY = DisplayColor.fromRGB(0xAAAAAA);
    public static final DisplayColor DARK_GRAY = DisplayColor.fromRGB(0x555555);
    public static final DisplayColor BLUE = DisplayColor.fromRGB(0x5555FF);
    public static final DisplayColor GREEN = DisplayColor.fromRGB(0x55FF55);
    public static final DisplayColor AQUA = DisplayColor.fromRGB(0x55FFFF);
    public static final DisplayColor RED = DisplayColor.fromRGB(0xFF5555);
    public static final DisplayColor LIGHT_PURPLE = DisplayColor.fromRGB(0xFF55FF);
    public static final DisplayColor YELLOW = DisplayColor.fromRGB(0xFFFF55);
    public static final DisplayColor WHITE = DisplayColor.fromRGB(0xFFFFFF);

    private final int alpha;
    private final int red;
    private final int green;
    private final int blue;

    private DisplayColor(int red, int green, int blue, int alpha) {
        Preconditions.checkArgument(red >= 0 && red <= 255, "Red must be between 0 and 255");
        Preconditions.checkArgument(green >= 0 && green <= 255, "Green must be between 0 and 255");
        Preconditions.checkArgument(blue >= 0 && blue <= 255, "Blue must be between 0 and 255");
        Preconditions.checkArgument(alpha >= 0 && alpha <= 255, "Alpha must be between 0 and 255");

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public static DisplayColor fromRGBA(int rgba) {
        return new DisplayColor((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF);
    }

    public static DisplayColor fromRGB(int rgb) {
        return new DisplayColor((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, 255);
    }

    public static DisplayColor fromRGBA(int red, int green, int blue, int alpha) {
        return new DisplayColor(red, green, blue, alpha);
    }

    public static DisplayColor fromRGB(int red, int green, int blue) {
        return new DisplayColor(red, green, blue, 255);
    }

    public static DisplayColor fromHsv(float hue, float saturation, float value) {
        int rgb = Color.HSBtoRGB(hue / 360f, saturation / 100f, value / 100f);
        return DisplayColor.fromRGB(rgb);
    }

    public static DisplayColor fromHsva(float hue, float saturation, float value, int alpha) {
        int rgb = Color.HSBtoRGB(hue / 360f, saturation / 100f, value / 100f);
        return DisplayColor.fromRGBA((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, alpha);
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

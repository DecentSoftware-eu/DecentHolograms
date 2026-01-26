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

public class DisplayColor {

    private final int alpha;
    private final int red;
    private final int green;
    private final int blue;

    public DisplayColor(int alpha, int red, int green, int blue) {
        Preconditions.checkArgument(alpha >= 0 && alpha <= 255, "Alpha must be between 0 and 255");
        Preconditions.checkArgument(red >= 0 && red <= 255, "Red must be between 0 and 255");
        Preconditions.checkArgument(green >= 0 && green <= 255, "Green must be between 0 and 255");
        Preconditions.checkArgument(blue >= 0 && blue <= 255, "Blue must be between 0 and 255");

        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static DisplayColor fromARGB(int argb) {
        return new DisplayColor((argb >> 24) & 0xFF, (argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF);
    }

    public static DisplayColor fromRGB(int rgb) {
        return new DisplayColor(255, (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
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

    public String asHex() {
        return String.format("%02X%02X%02X%02X", alpha, red, green, blue);
    }
}

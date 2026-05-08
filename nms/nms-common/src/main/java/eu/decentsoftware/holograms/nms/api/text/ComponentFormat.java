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

package eu.decentsoftware.holograms.nms.api.text;

/**
 * Component format holder. This class is responsible for holding the formatting information of a component
 * until it is later applied to an actual component.
 *
 * @author d0by
 * @see LegacyTextFormattingParser
 * @since 2.10.0
 */
public class ComponentFormat {

    public static final int UNSET_COLOR = -1;
    public static final byte EMPTY_FLAGS = 0b00000000;
    public static final byte OBFUSCATED_MASK = 1; // 0b00000001
    public static final byte BOLD_MASK = 1 << 1; // 0b00000010
    public static final byte STRIKETHROUGH_MASK = 1 << 2; // 0b00000100
    public static final byte UNDERLINED_MASK = 1 << 3; // 0b00001000
    public static final byte ITALIC_MASK = 1 << 4; // 0b00010000

    private int color = UNSET_COLOR;
    private byte flags = EMPTY_FLAGS;

    public void reset() {
        this.color = UNSET_COLOR;
        this.flags = EMPTY_FLAGS;
    }

    /**
     * Computes a unique key representing the current state of the color and formatting flags.
     *
     * <p>This value can safely be used as the key in a cache, if needed.</p>
     *
     * @return An integer key representing the combination of the color and formatting flags.
     */
    public int getKey() {
        if (color == UNSET_COLOR) {
            // Set the 0b10000000 bit to 1 when the color is not set
            // this prevents conflicts with black (0) color
            return flags | 0b10000000;
        }
        return (color << 8) | flags & 0xff;
    }

    public void setColor(int color) {
        this.color = color;
        // Color resets special formatting
        this.flags = EMPTY_FLAGS;
    }

    public int getColor() {
        return color;
    }

    public void setObfuscated() {
        flags |= OBFUSCATED_MASK;
    }

    public void setBold() {
        flags |= BOLD_MASK;
    }

    public void setStrikethrough() {
        flags |= STRIKETHROUGH_MASK;
    }

    public void setUnderlined() {
        flags |= UNDERLINED_MASK;
    }

    public void setItalic() {
        flags |= ITALIC_MASK;
    }

    public byte getFlags() {
        return flags;
    }
}

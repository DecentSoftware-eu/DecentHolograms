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

package eu.decentsoftware.holograms.api.utils.color;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StripColorUtilTest {

    @ParameterizedTest
    @MethodSource("provideStringsForStripFormatting")
    void testStripLegacyColorCodes(String input, String expected) {
        assertEquals(expected, StripColorUtil.stripLegacyColorCodes(input));
    }

    private static Object[][] provideStringsForStripFormatting() {
        return new Object[][]{
                // Test with & color codes
                {"&aHello &bWorld!", "Hello World!"},
                {"&0&1&2&3&4&5&6&7&8&9", ""},
                {"&a&b&c&d&e&f", ""},
                {"&k&l&m&n&o&r", ""},
                // Test with § color codes
                {"§aHello §bWorld!", "Hello World!"},
                {"§0§1§2§3§4§5§6§7§8§9", ""},
                {"§A§B§C§D§E§F", ""},
                {"§K§L§M§N§O§R", ""},
                // Test mixed & and §
                {"&aHello §bWorld!", "Hello World!"},
                {"§aTest&bString", "TestString"},
                // Test invalid color codes (should not be stripped)
                {"&gInvalid", "&gInvalid"},
                {"&zTest", "&zTest"},
                {"§gInvalid", "§gInvalid"},
                {"&Hello", "&Hello"},
                {"§World", "§World"},
                {"&§", "&§"},
                {"§&", "§&"},
                // Test edge cases
                {"&", "&"},
                {"§", "§"},
                {"&&a", "&"},
                {"§§a", "§"},
                {"&a", ""},
                {"§a", ""},
                {"Text&", "Text&"},
                {"Text§", "Text§"},
                // Test empty and null-like cases
                {"", ""},
                {"&a&b&c", ""},
                // Test text with color codes in the middle
                {"Hello&aWorld", "HelloWorld"},
                {"Test§bString§cHere", "TestStringHere"},
                // Test uppercase and lowercase
                {"&AHello", "Hello"},
                {"&ahello", "hello"},
                {"§Fworld", "world"},
                {"§fWORLD", "WORLD"},
                // Test multiple consecutive codes
                {"&a&b&cText", "Text"},
                {"§r§l§kFormatted", "Formatted"},
                // Test strings without any color codes
                {"No colors here", "No colors here"},
                {"12345", "12345"},
                {"!@#$%", "!@#$%"},
        };
    }
}
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

package eu.decentsoftware.holograms.display.attribute.command.handler;

import eu.decentsoftware.holograms.display.attribute.AttributeParseException;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecentColorAttributeCommandHelperTest {

    private static final Map<String, DecentColor> NAMED_COLORS = new HashMap<>();

    static {
        NAMED_COLORS.put("TRANSPARENT", DecentColor.TRANSPARENT);
        NAMED_COLORS.put("DEFAULT", DecentColor.DEFAULT_BACKGROUND);

        NAMED_COLORS.put("BLACK", DecentColor.BLACK);
        NAMED_COLORS.put("DARK_BLUE", DecentColor.DARK_BLUE);
        NAMED_COLORS.put("DARK_GREEN", DecentColor.DARK_GREEN);
        NAMED_COLORS.put("DARK_AQUA", DecentColor.DARK_AQUA);
        NAMED_COLORS.put("DARK_RED", DecentColor.DARK_RED);
        NAMED_COLORS.put("DARK_PURPLE", DecentColor.DARK_PURPLE);
        NAMED_COLORS.put("GOLD", DecentColor.GOLD);
        NAMED_COLORS.put("GRAY", DecentColor.GRAY);
        NAMED_COLORS.put("DARK_GRAY", DecentColor.DARK_GRAY);
        NAMED_COLORS.put("BLUE", DecentColor.BLUE);
        NAMED_COLORS.put("GREEN", DecentColor.GREEN);
        NAMED_COLORS.put("AQUA", DecentColor.AQUA);
        NAMED_COLORS.put("RED", DecentColor.RED);
        NAMED_COLORS.put("LIGHT_PURPLE", DecentColor.LIGHT_PURPLE);
        NAMED_COLORS.put("YELLOW", DecentColor.YELLOW);
        NAMED_COLORS.put("WHITE", DecentColor.WHITE);
    }

    private DecentColorAttributeCommandHelper handler;

    @BeforeEach
    void setUp() {
        handler = new DecentColorAttributeCommandHelper();
    }

    @Nested
    class ParseColorGeneralValidation {

        @Test
        void parseColor_noArgs_throwsException() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{}));
            assertEquals("No color specified.", ex.getMessage());
        }
    }

    @Nested
    class RgbParsing {

        @Test
        void parseRgb_validRgb() {
            DecentColor color = handler.parseColor(new String[]{"RGB", "1", "2", "3"});
            assertEquals(1, color.getRed());
            assertEquals(2, color.getGreen());
            assertEquals(3, color.getBlue());
            assertEquals(255, color.getAlpha());
        }

        @Test
        void parseRgb_invalidArgCount() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGB", "1", "2"}));
            assertTrue(ex.getMessage().contains("RGB expects"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "256"})
        void parseRgb_red_outOfRange(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGB", value, "0", "128"}));
            assertTrue(ex.getMessage().contains("Red must be between"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "256"})
        void parseRgb_green_outOfRange(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGB", "0", value, "128"}));
            assertTrue(ex.getMessage().contains("Green must be between"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "256"})
        void parseRgb_blue_outOfRange(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGB", "0", "0", value}));
            assertTrue(ex.getMessage().contains("Blue must be between"));
        }

        @Test
        void parseRgb_notEnoughArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGB"}));
            assertTrue(ex.getMessage().contains("RGB expects"));
        }

        @Test
        void parseRgb_tooManyArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGB", "1", "2", "3", "4"}));
            assertTrue(ex.getMessage().contains("RGB expects"));
        }

        @Test
        void parseRgb_red_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGB", "a", "2", "3"}));
            assertTrue(ex.getMessage().contains("Red must be a number."));
        }

        @Test
        void parseRgb_green_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGB", "1", "a", "3"}));
            assertTrue(ex.getMessage().contains("Green must be a number."));
        }

        @Test
        void parseRgb_blue_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGB", "1", "2", "a"}));
            assertTrue(ex.getMessage().contains("Blue must be a number."));
        }
    }

    @Nested
    class RgbaParsing {

        @Test
        void parseRgba_valid() {
            DecentColor color = handler.parseColor(new String[]{"RGBA", "1", "2", "3", "4"});
            assertEquals(1, color.getRed());
            assertEquals(2, color.getGreen());
            assertEquals(3, color.getBlue());
            assertEquals(4, color.getAlpha());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "256"})
        void parseRgba_alpha_outOfRange(String alphaValue) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGBA", "1", "2", "3", alphaValue}));
            assertTrue(ex.getMessage().contains("Alpha must be between"));
        }

        @Test
        void parseRgba_notEnoughArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGBA", "1", "2", "3"}));
            assertTrue(ex.getMessage().contains("RGBA expects"));
        }

        @Test
        void parseRgba_tooManyArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"RGBA", "1", "2", "3", "4", "5"}));
            assertTrue(ex.getMessage().contains("RGBA expects"));
        }
    }

    @Nested
    class HexParsing {

        @Test
        void parseHex_valid() {
            DecentColor color = handler.parseColor(new String[]{"HEX", "FF0080"});
            assertEquals(255, color.getRed());
            assertEquals(0, color.getGreen());
            assertEquals(128, color.getBlue());
        }

        @Test
        void parseHex_invalidLength() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HEX", "FFF"}));
            assertTrue(ex.getMessage().contains("Expected 6 hex characters"));
        }

        @Test
        void parseHex_invalidCharacters() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HEX", "ZZZZZZ"}));
            assertTrue(ex.getMessage().contains("Invalid hex value"));
        }

        @Test
        void parseHex_notEnoughArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HEX"}));
            assertTrue(ex.getMessage().contains("HEX expects"));
        }
    }

    @Nested
    class HexaParsing {

        @Test
        void parseHexa_valid() {
            DecentColor color = handler.parseColor(new String[]{"HEXA", "FF004080"});
            assertEquals(255, color.getRed());
            assertEquals(0, color.getGreen());
            assertEquals(64, color.getBlue());
            assertEquals(128, color.getAlpha());
        }

        @Test
        void parseHexa_notEnoughArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HEXA"}));
            assertTrue(ex.getMessage().contains("HEXA expects"));
        }
    }

    @Nested
    class NamedParsing {

        @Test
        void parseNamed_valid() {
            for (String namedColor : NAMED_COLORS.keySet()) {
                DecentColor color = handler.parseColor(new String[]{"NAMED", namedColor});

                assertSame(NAMED_COLORS.get(namedColor), color);
            }
        }

        @Test
        void parseNamed_noArgs_returnsNull() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"NAMED"}));
            assertEquals("NAMED expects a color name.", ex.getMessage());
        }

        @Test
        void parseNamed_unknownColor() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"NAMED", "notacolor"}));
            assertTrue(ex.getMessage().contains("Unknown named color"));
        }
    }

    @Nested
    class HsvParsing {

        @Test
        void parseHsv_valid() {
            DecentColor color = handler.parseColor(new String[]{"HSV", "0", "100", "100"});
            assertNotNull(color);
        }

        @Test
        void parseHsv_notEnoughArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSV", "0"}));
            assertTrue(ex.getMessage().contains("HSV expects"));
        }

        @Test
        void parseHsv_tooManyArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSV", "0", "100", "100", "100"}));
            assertTrue(ex.getMessage().contains("HSV expects"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "361"})
        void parseHsv_range_outOfBounds(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSV", value, "0", "0"}));
            assertTrue(ex.getMessage().contains("Hue must be between"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "101"})
        void parseHsv_saturation_outOfBounds(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSV", "0", value, "0"}));
            assertTrue(ex.getMessage().contains("Saturation must be between"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "101"})
        void parseHsv_value_outOfBounds(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSV", "0", "0", value}));
            assertTrue(ex.getMessage().contains("Value must be between"));
        }

        @Test
        void parseHsv_hue_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "a", "50", "50", "128"}));
            assertTrue(ex.getMessage().contains("Hue must be a number."));
        }

        @Test
        void parseHsv_saturation_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "180", "a", "50", "128"}));
            assertTrue(ex.getMessage().contains("Saturation must be a number."));
        }

        @Test
        void parseHsv_value_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "180", "50", "a", "128"}));
            assertTrue(ex.getMessage().contains("Value must be a number."));
        }
    }

    @Nested
    class HsvaParsing {

        @Test
        void parseHsva_valid() {
            DecentColor color = handler.parseColor(new String[]{"HSVA", "180", "50", "50", "128"});
            assertEquals(128, color.getAlpha());
        }

        @Test
        void parseHsva_notEnoughArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "180", "50", "50"}));
            assertTrue(ex.getMessage().contains("HSVA expects"));
        }

        @Test
        void parseHsva_tooManyArgs() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "180", "50", "50", "128", "128"}));
            assertTrue(ex.getMessage().contains("HSVA expects"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "256"})
        void parseHsva_alpha_outOfBounds(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "180", "50", "50", value}));
            assertTrue(ex.getMessage().contains("Alpha must be between"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "361"})
        void parseHsva_range_outOfBounds(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSV", value, "0", "0"}));
            assertTrue(ex.getMessage().contains("Hue must be between"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "101"})
        void parseHsva_saturation_outOfBounds(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSV", "0", value, "0"}));
            assertTrue(ex.getMessage().contains("Saturation must be between"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "101"})
        void parseHsva_value_outOfBounds(String value) {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSV", "0", "0", value}));
            assertTrue(ex.getMessage().contains("Value must be between"));
        }

        @Test
        void parseHsva_hue_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "a", "50", "50", "128"}));
            assertTrue(ex.getMessage().contains("Hue must be a number."));
        }

        @Test
        void parseHsva_saturation_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "180", "a", "50", "128"}));
            assertTrue(ex.getMessage().contains("Saturation must be a number."));
        }

        @Test
        void parseHsva_value_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "180", "50", "a", "128"}));
            assertTrue(ex.getMessage().contains("Value must be a number."));
        }

        @Test
        void parseHsva_alpha_notNumber() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"HSVA", "180", "50", "50", "a"}));
            assertTrue(ex.getMessage().contains("Alpha must be a number."));
        }
    }

    @Nested
    class FallbackParsing {

        @Test
        void parseFallback_named() {
            DecentColor color = handler.parseColor(new String[]{"white"});
            assertSame(DecentColor.WHITE, color);
        }

        @Test
        void parseFallback_hex6() {
            DecentColor color = handler.parseColor(new String[]{"00FF80"});
            assertEquals(0, color.getRed());
            assertEquals(255, color.getGreen());
            assertEquals(128, color.getBlue());
        }

        @Test
        void parseFallback_hex8() {
            DecentColor color = handler.parseColor(new String[]{"004080FF"});

            assertEquals(0, color.getRed());
            assertEquals(64, color.getGreen());
            assertEquals(128, color.getBlue());
            assertEquals(255, color.getAlpha());
        }

        @Test
        void parseFallback_invalid() {
            AttributeParseException ex = assertThrows(AttributeParseException.class,
                    () -> handler.parseColor(new String[]{"???"}));
            assertTrue(ex.getMessage().contains("Invalid color format"));
        }
    }

    @Nested
    class HintGeneration {

        @Test
        void getHints_invalidFormat_returnsEmpty() {
            List<String> hints = handler.getHints(new String[]{"UNKNOWN", ""});
            assertTrue(hints.isEmpty());
        }

        @Test
        void getHints_firstArg_returnsFormatsAndDefaults() {
            List<String> hints = handler.getHints(new String[]{"R"});

            assertTrue(hints.contains("RGB"));
            assertTrue(hints.contains("RGBA"));
            assertTrue(hints.contains("HEX"));
            assertTrue(hints.contains("HEXA"));
            assertTrue(hints.contains("NAMED"));
            assertTrue(hints.contains("HSV"));
            assertTrue(hints.contains("TRANSPARENT"));
            assertTrue(hints.contains("DEFAULT"));
        }

        @Test
        void getHints_rgb_returnsHints() {
            List<String> hints = handler.getHints(new String[]{"RGB", ""});
            assertEquals(Arrays.asList("0", "64", "128", "192", "255"), hints);
        }

        @Test
        void getHints_named_returnsNamedColors() {
            List<String> hints = handler.getHints(new String[]{"NAMED", ""});

            assertNotNull(hints);
            assertEquals(NAMED_COLORS.size(), hints.size());
            for (String namedColor : NAMED_COLORS.keySet()) {
                assertTrue(hints.contains(namedColor));
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {"HSVA", "HSV"})
        void getHints_hsv_hue(String format) {
            List<String> hints = handler.getHints(new String[]{format, "anything"});
            assertEquals(Arrays.asList("0", "60", "120", "180", "240", "300", "360"), hints);
        }

        @ParameterizedTest
        @ValueSource(strings = {"HSVA", "HSV"})
        void getHints_hsv_saturation(String format) {
            List<String> hints = handler.getHints(new String[]{format, "360", "anything"});
            assertEquals(Arrays.asList("0", "25", "50", "75", "100"), hints);
        }

        @ParameterizedTest
        @ValueSource(strings = {"HSVA", "HSV"})
        void getHints_hsv_value(String format) {
            List<String> hints = handler.getHints(new String[]{format, "240", "100", "anything"});
            assertEquals(Arrays.asList("0", "25", "50", "75", "100"), hints);
        }

        @Test
        void getHints_hsva_alpha_returnsHints() {
            List<String> hints = handler.getHints(new String[]{"HSVA", "120", "100", "50", "anything"});
            assertEquals(Arrays.asList("0", "64", "128", "192", "255"), hints);
        }

        @Test
        void getHints_hex_returnsHints() {
            List<String> hints = handler.getHints(new String[]{"HEX", "anything"});

            assertTrue(hints.contains("000000"));
            assertTrue(hints.contains("FFFFFF"));
            assertTrue(hints.contains("FF0000"));
            assertTrue(hints.contains("00FF00"));
            assertTrue(hints.contains("0000FF"));
        }

        @Test
        void getHints_hexa_returnsHints() {
            List<String> hints = handler.getHints(new String[]{"HEXA", "anything"});

            assertTrue(hints.contains("000000FF"));
            assertTrue(hints.contains("FFFFFFFF"));
            assertTrue(hints.contains("FF0000FF"));
            assertTrue(hints.contains("00FF00FF"));
            assertTrue(hints.contains("0000FFFF"));
        }
    }

    private static Object[][] provideFormatsWithArgCounts() {
        return new Object[][]{
                {"RGB", 3},
                {"RGBA", 4},
                {"HEX", 1},
                {"HEXA", 1},
                {"NAMED", 1},
                {"HSV", 3},
                {"HSVA", 4},
        };
    }

    @ParameterizedTest
    @MethodSource("provideFormatsWithArgCounts")
    void getHints_tooManyArguments_returnsEmpty(String format, int expectedArgCount) {
        String[] args = new String[expectedArgCount + 2];
        args[0] = format;

        List<String> hints = handler.getHints(args);

        assertNotNull(hints);
        assertTrue(hints.isEmpty());
    }
}

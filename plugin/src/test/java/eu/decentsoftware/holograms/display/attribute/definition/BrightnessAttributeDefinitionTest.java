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

package eu.decentsoftware.holograms.display.attribute.definition;

import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.AttributeParseException;
import eu.decentsoftware.holograms.display.attribute.value.compiled.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.compiled.StaticCompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class BrightnessAttributeDefinitionTest {

    private static final String ATTRIBUTE_NAME = "brightness";
    private BrightnessAttributeDefinition definition;

    @BeforeEach
    void setUp() {
        definition = new BrightnessAttributeDefinition();
    }

    @Test
    void testKey() {
        AttributeKey<DisplayBrightness> key = definition.getKey();

        assertNotNull(key);
        assertSame(BrightnessAttributeDefinition.KEY, key);
        assertEquals(ATTRIBUTE_NAME, key.getName());
        assertEquals(DisplayBrightness.class, key.getType());
    }

    @Test
    void testDefaultValue() {
        assertNull(definition.getDefaultValue());
    }

    private static Object[][] provideValuesForApply() {
        return new Object[][]{
                // Default value
                {null, null},
                {DisplayBrightness.of(0, 0), DisplayBrightness.of(0, 0)},
                {DisplayBrightness.of(15, 15), DisplayBrightness.of(15, 15)},
                {DisplayBrightness.of(7, 10), DisplayBrightness.of(7, 10)},
        };
    }

    @ParameterizedTest
    @MethodSource("provideValuesForApply")
    void testApply(DisplayBrightness value, DisplayBrightness expectedValue) {
        CompiledAttributeValue<DisplayBrightness> attribute = new StaticCompiledAttributeValue<>(value);
        FinalDisplayRenderState state = new FinalDisplayRenderState("id");

        definition.apply(attribute, state);

        assertEquals(1, state.getMetadataValues().size());
        MetadataValue<DisplayBrightness> metadataValue = state.getMetadataValue(BuiltInMetadataKeys.BRIGHTNESS);
        assertNotNull(metadataValue);
        assertDisplayBrightness(expectedValue, metadataValue.getValue());
        assertFalse(metadataValue.isAnimated());
    }

    @Test
    void testFormat_nullValue() {
        assertNull(definition.format(null));
    }

    @Test
    void testFormat() {
        assertEquals("Block Light: 7, Sky Light: 12", definition.format(DisplayBrightness.of(7, 12)));
    }

    private void assertDisplayBrightness(DisplayBrightness expected, DisplayBrightness actual) {
        if (expected == null) {
            assertNull(actual);
            return;
        }
        assertEquals(expected.getBlockLight(), actual.getBlockLight());
        assertEquals(expected.getSkyLight(), actual.getSkyLight());
    }

    private static Object[][] provideValidInputsForParse() {
        return new Object[][]{
                {new String[]{"0", "0"}, DisplayBrightness.of(0, 0)},
                {new String[]{"15", "15"}, DisplayBrightness.of(15, 15)},
                {new String[]{"7", "10"}, DisplayBrightness.of(7, 10)},
        };
    }

    @ParameterizedTest
    @MethodSource("provideValidInputsForParse")
    void testParse_validInputs(String[] args, DisplayBrightness expectedValue) {
        DisplayBrightness result = definition.parse(args);
        assertEquals(expectedValue.getBlockLight(), result.getBlockLight());
        assertEquals(expectedValue.getSkyLight(), result.getSkyLight());
    }

    private static Object[][] provideInvalidInputsForParse() {
        return new Object[][]{
                {"-1", "15", "Block Light must be between 0 and 15."},
                {"16", "15", "Block Light must be between 0 and 15."},
                {"15", "-1", "Sky Light must be between 0 and 15."},
                {"15", "16", "Sky Light must be between 0 and 15."},
                {"", "15", "Block Light must be an integer."},
                {"0xff", "15", "Block Light must be an integer."},
                {"anything", "15", "Block Light must be an integer."},
                {"15", "anything", "Sky Light must be an integer."},
        };
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInputsForParse")
    void testParse_invalidInputs(String blockLight, String skyLight, String expectedMessage) {
        AttributeParseException exception = assertThrows(AttributeParseException.class, () -> definition.parse(new String[]{blockLight, skyLight}));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3, 4})
    void testParse_insufficientArguments(int argumentsLength) {
        AttributeParseException exception = assertThrows(AttributeParseException.class, () -> definition.parse(new String[argumentsLength]));

        assertEquals("Brightness must be specified as two separate values for block light and sky light.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 3, 4})
    void testHints_notFirstOrSecondArgument(int argumentsLength) {
        CommandSender sender = mock(CommandSender.class);
        String[] args = new String[argumentsLength];

        List<String> hints = definition.getHints(sender, args);

        assertNotNull(hints);
        assertTrue(hints.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "0", "15", "anything"})
    void testHints_firstArgument(String firstArgument) {
        CommandSender sender = mock(CommandSender.class);
        String[] args = new String[]{firstArgument};

        List<String> hints = definition.getHints(sender, args);

        assertNotNull(hints);
        assertEquals(getHints(), hints);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "0", "15", "anything"})
    void testHints_secondArgument(String secondArgument) {
        CommandSender sender = mock(CommandSender.class);
        String[] args = new String[]{"15", secondArgument};

        List<String> hints = definition.getHints(sender, args);

        assertNotNull(hints);
        assertEquals(getHints(), hints);
    }

    private List<String> getHints() {
        return IntStream.range(0, 16).boxed().map(String::valueOf).collect(Collectors.toList());
    }
}
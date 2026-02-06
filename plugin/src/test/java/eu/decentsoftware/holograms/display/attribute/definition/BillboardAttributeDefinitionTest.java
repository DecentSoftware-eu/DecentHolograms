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
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.StaticDisplayAttribute;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class BillboardAttributeDefinitionTest {

    private static final String ATTRIBUTE_NAME = "billboard";
    private BillboardAttributeDefinition definition;

    @BeforeEach
    void setUp() {
        definition = new BillboardAttributeDefinition();
    }

    @Test
    void testKey() {
        AttributeKey<DisplayBillboardConstraints> key = definition.getKey();

        assertNotNull(key);
        assertSame(BillboardAttributeDefinition.KEY, key);
        assertEquals(ATTRIBUTE_NAME, key.getName());
        assertEquals(DisplayBillboardConstraints.class, key.getType());
    }

    @Test
    void testDefaultValue() {
        assertEquals(DisplayBillboardConstraints.FIXED, definition.getDefaultValue());
    }

    private static Object[][] provideValuesForApply() {
        return new Object[][]{
                // Default value
                {null, DisplayBillboardConstraints.FIXED},
                {DisplayBillboardConstraints.FIXED, DisplayBillboardConstraints.FIXED},
                {DisplayBillboardConstraints.HORIZONTAL, DisplayBillboardConstraints.HORIZONTAL},
                {DisplayBillboardConstraints.VERTICAL, DisplayBillboardConstraints.VERTICAL},
                {DisplayBillboardConstraints.CENTER, DisplayBillboardConstraints.CENTER},
        };
    }

    @ParameterizedTest
    @MethodSource("provideValuesForApply")
    void testApply(DisplayBillboardConstraints value, DisplayBillboardConstraints expectedValue) {
        DisplayAttribute<DisplayBillboardConstraints> attribute = new StaticDisplayAttribute<>(ATTRIBUTE_NAME, value);
        DisplayRenderState state = new DisplayRenderState("id");
        DisplayRenderContext context = mock(DisplayRenderContext.class);

        definition.apply(attribute, state, context);

        assertEquals(1, state.getMetadataValues().size());
        MetadataValue<DisplayBillboardConstraints> metadataValue = state.getMetadataValue(BuiltInMetadataKeys.BILLBOARD_CONSTRAINTS);
        assertNotNull(metadataValue);
        assertEquals(expectedValue, metadataValue.getValue());
        assertFalse(metadataValue.isAnimated());
    }

    private static Object[][] provideValidInputsForParse() {
        return new Object[][]{
                {new String[]{"FIXED"}, DisplayBillboardConstraints.FIXED},
                {new String[]{"HORIZONTAL"}, DisplayBillboardConstraints.HORIZONTAL},
                {new String[]{"VERTICAL"}, DisplayBillboardConstraints.VERTICAL},
                {new String[]{"CENTER"}, DisplayBillboardConstraints.CENTER},
                // Only the first argument should be used
                {new String[]{"FIXED", "anything else after"}, DisplayBillboardConstraints.FIXED},
        };
    }

    @ParameterizedTest
    @MethodSource("provideValidInputsForParse")
    void testParse_validInputs(String[] args, DisplayBillboardConstraints expectedValue) {
        assertEquals(expectedValue, definition.parse(args));
    }

    @Test
    void testParse_unknownValue() {
        AttributeParseException exception = assertThrows(AttributeParseException.class, () -> definition.parse(new String[]{"UNKNOWN"}));

        assertEquals("Billboard options are: FIXED, VERTICAL, HORIZONTAL, CENTER", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 3})
    void testHints_notFirstArgument(int argumentsLength) {
        CommandSender sender = mock(CommandSender.class);
        String[] args = new String[argumentsLength];

        List<String> hints = definition.getHints(sender, args);

        assertNotNull(hints);
        assertTrue(hints.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "F", "anything"})
    void testHints_firstArgument(String firstArgument) {
        CommandSender sender = mock(CommandSender.class);
        String[] args = new String[]{firstArgument};

        List<String> hints = definition.getHints(sender, args);

        assertNotNull(hints);
        assertEquals(Arrays.asList("FIXED", "VERTICAL", "HORIZONTAL", "CENTER"), hints);
    }
}
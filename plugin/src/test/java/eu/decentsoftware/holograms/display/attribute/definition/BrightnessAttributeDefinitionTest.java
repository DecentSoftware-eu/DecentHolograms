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
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.StaticCompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.display.BrightnessValue;
import eu.decentsoftware.holograms.display.render.state.PresentedRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

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
        PresentedRenderState state = new PresentedRenderState("id", DisplayType.TEXT);

        definition.apply(attribute, state);

        assertDisplayBrightness(expectedValue, value);
    }

    @Test
    void testFormat_nullValue() {
        assertNull(definition.format(null));
    }

    @Test
    void testFormat() {
        BrightnessValue brightnessValue = new BrightnessValue(7, 12);
        assertEquals("Block Light: 7, Sky Light: 12", definition.format(brightnessValue));
    }

    private void assertDisplayBrightness(DisplayBrightness expected, DisplayBrightness actual) {
        if (expected == null) {
            assertNull(actual);
            return;
        }
        assertEquals(expected.getBlockLight(), actual.getBlockLight());
        assertEquals(expected.getSkyLight(), actual.getSkyLight());
    }
}
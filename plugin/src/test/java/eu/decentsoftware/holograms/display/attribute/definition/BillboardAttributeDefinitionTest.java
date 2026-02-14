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
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.StaticCompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.display.BillboardConstraintsValue;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

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
        AttributeValue<DisplayBillboardConstraints> defaultValue = definition.getDefaultValue();

        assertInstanceOf(BillboardConstraintsValue.class, defaultValue);
        assertEquals(DisplayBillboardConstraints.FIXED, ((BillboardConstraintsValue) defaultValue).getBillboardConstraints());
    }

    private static Object[][] provideValuesForApply() {
        return new Object[][]{
                {DisplayBillboardConstraints.FIXED, DisplayBillboardConstraints.FIXED},
                {DisplayBillboardConstraints.HORIZONTAL, DisplayBillboardConstraints.HORIZONTAL},
                {DisplayBillboardConstraints.VERTICAL, DisplayBillboardConstraints.VERTICAL},
                {DisplayBillboardConstraints.CENTER, DisplayBillboardConstraints.CENTER},
        };
    }

    @ParameterizedTest
    @MethodSource("provideValuesForApply")
    void testApply(DisplayBillboardConstraints value, DisplayBillboardConstraints expectedValue) {
        CompiledAttributeValue<DisplayBillboardConstraints> compiledAttributeValue = new StaticCompiledAttributeValue<>(value);
        FinalDisplayRenderState state = new FinalDisplayRenderState("id");

        definition.apply(compiledAttributeValue, state);

        assertEquals(1, state.getMetadataValues().size());
        MetadataValue<DisplayBillboardConstraints> metadataValue = state.getMetadataValue(BuiltInMetadataKeys.BILLBOARD_CONSTRAINTS);
        assertNotNull(metadataValue);
        assertEquals(expectedValue, metadataValue.getValue());
        assertFalse(metadataValue.isAnimated());
    }
}
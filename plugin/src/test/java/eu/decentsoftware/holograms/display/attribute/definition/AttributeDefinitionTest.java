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

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.primitives.StringValue;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttributeDefinitionTest {

    @Test
    void testDefaults() {
        AttributeDefinition<String> definition = new TestAttributeDefinition();

        assertEquals("test", definition.getName());
        assertEquals(String.class, definition.getValueType());

        DisplayType[] applicableDisplayTypes = definition.getApplicableDisplayTypes();
        assertEquals(DisplayType.values().length, applicableDisplayTypes.length);

        List<String> hints = definition.getHints(mock(CommandSender.class), new String[0]);
        assertNotNull(hints);
        assertTrue(hints.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(DisplayType.class)
    void testApplicableTo(DisplayType displayType) {
        AttributeDefinition<String> definition = new TestAttributeDefinition();
        DisplayBase displayBase = mock(DisplayBase.class);
        when(displayBase.getType()).thenReturn(displayType);

        assertTrue(definition.applicableTo(displayBase));
    }

    @Test
    void testApplicableTo_notApplicableDisplayType() {
        AttributeDefinition<String> definition = new TestAttributeDefinitionWithCustomApplicableDisplayTypes(
                new DisplayType[]{DisplayType.BLOCK});
        DisplayBase displayBase = mock(DisplayBase.class);
        when(displayBase.getType()).thenReturn(DisplayType.ITEM);

        assertFalse(definition.applicableTo(displayBase));
    }

    @Test
    void testFormat_null() {
        AttributeDefinition<String> definition = new TestAttributeDefinition();
        assertNull(definition.format(null));
    }

    @Test
    void testFormat() {
        AttributeDefinition<String> definition = new TestAttributeDefinition();
        assertEquals("test", definition.format(new StringValue("test", null)));
    }

    private static class TestAttributeDefinition implements AttributeDefinition<String> {
        @Override
        public @NotNull AttributeKey<String> getKey() {
            return AttributeKey.of("test", String.class);
        }

        @Override
        public @Nullable AttributeValue<String> getDefaultValue() {
            return new StringValue("default", null);
        }

        @Override
        public void apply(CompiledAttributeValue<String> value, FinalDisplayRenderState state) {

        }

        @Override
        public @NonNull AttributeValue<String> parse(String[] args) {
            return new StringValue("parsed", null);
        }
    }

    private static class TestAttributeDefinitionWithCustomApplicableDisplayTypes extends TestAttributeDefinition {
        private final DisplayType[] applicableDisplayTypes;

        private TestAttributeDefinitionWithCustomApplicableDisplayTypes(DisplayType[] applicableDisplayTypes) {
            this.applicableDisplayTypes = applicableDisplayTypes;
        }

        @Override
        public @NotNull DisplayType[] getApplicableDisplayTypes() {
            return applicableDisplayTypes;
        }
    }
}
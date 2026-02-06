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

package eu.decentsoftware.holograms.display.attribute;

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AttributeCommandHandlerTest {

    private AttributeDefinitionRegistry registry;
    private AttributeCommandHandler handler;

    @BeforeEach
    void setUp() {
        registry = mock(AttributeDefinitionRegistry.class);
        handler = new AttributeCommandHandler(registry);
    }

    @Nested
    class SetAttributeTests {

        @Test
        void setAttribute_validValue_setsStaticAttributeOnDisplay() {
            DisplayBase display = mock(DisplayBase.class);
            AttributeDefinition<String> definition = mock(AttributeDefinition.class);
            AttributeKey<String> key = mock(AttributeKey.class);

            when(definition.getKey()).thenReturn(key);
            when(definition.getName()).thenReturn("test");
            when(definition.parse(new String[]{"value"})).thenReturn("parsed");

            handler.setAttribute(display, definition, new String[]{"value"});

            ArgumentCaptor<StaticDisplayAttribute<String>> captor = ArgumentCaptor.forClass(StaticDisplayAttribute.class);

            verify(display).setAttribute(eq(key), captor.capture());
            StaticDisplayAttribute<String> attribute = captor.getValue();

            assertEquals("test", attribute.getName());
            assertEquals("parsed", attribute.getValue());
        }
    }

    @Nested
    class ResetAttributeTests {

        @Test
        void resetAttribute_setsNullStaticAttribute() {
            DisplayBase display = mock(DisplayBase.class);
            AttributeDefinition<Integer> definition = mock(AttributeDefinition.class);
            AttributeKey<Integer> key = mock(AttributeKey.class);

            when(definition.getKey()).thenReturn(key);
            when(definition.getName()).thenReturn("number");

            handler.resetAttribute(display, definition);

            ArgumentCaptor<StaticDisplayAttribute<Integer>> captor = ArgumentCaptor.forClass(StaticDisplayAttribute.class);

            verify(display).setAttribute(eq(key), captor.capture());
            StaticDisplayAttribute<Integer> attribute = captor.getValue();

            assertEquals("number", attribute.getName());
            assertNull(attribute.getValue());
        }
    }

    @Nested
    class GetAttributeTests {

        @Test
        void getAttribute_missingAttribute_returnsNull() {
            DisplayBase display = mock(DisplayBase.class);
            AttributeDefinition<String> definition = mock(AttributeDefinition.class);
            AttributeKey<String> key = mock(AttributeKey.class);

            when(definition.getKey()).thenReturn(key);
            when(display.getAttribute(key)).thenReturn(null);

            String result = handler.getAttribute(display, definition);
            assertNull(result);
        }

        @Test
        void getAttribute_existingAttribute_formatsValue() {
            DisplayBase display = mock(DisplayBase.class);
            AttributeDefinition<String> definition = mock(AttributeDefinition.class);
            AttributeKey<String> key = mock(AttributeKey.class);
            DisplayAttribute<String> attribute = mock(DisplayAttribute.class);

            when(definition.getKey()).thenReturn(key);
            when(display.getAttribute(key)).thenReturn(attribute);
            when(attribute.getValue()).thenReturn("raw");
            when(definition.format("raw")).thenReturn("formatted");

            String result = handler.getAttribute(display, definition);
            assertEquals("formatted", result);
        }
    }

    @Nested
    class ApplicableAttributesTests {

        @Test
        void getApplicableAttributes_delegatesToRegistry() {
            DisplayBase display = mock(DisplayBase.class);
            DisplayType type = mock(DisplayType.class);
            List<AttributeDefinition<?>> definitions = Collections.singletonList(mock(AttributeDefinition.class));

            when(display.getType()).thenReturn(type);
            when(registry.getDefinitionsByDisplayType(type)).thenReturn(definitions);

            List<AttributeDefinition<?>> result = handler.getApplicableAttributes(display);
            assertSame(definitions, result);
        }

        @Test
        void getApplicableAttributeNames_mapsNamesCorrectly() {
            DisplayBase display = mock(DisplayBase.class);
            DisplayType type = mock(DisplayType.class);

            AttributeDefinition<?> a = mock(AttributeDefinition.class);
            AttributeDefinition<?> b = mock(AttributeDefinition.class);

            when(a.getName()).thenReturn("alpha");
            when(b.getName()).thenReturn("beta");

            when(display.getType()).thenReturn(type);
            when(registry.getDefinitionsByDisplayType(type)).thenReturn(Arrays.asList(a, b));

            List<String> names = handler.getApplicableAttributeNames(display);

            assertEquals(Arrays.asList("alpha", "beta"), names);
        }
    }

    @Nested
    class AttributeDefinitionLookupTests {

        @Test
        void getAttributeDefinition_definitionMissing_returnsNull() {
            DisplayBase display = mock(DisplayBase.class);

            when(registry.getDefinitionByName("missing")).thenReturn(null);

            assertNull(handler.getAttributeDefinition("missing", display));
        }

        @Test
        void getAttributeDefinition_notApplicable_returnsNull() {
            DisplayBase display = mock(DisplayBase.class);
            AttributeDefinition definition = mock(AttributeDefinition.class);

            when(registry.getDefinitionByName("attr")).thenReturn(definition);
            when(definition.applicableTo(display)).thenReturn(false);

            assertNull(handler.getAttributeDefinition("attr", display));
        }

        @Test
        void getAttributeDefinition_applicable_returnsDefinition() {
            DisplayBase display = mock(DisplayBase.class);
            AttributeDefinition definition = mock(AttributeDefinition.class);

            when(registry.getDefinitionByName("attr")).thenReturn(definition);
            when(definition.applicableTo(display)).thenReturn(true);

            AttributeDefinition<?> result = handler.getAttributeDefinition("attr", display);

            assertSame(definition, result);
        }
    }
}

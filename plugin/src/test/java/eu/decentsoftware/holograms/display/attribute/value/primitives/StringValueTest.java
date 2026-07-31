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

package eu.decentsoftware.holograms.display.attribute.value.primitives;

import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.StaticCompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.placeholder.DisplayPlaceholderService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StringValueTest {

    @Mock
    private DisplayPlaceholderService placeholderService;

    @Nested
    class ConstructorTests {

        @Test
        void shouldThrowWhenPlaceholderServiceIsNull() {
            NullPointerException exception = assertThrows(NullPointerException.class, () -> new StringValue("test", null));

            assertEquals("placeholderService cannot be null", exception.getMessage());
        }

        @Test
        void shouldCreateWithValidInputs() {
            StringValue stringValue = new StringValue("test", placeholderService);

            assertEquals(StringValueType.TYPE_ID, stringValue.getTypeKey());
            assertEquals("test", stringValue.getValue());
            assertEquals("test", stringValue.toHumanReadableString());
            verify(placeholderService).containsPlaceholders("test");
        }

        @Test
        void shouldCreateWithNullValue() {
            StringValue stringValue = new StringValue(null, placeholderService);

            assertEquals(StringValueType.TYPE_ID, stringValue.getTypeKey());
            assertNull(stringValue.getValue());
            verify(placeholderService, never()).containsPlaceholders(any());
        }
    }

    @Nested
    class CompileTests {

        @Test
        void shouldCompileToStaticValueWhenNotContainsPlaceholders() {
            when(placeholderService.containsPlaceholders("test")).thenReturn(false);
            StringValue stringValue = new StringValue("test", placeholderService);
            DisplayRenderContext context = mock(DisplayRenderContext.class);

            CompiledAttributeValue<String> compiledValue = stringValue.compile(context);

            assertInstanceOf(StaticCompiledAttributeValue.class, compiledValue);
            assertEquals("test", compiledValue.evaluate());
            assertFalse(compiledValue.isDynamic());
            verify(placeholderService, never()).replacePlaceholders(any(), any());
        }

        @Test
        void shouldCompileToDynamicValueWhenContainsPlaceholders() {
            when(placeholderService.containsPlaceholders("test")).thenReturn(true);
            StringValue stringValue = new StringValue("test", placeholderService);
            DisplayRenderContext context = mock(DisplayRenderContext.class);
            when(placeholderService.replacePlaceholders("test", context)).thenReturn("replaced");

            CompiledAttributeValue<String> compiledValue = stringValue.compile(context);

            assertInstanceOf(StaticCompiledAttributeValue.class, compiledValue);
            assertEquals("replaced", compiledValue.evaluate());
            assertTrue(compiledValue.isDynamic());
            verify(placeholderService).replacePlaceholders("test", context);
        }

        @Test
        void shouldCompileToStaticValueWhenValueIsNull() {
            StringValue stringValue = new StringValue(null, placeholderService);
            DisplayRenderContext context = mock(DisplayRenderContext.class);

            CompiledAttributeValue<String> compiledValue = stringValue.compile(context);

            assertInstanceOf(StaticCompiledAttributeValue.class, compiledValue);
            assertNull(compiledValue.evaluate());
            assertFalse(compiledValue.isDynamic());
            verify(placeholderService, never()).replacePlaceholders(any(), any());
        }
    }
}
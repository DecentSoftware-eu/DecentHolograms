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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttributeKeyTest {

    @Nested
    class FactoryMethodTests {

        @Test
        void of_validNameAndType_createsKey() {
            AttributeKey<String> key = AttributeKey.of("test", String.class);

            assertNotNull(key);
            assertEquals("test", key.getName());
            assertEquals(String.class, key.getType());
        }

        @Test
        void of_nullName_throwsNullPointerException() {
            NullPointerException ex = assertThrows(NullPointerException.class,
                    () -> AttributeKey.of(null, String.class));
            assertEquals("name cannot be null", ex.getMessage());
        }

        @Test
        void of_nullType_throwsNullPointerException() {
            NullPointerException ex = assertThrows(NullPointerException.class,
                    () -> AttributeKey.of("test", null));
            assertEquals("type cannot be null", ex.getMessage());
        }
    }

    @Nested
    class AccessorTests {

        @Test
        void getName_returnsProvidedName() {
            AttributeKey<Integer> key = AttributeKey.of("count", Integer.class);
            assertEquals("count", key.getName());
        }

        @Test
        void getType_returnsProvidedType() {
            AttributeKey<Integer> key = AttributeKey.of("count", Integer.class);
            assertEquals(Integer.class, key.getType());
        }
    }

    @Nested
    class EqualityTests {

        @Test
        void equals_sameInstance_true() {
            AttributeKey<String> key = AttributeKey.of("a", String.class);
            assertEquals(key, key);
        }

        @Test
        void equals_sameNameSameType_true() {
            AttributeKey<String> a = AttributeKey.of("name", String.class);
            AttributeKey<String> b = AttributeKey.of("name", String.class);

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void equals_sameNameDifferentType_true() {
            AttributeKey<String> a = AttributeKey.of("name", String.class);
            AttributeKey<Integer> b = AttributeKey.of("name", Integer.class);

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void equals_differentName_false() {
            AttributeKey<String> a = AttributeKey.of("a", String.class);
            AttributeKey<String> b = AttributeKey.of("b", String.class);

            assertNotEquals(a, b);
        }

        @Test
        void equals_null_false() {
            AttributeKey<String> key = AttributeKey.of("a", String.class);
            assertNotEquals(null, key);
        }

        @Test
        void equals_differentType_false() {
            AttributeKey<String> key = AttributeKey.of("a", String.class);
            assertNotEquals(new Object(), key);
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        void hashCode_sameName_sameHash() {
            AttributeKey<String> a = AttributeKey.of("x", String.class);
            AttributeKey<Integer> b = AttributeKey.of("x", Integer.class);

            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void hashCode_differentName_differentHash() {
            AttributeKey<String> a = AttributeKey.of("x", String.class);
            AttributeKey<String> b = AttributeKey.of("y", String.class);

            assertNotEquals(a.hashCode(), b.hashCode());
        }
    }

    @Nested
    class ToStringTests {

        @Test
        void toString_containsName() {
            AttributeKey<String> key = AttributeKey.of("visible", String.class);
            assertEquals("AttributeKey[visible]", key.toString());
        }
    }
}

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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StaticDisplayAttributeTest {

    @Test
    void testConstruction() {
        StaticDisplayAttribute<Object> attribute = new StaticDisplayAttribute<>("test", "value");

        assertNotNull(attribute);
        assertEquals("test", attribute.getName());
        assertEquals("value", attribute.getValue());
        assertEquals(DisplayAttributeValueType.STATIC, attribute.getValueType());
    }

    @Test
    void testCopy() {
        StaticDisplayAttribute<Object> attribute = new StaticDisplayAttribute<>("test", "value");
        DisplayAttribute<Object> copy = attribute.copy();

        assertInstanceOf(StaticDisplayAttribute.class, copy);
        assertEquals("test", copy.getName());
        assertEquals("value", copy.getValue());
        assertEquals(DisplayAttributeValueType.STATIC, copy.getValueType());
    }
}
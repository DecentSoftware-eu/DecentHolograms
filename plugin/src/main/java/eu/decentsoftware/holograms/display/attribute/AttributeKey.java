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

import java.util.Objects;

public final class AttributeKey<T> {

    private final String name;
    private final Class<T> type;

    private AttributeKey(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <T> AttributeKey<T> of(String name, Class<T> type) {
        return new AttributeKey<>(name, type);
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AttributeKey[" + name + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AttributeKey)) {
            return false;
        }
        AttributeKey<?> that = (AttributeKey<?>) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
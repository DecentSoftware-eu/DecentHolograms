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

package eu.decentsoftware.holograms.platform.api.render.metadata;

public final class MetadataKey<T> {

    private final MetadataType type;
    private final Class<T> valueType;
    private final MetadataKeyEqualityHandler<T> equalityHandler;

    public MetadataKey(MetadataType type, Class<T> valueType) {
        this(type, valueType, Object::equals);
    }

    public MetadataKey(MetadataType type, Class<T> valueType, MetadataKeyEqualityHandler<T> equalityHandler) {
        this.type = type;
        this.valueType = valueType;
        this.equalityHandler = equalityHandler;
    }

    public MetadataValue<T> createValue(T value) {
        return new MetadataValue<>(this, value);
    }

    public boolean areValuesEqual(MetadataValue<T> value1, MetadataValue<T> value2) {
        return equalityHandler.areEqual(value1.getValue(), value2.getValue());
    }

    public MetadataType getType() {
        return type;
    }

    public Class<T> getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return "MetadataKey[" + type + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MetadataKey)) {
            return false;
        }

        MetadataKey<?> other = (MetadataKey<?>) o;
        return type == other.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}

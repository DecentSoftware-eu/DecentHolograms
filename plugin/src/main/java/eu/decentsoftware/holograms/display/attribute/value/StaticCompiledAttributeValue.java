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

package eu.decentsoftware.holograms.display.attribute.value;

/**
 * A static implementation of {@link CompiledAttributeValue} that represents
 * a precomputed or constant attribute value. This implementation holds a fixed
 * value of type {@code T} and provides it at every evaluation.
 *
 * @param <T> The type of the value being stored and returned
 * @author d0by
 * @since 2.10.0
 */
public final class StaticCompiledAttributeValue<T> implements CompiledAttributeValue<T> {

    private final T value;

    /**
     * Constructs a new {@code StaticCompiledAttributeValue} with the specified value.
     *
     * @param value The value to be held by this instance. This value is constant
     *              and is returned every time the {@link #evaluate()} method is called.
     */
    public StaticCompiledAttributeValue(T value) {
        this.value = value;
    }

    @Override
    public T evaluate() {
        return value;
    }
}

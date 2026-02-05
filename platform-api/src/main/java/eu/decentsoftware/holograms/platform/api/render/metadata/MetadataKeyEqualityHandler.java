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

/**
 * This interface defines a strategy for checking the equality of two objects of a specified type.
 *
 * @param <T> The type of object to be compared for equality.
 * @author d0by
 * @since 2.10.0
 */
public interface MetadataKeyEqualityHandler<T> {

    /**
     * Checks whether the two specified objects are equal.
     *
     * @param first  The first object.
     * @param second The second object.
     * @return True if the objects are equal, false otherwise.
     * @since 2.10.0
     */
    boolean areEqual(T first, T second);
}

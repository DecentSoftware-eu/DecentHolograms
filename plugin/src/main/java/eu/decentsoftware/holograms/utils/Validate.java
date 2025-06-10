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

package eu.decentsoftware.holograms.utils;

/**
 * Utility class for validation checks.
 * Provides methods to validate objects and throw exceptions with custom messages.
 *
 * @author d0by
 * @since 2.10.0
 */
public class Validate {

    private Validate() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Checks if the given object is null and throws an IllegalArgumentException with the provided message if it is.
     *
     * @param object  The object to check for nullity.
     * @param message The message to include in the exception if the object is null.
     * @throws IllegalArgumentException if the object is null.
     * @since 2.10.0
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

}

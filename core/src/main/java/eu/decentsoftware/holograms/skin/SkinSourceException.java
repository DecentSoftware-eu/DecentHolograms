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

package eu.decentsoftware.holograms.skin;

/**
 * An exception that may be thrown by a skin source.
 *
 * @author d0by
 * @see SkinSource
 * @since 2.9.6
 */
public class SkinSourceException extends RuntimeException {
    public SkinSourceException(String message) {
        super(message);
    }

    public SkinSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}

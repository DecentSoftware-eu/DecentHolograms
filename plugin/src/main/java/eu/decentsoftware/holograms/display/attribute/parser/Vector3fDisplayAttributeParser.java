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

package eu.decentsoftware.holograms.display.attribute.parser;

import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import org.jetbrains.annotations.NotNull;

public class Vector3fDisplayAttributeParser implements DisplayAttributeParser<DisplayVector3f> {

    @Override
    public DisplayVector3f parseValue(@NotNull String valueString) {
        String[] parts = valueString.split(",");
        if (parts.length != 3) {
            return null;
        }
        try {
            float x = Float.parseFloat(parts[0].trim());
            float y = Float.parseFloat(parts[1].trim());
            float z = Float.parseFloat(parts[2].trim());
            return new DisplayVector3f(x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

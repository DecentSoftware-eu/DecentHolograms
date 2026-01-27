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

import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import org.jetbrains.annotations.NotNull;

public class BrightnessDisplayAttributeParser implements DisplayAttributeParser<DisplayBrightness> {

    @Override
    public DisplayBrightness parseValue(@NotNull String stringValue) {
        String[] parts = stringValue.split(",");
        if (parts.length != 2) {
            throw new DisplayAttributeParseException("Invalid brightness value: " + stringValue);
        }
        int blockLight = parseInt(parts[0].trim());
        int skyLight = parseInt(parts[1].trim());
        if (blockLight < 0 || blockLight > 15 || skyLight < 0 || skyLight > 15) {
            throw new DisplayAttributeParseException("Brightness values must be between 0 and 15.");
        }
        return DisplayBrightness.of(blockLight, skyLight);
    }

    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new DisplayAttributeParseException("Invalid integer value: " + string);
        }
    }
}

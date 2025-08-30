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

package eu.decentsoftware.holograms.display.command.attribute;

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class Vector3fDisplayAttribute<D> implements DisplayAttribute {

    private final String name;
    private final BiConsumer<D, DisplayVector3f> applyValue;
    private final Class<D> applicableDisplayType;

    public Vector3fDisplayAttribute(String name, BiConsumer<D, DisplayVector3f> applyValue, Class<D> applicableDisplayType) {
        this.name = name;
        this.applyValue = applyValue;
        this.applicableDisplayType = applicableDisplayType;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getValueHints(@NotNull CommandSender sender, @NotNull String currentString) {
        return Arrays.asList("0,0,0", "0.5,0.5,0.5", "1,1,1");
    }

    @Override
    public void applyValue(@NotNull DisplayBase display, @NotNull String value) {
        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new DisplayAttributeValidationException("Attribute is not applicable to this display type.");
        }
        DisplayVector3f vector = parseValue(value);
        if (vector == null) {
            throw new DisplayAttributeValidationException("Expected a vector in the format x,y,z.");
        }
        this.applyValue.accept(applicableDisplayType.cast(display), vector);
    }

    private DisplayVector3f parseValue(@NotNull String valueString) {
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

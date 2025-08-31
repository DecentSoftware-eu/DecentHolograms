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
import eu.decentsoftware.holograms.display.attribute.parser.Vector3fDisplayAttributeParser;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class Vector3fCommandAttribute<D> implements CommandAttribute {

    private static final Vector3fDisplayAttributeParser PARSER = new Vector3fDisplayAttributeParser();
    private final String name;
    private final BiConsumer<D, DisplayVector3f> applyValue;
    private final Class<D> applicableDisplayType;

    public Vector3fCommandAttribute(String name, BiConsumer<D, DisplayVector3f> applyValue, Class<D> applicableDisplayType) {
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
            throw new CommandAttributeValidationException("Attribute is not applicable to this display type.");
        }
        DisplayVector3f vector = PARSER.parseValue(value);
        if (vector == null) {
            throw new CommandAttributeValidationException("Expected a vector in the format x,y,z.");
        }
        this.applyValue.accept(applicableDisplayType.cast(display), vector);
    }
}

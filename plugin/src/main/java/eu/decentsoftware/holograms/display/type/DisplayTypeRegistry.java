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

package eu.decentsoftware.holograms.display.type;

import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public final class DisplayTypeRegistry {

    private final Map<DisplayType, DisplayTypeDefinition<?>> definitions = new EnumMap<>(DisplayType.class);

    public <T extends DisplayTypeDefinition<?>> void registerDisplayType(DisplayType type, T definition) {
        this.definitions.put(type, definition);
    }

    @NotNull
    public DisplayTypeDefinition<?> getDefinition(@NotNull DisplayType type) {
        DisplayTypeDefinition<?> definition = this.definitions.get(type);
        if (definition == null) {
            throw new IllegalStateException("Unknown display type: " + type.name());
        }
        return definition;
    }
}

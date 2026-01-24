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

package eu.decentsoftware.holograms.display.attribute.definition;

import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Definition of an attribute that can be applied to a display.
 *
 * @param <T> The type of the value that the attribute can hold.
 * @author d0by
 * @see AttributeDefinitionRegistry
 * @since 2.10.0
 */
public interface AttributeDefinition<T> {

    /**
     * Get the name of the attribute.
     *
     * <p>Name also serves as a unique identifier.</p>
     *
     * @return The name.
     * @since 2.10.0
     */
    @NotNull
    String getName();

    /**
     * Get the type of the value that the attribute can hold.
     *
     * @return The type of the value.
     * @since 2.10.0
     */
    @NotNull
    Class<T> getValueType();

    /**
     * Get the parser used to parse the attribute's value from a string.
     *
     * @return The parser.
     * @see DisplayAttributeParser
     * @since 2.10.0
     */
    @NotNull
    DisplayAttributeParser<T> getParser();

    /**
     * Get the default value of the attribute.
     *
     * @return The default value.
     * @since 2.10.0
     */
    @Nullable
    T getDefaultValue();

    /**
     * Get the display types that this attribute can be applied to.
     *
     * @return The applicable display types.
     * @see DisplayType
     * @since 2.10.0
     */
    @NotNull
    default DisplayType[] getApplicableDisplayTypes() {
        return DisplayType.values();
    }

}

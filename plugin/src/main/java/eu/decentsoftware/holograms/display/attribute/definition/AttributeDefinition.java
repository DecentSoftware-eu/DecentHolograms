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

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.AttributeValidationException;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

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
     * Get the key of the attribute.
     *
     * @return The key.
     * @see AttributeKey
     * @since 2.10.0
     */
    @NotNull
    AttributeKey<T> getKey();

    /**
     * Get the name of the attribute.
     *
     * <p>Name also serves as a unique identifier.</p>
     *
     * @return The name.
     * @since 2.10.0
     */
    @NotNull
    default String getName() {
        return getKey().getName();
    }

    /**
     * Get the type of the value that the attribute can hold.
     *
     * @return The type of the value.
     * @since 2.10.0
     */
    @NotNull
    default Class<T> getValueType() {
        return getKey().getType();
    }

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

    /**
     * Validate a value for this attribute.
     *
     * @param value The value to validate.
     * @throws AttributeValidationException If the value is invalid.
     * @since 2.10.0
     */
    default void validate(T value) throws AttributeValidationException {
        // No validation by default
    }

    /**
     * Provide hints for possible values for this attribute.
     *
     * @param sender       The command sender requesting the hints.
     * @param currentInput The current input string.
     * @return A list of possible value hints.
     * @since 2.10.0
     */
    @NotNull
    default List<String> valueHints(CommandSender sender, String currentInput) {
        return Collections.emptyList();
    }

    /**
     * Format the attribute value as a string for display purposes.
     *
     * @param value The value to format.
     * @return The formatted string.
     * @since 2.10.0
     */
    default String format(T value) {
        return String.valueOf(value);
    }

    /**
     * Check if this attribute is applicable to a specific display.
     *
     * @param display The display to check.
     * @return True if applicable, false otherwise.
     * @since 2.10.0
     */
    default boolean applicableTo(DisplayBase display) {
        for (DisplayType applicableDisplayType : getApplicableDisplayTypes()) {
            if (applicableDisplayType == display.getType()) {
                return true;
            }
        }
        return false;
    }
}

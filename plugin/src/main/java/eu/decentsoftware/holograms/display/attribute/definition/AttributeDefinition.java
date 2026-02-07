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
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.AttributeParseException;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.StaticAttributeValue;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Apply the attribute to a {@link FinalDisplayRenderState}.
     *
     * <p>This method may perform post-processing on the value before applying it.</p>
     *
     * @param value The attribute value to apply.
     * @param state The render state.
     * @since 2.10.0
     */
    void apply(AttributeValue<T> value, FinalDisplayRenderState state);

    default void validate(String rawContent) throws AttributeParseException {
        // TODO
        String[] args = rawContent.split(" ");
        parse(args); // Throws AttributeParseException
    }

    default AttributeValue<T> resolve(DisplayAttribute<T> attribute, DisplayRenderContext context) {
        // TODO
        String rawValue = attribute.getRawValue();
        String[] args = rawValue.split(" ");
        T value = parse(args);
        return new StaticAttributeValue<>(value);
    }

    /**
     * Format the attribute value as a string for display purposes.
     *
     * @param value The value to format.
     * @return The formatted string.
     * @since 2.10.0
     */
    default String format(T value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    /**
     * Parse the attribute value from command arguments and validate it.
     *
     * @param args The command arguments.
     * @return The validated, parsed value.
     * @throws AttributeParseException If the value cannot be parsed.
     * @since 2.10.0
     */
    @NotNull
    T parse(String[] args);

    /**
     * Get command hints for this attribute based on the current input.
     *
     * @param sender The command sender.
     * @param args   The current input.
     * @return The hints.
     * @since 2.10.0
     */
    @NotNull
    default List<String> getHints(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}

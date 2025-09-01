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
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandAttribute {

    /**
     * Gets the name of the attribute.
     *
     * <p>This name is what's used in the command syntax. There can't be spaces in the name.</p>
     *
     * <p>Example name: {@code view-distance}</p>
     *
     * @return The name of the attribute.
     */
    @NotNull
    String getName();

    /**
     * Gets a list of possible values for this attribute based on what the user already typed.
     *
     * @param sender        The sender of the command.
     * @param currentString The current string the user has typed for this attribute.
     * @return A list of possible values to suggest to the user.
     */
    List<String> getValueHints(@NotNull CommandSender sender, @NotNull String currentString);

    /**
     * Applies the given value to the given display.
     *
     * @param display The display to apply the value to.
     * @param value   The value to apply.
     * @throws CommandAttributeValidationException If the given value is invalid for the given display.
     */
    void applyValue(@NotNull DisplayBase display, @NotNull String value);

    /**
     * Resets the value of this attribute on the given display to its default value.
     *
     * @param display The display to reset the value on.
     */
    void resetValue(@NotNull DisplayBase display);
}

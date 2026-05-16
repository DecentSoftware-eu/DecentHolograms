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

package eu.decentsoftware.holograms.platform.api.placeholder;

import org.jetbrains.annotations.NotNull;

/**
 * Provides a mechanism to replace placeholders within a given input string.
 *
 * <p>A placeholder provider is used to process input strings containing placeholders
 * and replace them with corresponding values based on the provided context. The specific
 * implementation of the provider determines how placeholders are identified and replaced.</p>
 *
 * <p>Typical usage involves integrating this interface with platform-specific placeholder
 * processing systems, such as PlaceholderAPI on Bukkit-based servers.</p>
 *
 * <p>Implementations of this interface may use the {@link PlaceholderContext} to
 * retrieve relevant contextual data, such as player information, to resolve placeholders.</p>
 *
 * @author d0by
 * @see PlaceholderContext
 * @since 2.10.0
 */
public interface PlaceholderProvider {

    /**
     * Replaces placeholders in the given input string using the provided context.
     *
     * @param input The input string.
     * @param ctx   Contextual information for placeholder resolution.
     * @return The input string with placeholders replaced.
     * @since 2.10.0
     */
    @NotNull
    String replace(@NotNull String input, @NotNull PlaceholderContext ctx);

    /**
     * Checks if the given input string contains placeholders.
     *
     * @param input The input string to check for placeholders. Must not be null.
     * @return True if the input contains placeholders, false otherwise.
     */
    boolean containsPlaceholders(@NotNull String input);
}

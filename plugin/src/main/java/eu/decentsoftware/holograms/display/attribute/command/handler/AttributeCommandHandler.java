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

package eu.decentsoftware.holograms.display.attribute.command.handler;

import eu.decentsoftware.holograms.display.attribute.AttributeParseException;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Interface for handling attribute command parsing and tab-completion.
 *
 * @param <T> The type of the attribute value this handler processes.
 * @author d0by
 * @since 2.10.0
 */
public interface AttributeCommandHandler<T> {

    /**
     * Gets the keyword associated with this command handler.
     * If null, this handler is considered the default handler for its attribute type.
     *
     * <p>The keyword is used to find the correct handler for given command arguments.
     * If the first argument of the value matches this keyword, this handler will
     * be used for tab-completion and parsing of the value. If the first argument
     * doesn't match any handler's keyword, the default handler is used.</p>
     *
     * @return The keyword for this handler, or null if it's the default handler.
     * @since 2.10.0
     */
    String getKeyword();

    /**
     * Checks if this handler is the default handler for its attribute type.
     *
     * @return True if this handler is the default handler, false otherwise.
     * @see #getKeyword()
     * @since 2.10.0
     */
    default boolean isDefault() {
        return getKeyword() == null;
    }

    /**
     * Parses the given command arguments into an AttributeValue of type T.
     *
     * @param args The command arguments.
     * @return The parsed attribute value.
     * @throws AttributeParseException If the attribute value cannot be parsed.
     * @since 2.10.0
     */
    AttributeValue<T> parse(String[] args);

    /**
     * Provides tab-completion hints based on the current command arguments.
     *
     * @param sender The sender of the command.
     * @param args   The current command arguments.
     * @return A list of possible completions for the current arguments.
     * @since 2.10.0
     */
    List<String> getHints(CommandSender sender, String[] args);
}

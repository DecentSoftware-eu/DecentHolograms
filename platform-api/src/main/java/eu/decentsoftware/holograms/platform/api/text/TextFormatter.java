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

package eu.decentsoftware.holograms.platform.api.text;

import eu.decentsoftware.holograms.platform.api.capability.PlatformCapabilities;
import eu.decentsoftware.holograms.platform.api.capability.PlatformCapability;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a contract for formatting text according to platform-specific text formats.
 *
 * <p>Implementations of this interface are responsible for processing input text
 * and returning a formatted version according to the specified {@link TextFormat}.
 * The formatter translates raw text containing formatting codes into platform-appropriate
 * representations that can be displayed to users.</p>
 *
 * <h3>Usage Example</h3>
 * <pre>{@code
 * TextFormatter formatter = // obtain formatter implementation
 * String formatted = formatter.format("&cRed text #ff90ff and <#ff90ff>gradient</#ff9000>");
 * // Result: formatted string with colors applied according to platform
 * }</pre>
 *
 * @author d0by
 * @see TextFormat
 * @see PlatformCapabilities#supports(PlatformCapability)
 * @since 2.10.0
 */
public interface TextFormatter {

    /**
     * Formats the given text according to the implementation's text format.
     *
     * @param text the raw text to format; may contain formatting codes
     * @return the formatted text with all formatting codes processed and applied
     * @see TextFormat
     * @since 2.10.0
     */
    @NotNull
    String format(@NotNull String text);
}

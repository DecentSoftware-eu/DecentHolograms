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

/**
 * Enum representing different text formatting options supported by the platform.
 *
 * <p>The {@code TextFormat} enum is used to specify the format in which text
 * should be handled or displayed in platform-specific implementations.
 * It can be queried using methods such as {@link PlatformCapabilities#supports(PlatformCapability)}
 * to determine if a particular format is supported.</p>
 *
 * <p>This enum is used in conjunction with the {@link PlatformCapability} marker interface
 * to identify features supported by a given platform.</p>
 *
 * @author d0by
 * @see PlatformCapabilities
 * @see TextFormatter
 * @since 2.10.0
 */
public enum TextFormat implements PlatformCapability {
    /**
     * Represents the legacy text formatting option.
     *
     * <h3>This format supports the following formatting:</h3>
     * <ul>
     *   <li><b>Legacy Color Codes:</b> Color codes represented by '&amp;' and '§' characters
     *       followed by a valid color character (e.g., {@code &c} for red, {@code §a} for green)</li>
     *   <li><b>RGB Colors:</b> Custom hexadecimal color codes in the format {@code #RRGGBB}
     *       (e.g., {@code #ff90ff} for a light purple)</li>
     *   <li><b>Gradients:</b> Color gradients using the syntax {@code <#RRGGBB>text</#RRGGBB>}
     *       (e.g., {@code <#ff90ff>Gradient</#ff9000>} creates a gradient from light purple to orange)</li>
     * </ul>
     *
     * @see PlatformCapabilities#supports(PlatformCapability)
     * @since 2.10.0
     */
    LEGACY
}

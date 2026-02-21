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

package eu.decentsoftware.holograms.platform.api.capability;

import eu.decentsoftware.holograms.platform.api.text.TextFormat;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a mechanism to check platform-specific capabilities.
 *
 * <p>An implementation of this interface is responsible for determining whether certain features
 * or functionalities, represented by {@link PlatformCapability}, are supported by the platform.</p>
 *
 * @author d0by
 * @see PlatformCapability
 * @since 2.10.0
 */
public interface PlatformCapabilities {

    /**
     * Checks whether the platform supports the specified capability.
     *
     * @param capability The capability to check.
     * @return True if the platform supports the specified capability, false otherwise.
     * @see PlatformCapability
     * @see MinecraftFeature
     * @see TextFormat
     * @since 2.10.0
     */
    boolean supports(@NotNull PlatformCapability capability);
}

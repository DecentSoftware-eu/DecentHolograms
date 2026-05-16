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

/**
 * Enum representing specific Minecraft features that can be queried for platform support.
 *
 * <p>Each constant in this enum defines a particular Minecraft feature. These constants act
 * as keys when performing capability checks using the {@link PlatformCapabilities} interface.</p>
 *
 * <p>This enum is used in conjunction with the {@link PlatformCapability} marker interface to
 * identify features supported by a given platform.</p>
 *
 * @author d0by
 * @see PlatformCapability
 * @see PlatformCapabilities
 * @since 2.10.0
 */
public enum MinecraftFeature implements PlatformCapability {
    /**
     * This capability represents the ability to use Minecraft's Display Entities,
     * introduced in Minecraft 1.19.4.
     */
    DISPLAY_ENTITIES
}

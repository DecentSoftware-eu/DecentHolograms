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

package eu.decentsoftware.holograms.platform.api.server;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * What the plugin is running on: which server software, and which Minecraft version.
 *
 * <p>Detected once during startup and passed around from there, rather than looked up on demand.</p>
 *
 * @author d0by
 * @see ServerPlatformDetector
 * @since 2.10.2
 */
public final class ServerPlatform {

    private final ServerPlatformType type;
    private final MinecraftVersion minecraftVersion;

    /**
     * Creates a new platform descriptor.
     *
     * @param type             The most specific matching platform.
     * @param minecraftVersion The Minecraft version the server is running.
     * @throws NullPointerException If either argument is null.
     */
    public ServerPlatform(@NotNull ServerPlatformType type, @NotNull MinecraftVersion minecraftVersion) {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(minecraftVersion, "minecraftVersion cannot be null");
        this.type = type;
        this.minecraftVersion = minecraftVersion;
    }

    /**
     * @return The most specific platform detected, for example {@code FOLIA} rather than {@code PAPER}.
     */
    @NotNull
    public ServerPlatformType getType() {
        return type;
    }

    /**
     * Checks whether the server is, or is derived from, the given platform.
     *
     * <p>Prefer this over comparing {@link #getType()} directly, so that forks inherit the
     * behavior of what they are built on.</p>
     *
     * @param type The platform to test against.
     * @return True if the server is the given platform or a descendant of it.
     */
    public boolean isA(@NotNull ServerPlatformType type) {
        return this.type.isA(type);
    }

    /**
     * @return The Minecraft version the server is running.
     */
    @NotNull
    public MinecraftVersion getMinecraftVersion() {
        return minecraftVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServerPlatform)) {
            return false;
        }
        ServerPlatform that = (ServerPlatform) obj;
        return type == that.type && minecraftVersion.equals(that.minecraftVersion);
    }

    @Override
    public int hashCode() {
        return 31 * type.hashCode() + minecraftVersion.hashCode();
    }

    @Override
    public String toString() {
        return type + " " + minecraftVersion;
    }
}

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

package eu.decentsoftware.holograms.platform.bukkit.server;

import eu.decentsoftware.holograms.platform.api.server.MinecraftVersion;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatform;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformDetector;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Works out which Bukkit-family server software the plugin is running on.
 *
 * @author d0by
 * @since 2.10.2
 */
public final class BukkitServerPlatformService {

    private final ServerPlatformDetector detector;

    public BukkitServerPlatformService() {
        this(new ServerPlatformDetector(BukkitServerPlatformProbes.create()));
    }

    /**
     * @param detector The detector to use. Intended for testing.
     */
    public BukkitServerPlatformService(@NotNull ServerPlatformDetector detector) {
        this.detector = detector;
    }

    /**
     * Identifies the running server.
     *
     * @return The detected platform, or empty if the server software or its version could not be identified.
     */
    @NotNull
    public Optional<ServerPlatform> detect() {
        Optional<MinecraftVersion> minecraftVersion = detectMinecraftVersion();
        return minecraftVersion.flatMap(detector::detect);
    }

    private Optional<MinecraftVersion> detectMinecraftVersion() {
        // getBukkitVersion() reports "1.20.6-R0.1-SNAPSHOT" on most servers, and something like
        // "26.1.2.build.52-beta" on recent Paper. MinecraftVersion#parse handles both, so no
        // platform-specific branch is needed. getVersion() is only a fallback for anything that
        // reports a shape parse cannot read at all.
        Optional<MinecraftVersion> parsed = parse(Bukkit.getServer().getBukkitVersion());
        return parsed.isPresent() ? parsed : parse(Bukkit.getServer().getVersion());
    }

    private static Optional<MinecraftVersion> parse(String version) {
        try {
            return Optional.of(MinecraftVersion.parse(version));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}

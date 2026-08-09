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

import eu.decentsoftware.holograms.platform.api.server.ServerPlatformProbe;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The probes for Bukkit-family server software.
 *
 * <p>Add a fork by adding a probe here. Nothing else needs to change: the detector runs every
 * probe and picks the most specific match.</p>
 *
 * @author d0by
 * @since 2.10.2
 */
public final class BukkitServerPlatformProbes {

    private BukkitServerPlatformProbes() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * @return The probes, in no particular order.
     */
    @NotNull
    @Unmodifiable
    public static List<ServerPlatformProbe> create() {
        return Collections.unmodifiableList(Arrays.asList(
                new ClassPresenceProbe(ServerPlatformType.BUKKIT,
                        "org.bukkit.Bukkit"),
                new ClassPresenceProbe(ServerPlatformType.SPIGOT,
                        "org.spigotmc.SpigotConfig"),
                // Paper has moved its configuration class twice; PaperBootstrap covers the rest.
                new ClassPresenceProbe(ServerPlatformType.PAPER,
                        "io.papermc.paper.configuration.Configuration",
                        "com.destroystokyo.paper.PaperConfig",
                        "io.papermc.paper.PaperBootstrap"),
                new ClassPresenceProbe(ServerPlatformType.FOLIA,
                        "io.papermc.paper.threadedregions.RegionizedServer")
        ));
    }
}

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

package eu.decentsoftware.holograms.platform.bukkit;

import eu.decentsoftware.holograms.nms.NmsAdapterFactory;
import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatform;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * What the Bukkit platform hands to the core once it has worked out where it is running.
 *
 * @author d0by
 * @see BukkitPlatformFactory
 * @since 2.10.2
 */
public final class BukkitPlatformBootstrap {

    private final ServerPlatform serverPlatform;
    private final NmsAdapter nmsAdapter;
    private final BukkitPlatformAdapter platformAdapter;

    BukkitPlatformBootstrap(@NotNull ServerPlatform serverPlatform,
                            @NotNull NmsAdapter nmsAdapter,
                            @NotNull BukkitPlatformAdapter platformAdapter) {
        this.serverPlatform = Objects.requireNonNull(serverPlatform, "serverPlatform cannot be null");
        this.nmsAdapter = Objects.requireNonNull(nmsAdapter, "nmsAdapter cannot be null");
        this.platformAdapter = Objects.requireNonNull(platformAdapter, "platformAdapter cannot be null");
    }

    /**
     * @return The detected server software and Minecraft version.
     */
    @NotNull
    public ServerPlatform getServerPlatform() {
        return serverPlatform;
    }

    /**
     * @return The NMS adapter for this server version.
     * @see NmsAdapterFactory
     */
    @NotNull
    public NmsAdapter getNmsAdapter() {
        return nmsAdapter;
    }

    /**
     * @return The platform adapter the core runs against.
     */
    @NotNull
    public BukkitPlatformAdapter getPlatformAdapter() {
        return platformAdapter;
    }
}

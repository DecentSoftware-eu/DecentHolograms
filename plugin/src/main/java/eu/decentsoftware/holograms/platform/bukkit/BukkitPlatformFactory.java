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

import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.logging.Log;
import eu.decentsoftware.holograms.nms.NmsAdapterFactory;
import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatform;
import eu.decentsoftware.holograms.platform.bukkit.server.BukkitServerPlatformService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Boots the Bukkit platform: works out where it is running, loads the matching NMS module, and
 * builds the adapter the core runs against.
 *
 * @author d0by
 * @since 2.10.2
 */
public final class BukkitPlatformFactory {

    private final BukkitServerPlatformService platformService;
    private final NmsAdapterFactory nmsAdapterFactory;

    public BukkitPlatformFactory() {
        this(new BukkitServerPlatformService(), new NmsAdapterFactory());
    }

    /**
     * Creates a new factory with the given dependencies.
     *
     * @param platformService   Detects the server software. Intended for testing.
     * @param nmsAdapterFactory Loads NMS modules. Intended for testing.
     */
    public BukkitPlatformFactory(@NotNull BukkitServerPlatformService platformService,
                                 @NotNull NmsAdapterFactory nmsAdapterFactory) {
        this.platformService = Objects.requireNonNull(platformService, "platformService cannot be null");
        this.nmsAdapterFactory = Objects.requireNonNull(nmsAdapterFactory, "nmsAdapterFactory cannot be null");
    }

    /**
     * Detects the server, loads its NMS module and assembles the platform.
     *
     * @param plugin The plugin instance.
     * @return Everything the core needs to start.
     * @throws UnsupportedServerException If the server software or version is not supported.
     */
    @NotNull
    public BukkitPlatformBootstrap create(@NotNull JavaPlugin plugin) {
        Objects.requireNonNull(plugin, "plugin cannot be null");

        ServerPlatform serverPlatform = platformService.detect()
                .orElseThrow(() -> new UnsupportedServerException(
                        "Could not identify this server. Version reported: " + Bukkit.getServer().getVersion()));

        Optional<Version> nmsVersion = Version.resolve(serverPlatform);
        if (!nmsVersion.isPresent()) {
            throw new UnsupportedServerException("Unsupported server version: " + serverPlatform);
        }

        // Kept until the remaining version checks move onto PlatformCapabilities.
        Version.setCurrent(nmsVersion.get());

        NmsAdapter nmsAdapter;
        try {
            nmsAdapter = nmsAdapterFactory.createNmsAdapter(nmsVersion.get().name());
        } catch (DecentHologramsNmsException e) {
            throw new UnsupportedServerException("Failed to load NMS module " + nmsVersion.get().name()
                    + " for " + serverPlatform + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UnsupportedServerException("Unexpected error loading NMS module " + nmsVersion.get().name()
                    + " for " + serverPlatform, e);
        }
        Log.info("Detected %s, using NMS module %s.", serverPlatform, nmsVersion.get().name());

        BukkitPlatformAdapter platformAdapter = new BukkitPlatformAdapter(plugin, nmsAdapter.getDisplayRendererFactory());
        return new BukkitPlatformBootstrap(serverPlatform, nmsAdapter, platformAdapter);
    }
}

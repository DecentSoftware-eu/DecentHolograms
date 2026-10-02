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
import eu.decentsoftware.holograms.platform.api.scheduler.PlatformScheduler;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatform;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformType;
import eu.decentsoftware.holograms.platform.bukkit.player.BukkitPlayer;
import eu.decentsoftware.holograms.platform.bukkit.player.BukkitPlayerFactory;
import eu.decentsoftware.holograms.platform.bukkit.player.FoliaPlayer;
import eu.decentsoftware.holograms.platform.bukkit.scheduler.BukkitPlatformScheduler;
import eu.decentsoftware.holograms.platform.bukkit.scheduler.FoliaPlatformScheduler;
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

        Version nmsVersion = resolveNmsVersion(serverPlatform);
        // Kept until the remaining version checks move onto PlatformCapabilities.
        Version.setCurrent(nmsVersion);

        NmsAdapter nmsAdapter = createNmsAdapter(nmsVersion, serverPlatform);
        Log.info("Detected %s, using NMS module %s.", serverPlatform, nmsVersion.name());

        BukkitPlatformAdapter platformAdapter = createAdapter(plugin, serverPlatform, nmsAdapter);
        return new BukkitPlatformBootstrap(serverPlatform, nmsAdapter, platformAdapter);
    }

    private static Version resolveNmsVersion(ServerPlatform serverPlatform) {
        Optional<Version> nmsVersionOptional = Version.resolve(serverPlatform);
        if (!nmsVersionOptional.isPresent()) {
            throw new UnsupportedServerException("Unsupported server version: " + serverPlatform);
        }
        return nmsVersionOptional.get();
    }

    private NmsAdapter createNmsAdapter(Version nmsVersion, ServerPlatform serverPlatform) {
        NmsAdapter nmsAdapter;
        try {
            nmsAdapter = nmsAdapterFactory.createNmsAdapter(nmsVersion.name());
        } catch (DecentHologramsNmsException e) {
            throw new UnsupportedServerException("Failed to load NMS module " + nmsVersion.name()
                    + " for " + serverPlatform + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UnsupportedServerException("Unexpected error loading NMS module " + nmsVersion.name()
                    + " for " + serverPlatform, e);
        }
        return nmsAdapter;
    }

    private static BukkitPlatformAdapter createAdapter(JavaPlugin plugin, ServerPlatform serverPlatform, NmsAdapter nmsAdapter) {
        try {
            boolean regionThreaded = serverPlatform.isA(ServerPlatformType.FOLIA);
            return new BukkitPlatformAdapter(
                    plugin,
                    nmsAdapter.getDisplayRendererFactory(),
                    getPlayerFactory(regionThreaded),
                    getScheduler(plugin, regionThreaded)
            );
        } catch (Exception e) {
            // Assembling the platform can fail if detection disagrees with what the server
            // actually offers - a Folia probe matching without the Folia API, say. Reported the
            // same way as an unsupported version, rather than escaping as a stack trace.
            throw new UnsupportedServerException("Failed to set up the platform for " + serverPlatform, e);
        }
    }

    private static BukkitPlayerFactory getPlayerFactory(boolean regionThreaded) {
        return regionThreaded ? FoliaPlayer::new : BukkitPlayer::new;
    }

    private static PlatformScheduler getScheduler(JavaPlugin plugin, boolean regionThreaded) {
        return regionThreaded ? new FoliaPlatformScheduler(plugin) : new BukkitPlatformScheduler(plugin);
    }
}

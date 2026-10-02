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

package eu.decentsoftware.holograms.platform.bukkit.player;

import eu.decentsoftware.holograms.logging.Log;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * A player on a region-threaded server.
 *
 * <p>Differs from {@link BukkitPlayer} in one respect: a move that crosses a region boundary
 * cannot be performed synchronously, so {@code Entity#teleport} is unsupported and the
 * asynchronous form has to be used instead. Everything else behaves identically.</p>
 *
 * @author d0by
 * @since 2.10.2
 */
public class FoliaPlayer extends BukkitPlayer {

    public FoliaPlayer(Player platformPlayer) {
        super(platformPlayer);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<Boolean> teleport(@NotNull DecentLocation location) {
        Location target = toBukkitLocation(location);
        if (target == null) {
            return CompletableFuture.completedFuture(false);
        }
        try {
            return (CompletableFuture<Boolean>) TeleportAsyncHolder.METHOD.invoke(getBukkitPlayer(), target);
        } catch (InvocationTargetException e) {
            Log.error("Failed to teleport %s.", e.getCause(), getName());
            return CompletableFuture.completedFuture(false);
        } catch (ReflectiveOperationException e) {
            Log.error("Failed to teleport %s.", e, getName());
            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * Resolved on first teleport rather than in a static initializer. Eager resolution would mean
     * that merely loading this class on a server without the Paper API fails with an
     * {@link ExceptionInInitializerError}, which cannot be recovered from.
     */
    private static final class TeleportAsyncHolder {

        private static final Method METHOD = find();

        private TeleportAsyncHolder() {
        }

        private static Method find() {
            try {
                // Paper API rather than Folia's own, but absent from the Spigot API this is built
                // against, so it has to be resolved at runtime.
                return Entity.class.getMethod("teleportAsync", Location.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(
                        "Entity#teleportAsync is not available on this server. FoliaPlayer should only be used "
                                + "once the platform has been detected as Folia.", e);
            }
        }
    }
}

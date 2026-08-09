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

import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import com.cryptomorin.xseries.XSound;
import eu.decentsoftware.holograms.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BukkitPlayer implements PlatformPlayer {

    private final Player platformPlayer;

    public BukkitPlayer(Player platformPlayer) {
        this.platformPlayer = platformPlayer;
    }

    public Player getBukkitPlayer() {
        return platformPlayer;
    }

    @Override
    public @NotNull String getName() {
        return platformPlayer.getName();
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return platformPlayer.getUniqueId();
    }

    @Override
    public DecentLocation getLocation() {
        Location location = platformPlayer.getLocation();
        return new DecentLocation(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    @NotNull
    @Override
    public CompletableFuture<Boolean> teleport(@NotNull DecentLocation location) {
        Objects.requireNonNull(location, "location cannot be null");
        World world = Bukkit.getWorld(location.getWorldName());
        if (world == null) {
            Log.warn("Cannot teleport %s: world '%s' is not loaded.", getName(), location.getWorldName());
            return CompletableFuture.completedFuture(false);
        }
        Location bukkitLocation = new Location(world, location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch());
        return CompletableFuture.completedFuture(platformPlayer.teleport(bukkitLocation));
    }

    @Override
    public void chat(@NotNull String message) {
        Objects.requireNonNull(message, "message cannot be null");
        platformPlayer.chat(message);
    }

    @Override
    public void sendMessage(@NotNull String message) {
        Objects.requireNonNull(message, "message cannot be null");
        platformPlayer.sendMessage(message);
    }

    @Override
    public void playSound(@NotNull String sound, float volume, float pitch) {
        Objects.requireNonNull(sound, "sound cannot be null");

        Sound bukkitSound = XSound.of(sound)
                .map(XSound::get)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown sound '" + sound + "'. Expected a Mojang sound key such as 'minecraft:entity.player.levelup'."));
        platformPlayer.playSound(platformPlayer.getLocation(), bukkitSound, volume, pitch);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BukkitPlayer)) {
            return false;
        }
        BukkitPlayer that = (BukkitPlayer) o;
        return Objects.equals(platformPlayer.getUniqueId(), that.platformPlayer.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(platformPlayer.getUniqueId());
    }
}

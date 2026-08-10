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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * These tests run against the Spigot API, which has neither {@code Entity#teleportAsync} nor the
 * Folia schedulers, so the teleport itself cannot be exercised here — only the paths that stop
 * before reaching it. What is worth pinning is that the class stays usable off Folia: an eager
 * lookup of the Paper method would make simply loading it fail unrecoverably.
 */
class FoliaPlayerTest {

    private Player bukkitPlayer;
    private FoliaPlayer player;
    private MockedStatic<Bukkit> bukkitMock;

    @BeforeEach
    void setUp() {
        bukkitPlayer = mock(Player.class);
        player = new FoliaPlayer(bukkitPlayer);
        bukkitMock = mockStatic(Bukkit.class);
    }

    @AfterEach
    void tearDown() {
        bukkitMock.close();
    }

    @Test
    void isLoadableWithoutThePaperApi() {
        // Would fail with ExceptionInInitializerError if teleportAsync were resolved eagerly.
        assertDoesNotThrow(() -> new FoliaPlayer(mock(Player.class)));
    }

    @Test
    void reportsFailureRatherThanThrowingWhenTheWorldIsNotLoaded() {
        bukkitMock.when(() -> Bukkit.getWorld("gone")).thenReturn(null);

        assertFalse(player.teleport(new DecentLocation("gone", 0, 0, 0, 0f, 0f)).join());
    }

    @Test
    void neverUsesTheSynchronousTeleport() {
        // The whole reason this class exists: Entity#teleport cannot move a player across
        // regions, so it must not be reached even as a fallback.
        bukkitMock.when(() -> Bukkit.getWorld("gone")).thenReturn(null);

        player.teleport(new DecentLocation("gone", 0, 0, 0, 0f, 0f)).join();

        verify(bukkitPlayer, never()).teleport(any(Location.class));
    }

    @Test
    void inheritsTheRemainingPlayerActions() {
        // Only teleport differs; anything else overridden here would be a mistake.
        player.chat("hello");
        player.sendMessage("hi");

        verify(bukkitPlayer).chat("hello");
        verify(bukkitPlayer).sendMessage("hi");
    }
}

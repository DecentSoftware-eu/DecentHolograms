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

import com.cryptomorin.xseries.XSound;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BukkitPlayerTest {

    @Mock
    private Player bukkitPlayer;
    @Mock
    private World world;
    @InjectMocks
    private BukkitPlayer player;
    private MockedStatic<Bukkit> bukkitMock;

    @BeforeEach
    void setUp() {
        bukkitMock = mockStatic(Bukkit.class);
    }

    @AfterEach
    void tearDown() {
        bukkitMock.close();
    }

    @Nested
    class TeleportTests {

        @Test
        void movesThePlayerToTheGivenLocation() {
            bukkitMock.when(() -> Bukkit.getWorld("world")).thenReturn(world);
            when(bukkitPlayer.teleport(any(Location.class))).thenReturn(true);

            assertTrue(player.teleport(new DecentLocation("world", 1.5, 64.0, -2.5, 90f, 45f)).join());

            ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
            verify(bukkitPlayer).teleport(captor.capture());
            Location sent = captor.getValue();
            assertEquals(world, sent.getWorld());
            assertEquals(1.5, sent.getX());
            assertEquals(64.0, sent.getY());
            assertEquals(-2.5, sent.getZ());
            assertEquals(90f, sent.getYaw());
            assertEquals(45f, sent.getPitch());
        }

        @Test
        void reportsFailureRatherThanThrowingWhenTheWorldIsNotLoaded() {
            bukkitMock.when(() -> Bukkit.getWorld("gone")).thenReturn(null);

            assertFalse(player.teleport(new DecentLocation("gone", 0, 0, 0, 0f, 0f)).join());

            verify(bukkitPlayer, never()).teleport(any(Location.class));
        }

        @Test
        void propagatesARejectedTeleport() {
            bukkitMock.when(() -> Bukkit.getWorld("world")).thenReturn(world);
            when(bukkitPlayer.teleport(any(Location.class))).thenReturn(false);

            assertFalse(player.teleport(new DecentLocation("world", 0, 0, 0, 0f, 0f)).join());
        }

        @Test
        void rejectsNullLocation() {
            assertThrows(NullPointerException.class, () -> player.teleport(null));
        }
    }

    @Nested
    class MessagingTests {

        @Test
        void chatSpeaksAsThePlayer() {
            player.chat("hello");

            verify(bukkitPlayer).chat("hello");
        }

        @Test
        void sendMessageDeliversTheTextUnchanged() {
            // Colourising is the caller's job; this must not double-process the text.
            player.sendMessage("§aalready coloured");

            verify(bukkitPlayer).sendMessage("§aalready coloured");
        }

        @Test
        void rejectsNullMessages() {
            assertThrows(NullPointerException.class, () -> player.chat(null));
            assertThrows(NullPointerException.class, () -> player.sendMessage(null));
        }
    }

    @Nested
    class SoundTests {

        private static final String CANONICAL = "entity.player.levelup";
        private static final String NAMESPACED = "minecraft:entity.player.levelup";
        private static final String BUKKIT_CONSTANT = "ENTITY_PLAYER_LEVELUP";

        private Sound expected() {
            return XSound.of(CANONICAL).map(XSound::get).orElseThrow(IllegalStateException::new);
        }

        @Test
        void playsACanonicalMinecraftKeyAtThePlayer() {
            Location at = new Location(world, 1, 2, 3);
            when(bukkitPlayer.getLocation()).thenReturn(at);

            player.playSound(CANONICAL, 0.5f, 1.5f);

            verify(bukkitPlayer).playSound(at, expected(), 0.5f, 1.5f);
        }

        @ParameterizedTest
        @ValueSource(strings = {CANONICAL, NAMESPACED, BUKKIT_CONSTANT, "Entity.Player.LevelUp"})
        void acceptsEverySpellingOfTheSameSound(String spelling) {
            // The Bukkit constant matters for backward compatibility: existing configurations are
            // written with it, and must keep working now that the contract is the Mojang key.
            when(bukkitPlayer.getLocation()).thenReturn(new Location(world, 0, 0, 0));

            player.playSound(spelling, 1f, 1f);

            verify(bukkitPlayer).playSound(any(Location.class), eq(expected()), anyFloat(), anyFloat());
        }

        @Test
        void rejectsAnUnknownSound() {
            // Reported rather than ignored, so a typo in someone's configuration is visible. The
            // caller decides how to surface it; nothing reaches the server either way.
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> player.playSound("not.a.real.sound", 1f, 1f));

            assertTrue(exception.getMessage().contains("not.a.real.sound"));
            verify(bukkitPlayer, never()).playSound(any(Location.class), any(Sound.class), anyFloat(), anyFloat());
        }

        @Test
        void rejectsNullSound() {
            assertThrows(NullPointerException.class, () -> player.playSound(null, 1f, 1f));
        }
    }
}

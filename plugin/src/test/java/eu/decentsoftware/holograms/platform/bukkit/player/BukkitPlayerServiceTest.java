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

import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BukkitPlayerServiceTest {

    /**
     * Stands in for the platform-specific implementation a server variant would supply, so the
     * tests assert that the service goes through the factory rather than constructing players itself.
     */
    private static final class FlavouredPlayer extends BukkitPlayer {
        private FlavouredPlayer(Player player) {
            super(player);
        }
    }

    private final List<Player> created = new ArrayList<>();
    private BukkitPlayerService service;

    @BeforeEach
    void setUp() {
        created.clear();
        service = new BukkitPlayerService(player -> {
            created.add(player);
            return new FlavouredPlayer(player);
        });
    }

    private Player player() {
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        return player;
    }

    @Nested
    class FactoryTests {

        @Test
        void buildsRegisteredPlayersThroughTheFactory() {
            Player player = player();

            service.registerPlayer(player);

            assertEquals(1, created.size());
            assertInstanceOf(FlavouredPlayer.class, service.getPlayer(player.getUniqueId()),
                    "the service must not construct BukkitPlayer directly");
        }

        @Test
        void buildsUnregisteredPlayersThroughTheFactoryToo() {
            Player player = player();

            PlatformPlayer resolved = service.getPlayer(player);

            assertInstanceOf(FlavouredPlayer.class, resolved);
            assertEquals(1, created.size());
        }

        @Test
        void rejectsAMissingFactory() {
            Exception exception = assertThrows(NullPointerException.class, () -> new BukkitPlayerService(null));

            assertEquals("playerFactory cannot be null", exception.getMessage());
        }
    }

    @Nested
    class RegistrationTests {

        @Test
        void reusesTheRegisteredInstance() {
            Player player = player();
            service.registerPlayer(player);

            PlatformPlayer resolved = service.getPlayer(player);

            assertSame(service.getPlayer(player.getUniqueId()), resolved);
            assertEquals(1, created.size(), "an already registered player must not be rebuilt");
        }

        @Test
        void registeringExposesThePlayerAsOnline() {
            Player player = player();

            service.registerPlayer(player);

            assertEquals(1, service.getOnlinePlayers().size());
        }

        @Test
        void unregisteringRemovesFromBothViews() {
            Player player = player();
            service.registerPlayer(player);

            service.unregisterPlayer(player);

            assertNull(service.getPlayer(player.getUniqueId()));
            assertTrue(service.getOnlinePlayers().isEmpty());
        }

        @Test
        void unregisteringDoesNotBuildAThrowawayPlayer() {
            Player player = player();
            service.registerPlayer(player);

            service.unregisterPlayer(player);

            assertEquals(1, created.size(), "removal should use the registered instance");
        }

        @Test
        void unregisteringSomeoneWhoWasNeverRegisteredIsHarmless() {
            service.unregisterPlayer(player());

            assertTrue(service.getOnlinePlayers().isEmpty());
            assertEquals(0, created.size());
        }

        @Test
        void tracksPlayersIndependently() {
            Player first = player();
            Player second = player();
            service.registerPlayer(first);
            service.registerPlayer(second);

            service.unregisterPlayer(first);

            assertNull(service.getPlayer(first.getUniqueId()));
            assertEquals(1, service.getOnlinePlayers().size());
            assertSame(service.getPlayer(second.getUniqueId()), service.getOnlinePlayers().iterator().next());
        }
    }

    @Nested
    class LookupTests {

        @Test
        void returnsNullForAnUnknownUniqueId() {
            assertNull(service.getPlayer(UUID.randomUUID()));
        }

        @Test
        void rejectsSomethingThatIsNotAPlayer() {
            assertThrows(IllegalArgumentException.class, () -> service.getPlayer(new Object()));
        }
    }
}

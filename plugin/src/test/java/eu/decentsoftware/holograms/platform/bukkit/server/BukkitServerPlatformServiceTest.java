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
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformProbe;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformType;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class BukkitServerPlatformServiceTest {

    private Server server;
    private MockedStatic<Bukkit> bukkitMock;

    @BeforeEach
    void setUp() {
        server = mock(Server.class);
        bukkitMock = mockStatic(Bukkit.class);
        bukkitMock.when(Bukkit::getServer).thenReturn(server);
    }

    @AfterEach
    void tearDown() {
        bukkitMock.close();
    }

    private static ServerPlatformDetector detectorReturning(ServerPlatformType type) {
        return new ServerPlatformDetector(Collections.singletonList(new ServerPlatformProbe() {
            @NotNull
            @Override
            public ServerPlatformType getType() {
                return type;
            }

            @Override
            public boolean matches() {
                return true;
            }
        }));
    }

    private static ServerPlatformDetector detectorMatchingNothing() {
        return new ServerPlatformDetector(Collections.emptyList());
    }

    private BukkitServerPlatformService service(ServerPlatformType platformType) {
        return new BukkitServerPlatformService(detectorReturning(platformType));
    }

    @Nested
    class VersionStringTests {

        @ParameterizedTest(name = "getBukkitVersion()={0} -> {1}")
        @CsvSource({
                "1.8.8-R0.1-SNAPSHOT,   1.8.8",
                "1.16.5-R0.1-SNAPSHOT,  1.16.5",
                "1.20.6-R0.1-SNAPSHOT,  1.20.6",
                "1.21.11-R0.1-SNAPSHOT, 1.21.11",
                "26.1.2.build.52-beta,  26.1.2",
                "26.2.build.7,          26.2",
        })
        void readsTheVersionFromGetBukkitVersion(String reported, String expected) {
            when(server.getBukkitVersion()).thenReturn(reported);

            ServerPlatform platform = service(ServerPlatformType.PAPER).detect().orElse(null);

            assertNotNull(platform);
            assertEquals(MinecraftVersion.parse(expected), platform.getMinecraftVersion());
        }

        @Test
        void fallsBackToGetVersionWhenBukkitVersionIsUnreadable() {
            when(server.getBukkitVersion()).thenReturn("unknown");
            when(server.getVersion()).thenReturn("1.21.4-R0.1-SNAPSHOT");

            ServerPlatform platform = service(ServerPlatformType.PAPER).detect().orElse(null);

            assertNotNull(platform);
            assertEquals(MinecraftVersion.of(1, 21, 4), platform.getMinecraftVersion());
        }

        @Test
        void returnsEmptyWhenNeitherVersionIsReadable() {
            when(server.getBukkitVersion()).thenReturn("unknown");
            when(server.getVersion()).thenReturn("also-unknown");

            assertFalse(service(ServerPlatformType.PAPER).detect().isPresent());
        }

        @Test
        void doesNotConsultGetVersionWhenBukkitVersionParses() {
            when(server.getBukkitVersion()).thenReturn("1.21.4-R0.1-SNAPSHOT");

            assertEquals(Optional.of(MinecraftVersion.of(1, 21, 4)),
                    service(ServerPlatformType.PAPER).detect().map(ServerPlatform::getMinecraftVersion));
        }
    }

    @Nested
    class PlatformTests {

        @Test
        void pairsTheDetectedPlatformWithTheVersion() {
            when(server.getBukkitVersion()).thenReturn("1.21.9-R0.1-SNAPSHOT");

            ServerPlatform platform = service(ServerPlatformType.FOLIA).detect().orElse(null);

            assertNotNull(platform);
            assertEquals(ServerPlatformType.FOLIA, platform.getType());
            assertEquals(MinecraftVersion.of(1, 21, 9), platform.getMinecraftVersion());
        }

        @Test
        void returnsEmptyWhenNoProbeMatches() {
            when(server.getBukkitVersion()).thenReturn("1.21.4-R0.1-SNAPSHOT");

            assertFalse(new BukkitServerPlatformService(detectorMatchingNothing()).detect().isPresent());
        }
    }
}

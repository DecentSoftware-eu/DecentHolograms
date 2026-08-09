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
import eu.decentsoftware.holograms.nms.NmsAdapterFactory;
import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.platform.api.server.MinecraftVersion;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatform;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformType;
import eu.decentsoftware.holograms.platform.bukkit.server.BukkitServerPlatformService;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BukkitPlatformFactoryTest {

    @Mock
    private JavaPlugin plugin;
    @Mock
    private BukkitServerPlatformService platformService;
    @Mock
    private NmsAdapterFactory nmsAdapterFactory;
    @Mock
    private NmsAdapter nmsAdapter;
    @Mock
    private Server server;
    @InjectMocks
    private BukkitPlatformFactory factory;
    private MockedStatic<Bukkit> bukkitMock;

    @BeforeEach
    void setUp() {
        bukkitMock = mockStatic(Bukkit.class);
        bukkitMock.when(Bukkit::getServer).thenReturn(server);
    }

    @AfterEach
    void tearDown() {
        bukkitMock.close();
    }

    private void detects(ServerPlatformType type, String minecraftVersion) {
        when(platformService.detect())
                .thenReturn(Optional.of(new ServerPlatform(type, MinecraftVersion.parse(minecraftVersion))));
    }

    @Nested
    class SuccessTests {

        @BeforeEach
        void detectPaper() {
            detects(ServerPlatformType.PAPER, "1.21.9");
            when(nmsAdapterFactory.createNmsAdapter(anyString())).thenReturn(nmsAdapter);
        }

        @Test
        void loadsTheModuleMatchingTheDetectedPlatform() {
            factory.create(plugin);

            verify(nmsAdapterFactory).createNmsAdapter(Version.paper_v1_21_R6.name());
        }

        @Test
        void returnsEverythingTheCoreNeeds() {
            BukkitPlatformBootstrap bootstrap = factory.create(plugin);

            assertEquals(ServerPlatformType.PAPER, bootstrap.getServerPlatform().getType());
            assertEquals(MinecraftVersion.of(1, 21, 9), bootstrap.getServerPlatform().getMinecraftVersion());
            assertSame(nmsAdapter, bootstrap.getNmsAdapter());
            assertNotNull(bootstrap.getPlatformAdapter());
        }

        @Test
        void recordsTheResolvedModuleForTheRemainingVersionChecks() {
            factory.create(plugin);

            assertTrue(Version.is(Version.paper_v1_21_R6));
        }
    }

    @Nested
    class FailureTests {

        @Test
        void rejectsAnUnidentifiableServer() {
            when(platformService.detect()).thenReturn(Optional.empty());

            UnsupportedServerException exception =
                    assertThrows(UnsupportedServerException.class, () -> factory.create(plugin));

            assertTrue(exception.getMessage().contains("Could not identify this server"));
            verify(nmsAdapterFactory, never()).createNmsAdapter(anyString());
        }

        @Test
        void rejectsAnUnsupportedVersion() {
            detects(ServerPlatformType.SPIGOT, "1.7.10");

            UnsupportedServerException exception =
                    assertThrows(UnsupportedServerException.class, () -> factory.create(plugin));

            assertTrue(exception.getMessage().contains("Unsupported server version"));
            verify(nmsAdapterFactory, never()).createNmsAdapter(anyString());
        }

        @Test
        void wrapsNmsLoadFailuresAndKeepsTheCause() {
            detects(ServerPlatformType.SPIGOT, "1.20.6");
            DecentHologramsNmsException cause = new DecentHologramsNmsException("module missing");
            when(nmsAdapterFactory.createNmsAdapter(anyString())).thenThrow(cause);

            UnsupportedServerException exception =
                    assertThrows(UnsupportedServerException.class, () -> factory.create(plugin));

            assertTrue(exception.getMessage().contains(Version.v1_20_R4.name()));
            assertSame(cause, exception.getCause());
        }

        @Test
        void wrapsUnexpectedNmsFailuresToo() {
            detects(ServerPlatformType.SPIGOT, "1.20.6");
            RuntimeException cause = new IllegalStateException("boom");
            when(nmsAdapterFactory.createNmsAdapter(anyString())).thenThrow(cause);

            UnsupportedServerException exception =
                    assertThrows(UnsupportedServerException.class, () -> factory.create(plugin));

            assertSame(cause, exception.getCause());
        }
    }

    @Nested
    class ValidationTests {

        @Test
        void rejectsNullArguments() {
            assertThrows(NullPointerException.class, () -> factory.create(null));
            assertThrows(NullPointerException.class, () -> new BukkitPlatformFactory(null, nmsAdapterFactory));
            assertThrows(NullPointerException.class, () -> new BukkitPlatformFactory(platformService, null));
        }
    }
}

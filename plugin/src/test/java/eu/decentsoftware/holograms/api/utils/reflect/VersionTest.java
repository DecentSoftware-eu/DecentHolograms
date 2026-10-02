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

package eu.decentsoftware.holograms.api.utils.reflect;

import eu.decentsoftware.holograms.platform.api.server.MinecraftVersion;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatform;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionTest {

    private static Optional<Version> resolve(String minecraftVersion, ServerPlatformType type) {
        return Version.resolve(MinecraftVersion.parse(minecraftVersion), type);
    }

    @Nested
    class PlatformSpecificModuleTests {

        @Test
        void spigotGetsTheNonPaperModule() {
            assertEquals(Optional.of(Version.v1_21_R6), resolve("1.21.9", ServerPlatformType.SPIGOT));
            assertEquals(Optional.of(Version.v1_21_R7), resolve("1.21.11", ServerPlatformType.SPIGOT));
        }

        @Test
        void paperGetsThePaperMappedModule() {
            // Paper matches the family-wide entry too. Picking that one would load Spigot mappings
            // on a Mojang-mapped server, so the most specific platform has to win.
            assertEquals(Optional.of(Version.paper_v1_21_R6), resolve("1.21.9", ServerPlatformType.PAPER));
            assertEquals(Optional.of(Version.paper_v1_21_R7), resolve("1.21.11", ServerPlatformType.PAPER));
        }

        @Test
        void foliaInheritsThePaperMappedModule() {
            // Folia has no entries of its own; it should fall through to Paper, not to the
            // family-wide entry.
            assertEquals(Optional.of(Version.paper_v1_21_R6), resolve("1.21.9", ServerPlatformType.FOLIA));
            assertEquals(Optional.of(Version.paper_v1_21_R7), resolve("1.21.11", ServerPlatformType.FOLIA));
        }

        @Test
        void plainBukkitFallsBackToTheNonPaperModule() {
            // CraftBukkit shares Spigot's mappings, so the non-Paper module covers it. Declaring
            // that module SPIGOT rather than BUKKIT would leave a bare CraftBukkit server with no
            // match at all and report it as unsupported.
            assertEquals(Optional.of(Version.v1_21_R6), resolve("1.21.9", ServerPlatformType.BUKKIT));
            assertEquals(Optional.of(Version.v1_21_R7), resolve("1.21.11", ServerPlatformType.BUKKIT));
        }
    }

    @Nested
    class SharedModuleTests {

        @Test
        void everyPlatformGetsTheSharedModule() {
            for (ServerPlatformType type : ServerPlatformType.values()) {
                assertEquals(Optional.of(Version.v1_20_R4), resolve("1.20.6", type),
                        type + " should use the shared module for 1.20.6");
            }
        }

        @Test
        void matchesEveryDeclaredVersionOfAModule() {
            assertEquals(Optional.of(Version.v1_8_R3), resolve("1.8.4", ServerPlatformType.SPIGOT));
            assertEquals(Optional.of(Version.v1_8_R3), resolve("1.8.8", ServerPlatformType.SPIGOT));
            assertEquals(Optional.of(Version.v1_20_R4), resolve("1.20.5", ServerPlatformType.SPIGOT));
        }

        @Test
        void handlesTheNewVersioningScheme() {
            assertEquals(Optional.of(Version.v26_1), resolve("26.1", ServerPlatformType.PAPER));
            assertEquals(Optional.of(Version.v26_1), resolve("26.1.2", ServerPlatformType.PAPER));
            assertEquals(Optional.of(Version.v26_2), resolve("26.2", ServerPlatformType.SPIGOT));
        }

        @Test
        void treatsAMissingPatchAsZero() {
            // The table lists "1.8"; a server reporting "1.8" must match it.
            assertEquals(Optional.of(Version.v1_8_R1), resolve("1.8", ServerPlatformType.SPIGOT));
        }
    }

    @Nested
    class UnsupportedVersionTests {

        @Test
        void returnsEmptyForUnknownVersions() {
            assertFalse(resolve("1.7.10", ServerPlatformType.SPIGOT).isPresent());
            assertFalse(resolve("1.21.99", ServerPlatformType.PAPER).isPresent());
            assertFalse(resolve("99.9", ServerPlatformType.BUKKIT).isPresent());
        }

        @Test
        void resolutionIsAnAllowlistRatherThanARange() {
            // Sitting between two declared versions is not enough — a version resolves only if the
            // table lists it explicitly. Says nothing about whether these versions ought to be in
            // the table; if one is added, this test should be updated to use another gap.
            assertFalse(resolve("1.8.2", ServerPlatformType.SPIGOT).isPresent());
            assertFalse(resolve("1.10.1", ServerPlatformType.SPIGOT).isPresent());
        }
    }

    @Nested
    class ResolveFromServerPlatformTests {

        @Test
        void resolvesFromADetectedPlatform() {
            ServerPlatform platform = new ServerPlatform(ServerPlatformType.PAPER, MinecraftVersion.parse("1.21.9"));

            assertEquals(Optional.of(Version.paper_v1_21_R6), Version.resolve(platform));
        }

        @Test
        void rejectsNullArguments() {
            assertThrows(NullPointerException.class, () -> Version.resolve(null));
            assertThrows(NullPointerException.class, () -> Version.resolve(null, ServerPlatformType.PAPER));
            assertThrows(NullPointerException.class,
                    () -> Version.resolve(MinecraftVersion.of(1, 21), null));
        }
    }

    @Nested
    class TableIntegrityTests {

        @Test
        void everyModuleDeclaresAtLeastOneVersion() {
            for (Version version : Version.values()) {
                assertFalse(version.getMinecraftVersions().isEmpty(),
                        version.name() + " declares no Minecraft versions and can never be resolved");
            }
        }

        @Test
        void everyModuleIsReachable() {
            // A module only ever loads if resolving one of its own versions on its own platform
            // returns it. A duplicate entry at the same specificity would silently shadow another.
            for (Version version : Version.values()) {
                for (MinecraftVersion minecraftVersion : version.getMinecraftVersions()) {
                    assertEquals(Optional.of(version), Version.resolve(minecraftVersion, version.getPlatform()),
                            version.name() + " is shadowed for " + minecraftVersion);
                }
            }
        }

        @Test
        void versionStringsParseBackToThemselves() {
            for (Version version : Version.values()) {
                for (MinecraftVersion minecraftVersion : version.getMinecraftVersions()) {
                    assertTrue(version.supports(minecraftVersion),
                            version.name() + " does not support its own declared version " + minecraftVersion);
                }
            }
        }
    }
}

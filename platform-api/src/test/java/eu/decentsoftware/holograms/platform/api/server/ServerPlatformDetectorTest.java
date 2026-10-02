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

package eu.decentsoftware.holograms.platform.api.server;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerPlatformDetectorTest {

    private static ServerPlatformProbe probe(ServerPlatformType type, boolean matches) {
        return new ServerPlatformProbe() {
            @NotNull
            @Override
            public ServerPlatformType getType() {
                return type;
            }

            @Override
            public boolean matches() {
                return matches;
            }
        };
    }

    private static final class CountingProbe implements ServerPlatformProbe {

        private final ServerPlatformType type;
        private final boolean matches;
        private int calls;

        private CountingProbe(ServerPlatformType type, boolean matches) {
            this.type = type;
            this.matches = matches;
        }

        @NotNull
        @Override
        public ServerPlatformType getType() {
            return type;
        }

        @Override
        public boolean matches() {
            calls++;
            return matches;
        }
    }

    @Nested
    class DetectionTests {

        @Test
        void picksTheMostSpecificMatch() {
            // A Folia server matches everything it is derived from.
            ServerPlatformDetector detector = new ServerPlatformDetector(Arrays.asList(
                    probe(ServerPlatformType.BUKKIT, true),
                    probe(ServerPlatformType.SPIGOT, true),
                    probe(ServerPlatformType.PAPER, true),
                    probe(ServerPlatformType.FOLIA, true)
            ));

            assertEquals(Optional.of(ServerPlatformType.FOLIA), detector.detectType());
        }

        @Test
        void isIndependentOfProbeOrder() {
            List<ServerPlatformProbe> probes = Arrays.asList(
                    probe(ServerPlatformType.FOLIA, false),
                    probe(ServerPlatformType.PAPER, true),
                    probe(ServerPlatformType.BUKKIT, true),
                    probe(ServerPlatformType.SPIGOT, true)
            );
            List<ServerPlatformProbe> reversed = new java.util.ArrayList<>(probes);
            Collections.reverse(reversed);

            assertEquals(new ServerPlatformDetector(probes).detectType(),
                    new ServerPlatformDetector(reversed).detectType());
            assertEquals(Optional.of(ServerPlatformType.PAPER), new ServerPlatformDetector(probes).detectType());
        }

        @Test
        void ignoresProbesThatDoNotMatch() {
            ServerPlatformDetector detector = new ServerPlatformDetector(Arrays.asList(
                    probe(ServerPlatformType.BUKKIT, true),
                    probe(ServerPlatformType.SPIGOT, false),
                    probe(ServerPlatformType.PAPER, false),
                    probe(ServerPlatformType.FOLIA, false)
            ));

            assertEquals(Optional.of(ServerPlatformType.BUKKIT), detector.detectType());
        }

        @Test
        void returnsEmptyWhenNothingMatches() {
            ServerPlatformDetector detector = new ServerPlatformDetector(Arrays.asList(
                    probe(ServerPlatformType.BUKKIT, false),
                    probe(ServerPlatformType.PAPER, false)
            ));

            assertFalse(detector.detectType().isPresent());
            assertFalse(detector.detect(MinecraftVersion.of(1, 21, 4)).isPresent());
        }

        @Test
        void returnsEmptyWithoutProbes() {
            assertFalse(new ServerPlatformDetector(Collections.emptyList()).detectType().isPresent());
        }

        @Test
        void pairsThePlatformWithTheVersion() {
            ServerPlatformDetector detector = new ServerPlatformDetector(Collections.singletonList(
                    probe(ServerPlatformType.PAPER, true)
            ));

            ServerPlatform platform = detector.detect(MinecraftVersion.parse("1.21.4")).orElse(null);

            assertNotNull(platform);
            assertEquals(ServerPlatformType.PAPER, platform.getType());
            assertEquals(MinecraftVersion.of(1, 21, 4), platform.getMinecraftVersion());
            assertTrue(platform.isA(ServerPlatformType.SPIGOT));
            assertFalse(platform.isA(ServerPlatformType.FOLIA));
        }

        @Test
        void stopsAtTheFirstMatch() {
            CountingProbe folia = new CountingProbe(ServerPlatformType.FOLIA, true);
            CountingProbe paper = new CountingProbe(ServerPlatformType.PAPER, true);
            CountingProbe bukkit = new CountingProbe(ServerPlatformType.BUKKIT, true);

            ServerPlatformDetector detector = new ServerPlatformDetector(Arrays.asList(bukkit, paper, folia));

            assertEquals(Optional.of(ServerPlatformType.FOLIA), detector.detectType());
            assertEquals(1, folia.calls);
            assertEquals(0, paper.calls, "Probes below the first match must not be consulted");
            assertEquals(0, bukkit.calls, "Probes below the first match must not be consulted");
        }

        @Test
        void consultsLessSpecificProbesUntilOneMatches() {
            CountingProbe folia = new CountingProbe(ServerPlatformType.FOLIA, false);
            CountingProbe paper = new CountingProbe(ServerPlatformType.PAPER, false);
            CountingProbe spigot = new CountingProbe(ServerPlatformType.SPIGOT, true);
            CountingProbe bukkit = new CountingProbe(ServerPlatformType.BUKKIT, true);

            ServerPlatformDetector detector = new ServerPlatformDetector(Arrays.asList(bukkit, spigot, paper, folia));

            assertEquals(Optional.of(ServerPlatformType.SPIGOT), detector.detectType());
            assertEquals(1, folia.calls);
            assertEquals(1, paper.calls);
            assertEquals(1, spigot.calls);
            assertEquals(0, bukkit.calls);
        }

        @Test
        void tieBreakSurvivesEnumReordering() {
            // Equal-depth siblings both matching is ambiguous; all that is promised is that the
            // answer is stable. Ordering by name means it does not depend on ordinal().
            ServerPlatformType first = ServerPlatformType.PAPER;
            ServerPlatformType second = ServerPlatformType.FOLIA;
            assertEquals(first.getSpecificity() + 1, second.getSpecificity(),
                    "Test assumes FOLIA sits directly below PAPER");

            ServerPlatformDetector forwards = new ServerPlatformDetector(Arrays.asList(
                    probe(first, true), probe(second, true)));
            ServerPlatformDetector backwards = new ServerPlatformDetector(Arrays.asList(
                    probe(second, true), probe(first, true)));

            assertEquals(forwards.detectType(), backwards.detectType());
        }

        @Test
        void rejectsNullArguments() {
            assertThrows(NullPointerException.class, () -> new ServerPlatformDetector(null));
            assertThrows(NullPointerException.class, () -> new ServerPlatformDetector(Collections.singletonList(null)));
            assertThrows(NullPointerException.class, () -> new ServerPlatformDetector(Collections.emptyList()).detect(null));
        }
    }
}

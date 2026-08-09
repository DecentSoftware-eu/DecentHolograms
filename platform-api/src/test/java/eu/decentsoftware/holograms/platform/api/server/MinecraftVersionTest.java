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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinecraftVersionTest {

    @Nested
    class ParsingTests {

        @ParameterizedTest(name = "{0} -> {1}.{2}.{3}")
        @CsvSource({
                "1.8,                   1,  8,  0",
                "1.8.8,                 1,  8,  8",
                "1.21.11,               1,  21, 11",
                "26.1,                  26, 1,  0",
                "26.2,                  26, 2,  0",
                "26.1.2,                26, 1,  2",
                // Shapes servers actually report
                "1.20.6-R0.1-SNAPSHOT,  1,  20, 6",
                "1.21.4-R0.1-SNAPSHOT,  1,  21, 4",
                "26.1.2.build.52,       26, 1,  2",
                "26.1.2.build.52-beta,  26, 1,  2",
                "1.16.5-R0.1,           1,  16, 5",
        })
        void parsesServerReportedStrings(String input, int major, int minor, int patch) {
            assertEquals(MinecraftVersion.of(major, minor, patch), MinecraftVersion.parse(input));
        }

        @ParameterizedTest
        @NullAndEmptySource
        void rejectsMissingInput(String input) {
            assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.parse(input));
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "abc", "1.x", "-1.2", "x.1.2"})
        void rejectsStringsWithoutMajorAndMinor(String input) {
            assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.parse(input));
        }

        @Test
        void rejectsNegativeComponents() {
            assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.of(-1, 0, 0));
            assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.of(1, -1, 0));
            assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.of(1, 0, -1));
        }
    }

    @Nested
    class ComparisonTests {

        @Test
        void ordersWithinTheLegacyScheme() {
            assertTrue(MinecraftVersion.parse("1.21.4").isAtLeast(MinecraftVersion.parse("1.21.4")));
            assertTrue(MinecraftVersion.parse("1.21.4").isAtLeast(MinecraftVersion.parse("1.21.3")));
            assertTrue(MinecraftVersion.parse("1.21.0").isAtLeast(MinecraftVersion.parse("1.20.6")));
            assertFalse(MinecraftVersion.parse("1.8.8").isAtLeast(MinecraftVersion.parse("1.13")));
        }

        @Test
        void ordersAcrossTheSchemeChange() {
            // Every 1.x release predates every 26.x release
            assertTrue(MinecraftVersion.parse("26.1").isAtLeast(MinecraftVersion.parse("1.21.11")));
            assertTrue(MinecraftVersion.parse("26.2").isAtLeast(MinecraftVersion.parse("26.1.2")));
            assertFalse(MinecraftVersion.parse("1.21.11").isAtLeast(MinecraftVersion.parse("26.1")));
            assertTrue(MinecraftVersion.parse("1.21.11").isBefore(MinecraftVersion.parse("26.1")));
        }

        @Test
        void treatsMissingPatchAsZero() {
            assertEquals(MinecraftVersion.of(1, 8, 0), MinecraftVersion.parse("1.8"));
            assertTrue(MinecraftVersion.parse("1.8.1").isAtLeast(MinecraftVersion.parse("1.8")));
            assertFalse(MinecraftVersion.parse("1.8").isAtLeast(MinecraftVersion.parse("1.8.1")));
        }

        @Test
        void comparesAgainstLooseComponents() {
            MinecraftVersion version = MinecraftVersion.parse("1.21.4");

            assertTrue(version.isAtLeast(1, 13));
            assertTrue(version.isAtLeast(1, 21, 4));
            assertFalse(version.isAtLeast(1, 21, 5));
            assertFalse(version.isAtLeast(26, 1));
            assertTrue(version.isBefore(26, 1));
        }
    }

    @Nested
    class ValueSemanticsTests {

        @Test
        void equalityIgnoresHowTheVersionWasBuilt() {
            assertEquals(MinecraftVersion.parse("1.20.6-R0.1-SNAPSHOT"), MinecraftVersion.of(1, 20, 6));
            assertEquals(MinecraftVersion.parse("1.20.6").hashCode(), MinecraftVersion.of(1, 20, 6).hashCode());
            assertNotEquals(MinecraftVersion.of(1, 20, 6), MinecraftVersion.of(1, 20, 5));
        }

        @Test
        void printsBackTheVersionShape() {
            assertEquals("1.8", MinecraftVersion.parse("1.8").toString());
            assertEquals("1.21.4", MinecraftVersion.parse("1.21.4").toString());
            assertEquals("26.1", MinecraftVersion.parse("26.1").toString());
            assertEquals("26.1.2", MinecraftVersion.parse("26.1.2").toString());
        }
    }
}

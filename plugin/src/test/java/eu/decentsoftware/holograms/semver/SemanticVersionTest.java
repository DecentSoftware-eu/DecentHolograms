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

package eu.decentsoftware.holograms.semver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SemanticVersionTest {

    private static Object[][] provideInvalidVersionStrings() {
        return new Object[][]{
                {null, "Version string cannot be null or empty"},
                {"", "Version string cannot be null or empty"},
                {"1.2", "Version string must contain at least major, minor, and patch components"},
                {"1.2.3.4", "Version string must contain at most three components: major, minor, and patch"},
                {"a.0.3", "Invalid version format: a.0.3"},
                {"1.a.3", "Invalid version format: 1.a.3"},
                {"1.0.a", "Invalid version format: 1.0.a"},
                {"-1.0.0", "Major version must be a non-negative integer"},
                {"1.-1.0", "Minor version must be a non-negative integer"},
                {"1.0.-1", "Patch version must be a non-negative integer"},
                {"1.0.0-SNAPSHOT-EXTRA", "Invalid version format: 1.0.0-SNAPSHOT-EXTRA"},
                {"1.0.0-DEV", "Invalid version format: 1.0.0-DEV"},
                {"1.0.0-", "Invalid version format: 1.0.0-"},
        };
    }

    @ParameterizedTest
    @MethodSource("provideInvalidVersionStrings")
    void testFromString_invalidVersionStrings(String versionString, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> SemanticVersion.fromString(versionString));

        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Object[][] provideValidVersionStrings() {
        return new Object[][]{
                {"1.0.0", 1, 0, 0, false},
                {"1.2.3", 1, 2, 3, false},
                {"10.20.30", 10, 20, 30, false},
                {"0.0.0", 0, 0, 0, false},
                {"1.0.0-SNAPSHOT", 1, 0, 0, true},
                {"2.3.4-SNAPSHOT", 2, 3, 4, true}
        };
    }

    @ParameterizedTest
    @MethodSource("provideValidVersionStrings")
    void testFromString_validVersionStrings(String versionString, int major, int minor, int patch, boolean isSnapshot) {
        SemanticVersion version = SemanticVersion.fromString(versionString);

        assertEquals(major, version.getMajor());
        assertEquals(minor, version.getMinor());
        assertEquals(patch, version.getPatch());
        assertEquals(isSnapshot, version.isSnapshot());
    }

    @ParameterizedTest
    @MethodSource("provideValidVersionStrings")
    void testToString(String versionString, int major, int minor, int patch, boolean isSnapshot) {
        SemanticVersion version = SemanticVersion.fromString(versionString);
        String expectedString = major + "." + minor + "." + patch + (isSnapshot ? "-SNAPSHOT" : "");

        assertEquals(expectedString, version.toString());
    }

    private static Object[][] provideComparisonVersions() {
        return new Object[][]{
                {"1.0.0", "1.0.0", 0},
                {"1.0.0-SNAPSHOT", "1.0.0-SNAPSHOT", 0},
                {"1.0.0", "1.0.1", -1},
                {"1.0.1", "1.0.0", 1},
                {"1.2.3", "1.2.3-SNAPSHOT", 1},
                {"1.2.3-SNAPSHOT", "1.2.3", -1},
                {"2.0.0", "1.9.9", 1},
                {"1.9.9", "2.0.0", -1},
                {"1.1.1", "1.0.1", 1},
                {"1.0.1", "1.1.1", -1},
        };
    }

    @ParameterizedTest
    @MethodSource("provideComparisonVersions")
    void testCompareTo(String versionString1, String versionString2, int expectedComparison) {
        SemanticVersion version1 = SemanticVersion.fromString(versionString1);
        SemanticVersion version2 = SemanticVersion.fromString(versionString2);

        assertEquals(expectedComparison, version1.compareTo(version2));
    }

    @ParameterizedTest
    @MethodSource("provideComparisonVersions")
    void testEqualsAndHashCode(String versionString1, String versionString2, int expectedComparison) {
        SemanticVersion version1 = SemanticVersion.fromString(versionString1);
        SemanticVersion version2 = SemanticVersion.fromString(versionString2);

        if (expectedComparison == 0) {
            assertEquals(version1, version2);
            assertEquals(version1.hashCode(), version2.hashCode());
        } else {
            assertNotEquals(version1, version2);
            assertNotEquals(version1.hashCode(), version2.hashCode());
        }
    }

    @Test
    void testEquals_invalidInput() {
        SemanticVersion version = SemanticVersion.fromString("1.0.0");

        assertNotEquals(version, null);
        assertNotEquals(version, new Object());
    }
}
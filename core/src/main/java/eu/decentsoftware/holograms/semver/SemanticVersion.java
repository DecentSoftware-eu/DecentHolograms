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

import org.jetbrains.annotations.NotNull;

/**
 * Represents a semantic version following the format major.minor.patch[-SNAPSHOT].
 * This class provides methods to parse, compare, and represent semantic versions.
 *
 * @author d0by
 * @since 2.9.6
 */
public class SemanticVersion implements Comparable<SemanticVersion> {

    private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
    private final int major;
    private final int minor;
    private final int patch;
    private final boolean snapshot;

    private SemanticVersion(int major, int minor, int patch, boolean snapshot) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.snapshot = snapshot;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public boolean isSnapshot() {
        return snapshot;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch + (snapshot ? SNAPSHOT_SUFFIX : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SemanticVersion)) {
            return false;
        }
        SemanticVersion that = (SemanticVersion) obj;
        return major == that.major && minor == that.minor && patch == that.patch && snapshot == that.snapshot;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        result = 31 * result + (snapshot ? 1 : 0);
        return result;
    }

    @Override
    public int compareTo(@NotNull SemanticVersion other) {
        if (this.major != other.major) {
            return Integer.compare(this.major, other.major);
        }
        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor);
        }
        if (this.patch != other.patch) {
            return Integer.compare(this.patch, other.patch);
        }
        // Snapshot versions are considered "less than" non-snapshot versions
        return Boolean.compare(other.snapshot, this.snapshot);
    }

    /**
     * Parses a semantic version from a string.
     * The string must be in the format "major.minor.patch" or "major.minor.patch-SNAPSHOT".
     *
     * @param versionString the version string to parse
     * @return a SemanticVersion object representing the parsed version
     * @throws IllegalArgumentException if the version string is invalid
     */
    @NotNull
    public static SemanticVersion fromString(String versionString) {
        if (versionString == null || versionString.isEmpty()) {
            throw new IllegalArgumentException("Version string cannot be null or empty");
        }

        String[] numberParts = versionString.split("\\.");
        if (numberParts.length < 3) {
            throw new IllegalArgumentException("Version string must contain at least major, minor, and patch components");
        }
        if (numberParts.length > 3) {
            throw new IllegalArgumentException("Version string must contain at most three components: major, minor, and patch");
        }

        try {
            int majorVersion = parseVersionNumber(numberParts[0], "Major version");
            int minorVersion = parseVersionNumber(numberParts[1], "Minor version");
            int patchVersion = parseVersionNumber(numberParts[2].replace(SNAPSHOT_SUFFIX, ""), "Patch version");
            boolean isSnapshot = numberParts[2].endsWith(SNAPSHOT_SUFFIX);

            return new SemanticVersion(majorVersion, minorVersion, patchVersion, isSnapshot);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid version format: " + versionString, e);
        }
    }

    private static int parseVersionNumber(String numberString, String versionPart) {
        int majorVersion = Integer.parseInt(numberString);
        if (majorVersion < 0) {
            throw new IllegalArgumentException(versionPart + " must be a non-negative integer");
        }
        return majorVersion;
    }
}

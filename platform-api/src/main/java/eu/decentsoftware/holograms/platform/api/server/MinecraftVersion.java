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

/**
 * A Minecraft version, comparable across both the {@code 1.x.y} and the newer {@code YY.x.y}
 * numbering schemes.
 *
 * <p>Both schemes compare correctly as plain numeric tuples, because the major component only
 * ever increased when the scheme changed - every {@code 1.x} version sorts before every
 * {@code 26.x} version.</p>
 *
 * @author d0by
 * @since 2.10.2
 */
public final class MinecraftVersion implements Comparable<MinecraftVersion> {

    private static final int MAX_COMPONENTS = 3;

    private final int major;
    private final int minor;
    private final int patch;

    private MinecraftVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * @param major The major component, e.g. 1 in 1.21.4.
     * @param minor The minor component, e.g. 21 in 1.21.4.
     * @return The version, with a patch component of zero.
     * @throws IllegalArgumentException If any component is negative.
     */
    @NotNull
    public static MinecraftVersion of(int major, int minor) {
        return of(major, minor, 0);
    }

    /**
     * @param major The major component, e.g. 1 in 1.21.4.
     * @param minor The minor component, e.g. 21 in 1.21.4.
     * @param patch The patch component, e.g. 4 in 1.21.4.
     * @return The version.
     * @throws IllegalArgumentException If any component is negative.
     */
    @NotNull
    public static MinecraftVersion of(int major, int minor, int patch) {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version components cannot be negative");
        }
        return new MinecraftVersion(major, minor, patch);
    }

    /**
     * Parses a version from a server-reported version string.
     *
     * <p>Anything from the first {@code -} onwards is discarded, then the leading numeric
     * components are read. This accepts the shapes servers actually report, such as
     * {@code 1.20.6-R0.1-SNAPSHOT}, {@code 26.1.2.build.52} and {@code 1.8}.</p>
     *
     * @param version The version string.
     * @return The parsed version. A missing patch component is treated as zero.
     * @throws IllegalArgumentException If the string has no major and minor component.
     */
    @NotNull
    public static MinecraftVersion parse(String version) {
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("Version string cannot be null or empty");
        }

        int suffixIndex = version.indexOf('-');
        String core = suffixIndex < 0 ? version : version.substring(0, suffixIndex);

        String[] parts = core.split("\\.");
        int[] components = new int[MAX_COMPONENTS];
        int found = 0;
        for (String part : parts) {
            if (found == MAX_COMPONENTS || !isNumeric(part)) {
                break;
            }
            try {
                components[found] = Integer.parseInt(part);
            } catch (NumberFormatException e) {
                // Numeric but too large to be a version component.
                break;
            }
            found++;
        }

        if (found < 2) {
            throw new IllegalArgumentException("Version string must contain at least a major and minor component: " + version);
        }
        return new MinecraftVersion(components[0], components[1], components[2]);
    }

    private static boolean isNumeric(String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
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

    /**
     * @param other The version to compare against.
     * @return True if this version is the same as, or later than, the given one.
     */
    public boolean isAtLeast(@NotNull MinecraftVersion other) {
        return compareTo(other) >= 0;
    }

    /**
     * @param major The major component to compare against.
     * @param minor The minor component to compare against.
     * @return True if this version is the same as, or later than, the given one.
     */
    public boolean isAtLeast(int major, int minor) {
        return isAtLeast(of(major, minor));
    }

    /**
     * @param major The major component to compare against.
     * @param minor The minor component to compare against.
     * @param patch The patch component to compare against.
     * @return True if this version is the same as, or later than, the given one.
     */
    public boolean isAtLeast(int major, int minor, int patch) {
        return isAtLeast(of(major, minor, patch));
    }

    /**
     * @param other The version to compare against.
     * @return True if this version is earlier than the given one.
     */
    public boolean isBefore(@NotNull MinecraftVersion other) {
        return compareTo(other) < 0;
    }

    /**
     * @param major The major component to compare against.
     * @param minor The minor component to compare against.
     * @return True if this version is earlier than the given one.
     */
    public boolean isBefore(int major, int minor) {
        return isBefore(of(major, minor));
    }

    @Override
    public int compareTo(@NotNull MinecraftVersion other) {
        if (major != other.major) {
            return Integer.compare(major, other.major);
        }
        if (minor != other.minor) {
            return Integer.compare(minor, other.minor);
        }
        return Integer.compare(patch, other.patch);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MinecraftVersion)) {
            return false;
        }
        MinecraftVersion that = (MinecraftVersion) obj;
        return major == that.major && minor == that.minor && patch == that.patch;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        return result;
    }

    @Override
    public String toString() {
        return patch == 0 ? major + "." + minor : major + "." + minor + "." + patch;
    }
}

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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A kind of server software.
 *
 * <p>Server platforms form a lineage rather than a flat set: Folia is a Paper, Paper is a Spigot,
 * Spigot is a Bukkit. Use {@link #isA(ServerPlatformType)} for capability questions - a feature
 * available on Paper should light up on Folia too - and the type itself only for reporting or for
 * choosing an implementation.</p>
 *
 * @author d0by
 * @since 2.10.2
 */
public enum ServerPlatformType {
    /**
     * CraftBukkit, and the baseline every other Bukkit-family platform builds on.
     */
    BUKKIT,
    /**
     * Spigot.
     */
    SPIGOT,
    /**
     * Paper, and forks of it that are not identified separately.
     */
    PAPER,
    /**
     * Folia, Paper's region-threaded fork.
     */
    FOLIA;

    private ServerPlatformType parent;

    static {
        SPIGOT.parent = BUKKIT;
        PAPER.parent = SPIGOT;
        FOLIA.parent = PAPER;
    }

    /**
     * @return The platform this one is derived from, or null if it is a root.
     */
    @Nullable
    public ServerPlatformType getParent() {
        return parent;
    }

    /**
     * Checks whether this platform is, or is derived from, the given one.
     *
     * <p>For example {@code FOLIA.isA(PAPER)} is true, while {@code PAPER.isA(FOLIA)} is not.</p>
     *
     * @param type The platform to test against.
     * @return True if this platform is the given one or a descendant of it.
     * @throws NullPointerException If type is null.
     */
    public boolean isA(@NotNull ServerPlatformType type) {
        Objects.requireNonNull(type, "type cannot be null");
        for (ServerPlatformType current = this; current != null; current = current.parent) {
            if (current == type) {
                return true;
            }
        }
        return false;
    }

    /**
     * How specific this platform is, measured as its depth in the lineage.
     *
     * <p>Derived from {@link #getParent()} rather than declared, so it cannot fall out of step
     * with the lineage. Detection picks the highest value among matching platforms.</p>
     *
     * @return The number of platforms this one is derived from.
     */
    public int getSpecificity() {
        int depth = 0;
        for (ServerPlatformType current = parent; current != null; current = current.parent) {
            depth++;
        }
        return depth;
    }
}

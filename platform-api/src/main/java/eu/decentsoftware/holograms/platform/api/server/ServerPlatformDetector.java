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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Identifies the server software by asking the registered {@link ServerPlatformProbe}s.
 *
 * <p>Probes are sorted most-specific-first when the detector is built, and detection returns the
 * first match. A platform is always deeper in the lineage than what it derives from, so a
 * matching Folia probe is consulted before Paper, Paper before Spigot, and so on. The order the
 * probes are supplied in therefore does not matter.</p>
 *
 * <p>Probes after the first match are never consulted, so they must be free of side effects.</p>
 *
 * @author d0by
 * @since 2.10.2
 */
public final class ServerPlatformDetector {

    /**
     * Most specific first, then by name.
     *
     * <p>The name is only there to keep the order stable. Two platforms at the same depth both
     * matching is an ambiguity no rule can resolve correctly, so the goal is a reproducible
     * answer rather than a meaningful one. Ordering by name rather than by declaration order
     * means reordering or inserting enum constants cannot silently change detection.</p>
     */
    private static final Comparator<ServerPlatformProbe> MOST_SPECIFIC_FIRST =
            Comparator.comparingInt((ServerPlatformProbe probe) -> probe.getType().getSpecificity())
                    .reversed()
                    .thenComparing(probe -> probe.getType().name());

    private final List<ServerPlatformProbe> probes;

    /**
     * Creates a new detector that consults the given probes.
     *
     * @param probes The probes to consult, in any order.
     * @throws NullPointerException If probes is null or contains null.
     */
    public ServerPlatformDetector(@NotNull Collection<ServerPlatformProbe> probes) {
        Objects.requireNonNull(probes, "probes cannot be null");
        List<ServerPlatformProbe> sorted = new ArrayList<>(probes.size());
        for (ServerPlatformProbe probe : probes) {
            sorted.add(Objects.requireNonNull(probe, "probes cannot contain null"));
        }
        sorted.sort(MOST_SPECIFIC_FIRST);
        this.probes = Collections.unmodifiableList(sorted);
    }

    /**
     * Identifies the server software.
     *
     * @return The most specific matching platform, or empty if no probe matched.
     * @since 2.10.2
     */
    @NotNull
    public Optional<ServerPlatformType> detectType() {
        for (ServerPlatformProbe probe : probes) {
            if (probe.matches()) {
                return Optional.of(probe.getType());
            }
        }
        return Optional.empty();
    }

    /**
     * Identifies the server software and pairs it with the given Minecraft version.
     *
     * @param minecraftVersion The Minecraft version the server is running.
     * @return The detected platform, or empty if no probe matched.
     * @throws NullPointerException If minecraftVersion is null.
     * @since 2.10.2
     */
    @NotNull
    public Optional<ServerPlatform> detect(@NotNull MinecraftVersion minecraftVersion) {
        Objects.requireNonNull(minecraftVersion, "minecraftVersion cannot be null");
        return detectType().map(type -> new ServerPlatform(type, minecraftVersion));
    }
}

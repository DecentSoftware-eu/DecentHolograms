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

import eu.decentsoftware.holograms.platform.api.server.ServerPlatformProbe;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassPresenceProbeTest {

    private static final String PRESENT = "java.lang.String";
    private static final String ABSENT = "eu.decentsoftware.holograms.DoesNotExist";

    @Test
    void matchesWhenTheMarkerIsPresent() {
        assertTrue(new ClassPresenceProbe(ServerPlatformType.BUKKIT, PRESENT).matches());
    }

    @Test
    void doesNotMatchWhenTheMarkerIsAbsent() {
        assertFalse(new ClassPresenceProbe(ServerPlatformType.BUKKIT, ABSENT).matches());
    }

    @Test
    void matchesWhenAnyMarkerIsPresent() {
        assertTrue(new ClassPresenceProbe(ServerPlatformType.PAPER, ABSENT, PRESENT).matches());
        assertTrue(new ClassPresenceProbe(ServerPlatformType.PAPER, PRESENT, ABSENT).matches());
    }

    @Test
    void doesNotMatchWhenEveryMarkerIsAbsent() {
        assertFalse(new ClassPresenceProbe(ServerPlatformType.PAPER, ABSENT, ABSENT + "2").matches());
    }

    @Test
    void reportsItsType() {
        assertEquals(ServerPlatformType.FOLIA, new ClassPresenceProbe(ServerPlatformType.FOLIA, PRESENT).getType());
    }

    @Test
    void rejectsInvalidConstruction() {
        assertThrows(NullPointerException.class, () -> new ClassPresenceProbe(null, PRESENT));
        assertThrows(IllegalArgumentException.class, () -> new ClassPresenceProbe(ServerPlatformType.BUKKIT));
    }

    @Test
    void bukkitProbesCoverEveryPlatformType() {
        List<ServerPlatformProbe> probes = BukkitServerPlatformProbes.create();

        assertEquals(ServerPlatformType.values().length, probes.size());
        for (ServerPlatformType type : ServerPlatformType.values()) {
            assertTrue(probes.stream().anyMatch(probe -> probe.getType() == type),
                    "No probe registered for " + type);
        }
    }

    @Test
    void bukkitIsDetectedFromTheApiOnTheTestClasspath() {
        // The tests run with the Spigot API present but no server implementation, so the Bukkit
        // probe should match and the Paper and Folia probes should not.
        List<ServerPlatformProbe> probes = BukkitServerPlatformProbes.create();

        assertTrue(matches(probes, ServerPlatformType.BUKKIT));
        assertFalse(matches(probes, ServerPlatformType.PAPER));
        assertFalse(matches(probes, ServerPlatformType.FOLIA));
    }

    private static boolean matches(List<ServerPlatformProbe> probes, ServerPlatformType type) {
        return probes.stream().filter(probe -> probe.getType() == type).allMatch(ServerPlatformProbe::matches);
    }
}

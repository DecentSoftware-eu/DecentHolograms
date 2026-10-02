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

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerPlatformTypeTest {

    @Nested
    class LineageTests {

        @Test
        void eachPlatformDeclaresItsParent() {
            assertNull(ServerPlatformType.BUKKIT.getParent());
            assertEquals(ServerPlatformType.BUKKIT, ServerPlatformType.SPIGOT.getParent());
            assertEquals(ServerPlatformType.SPIGOT, ServerPlatformType.PAPER.getParent());
            assertEquals(ServerPlatformType.PAPER, ServerPlatformType.FOLIA.getParent());
        }

        @Test
        void bukkitIsTheOnlyRoot() {
            for (ServerPlatformType type : ServerPlatformType.values()) {
                if (type == ServerPlatformType.BUKKIT) {
                    continue;
                }
                assertTrue(type.isA(ServerPlatformType.BUKKIT),
                        type + " must descend from BUKKIT, or detection needs to handle several roots");
            }
        }

        @Test
        void noPlatformIsItsOwnAncestor() {
            for (ServerPlatformType type : ServerPlatformType.values()) {
                Set<ServerPlatformType> seen = EnumSet.noneOf(ServerPlatformType.class);
                for (ServerPlatformType current = type; current != null; current = current.getParent()) {
                    assertTrue(seen.add(current), "Cycle in the parent chain of " + type + " at " + current);
                }
            }
        }
    }

    @Nested
    class IsATests {

        @Test
        void everyPlatformIsItself() {
            for (ServerPlatformType type : ServerPlatformType.values()) {
                assertTrue(type.isA(type), type + " must be itself");
            }
        }

        @Test
        void matchesEveryAncestor() {
            assertTrue(ServerPlatformType.FOLIA.isA(ServerPlatformType.PAPER));
            assertTrue(ServerPlatformType.FOLIA.isA(ServerPlatformType.SPIGOT));
            assertTrue(ServerPlatformType.FOLIA.isA(ServerPlatformType.BUKKIT));
            assertTrue(ServerPlatformType.PAPER.isA(ServerPlatformType.SPIGOT));
            assertTrue(ServerPlatformType.SPIGOT.isA(ServerPlatformType.BUKKIT));
        }

        @Test
        void doesNotMatchDescendants() {
            assertFalse(ServerPlatformType.BUKKIT.isA(ServerPlatformType.SPIGOT));
            assertFalse(ServerPlatformType.SPIGOT.isA(ServerPlatformType.PAPER));
            assertFalse(ServerPlatformType.PAPER.isA(ServerPlatformType.FOLIA));
            assertFalse(ServerPlatformType.BUKKIT.isA(ServerPlatformType.FOLIA));
        }

        @Test
        void isTransitive() {
            for (ServerPlatformType a : ServerPlatformType.values()) {
                for (ServerPlatformType b : ServerPlatformType.values()) {
                    for (ServerPlatformType c : ServerPlatformType.values()) {
                        if (a.isA(b) && b.isA(c)) {
                            assertTrue(a.isA(c), a + " isA " + b + " isA " + c + ", so " + a + " must be a " + c);
                        }
                    }
                }
            }
        }

        @Test
        void isAntisymmetric() {
            for (ServerPlatformType a : ServerPlatformType.values()) {
                for (ServerPlatformType b : ServerPlatformType.values()) {
                    if (a != b && a.isA(b)) {
                        assertFalse(b.isA(a), a + " and " + b + " cannot each descend from the other");
                    }
                }
            }
        }

        @Test
        void rejectsNullType() {
            Exception exception = assertThrows(NullPointerException.class,
                    () -> ServerPlatformType.PAPER.isA(null));

            assertEquals("type cannot be null", exception.getMessage());
        }
    }

    @Nested
    class SpecificityTests {

        @Test
        void followsKnownDepths() {
            assertEquals(0, ServerPlatformType.BUKKIT.getSpecificity());
            assertEquals(1, ServerPlatformType.SPIGOT.getSpecificity());
            assertEquals(2, ServerPlatformType.PAPER.getSpecificity());
            assertEquals(3, ServerPlatformType.FOLIA.getSpecificity());
        }

        @Test
        void isOneDeeperThanTheParent() {
            for (ServerPlatformType type : ServerPlatformType.values()) {
                ServerPlatformType parent = type.getParent();
                if (parent == null) {
                    assertEquals(0, type.getSpecificity(), type + " is a root, so its depth must be zero");
                } else {
                    assertEquals(parent.getSpecificity() + 1, type.getSpecificity(),
                            type + " must be exactly one deeper than " + parent);
                }
            }
        }

        @Test
        void descendantsAreStrictlyMoreSpecific() {
            // ServerPlatformDetector sorts by specificity and returns the first match. That is
            // only correct if a platform always outranks everything it derives from.
            for (ServerPlatformType a : ServerPlatformType.values()) {
                for (ServerPlatformType b : ServerPlatformType.values()) {
                    if (a != b && a.isA(b)) {
                        assertTrue(a.getSpecificity() > b.getSpecificity(),
                                a + " descends from " + b + ", so it must sort ahead of it");
                    }
                }
            }
        }

        @Test
        void isNeverNegative() {
            for (ServerPlatformType type : ServerPlatformType.values()) {
                assertTrue(type.getSpecificity() >= 0, type + " must have a non-negative depth");
            }
        }
    }
}

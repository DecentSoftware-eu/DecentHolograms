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

package eu.decentsoftware.holograms.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpiringCacheTest {

    private AtomicLong clock;
    private ExpiringCache<String, String> cache;

    @BeforeEach
    void setUp() {
        clock = new AtomicLong(0);
        cache = new ExpiringCache<>(1, TimeUnit.MINUTES, 3, clock::get);
    }

    @Nested
    class ConstructionTests {

        @Test
        void rejectsNullUnit() {
            Exception exception = assertThrows(NullPointerException.class,
                    () -> new ExpiringCache<String, String>(1, null));

            assertEquals("unit cannot be null", exception.getMessage());
        }

        @Test
        void rejectsNullClock() {
            Exception exception = assertThrows(NullPointerException.class,
                    () -> new ExpiringCache<String, String>(1, TimeUnit.MINUTES, 1, null));

            assertEquals("clock cannot be null", exception.getMessage());
        }

        @Test
        void rejectsNonPositiveTtl() {
            assertThrows(IllegalArgumentException.class,
                    () -> new ExpiringCache<String, String>(0, TimeUnit.MINUTES));
        }

        @Test
        void rejectsNonPositiveMaxSize() {
            assertThrows(IllegalArgumentException.class,
                    () -> new ExpiringCache<String, String>(1, TimeUnit.MINUTES, 0));
        }
    }

    @Nested
    class ReadWriteTests {

        @Test
        void returnsNullForMissingKey() {
            assertNull(cache.getIfPresent("absent"));
        }

        @Test
        void returnsStoredValue() {
            cache.put("key", "value");

            assertEquals("value", cache.getIfPresent("key"));
        }

        @Test
        void overwritesExistingValue() {
            cache.put("key", "first");
            cache.put("key", "second");

            assertEquals("second", cache.getIfPresent("key"));
            assertEquals(1, cache.size());
        }

        @Test
        void rejectsNullKeyAndValue() {
            assertThrows(NullPointerException.class, () -> cache.put(null, "value"));
            assertThrows(NullPointerException.class, () -> cache.put("key", null));
            assertThrows(NullPointerException.class, () -> cache.getIfPresent(null));
        }
    }

    @Nested
    class ExpiryTests {

        @Test
        void valueSurvivesUntilTtlElapses() {
            cache.put("key", "value");
            clock.set(TimeUnit.MINUTES.toMillis(1) - 1);

            assertEquals("value", cache.getIfPresent("key"));
        }

        @Test
        void valueExpiresOnceTtlElapses() {
            cache.put("key", "value");
            clock.set(TimeUnit.MINUTES.toMillis(1));

            assertNull(cache.getIfPresent("key"));
        }

        @Test
        void expiredEntriesAreNotCounted() {
            cache.put("key", "value");
            clock.set(TimeUnit.MINUTES.toMillis(1));

            assertEquals(0, cache.size());
        }

        @Test
        void writeResetsExpiry() {
            cache.put("key", "value");
            clock.set(TimeUnit.SECONDS.toMillis(59));
            cache.put("key", "value");
            clock.set(TimeUnit.SECONDS.toMillis(118));

            assertEquals("value", cache.getIfPresent("key"));
        }
    }

    @Nested
    class EvictionTests {

        @Test
        void dropsExpiredEntriesToMakeRoom() {
            cache.put("a", "1");
            cache.put("b", "2");
            cache.put("c", "3");
            clock.set(TimeUnit.MINUTES.toMillis(1));

            cache.put("d", "4");

            assertEquals(1, cache.size());
            assertEquals("4", cache.getIfPresent("d"));
        }

        @Test
        void evictsEntryClosestToExpiryWhenFull() {
            cache.put("oldest", "1");
            clock.set(1);
            cache.put("middle", "2");
            clock.set(2);
            cache.put("newest", "3");

            clock.set(3);
            cache.put("added", "4");

            assertNull(cache.getIfPresent("oldest"));
            assertNotNull(cache.getIfPresent("middle"));
            assertNotNull(cache.getIfPresent("newest"));
            assertNotNull(cache.getIfPresent("added"));
            assertEquals(3, cache.size());
        }

        @Test
        void overwriteDoesNotTriggerEviction() {
            cache.put("a", "1");
            cache.put("b", "2");
            cache.put("c", "3");

            cache.put("a", "updated");

            assertEquals(3, cache.size());
            assertEquals("updated", cache.getIfPresent("a"));
            assertNotNull(cache.getIfPresent("b"));
            assertNotNull(cache.getIfPresent("c"));
        }
    }

    @Nested
    class InvalidationTests {

        @Test
        void invalidateRemovesSingleEntry() {
            cache.put("a", "1");
            cache.put("b", "2");

            cache.invalidate("a");

            assertNull(cache.getIfPresent("a"));
            assertEquals("2", cache.getIfPresent("b"));
        }

        @Test
        void invalidateAllRemovesEverything() {
            cache.put("a", "1");
            cache.put("b", "2");

            cache.invalidateAll();

            assertEquals(0, cache.size());
        }
    }
}

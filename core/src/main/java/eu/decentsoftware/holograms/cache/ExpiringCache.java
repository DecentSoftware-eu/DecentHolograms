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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

/**
 * A thread-safe cache whose entries expire a fixed duration after they are written.
 *
 * <p>Expired entries are removed lazily, either when they are read or when the cache runs out
 * of room on write. If the cache is still full once expired entries have been dropped, the
 * entry closest to expiring is evicted to make space.</p>
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @author d0by
 * @since 2.10.2
 */
public class ExpiringCache<K, V> {

    private static final int DEFAULT_MAX_SIZE = 1024;

    private final ConcurrentMap<K, Entry<V>> entries = new ConcurrentHashMap<>();
    private final long ttlMillis;
    private final int maxSize;
    private final LongSupplier clock;

    /**
     * Creates a cache holding up to {@value #DEFAULT_MAX_SIZE} entries.
     *
     * @param ttl  How long an entry stays valid after being written.
     * @param unit The time unit of {@code ttl}.
     */
    public ExpiringCache(long ttl, @NotNull TimeUnit unit) {
        this(ttl, unit, DEFAULT_MAX_SIZE);
    }

    /**
     * Creates a cache holding up to {@code maxSize} entries.
     *
     * @param ttl     How long an entry stays valid after being written.
     * @param unit    The time unit of {@code ttl}.
     * @param maxSize The maximum number of entries to hold.
     */
    public ExpiringCache(long ttl, @NotNull TimeUnit unit, int maxSize) {
        this(ttl, unit, maxSize, System::currentTimeMillis);
    }

    /**
     * Creates a cache holding up to {@code maxSize} entries, using the given clock to determine when entries expire.
     *
     * @param ttl     How long an entry stays valid after being written.
     * @param unit    The time unit of {@code ttl}.
     * @param maxSize The maximum number of entries to hold.
     * @param clock   Supplies the current time in milliseconds. Intended for testing.
     */
    public ExpiringCache(long ttl, @NotNull TimeUnit unit, int maxSize, @NotNull LongSupplier clock) {
        Objects.requireNonNull(unit, "unit cannot be null");
        Objects.requireNonNull(clock, "clock cannot be null");
        if (ttl <= 0) {
            throw new IllegalArgumentException("ttl must be positive");
        }
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be positive");
        }
        this.ttlMillis = unit.toMillis(ttl);
        this.maxSize = maxSize;
        this.clock = clock;
    }

    /**
     * Returns the value cached for the given key, if it is present and has not expired.
     *
     * @param key The key.
     * @return The cached value, or null if absent or expired.
     */
    @Nullable
    public V getIfPresent(@NotNull K key) {
        Objects.requireNonNull(key, "key cannot be null");
        Entry<V> entry = entries.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired(clock.getAsLong())) {
            entries.remove(key, entry);
            return null;
        }
        return entry.value;
    }

    /**
     * Caches a value, replacing any existing entry for the key and resetting its expiry.
     *
     * @param key   The key.
     * @param value The value.
     */
    public void put(@NotNull K key, @NotNull V value) {
        put(key, value, ttlMillis);
    }

    /**
     * Caches a value with a custom lifetime, replacing any existing entry for the key.
     *
     * @param key   The key.
     * @param value The value.
     * @param ttl   How long this entry stays valid, overriding the cache-wide default.
     * @param unit  The time unit of {@code ttl}.
     */
    public void put(@NotNull K key, @NotNull V value, long ttl, @NotNull TimeUnit unit) {
        Objects.requireNonNull(unit, "unit cannot be null");
        if (ttl <= 0) {
            throw new IllegalArgumentException("ttl must be positive");
        }
        put(key, value, unit.toMillis(ttl));
    }

    private void put(@NotNull K key, @NotNull V value, long entryTtlMillis) {
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
        long now = clock.getAsLong();
        if (!entries.containsKey(key) && entries.size() >= maxSize) {
            makeRoom(now);
        }
        entries.put(key, new Entry<>(value, now + entryTtlMillis));
    }

    /**
     * Removes the entry for the given key, if any.
     *
     * @param key The key.
     */
    public void invalidate(@NotNull K key) {
        Objects.requireNonNull(key, "key cannot be null");
        entries.remove(key);
    }

    /**
     * Removes all entries.
     */
    public void invalidateAll() {
        entries.clear();
    }

    /**
     * @return The number of entries that are present and have not expired.
     */
    public int size() {
        long now = clock.getAsLong();
        int count = 0;
        for (Entry<V> entry : entries.values()) {
            if (!entry.isExpired(now)) {
                count++;
            }
        }
        return count;
    }

    private void makeRoom(long now) {
        entries.values().removeIf(entry -> entry.isExpired(now));
        if (entries.size() < maxSize) {
            return;
        }
        entries.entrySet().stream()
                .min(Comparator.comparingLong(entry -> entry.getValue().expiresAtMillis))
                .ifPresent(entry -> entries.remove(entry.getKey(), entry.getValue()));
    }

    private static final class Entry<V> {

        private final V value;
        private final long expiresAtMillis;

        private Entry(V value, long expiresAtMillis) {
            this.value = value;
            this.expiresAtMillis = expiresAtMillis;
        }

        private boolean isExpired(long now) {
            return now >= expiresAtMillis;
        }
    }
}

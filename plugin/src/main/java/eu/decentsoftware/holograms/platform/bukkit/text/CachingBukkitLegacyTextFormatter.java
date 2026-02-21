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

package eu.decentsoftware.holograms.platform.bukkit.text;

import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.platform.api.text.TextFormat;
import eu.decentsoftware.holograms.platform.api.text.TextFormatter;
import eu.decentsoftware.holograms.profiler.DecentProfiler;
import eu.decentsoftware.holograms.profiler.Metrics;
import eu.decentsoftware.holograms.profiler.TimerHandle;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A caching implementation of the {@link TextFormatter} interface designed for
 * Bukkit's legacy text formatting. This formatter applies color codes and formatting
 * styles, caching the results for improved performance when processing recurring text.
 *
 * <h4>Thread Safety:</h4>
 * Though the class is backed by a {@link LinkedHashMap}, it is not inherently
 * thread-safe. External synchronization is required when accessing its operations
 * from multiple threads.
 *
 * <h4>Usage Notes:</h4>
 * - Call {@link #invalidate()} to clear the cache when necessary, such as when
 * text formatting rules have changed or when the plugin is being shut down.
 *
 * @author d0by
 * @see TextFormatter
 * @see TextFormat#LEGACY
 * @since 2.10.0
 */
public class CachingBukkitLegacyTextFormatter implements TextFormatter {

    public static final int DEFAULT_MAX_CACHE_SIZE = 2000;
    private final Map<String, String> cache;

    /**
     * Creates a new instance of the {@code CachingBukkitLegacyTextFormatter} with the default maximum cache size.
     *
     * @see #CachingBukkitLegacyTextFormatter(int)
     * @see #DEFAULT_MAX_CACHE_SIZE
     * @since 2.10.0
     */
    public CachingBukkitLegacyTextFormatter() {
        this(DEFAULT_MAX_CACHE_SIZE);
    }

    /**
     * Constructs a new {@code CachingBukkitLegacyTextFormatter} instance with the specified maximum cache size.
     *
     * <p>The formatter uses the least recently used (LRU) eviction policy, implemented via a
     * {@link LinkedHashMap} with access order enabled. Text formatting results are cached
     * to improve performance for frequently recurring input strings.</p>
     *
     * @param maxSize the maximum number of entries that the cache can hold. When the size
     *                exceeds this limit, the least recently accessed entry will be evicted.
     *                Must be a positive integer.
     * @throws IllegalArgumentException if {@code maxSize} is not a positive integer.
     * @since 2.10.0
     */
    public CachingBukkitLegacyTextFormatter(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be a positive integer");
        }
        // Using accessOrder=true to implement LRU eviction policy
        this.cache = new LinkedHashMap<String, String>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > maxSize;
            }
        };
    }

    @Override
    public @NotNull String format(@NotNull String text) {
        try (TimerHandle ignored = DecentProfiler.getInstance().startTimer(Metrics.POST_PROCESS_TEXT_FORMAT_LINE)) {
            String cached = cache.get(text);
            if (cached != null) {
                return cached;
            }

            String formatted = Common.colorize(text);
            cache.put(text, formatted);
            return formatted;
        }
    }

    /**
     * Clears the internal cache of formatted text strings.
     *
     * <p>Thread Safety: If the formatter is accessed concurrently by multiple threads,
     * it is the caller's responsibility to ensure proper synchronization before
     * calling this method.</p>
     *
     * @since 2.10.0
     */
    public void invalidate() {
        cache.clear();
    }
}

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

package eu.decentsoftware.holograms.profiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A lightweight, histogram-based profiling system for measuring operation performance.
 *
 * <p>DecentProfiler provides a simple API for timing operations and tracking their performance
 * distribution using histogram buckets. It is designed to have zero overhead when disabled
 * and minimal overhead when enabled (~50-100ns per measurement).</p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Histogram-based timing: Tracks distribution, not just averages</li>
 *   <li>Zero overhead when disabled: Returns no-op handles that are optimized away by JIT</li>
 *   <li>Thread-safe: All operations can be called concurrently</li>
 *   <li>Minimal memory: ~120 bytes per timer regardless of measurement count</li>
 *   <li>Automatic statistics: Min, max, average, percentiles, and distribution</li>
 * </ul>
 *
 * <p><b>Basic Usage:</b></p>
 * <pre>{@code
 * // Enable profiling
 * DecentProfiler.getInstance().start();
 *
 * // Measure operations using try-with-resources
 * try (TimerHandle timer = DecentProfiler.getInstance().startTimer("text.cache.parse")) {
 *     parseText();
 * }
 *
 * // Or just count occurrences
 * DecentProfiler.getInstance().increment("cache.hit");
 *
 * // Get statistics
 * String stats = DecentProfiler.getInstance().getStats();
 * System.out.println(stats);
 *
 * // Disable profiling
 * DecentProfiler.getInstance().stop();
 * }</pre>
 *
 * <p><b>Naming Convention:</b> Use hierarchical dot-separated names for timers:
 * <ul>
 *   <li>{@code "post_processing.text_format"} - Subsystem and operation</li>
 *   <li>{@code "render.diff"}</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b> This class is thread-safe. Multiple threads can start timers,
 * increment counters, and retrieve statistics concurrently without external synchronization.</p>
 *
 * <p><b>Singleton Pattern:</b> Use {@link #getInstance()} to access the global profiler instance.
 * The profiler is disabled by default and must be explicitly enabled via {@link #start()}.</p>
 *
 * @author d0by
 * @see Timer
 * @see TimerHandle
 * @since 2.10.0
 */
public final class DecentProfiler {
    private static final DecentProfiler instance = new DecentProfiler();
    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final ConcurrentHashMap<String, Timer> timers = new ConcurrentHashMap<>();

    private DecentProfiler() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Returns the singleton profiler instance.
     *
     * @return The global profiler instance
     */
    public static DecentProfiler getInstance() {
        return instance;
    }

    /**
     * Starts timing an operation and returns a handle to automatically record elapsed time.
     *
     * <p>Use with try-with-resources for automatic timing:</p>
     * <pre>{@code
     * try (TimerHandle timer = profiler.startTimer("operation.name")) {
     *     // Code to measure
     * } // Automatically records elapsed time when exiting block
     * }</pre>
     *
     * <p>If profiling is disabled, returns {@link TimerHandle#NOOP} which has zero overhead.</p>
     *
     * <p><b>Thread Safety:</b> This method is thread-safe and can be called concurrently.</p>
     *
     * @param name The unique name for this timer (e.g., "post_process.text_animations.line")
     * @return A timer handle to be used in try-with-resources blocks for automatic timing
     * @see TimerHandle
     */
    public TimerHandle startTimer(String name) {
        if (!enabled.get()) {
            return TimerHandle.NOOP;
        }
        Timer timer = timers.computeIfAbsent(name, Timer::new);
        return new TimerHandle(timer, System.nanoTime());
    }

    /**
     * Increments a counter without timing.
     *
     * <p>Useful for counting events or occurrences:</p>
     * <pre>{@code
     * profiler.increment("cache.hit");
     * profiler.increment("animation.skipped");
     * }</pre>
     *
     * <p>If profiling is disabled, this method does nothing and returns immediately.</p>
     *
     * <p><b>Thread Safety:</b> This method is thread-safe and can be called concurrently.</p>
     *
     * @param name The unique name for this counter (e.g., "cache.hit")
     */
    public void increment(String name) {
        if (!enabled.get()) {
            return;
        }
        Timer timer = timers.computeIfAbsent(name, Timer::new);
        timer.record(0); // Just increment count
    }

    /**
     * Checks whether profiling is currently enabled.
     *
     * @return true if profiling is enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled.get();
    }

    /**
     * Enables profiling. All subsequent {@link #startTimer(String)} calls will record measurements.
     *
     * <p>This does not reset existing statistics. Use {@link #reset()} to clear data before starting if needed.</p>
     */
    public void start() {
        enabled.set(true);
    }

    /**
     * Disables profiling. All subsequent {@link #startTimer(String)} calls will return
     * no-op handles with zero overhead.
     *
     * <p>Existing statistics are preserved and can still be retrieved via {@link #getStats()}.</p>
     */
    public void stop() {
        enabled.set(false);
    }

    /**
     * Resets all statistics for all timers.
     *
     * <p>This clears all recorded measurements but does not remove timer instances.
     * Timers will start fresh from zero after reset.</p>
     */
    public void reset() {
        timers.values().forEach(Timer::reset);
    }

    /**
     * Retrieves a specific timer by name.
     *
     * @param name The timer name
     * @return The timer instance, or null if no measurements have been recorded for this name
     */
    public Timer getTimer(String name) {
        return timers.get(name);
    }

    /**
     * Returns all registered timer names.
     *
     * <p>Timer names are registered on first use (first {@link #startTimer(String)} or
     * {@link #increment(String)} call).</p>
     *
     * @return An unmodifiable collection of timer names
     */
    public Collection<String> getTimerNames() {
        return timers.keySet();
    }

    /**
     * Generates a human-readable report of all timers and their statistics.
     *
     * <p>The report includes average time, total calls, and key percentiles (P50, P95, P99)
     * for each timer. Timers are sorted by average time in descending order.</p>
     *
     * @return A formatted string containing the profiler statistics
     */
    public String getStats() {
        StringBuilder sb = new StringBuilder("\n=== Profiler Statistics ===\n");

        // Sort by total time (most expensive first)
        List<Timer> sorted = new ArrayList<>(timers.values());
        sorted.sort((a, b) -> Long.compare(b.getTotalTime(), a.getTotalTime()));

        for (Timer timer : sorted) {
            sb.append(String.format("%s:\n  %,10d ns avg | %,10d calls | P50: %8s | P95: %8s | P99: %8s\n",
                    timer.getId(),
                    timer.getAvg(),
                    timer.getTotalCount(),
                    timer.getPercentile(50),
                    timer.getPercentile(95),
                    timer.getPercentile(99)
            ));
        }

        sb.append("============================\n");
        sb.append("Use '/dh profiler stats <timer>' for detailed statistics.");
        return sb.toString();
    }
}
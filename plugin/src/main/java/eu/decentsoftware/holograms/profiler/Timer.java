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

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * A histogram-based timer that records operation execution times and tracks distribution statistics.
 *
 * <p><b>Histogram Buckets:</b> Measurements are categorized into predefined time ranges
 * (e.g., &lt;100ns, &lt;1µs, &lt;10µs, &lt;1ms) allowing you to see how many operations
 * fall into each performance tier. This is crucial for identifying outliers and understanding
 * the true distribution of performance, not just averages.</p>
 *
 * <p><b>Thread Safety:</b> All operations are thread-safe using atomic operations. Multiple
 * threads can record measurements concurrently without external synchronization.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * Timer timer = new Timer("database.query");
 *
 * // Record measurements
 * timer.record(1_234); // 1.234µs
 * timer.record(450);   // 450ns
 * timer.record(50_000); // 50µs
 *
 * // Get statistics
 * System.out.println("Average: " + timer.getAvg() + "ns");
 * System.out.println("P99: " + timer.getPercentile(99));
 * System.out.println(timer.getFormattedStats());
 * }</pre>
 *
 * <p><b>Why Histograms Matter:</b> Averages can hide performance problems. For example,
 * an average of 1,000ns might come from operations taking [10,000ns, 100ns, 100ns, ...], where
 * most operations are fast but outliers exist. The histogram distribution reveals this.</p>
 *
 * @author d0by
 * @see DecentProfiler
 * @see TimerHandle
 * @since 2.10.0
 */
public class Timer {

    /**
     * Predefined histogram buckets defining time ranges for categorizing measurements.
     * Measurements are placed into the first bucket where {@code timeNs < upperBound}.
     */
    private static final Bucket[] BUCKETS = {
            new Bucket(100, "< 100ns"),
            new Bucket(250, "< 250ns"),
            new Bucket(500, "< 500ns"),
            new Bucket(1_000, "< 1µs"),
            new Bucket(5_000, "< 5µs"),
            new Bucket(10_000, "< 10µs"),
            new Bucket(50_000, "< 50µs"),
            new Bucket(100_000, "< 100µs"),
            new Bucket(500_000, "< 500µs"),
            new Bucket(1_000_000, "< 1ms"),
            new Bucket(Long.MAX_VALUE, ">= 1ms")
    };

    private final String name;
    private final AtomicLong totalCount = new AtomicLong(0);
    private final AtomicLong totalTime = new AtomicLong(0);
    private final AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong maxTime = new AtomicLong(Long.MIN_VALUE);
    private final AtomicLongArray bucketCounts = new AtomicLongArray(BUCKETS.length);

    /**
     * Creates a new timer with the specified name.
     *
     * @param name The unique identifier for this timer (e.g., "text.cache.parse")
     */
    Timer(String name) {
        this.name = name;
    }

    public String getId() {
        return name;
    }

    /**
     * Records a single timing measurement in nanoseconds.
     *
     * <p>This method updates:
     * <ul>
     *   <li>Total count and accumulated time (for average calculation)</li>
     *   <li>Minimum and maximum observed times</li>
     *   <li>Histogram bucket counts (for distribution analysis)</li>
     * </ul>
     *
     * <p><b>Thread Safety:</b> This method is thread-safe and can be called concurrently
     * from multiple threads.</p>
     *
     * <p><b>Performance:</b> Recording a measurement takes approximately 50-100ns due to
     * atomic operations and bucket lookup.</p>
     *
     * @param timeNs The elapsed time in nanoseconds to record
     */
    public void record(long timeNs) {
        totalCount.incrementAndGet();
        totalTime.addAndGet(timeNs);

        // Update min/max
        updateMin(timeNs);
        updateMax(timeNs);

        // Find bucket and increment
        for (int i = 0; i < BUCKETS.length; i++) {
            if (timeNs < BUCKETS[i].upperBound) {
                bucketCounts.incrementAndGet(i);
                break;
            }
        }
    }

    /**
     * Atomically updates the minimum recorded time if the new time is smaller.
     */
    private void updateMin(long timeNs) {
        long currentMin;
        do {
            currentMin = minTime.get();
        } while (timeNs < currentMin && !minTime.compareAndSet(currentMin, timeNs));
    }

    /**
     * Atomically updates the maximum recorded time if the new time is larger.
     */
    private void updateMax(long timeNs) {
        long currentMax;
        do {
            currentMax = maxTime.get();
        } while (timeNs > currentMax && !maxTime.compareAndSet(currentMax, timeNs));
    }

    /**
     * Returns the total number of measurements recorded.
     *
     * @return The total count of recorded measurements
     */
    public long getTotalCount() {
        return totalCount.get();
    }

    /**
     * Calculates and returns the average time across all measurements.
     *
     * @return The average time in nanoseconds, or 0 if no measurements recorded
     */
    public long getAvg() {
        long count = totalCount.get();
        return count == 0 ? 0 : totalTime.get() / count;
    }

    /**
     * Returns the minimum time recorded.
     *
     * @return The minimum time in nanoseconds, or 0 if no measurements recorded
     */
    public long getMin() {
        long min = minTime.get();
        return min == Long.MAX_VALUE ? 0 : min;
    }

    /**
     * Returns the maximum time recorded.
     *
     * <p><b>Note:</b> Very high maximum values (&gt;1ms for sub-microsecond operations)
     * are often caused by JVM garbage collection, OS context switches, or JIT compilation
     * rather than the measured operation itself.</p>
     *
     * @return The maximum time in nanoseconds, or 0 if no measurements recorded
     */
    public long getMax() {
        long max = maxTime.get();
        return max == Long.MIN_VALUE ? 0 : max;
    }

    /**
     * Returns the count of measurements in a specific histogram bucket.
     *
     * @param index The bucket index (0 to BUCKETS.length - 1)
     * @return The number of measurements in this bucket
     */
    public long getBucketCount(int index) {
        return bucketCounts.get(index);
    }

    /**
     * Calculates what percentage of measurements fall into a specific bucket.
     *
     * @param index The bucket index (0 to BUCKETS.length - 1)
     * @return The percentage (0.0 to 100.0) of measurements in this bucket
     */
    public double getBucketPercentage(int index) {
        long total = totalCount.get();
        return total == 0 ? 0.0 : (double) bucketCounts.get(index) / total * 100.0;
    }

    /**
     * Estimates the percentile value based on histogram buckets.
     *
     * <p>Returns the upper bound label of the bucket containing the percentile.
     * For example, if P95 falls in the "&lt;5µs" bucket, this returns "&lt;5µs".</p>
     *
     * <p><b>Accuracy:</b> This is an approximation based on histogram buckets,
     * not exact percentile calculation. It's accurate enough for performance analysis
     * while using minimal memory.</p>
     *
     * <p><b>Common Percentiles:</b></p>
     * <ul>
     *   <li>P50 (median): Half of operations are faster than this</li>
     *   <li>P95: 95% of operations are faster than this</li>
     *   <li>P99: 99% of operations are faster than this (useful for finding outliers)</li>
     *   <li>P99.9: 99.9% of operations are faster than this (rare worst cases)</li>
     * </ul>
     *
     * @param p The percentile to calculate (0.0 to 100.0)
     * @return The bucket label containing this percentile (e.g., "&lt;5µs")
     */
    public String getPercentile(double p) {
        long total = totalCount.get();
        long targetCount = (long) (total * p / 100.0);

        long cumulative = 0;
        for (int i = 0; i < BUCKETS.length; i++) {
            cumulative += bucketCounts.get(i);
            if (cumulative >= targetCount) {
                return BUCKETS[i].label; // Return bucket upper bound
            }
        }
        return BUCKETS[BUCKETS.length - 1].label;
    }

    public void reset() {
        totalCount.set(0);
        totalTime.set(0);
        minTime.set(Long.MAX_VALUE);
        maxTime.set(Long.MIN_VALUE);
        for (int i = 0; i < bucketCounts.length(); i++) {
            bucketCounts.set(i, 0);
        }
    }

    public String getFormattedStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(name).append(":");
        sb.append(String.format("\n  Count: %,d", getTotalCount()));
        sb.append(String.format("\n  Avg: %,d ns", getAvg()));
        sb.append(String.format("\n  Min: %,d ns", getMin()));
        sb.append(String.format("\n  Max: %,d ns", getMax()));
        sb.append(String.format("\n  P50: %s", getPercentile(50)));
        sb.append(String.format("\n  P95: %s", getPercentile(95)));
        sb.append(String.format("\n  P99: %s", getPercentile(99)));
        sb.append(String.format("\n  P99.9: %s", getPercentile(99.9)));
        sb.append("\n  Distribution:");

        for (int i = 0; i < BUCKETS.length; i++) {
            long bucketCount = getBucketCount(i);
            if (bucketCount > 0) {
                double percentage = getBucketPercentage(i);
                sb.append(String.format("\n    %s: %,d (%.2f%%)",
                        BUCKETS[i].label, bucketCount, percentage));
            }
        }

        return sb.toString();
    }

    private static class Bucket {
        private final long upperBound;
        private final String label;

        private Bucket(long upperBound, String label) {
            this.upperBound = upperBound;
            this.label = label;
        }
    }
}
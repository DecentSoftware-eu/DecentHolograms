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

public class Timer implements ProfilerMetric {

    // Histogram buckets (nanoseconds)
    private static final long[] BUCKETS = {
            100,           // < 100ns
            500,           // < 500ns
            1_000,         // < 1µs
            5_000,         // < 5µs
            10_000,        // < 10µs
            50_000,        // < 50µs
            100_000,       // < 100µs
            500_000,       // < 500µs
            1_000_000,     // < 1ms
            Long.MAX_VALUE // >= 1ms
    };

    private final String name;
    private final AtomicLong totalCount = new AtomicLong(0);
    private final AtomicLong totalTime = new AtomicLong(0);
    private final AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong maxTime = new AtomicLong(Long.MIN_VALUE);
    private final AtomicLongArray bucketCounts = new AtomicLongArray(BUCKETS.length);

    public Timer(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    public void record(long timeNs) {
        totalCount.incrementAndGet();
        totalTime.addAndGet(timeNs);

        // Update min/max
        updateMin(timeNs);
        updateMax(timeNs);

        // Find bucket and increment
        for (int i = 0; i < BUCKETS.length; i++) {
            if (timeNs < BUCKETS[i]) {
                bucketCounts.incrementAndGet(i);
                break;
            }
        }
    }

    private void updateMin(long timeNs) {
        long currentMin;
        do {
            currentMin = minTime.get();
        } while (timeNs < currentMin && !minTime.compareAndSet(currentMin, timeNs));
    }

    private void updateMax(long timeNs) {
        long currentMax;
        do {
            currentMax = maxTime.get();
        } while (timeNs > currentMax && !maxTime.compareAndSet(currentMax, timeNs));
    }

    public String getName() {
        return name;
    }

    public long getTotalCount() {
        return totalCount.get();
    }

    public long getAvg() {
        long count = totalCount.get();
        return count == 0 ? 0 : totalTime.get() / count;
    }

    public long getMin() {
        long min = minTime.get();
        return min == Long.MAX_VALUE ? 0 : min;
    }

    public long getMax() {
        long max = maxTime.get();
        return max == Long.MIN_VALUE ? 0 : max;
    }

    public long getBucketCount(int index) {
        return bucketCounts.get(index);
    }

    public double getBucketPercentage(int index) {
        long total = totalCount.get();
        return total == 0 ? 0.0 : (double) bucketCounts.get(index) / total * 100.0;
    }

    public long getPercentile(double p) {
        long total = totalCount.get();
        long targetCount = (long) (total * p / 100.0);

        long cumulative = 0;
        for (int i = 0; i < BUCKETS.length; i++) {
            cumulative += bucketCounts.get(i);
            if (cumulative >= targetCount) {
                return BUCKETS[i]; // Return bucket upper bound
            }
        }
        return BUCKETS[BUCKETS.length - 1];
    }

    @Override
    public void reset() {
        totalCount.set(0);
        totalTime.set(0);
        minTime.set(Long.MAX_VALUE);
        maxTime.set(Long.MIN_VALUE);
        for (int i = 0; i < bucketCounts.length(); i++) {
            bucketCounts.set(i, 0);
        }
    }

    @Override
    public String getFormattedStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(name).append(":");
        sb.append(String.format("\n  Count: %,d", getTotalCount()));
        sb.append(String.format("\n  Avg: %,d ns", getAvg()));
        sb.append(String.format("\n  Min: %,d ns", getMin()));
        sb.append(String.format("\n  Max: %,d ns", getMax()));
        sb.append(String.format("\n  P95: <%,d ns", getPercentile(95)));
        sb.append(String.format("\n  P99: <%,d ns", getPercentile(99)));
        sb.append(String.format("\n  P99.9: <%,d ns", getPercentile(99.9)));
        sb.append("\n  Distribution:");

        String[] labels = {
                "< 100ns", "< 500ns", "< 1µs", "< 5µs", "< 10µs",
                "< 50µs", "< 100µs", "< 500µs", "< 1ms", ">= 1ms"
        };

        for (int i = 0; i < BUCKETS.length; i++) {
            long bucketCount = getBucketCount(i);
            if (bucketCount > 0) {
                double percentage = getBucketPercentage(i);
                sb.append(String.format("\n    %s: %,d (%.2f%%)",
                        labels[i], bucketCount, percentage));
            }
        }

        return sb.toString();
    }
}
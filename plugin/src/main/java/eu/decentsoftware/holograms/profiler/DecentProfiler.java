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

public final class DecentProfiler {
    private static final DecentProfiler instance = new DecentProfiler();
    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final ConcurrentHashMap<String, Timer> timers = new ConcurrentHashMap<>();

    public static DecentProfiler getInstance() {
        return instance;
    }

    public TimerHandle startTimer(String name) {
        if (!enabled.get()) {
            return TimerHandle.NOOP;
        }
        Timer timer = timers.computeIfAbsent(name, Timer::new);
        return new TimerHandle(timer, System.nanoTime());
    }

    public void increment(String name) {
        if (!enabled.get()) {
            return;
        }
        Timer timer = timers.computeIfAbsent(name, Timer::new);
        timer.record(0); // Just increment count
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void start() {
        enabled.set(true);
    }

    public void stop() {
        enabled.set(false);
    }

    public void reset() {
        timers.values().forEach(Timer::reset);
    }

    public Timer getTimer(String name) {
        return timers.get(name);
    }

    public Collection<String> getTimerNames() {
        return timers.keySet();
    }

    public String getStats() {
        StringBuilder sb = new StringBuilder("\n=== Profiler Statistics ===");

        // Sort by total time (most expensive first)
        List<Timer> sorted = new ArrayList<>(timers.values());
        sorted.sort((a, b) -> Long.compare(
                b.getTotalCount() * b.getAvg(),
                a.getTotalCount() * a.getAvg()
        ));

        for (Timer timer : sorted) {
            sb.append(timer.getFormattedStats());
        }

        sb.append("\n============================");
        return sb.toString();
    }
}
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

public class TimerHandle implements AutoCloseable {
    public static final TimerHandle NOOP = new TimerHandle(null, 0);

    private final Timer timer;
    private final long startTime;

    TimerHandle(Timer timer, long startTime) {
        this.timer = timer;
        this.startTime = startTime;
    }

    @Override
    public void close() {
        if (timer != null) {
            long elapsed = System.nanoTime() - startTime;
            timer.record(elapsed);
        }
    }

    public void stop() {
        close();
    }
}
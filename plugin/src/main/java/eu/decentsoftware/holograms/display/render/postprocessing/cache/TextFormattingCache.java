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

package eu.decentsoftware.holograms.display.render.postprocessing.cache;

import eu.decentsoftware.holograms.profiler.Metrics;
import eu.decentsoftware.holograms.profiler.DecentProfiler;
import eu.decentsoftware.holograms.profiler.TimerHandle;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class TextFormattingCache {

    public static final int DEFAULT_MAX_SIZE = 2000;
    private final Map<String, String> cache;

    public TextFormattingCache(int maxSize) {
        // Using accessOrder=true to implement LRU eviction policy
        this.cache = new LinkedHashMap<String, String>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > maxSize;
            }
        };
    }

    public String parse(String rawText, Function<String, String> parser) {
        try (TimerHandle ignored = DecentProfiler.getInstance().startTimer(Metrics.POST_PROCESS_TEXT_FORMAT_LINE)) {
            return cache.computeIfAbsent(rawText, parser);
        }
    }

    public void invalidate() {
        cache.clear();
    }
}

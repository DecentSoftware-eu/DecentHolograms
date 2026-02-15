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

/**
 * Centralized class for defining metric identifiers used in the profiler.
 */
public final class Metrics {

    private Metrics() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String POST_PROCESS = "post_process";
    public static final String POST_PROCESS_TEXT_FORMAT = "post_process.text_format";
    public static final String POST_PROCESS_TEXT_FORMAT_LINE = "post_process.text_format.line";
    public static final String POST_PROCESS_TEXT_ANIMATIONS = "post_process.text_animations";
    public static final String POST_PROCESS_TEXT_ANIMATIONS_LINE = "post_process.text_animations.line";
    public static final String RENDER_DIFF = "render.diff";
    public static final String RENDER_PLATFORM = "render.platform";
}

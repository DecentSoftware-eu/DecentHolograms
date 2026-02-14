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

package eu.decentsoftware.holograms.display.attribute.value;

import eu.decentsoftware.holograms.display.render.DisplayRenderContext;

/**
 * Represents a compiled attribute value that can dynamically compute or provide
 * a specific value of type {@code T}. This is the result of compiling an {@link AttributeValue}
 * within a specific {@link DisplayRenderContext}.
 *
 * <p>Compiled attribute values are evaluated during post-processing to produce the final
 * rendered value. Post-processing occurs many times per second (potentially at sub-tick rates),
 * so implementations must be extremely lightweight and efficient. All expensive operations
 * (placeholder resolution, parsing, complex calculations) should have been performed during
 * the compilation phase.</p>
 *
 * <p>Compiled values may be static (always return the same value) or dynamic (compute
 * values based on elapsed time or pre-computed state). Dynamic values typically use
 * {@code System.currentTimeMillis()} or similar mechanisms to determine animation frames.</p>
 *
 * <p><b>Performance Guidelines:</b></p>
 * <ul>
 *   <li>Evaluation should complete in microseconds, not milliseconds</li>
 *   <li>Avoid I/O, network calls, or blocking operations</li>
 *   <li>Pre-compute values during compilation whenever possible</li>
 * </ul>
 *
 * @param <T> The type of the value produced by this compiled attribute
 * @author d0by
 * @see AttributeValue
 * @see DisplayRenderContext
 * @since 2.10.0
 */
public interface CompiledAttributeValue<T> {

    /**
     * Evaluates and returns the current value to be rendered. This method is called during
     * post-processing, potentially many times per second, and must be extremely efficient.
     *
     * <p>For static values, this always returns the same pre-computed result. For animated
     * or dynamic values, this may return different results on each invocation based on
     * elapsed time or other lightweight state calculations.</p>
     *
     * <p><b>Implementation Examples:</b></p>
     * <ul>
     *   <li>Static color: {@code return preComputedColor;}</li>
     *   <li>Rainbow animation: {@code return DecentColor.fromHsv((System.currentTimeMillis() / 20f) % 360, 100, 100);}</li>
     *   <li>Pulsing scale: {@code return 1.0 + 0.2 * Math.sin(System.currentTimeMillis() / 500.0);}</li>
     * </ul>
     *
     * @return The current value to be rendered, never null
     */
    T evaluate();
}

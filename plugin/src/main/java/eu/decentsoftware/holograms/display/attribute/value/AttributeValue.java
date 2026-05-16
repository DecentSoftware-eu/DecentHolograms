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
 * Represents a raw, uncompiled attribute value that can be compiled into a
 * {@link CompiledAttributeValue} for runtime evaluation during post-processing.
 *
 * <p>AttributeValues are the data-layer representation of attribute configurations,
 * typically deserialized from YAML files or created through commands. They contain
 * the raw parameters needed to create compiled values but do not perform any
 * computation themselves.</p>
 *
 * <p>The compilation process ({@link #compile(DisplayRenderContext)}) transforms
 * this raw data into an optimized, executable form. The compilation process should
 * take care of all expensive operations like placeholder resolution or parsing,
 * so that they are only performed once and can be reused for multiple evaluations
 * during post-processing. The resulting {@link CompiledAttributeValue} contains only
 * the minimal state needed for efficient runtime evaluation.</p>
 *
 * @param <T> The type of value this attribute produces when compiled and evaluated
 * @author d0by
 * @see CompiledAttributeValue
 * @see DisplayRenderContext
 * @since 2.10.0
 */
public interface AttributeValue<T> {

    /**
     * Returns the unique type identifier for this attribute value.
     *
     * <p>This key is used for serialization/deserialization and must match
     * a registered {@link eu.decentsoftware.holograms.display.attribute.value.AttributeValueType}
     * in the {@link eu.decentsoftware.holograms.display.attribute.value.AttributeValueTypeRegistry}.</p>
     *
     * @return The type key for this attribute value, never null
     */
    String getTypeKey();

    /**
     * Compiles this raw attribute value into an executable {@link CompiledAttributeValue}
     * by pre-computing any expensive operations and evaluating per-player data as needed.
     *
     * <p>This method is called once during logical state creation (once per a displays
     * update-interval). Expensive operations like placeholder resolution, text parsing,
     * or complex calculations should be performed here. The resulting
     * {@link CompiledAttributeValue} is then evaluated many times per second during
     * post-processing and should only perform lightweight operations like selecting
     * the current animation frame based on time.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * public class PlaceholderAttributeValue implements AttributeValue<String> {
     *     private final String placeholder;
     *     private final PlaceholderService placeholderService;
     *     // constructor...
     *
     *     @Override
     *     public CompiledAttributeValue<String> compile(DisplayRenderContext context) {
     *         DecentPlayer player = context.getPlayer();
     *
     *         // Resolve the placeholder value once during compilation (expensive)
     *         String resolvedText = placeholderService.setPlaceholders(player, placeholder);
     *
     *         // Return a compiled value that just returns the pre-resolved text (fast)
     *         return () -> resolvedText;
     *     }
     * }
     * }</pre>
     *
     * @param context The render context providing access to contextual and player-specific data
     * @return A compiled attribute value ready for evaluation, never null
     */
    CompiledAttributeValue<T> compile(DisplayRenderContext context);

    /**
     * Returns a human-readable string representation of this attribute value.
     *
     * <p>This method should provide a concise but informative description of the attribute value,
     * including its type and any relevant parameters. The exact format is up to the implementation,
     * but it should be clear enough to understand the nature of the attribute value at a glance.</p>
     *
     * @return A string representation of this attribute value, never null
     */
    String toHumanReadableString();
}

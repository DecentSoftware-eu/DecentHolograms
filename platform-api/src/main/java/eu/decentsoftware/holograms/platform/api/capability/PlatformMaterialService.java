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

package eu.decentsoftware.holograms.platform.api.capability;

import java.util.List;

/**
 * Platform abstraction for resolving and classifying Minecraft materials.
 *
 * <p>This service provides a unified way to translate platform-specific
 * material identifiers into canonical Mojang registry IDs and to query
 * material metadata in a platform-independent manner.</p>
 *
 * <p>The canonical representation of a material is its Mojang namespaced
 * identifier in the format {@code minecraft:<id>} (for example
 * {@code minecraft:diamond_block} or {@code minecraft:lapis_lazuli}).</p>
 *
 * <p>Implementations are responsible for handling any platform-specific
 * naming conventions, legacy formats, aliases, or version differences.
 * The core system must operate exclusively on Mojang namespaced IDs.</p>
 *
 * <p>All returned material names must:</p>
 * <ul>
 *     <li>Be fully qualified (e.g. {@code minecraft:stone})</li>
 *     <li>Use lowercase characters</li>
 *     <li>Represent the modern Mojang registry identity of the material</li>
 * </ul>
 *
 * <p>This interface forms the boundary between the platform adapter layer
 * and the platform-agnostic core.</p>
 *
 * @author d0by
 * @since 2.10.0
 */
public interface PlatformMaterialService {

    /**
     * Resolves a platform-specific material identifier into a canonical Mojang namespaced ID.
     *
     * <p>The provided identifier may be platform-defined, legacy, or version-specific.
     * Implementations must normalize it to the corresponding modern Mojang registry identifier.</p>
     *
     * <p>Examples of valid return values:</p>
     * <pre>
     * minecraft:diamond_block
     * minecraft:lapis_lazuli
     * minecraft:clock
     * </pre>
     *
     * @param materialName the platform-specific material identifier; may be legacy
     * @return the canonical Mojang namespaced ID, or {@code null} if the material cannot be resolved
     */
    String toMojangNamespacedKey(String materialName);

    /**
     * Returns all available item material identifiers for the current platform.
     *
     * <p>The returned list must contain canonical Mojang namespaced IDs only
     * (e.g. {@code minecraft:diamond_sword}).</p>
     *
     * <p>The list should represent all materials that are considered items
     * by the underlying platform implementation.</p>
     *
     * @return an immutable list of item material names in {@code minecraft:<id>} format
     */
    List<String> getItemMaterialNames();

    /**
     * Returns all available block material identifiers for the current platform.
     *
     * <p>The returned list must contain canonical Mojang namespaced IDs only
     * (e.g. {@code minecraft:diamond_block}).</p>
     *
     * <p>The list should represent all materials that are considered blocks
     * by the underlying platform implementation.</p>
     *
     * @return an immutable list of block material names in {@code minecraft:<id>} format
     */
    List<String> getBlockMaterialNames();

    /**
     * Checks if the provided material name represents an item material.
     *
     * <p>The material names are in the Mojang namespaced format (e.g., {@code minecraft:diamond_sword}).</p>
     *
     * @param materialName the name of the material to check in the {@code minecraft:<id>} format
     * @return true if the material name corresponds to an item, false otherwise
     */
    boolean isItem(String materialName);

    /**
     * Checks if the provided material name represents a block material.
     *
     * <p>The material names are in the Mojang namespaced format (e.g., {@code minecraft:diamond_block}).</p>
     *
     * @param materialName the name of the material to check in the {@code minecraft:<id>} format
     * @return true if the material name corresponds to a block, false otherwise
     */
    boolean isBlock(String materialName);
}

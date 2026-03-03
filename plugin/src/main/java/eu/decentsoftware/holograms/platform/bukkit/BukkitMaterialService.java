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

package eu.decentsoftware.holograms.platform.bukkit;

import com.cryptomorin.xseries.XMaterial;

import java.util.Locale;
import java.util.Optional;

/**
 * Bukkit-specific service responsible for translating material identifiers
 * (including legacy Bukkit names) into canonical Mojang registry IDs.
 *
 * <p>This class acts as a compatibility bridge between legacy Bukkit material
 * naming conventions (e.g. {@code INK_SACK:4}) and the modern Mojang
 * namespaced format (e.g. {@code minecraft:lapis_lazuli}).</p>
 *
 * <p>The returned identifiers are always in the {@code minecraft:<id>} format
 * and are suitable for use as canonical, platform-agnostic material keys
 * inside the core system.</p>
 *
 * <p>This service is intended to be used only within Bukkit-based platform adapters.
 * The core module should operate exclusively on Mojang namespaced IDs.</p>
 *
 * @author d0by
 * @since 2.10.0
 */
public class BukkitMaterialService {

    private static final String MINECRAFT_NAMESPACE = "minecraft:";

    /**
     * Converts a Bukkit material identifier into a Mojang namespaced key.
     *
     * <p>The input may represent:</p>
     * <ul>
     *     <li>A modern Bukkit material name (e.g. {@code DIAMOND_BLOCK})</li>
     *     <li>A legacy material name (e.g. {@code WATCH})</li>
     *     <li>A legacy material with data value (e.g. {@code INK_SACK:4})</li>
     * </ul>
     *
     * <p>Legacy names and data values are resolved to their modern equivalents.</p>
     *
     * <p><b>Examples:</b></p>
     * <pre>
     * DIAMOND_BLOCK  -> minecraft:diamond_block
     * INK_SACK:4     -> minecraft:lapis_lazuli
     * WATCH          -> minecraft:clock
     * </pre>
     *
     * @param materialName the Bukkit material identifier to convert; not {@code null}
     * @return an {@link Optional} containing the canonical Mojang namespaced ID if the material could be resolved; otherwise {@link Optional#empty()}
     * @since 2.10.0
     */
    public Optional<String> toMojangNamespacedKey(String materialName) {
        return XMaterial.matchXMaterial(materialName)
                .map(xMaterial -> MINECRAFT_NAMESPACE + xMaterial.name().toLowerCase(Locale.ROOT));
    }
}

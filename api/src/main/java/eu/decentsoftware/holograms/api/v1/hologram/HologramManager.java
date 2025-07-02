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

package eu.decentsoftware.holograms.api.v1.hologram;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

/**
 * This manager provides operations for creating and managing holograms.
 * <p>
 * Holograms created with this manager are isolated to the API instance that created them.
 * They will not be available from other API instances, even if they are on the same server.
 * <p>
 * Holograms created with this manager will be automatically destroyed when the API instance is stopped.
 * This can happen when the server is stopped or when the plugin that created the API instance is disabled.
 *
 * @author d0by
 * @see Hologram
 * @since 2.10.0
 */
public interface HologramManager {

    /**
     * Creates a new instance of {@link HologramBuilder} for constructing a hologram.
     *
     * @return A new instance of {@link HologramBuilder}.
     * @see HologramBuilder
     * @since 2.10.0
     */
    @NotNull
    HologramBuilder newHologram();

    /**
     * Get an unmodifiable collection of all holograms, created by this API instance.
     *
     * @return The unmodifiable collection of holograms.
     * @see Hologram
     * @since 2.10.0
     */
    @NotNull
    @Unmodifiable
    Collection<Hologram> getHolograms();

}

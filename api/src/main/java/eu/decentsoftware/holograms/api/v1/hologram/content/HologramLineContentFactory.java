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

package eu.decentsoftware.holograms.api.v1.hologram.content;

import eu.decentsoftware.holograms.api.v1.DecentEntityType;
import eu.decentsoftware.holograms.api.v1.platform.DecentItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This factory provides methods for creating various types of hologram line content.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface HologramLineContentFactory {

    /**
     * Creates a new text hologram line content with the specified text.
     *
     * @param text The text to display in the hologram line.
     * @return A new instance of {@link TextHologramLineContent}.
     */
    TextHologramLineContent createTextContent(@NotNull String text);

    /**
     * Creates a new icon hologram line content with the specified item stack.
     *
     * @param itemStack The item stack to display in the hologram line.
     * @return A new instance of {@link IconHologramLineContent}.
     */
    IconHologramLineContent createIconContent(@NotNull DecentItemStack itemStack);

    /**
     * Creates a new head hologram line content with the specified item stack.
     *
     * @param itemStack The item stack representing the head to display in the hologram line.
     * @return A new instance of {@link HeadHologramLineContent}.
     */
    HeadHologramLineContent createHeadContent(@NotNull DecentItemStack itemStack);

    /**
     * Creates a new small head hologram line content with the specified item stack.
     *
     * @param itemStack The item stack representing the small head to display in the hologram line.
     * @return A new instance of {@link SmallHeadHologramLineContent}.
     */
    SmallHeadHologramLineContent createSmallHeadContent(@NotNull DecentItemStack itemStack);

    /**
     * Creates a new entity hologram line content with the specified entity type.
     *
     * @param entityType The type of entity to display in the hologram line.
     * @return A new instance of {@link EntityHologramLineContent}.
     */
    EntityHologramLineContent createEntityContent(@NotNull DecentEntityType entityType);

}

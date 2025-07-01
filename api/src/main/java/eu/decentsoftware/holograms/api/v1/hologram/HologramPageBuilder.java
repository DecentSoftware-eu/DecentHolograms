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

import eu.decentsoftware.holograms.api.v1.DecentEntityType;
import eu.decentsoftware.holograms.api.v1.hologram.content.HologramLineContent;
import eu.decentsoftware.holograms.api.v1.platform.GenericItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Builder for configuring hologram pages.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface HologramPageBuilder {

    /**
     * Adds a new line with custom content to the page.
     *
     * @param content The content to add to the line.
     * @return A builder for configuring the new line.
     * @since 2.10.0
     */
    @NotNull
    HologramLineBuilder addLine(@NotNull HologramLineContent content);

    /**
     * Adds a new text line to the page.
     *
     * @param text The text content to add.
     * @return A builder for configuring the new line.
     * @since 2.10.0
     */
    @NotNull
    HologramLineBuilder addTextLine(@NotNull String text);

    /**
     * Adds a new line displaying an item icon.
     *
     * @param itemStack The item to display.
     * @return A builder for configuring the new line.
     * @since 2.10.0
     */
    @NotNull
    HologramLineBuilder addIconLine(@NotNull GenericItemStack itemStack);

    /**
     * Adds a new line displaying a player head.
     *
     * @param itemStack The head item to display.
     * @return A builder for configuring the new line.
     * @since 2.10.0
     */
    @NotNull
    HologramLineBuilder addHeadLine(@NotNull GenericItemStack itemStack);

    /**
     * Adds a new line displaying a small player head.
     *
     * @param itemStack The head item to display.
     * @return A builder for configuring the new line.
     * @since 2.10.0
     */
    @NotNull
    HologramLineBuilder addSmallHeadLine(@NotNull GenericItemStack itemStack);

    /**
     * Adds a new line displaying an entity.
     *
     * @param entityType The type of entity to display.
     * @return A builder for configuring the new line.
     * @since 2.10.0
     */
    @NotNull
    HologramLineBuilder addEntityLine(@NotNull DecentEntityType entityType);

    /**
     * Returns to the parent hologram builder.
     *
     * @return The parent hologram builder.
     * @since 2.10.0
     */
    @NotNull
    HologramBuilder and();

}
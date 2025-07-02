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
import eu.decentsoftware.holograms.api.v1.platform.DecentItemStack;
import org.jetbrains.annotations.Contract;
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
     * @return A builder for configuring the new line.
     * @since 2.10.0
     */
    @NotNull
    HologramLineBuilder withLine();

    /**
     * Adds a new line with custom content to the page.
     *
     * @param content The content to add to the line.
     * @return This builder instance.
     * @see HologramLineContent
     * @since 2.10.0
     */
    @NotNull
    @Contract("_ -> this")
    HologramPageBuilder withLine(@NotNull HologramLineContent content);

    /**
     * Adds a new text line to the page.
     *
     * @param text The text content to add.
     * @return This builder instance.
     * @since 2.10.0
     */
    @NotNull
    @Contract("_ -> this")
    HologramPageBuilder withTextLine(@NotNull String text);

    /**
     * Adds a new line displaying an item icon.
     *
     * @param itemStack The item to display.
     * @return This builder instance.
     * @see DecentItemStack
     * @since 2.10.0
     */
    @NotNull
    @Contract("_ -> this")
    HologramPageBuilder withIconLine(@NotNull DecentItemStack itemStack);

    /**
     * Adds a new line displaying a player head.
     *
     * @param itemStack The head item to display.
     * @return This builder instance.
     * @see DecentItemStack
     * @since 2.10.0
     */
    @NotNull
    @Contract("_ -> this")
    HologramPageBuilder withHeadLine(@NotNull DecentItemStack itemStack);

    /**
     * Adds a new line displaying a small player head.
     *
     * @param itemStack The head item to display.
     * @return This builder instance.
     * @see DecentItemStack
     * @since 2.10.0
     */
    @NotNull
    @Contract("_ -> this")
    HologramPageBuilder withSmallHeadLine(@NotNull DecentItemStack itemStack);

    /**
     * Adds a new line displaying an entity.
     *
     * @param entityType The type of entity to display.
     * @return This builder instance.
     * @see DecentEntityType
     * @since 2.10.0
     */
    @NotNull
    @Contract("_ -> this")
    HologramPageBuilder withEntityLine(@NotNull DecentEntityType entityType);

    /**
     * Returns to the parent hologram builder.
     *
     * @return The parent hologram builder.
     * @since 2.10.0
     */
    @NotNull
    HologramBuilder and();

}
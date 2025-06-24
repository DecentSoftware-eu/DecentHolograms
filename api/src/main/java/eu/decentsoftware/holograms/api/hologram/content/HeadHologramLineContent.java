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

package eu.decentsoftware.holograms.api.hologram.content;

import eu.decentsoftware.holograms.api.platform.GenericItemStack;
import org.jetbrains.annotations.NotNull;

public interface HeadHologramLineContent extends HologramLineContent {

    /**
     * Get the {@link GenericItemStack} of the line. This is the item that will be displayed
     * as a head in the hologram.
     *
     * @return The {@link GenericItemStack} of the line.
     * @since 2.10.0
     */
    @NotNull
    GenericItemStack getItemStack();

    /**
     * Set the {@link GenericItemStack} of the line. This is the item that will be displayed
     * as a head in the hologram.
     * <p>
     * This method also updates the line accordingly.
     *
     * @param itemStack The {@link GenericItemStack} of the line.
     * @since 2.10.0
     */
    void setItemStack(@NotNull GenericItemStack itemStack);

}

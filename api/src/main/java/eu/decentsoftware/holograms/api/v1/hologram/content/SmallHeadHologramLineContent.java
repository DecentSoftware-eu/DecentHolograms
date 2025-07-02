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

import eu.decentsoftware.holograms.api.v1.platform.DecentItemStack;
import org.jetbrains.annotations.NotNull;

public interface SmallHeadHologramLineContent extends HologramLineContent {

    /**
     * Get the {@link DecentItemStack} of the line. This is the item that will be displayed
     * as a small head in the hologram.
     *
     * @return The {@link DecentItemStack} of the line.
     * @since 2.10.0
     */
    @NotNull
    DecentItemStack getItemStack();
}

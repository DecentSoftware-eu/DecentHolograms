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

package eu.decentsoftware.holograms.api.hologram.line;

import eu.decentsoftware.holograms.api.hologram.HologramPage;
import eu.decentsoftware.holograms.api.location.DecentOffsets;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a hologram line. A line is a collection of components
 * that can be moved in the world. A line can be added to a {@link HologramPage}.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface HologramLine {

    /**
     * Get the height of this line in blocks.
     *
     * @return The height of this line.
     * @since 2.10.0
     */
    double getHeight();

    /**
     * Set the height of this hologram line. This method also updates the line accordingly.
     *
     * @param height The height to set for this hologram line in blocks.
     * @since 2.10.0
     */
    void setHeight(double height);

    /**
     * Get the offsets of this line.
     * <p>
     * Offsets are used to adjust the position of the line relative to its base position.
     *
     * @return The offsets of this line.
     * @since 2.10.0
     */
    @NotNull
    DecentOffsets getOffsets();

    /**
     * Set the offsets of this line. This method also updates the line accordingly.
     * <p>
     * Offsets are used to adjust the position of the line relative to its base position.
     *
     * @param offsets The offsets to set for this hologram line.
     * @since 2.10.0
     */
    void setOffsets(@NotNull DecentOffsets offsets);

}

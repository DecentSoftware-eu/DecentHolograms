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
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * This class represents a hologram page. A page is a collection of lines.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface HologramPage {

    /**
     * Get the line at the specified index.
     *
     * @param index The index of the line to get.
     * @return The line at the specified index or null if the index is out of bounds.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
     * @since 2.10.0
     */
    @Nullable
    HologramLine getLine(int index);

    /**
     * Remove the line at the specified index.
     *
     * @param index The index of the line to remove.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
     * @since 2.10.0
     */
    void removeLine(int index);

    /**
     * Add a line to the end of this page.
     *
     * @param line The line to add.
     * @since 2.10.0
     */
    void appendLine(@NotNull HologramLine line);

    /**
     * Add a line to the end of this page.
     *
     * @param index The index to add the line at.
     * @param line  The line to add.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
     * @since 2.10.0
     */
    void insertLine(int index, @NotNull HologramLine line);

    /**
     * Retrieves an unmodifiable list of all lines present in this hologram page.
     *
     * @return An unmodifiable list of {@link HologramLine} objects representing the lines in this page.
     * @since 2.10.0
     */
    @NotNull
    @Unmodifiable
    List<HologramLine> getLines();

    /**
     * Get the current number of lines.
     *
     * @return The current number of lines.
     * @since 2.10.0
     */
    int size();

    /**
     * Remove all lines from this page.
     *
     * @since 2.10.0
     */
    void clearLines();

    /**
     * Get the height of this page.
     * This is the combined height of all the lines on this page.
     *
     * @return The combined height of this page.
     * @since 2.10.0
     */
    double getHeight();

}

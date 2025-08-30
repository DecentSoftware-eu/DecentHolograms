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

package eu.decentsoftware.holograms.nms.api.display.renderer;

import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import org.bukkit.entity.Player;

public interface NmsDisplayRenderer<T> {

    /**
     * Displays the display content to the specified player at the given position.
     *
     * @param player The player who will see the display.
     * @param data   The display part data containing the position and content to display.
     */
    void display(Player player, NmsHologramPartData<T> data);

    /**
     * Updates the properties of a display for the specified player.
     * This method can be used to modify attributes such as scale, rotation, or other property-specific details.
     *
     * @param player The player who sees the display.
     * @param data   The display part data containing the updated properties.
     */
    void updateProperties(Player player, NmsHologramPartData<T> data);

    /**
     * Updates the content of an already displayed display for the given player.
     * This can be used to modify text, change an item, or update an entity.
     *
     * @param player The player who sees the display.
     * @param data   The display part data containing the new content.
     */
    void updateContent(Player player, NmsHologramPartData<T> data);

    /**
     * Moves the display to a new position for the specified player.
     *
     * @param player The player who sees the display.
     * @param data   The display part data containing the new position.
     */
    void move(Player player, NmsHologramPartData<T> data);

    /**
     * Hides the display from the specified player.
     *
     * @param player The player from whom the display should be hidden.
     */
    void hide(Player player);

}

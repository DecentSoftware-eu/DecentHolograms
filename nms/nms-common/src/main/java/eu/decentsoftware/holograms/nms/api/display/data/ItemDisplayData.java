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

package eu.decentsoftware.holograms.nms.api.display.data;

import org.bukkit.inventory.ItemStack;

public class ItemDisplayData extends DisplayData {

    private ItemStack displayedItem;
    private ItemDisplayType displayType;
    private DisplayColor glowColor;

    public ItemStack getDisplayedItem() {
        return displayedItem;
    }

    public void setDisplayedItem(ItemStack displayedItem) {
        this.displayedItem = displayedItem;
    }

    public ItemDisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(ItemDisplayType displayType) {
        this.displayType = displayType;
    }

    public DisplayColor getGlowColor() {
        return glowColor;
    }

    public void setGlowColor(DisplayColor glowColor) {
        this.glowColor = glowColor;
    }
}

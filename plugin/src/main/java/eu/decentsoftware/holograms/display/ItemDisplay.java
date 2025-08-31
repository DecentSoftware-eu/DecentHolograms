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

package eu.decentsoftware.holograms.display;

import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.StaticDisplayAttribute;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayType;

public class ItemDisplay extends DisplayBase {

    private HologramItem displayedItem;
    private DisplayAttribute<ItemDisplayType> displayTypeAttribute;
    private DisplayAttribute<DisplayColor> glowColorAttribute;

    public ItemDisplay(String name, DecentLocation location) {
        super(name, location);
    }

    @Override
    public DisplayType getType() {
        return DisplayType.ITEM;
    }

    public HologramItem getDisplayedItem() {
        return displayedItem;
    }

    public void setDisplayedItem(HologramItem displayedItem) {
        this.displayedItem = displayedItem;
    }

    public ItemDisplayType getDisplayType() {
        return displayTypeAttribute.getValue();
    }

    public void setDisplayType(ItemDisplayType displayType) {
        this.displayTypeAttribute = new StaticDisplayAttribute<>(displayType);
    }

    public DisplayColor getGlowColor() {
        return glowColorAttribute.getValue();
    }

    public void setGlowColor(DisplayColor glowColor) {
        this.glowColorAttribute = new StaticDisplayAttribute<>(glowColor);
    }

    public DisplayAttribute<ItemDisplayType> getDisplayTypeAttribute() {
        return displayTypeAttribute;
    }

    public void setDisplayTypeAttribute(DisplayAttribute<ItemDisplayType> displayTypeAttribute) {
        this.displayTypeAttribute = displayTypeAttribute;
    }

    public DisplayAttribute<DisplayColor> getGlowColorAttribute() {
        return glowColorAttribute;
    }

    public void setGlowColorAttribute(DisplayAttribute<DisplayColor> glowColorAttribute) {
        this.glowColorAttribute = glowColorAttribute;
    }
}

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

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayData;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayType;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsItemDisplayRenderer;
import org.bukkit.entity.Player;

public class ItemDisplay extends DisplayBase {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    private final NmsItemDisplayRenderer renderer;
    private HologramItem displayedItem;
    private ItemDisplayType displayType = ItemDisplayType.NONE;
    private DisplayColor glowColor;

    public ItemDisplay(String name, DecentLocation location) {
        super(name, location);
        this.renderer = DECENT_HOLOGRAMS.getNmsAdapter().getDisplayRendererFactory().createItemDisplayRenderer();
    }

    @Override
    public ItemDisplayData createDisplayData(Player player) {
        ItemDisplayData data = new ItemDisplayData();
        data.setDisplayedItem(displayedItem.parse(player));
        if (displayType != null) {
            data.setDisplayType(displayType);
        }
        data.setGlowColor(glowColor);

        if (translation != null) {
            data.setTranslation(translation);
        }
        if (scale != null) {
            data.setScale(scale);
        }
        if (billboardConstraints != null) {
            data.setBillboardConstraints(billboardConstraints);
        }
        if (brightnessOverride != null) {
            data.setBrightnessOverride(brightnessOverride);
        }
        data.setViewRange(viewRange);
        data.setShadowRadius(shadowRadius);
        data.setShadowStrength(shadowStrength);
        return data;
    }

    @Override
    public NmsDisplayRenderer<ItemDisplayData> getDisplayRenderer() {
        return renderer;
    }

    public HologramItem getDisplayedItem() {
        return displayedItem;
    }

    public void setDisplayedItem(HologramItem displayedItem) {
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

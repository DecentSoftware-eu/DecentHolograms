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
import eu.decentsoftware.holograms.nms.api.display.data.BlockDisplayData;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsBlockDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsDisplayRenderer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BlockDisplay extends DisplayBase<BlockDisplayData> {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    private final NmsBlockDisplayRenderer renderer;
    private Material material;
    private DisplayColor glowColor;

    public BlockDisplay(String name, DecentLocation location) {
        super(name, location);
        this.renderer = DECENT_HOLOGRAMS.getNmsAdapter().getDisplayRendererFactory().createBlockDisplayRenderer();
    }

    @Override
    public BlockDisplayData createDisplayData(Player player) {
        BlockDisplayData data = new BlockDisplayData();
        data.setMaterial(material);
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
    public NmsDisplayRenderer<BlockDisplayData> getDisplayRenderer() {
        return renderer;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public DisplayColor getGlowColor() {
        return glowColor;
    }

    public void setGlowColor(DisplayColor glowColor) {
        this.glowColor = glowColor;
    }
}

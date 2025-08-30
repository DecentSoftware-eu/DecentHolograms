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
import eu.decentsoftware.holograms.display.attributes.DisplayAttribute;
import eu.decentsoftware.holograms.display.attributes.FixedDisplayAttribute;
import eu.decentsoftware.holograms.nms.api.display.data.BlockDisplayData;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsBlockDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsDisplayRenderer;
import org.bukkit.Material;

public class BlockDisplay extends DisplayBase<BlockDisplayData> {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    private final NmsBlockDisplayRenderer renderer;
    private Material material;
    private DisplayAttribute<DisplayColor> glowColorAttribute;

    public BlockDisplay(String name, DecentLocation location) {
        super(name, location);
        this.renderer = DECENT_HOLOGRAMS.getNmsAdapter().getDisplayRendererFactory().createBlockDisplayRenderer();
    }

    @Override
    public NmsDisplayRenderer<BlockDisplayData> getDisplayRenderer() {
        return renderer;
    }

    @Override
    public DisplayType getType() {
        return DisplayType.BLOCK;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public DisplayColor getGlowColor() {
        return glowColorAttribute.getValue();
    }

    public void setGlowColor(DisplayColor glowColor) {
        this.glowColorAttribute = new FixedDisplayAttribute<>(glowColor);
    }

    public DisplayAttribute<DisplayColor> getGlowColorAttribute() {
        return glowColorAttribute;
    }

    public void setGlowColorAttribute(DisplayAttribute<DisplayColor> glowColorAttribute) {
        this.glowColorAttribute = glowColorAttribute;
    }
}

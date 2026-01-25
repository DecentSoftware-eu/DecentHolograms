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

import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.location.DecentLocation;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlockDisplay extends DisplayBase {

    private Material material;
    private DisplayAttribute<DisplayColor> glowColorAttribute;

    public BlockDisplay(String name, DecentLocation location, DisplaySettings settings) {
        super(name, location, settings);
    }

    @Override
    public DisplayType getType() {
        return DisplayType.BLOCK;
    }

    @Override
    public Collection<DisplayAttribute<?>> getAttributes() {
        return Collections.unmodifiableList(Arrays.asList(
                translationAttribute,
                scaleAttribute,
                billboardAttribute,
                brightnessAttribute,
                shadowRadiusAttribute,
                shadowStrengthAttribute,

                glowColorAttribute
        ));
    }

    @Override
    public void setAttributes(List<DisplayAttribute<?>> attributes) {
        super.setAttributes(attributes);

        for (DisplayAttribute<?> attribute : attributes) {
            switch (attribute.getName()) {
                case "glow-color":
                    this.glowColorAttribute = (DisplayAttribute<DisplayColor>) attribute;
                    break;
            }
        }
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public DisplayAttribute<DisplayColor> getGlowColorAttribute() {
        return glowColorAttribute;
    }

    public void setGlowColorAttribute(DisplayAttribute<DisplayColor> glowColorAttribute) {
        this.glowColorAttribute = glowColorAttribute;
    }
}

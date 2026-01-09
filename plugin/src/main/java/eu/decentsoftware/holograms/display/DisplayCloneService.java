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

import java.util.Objects;

public class DisplayCloneService {

    public DisplayBase cloneDisplay(DisplayBase display, String newName) {
        Objects.requireNonNull(display, "display cannot be null");
        Objects.requireNonNull(newName, "newName cannot be null");

        DisplayType type = display.getType();
        switch (type) {
            case TEXT:
                return cloneTextDisplay((TextDisplay) display, newName);
            case BLOCK:
                return cloneBlockDisplay((BlockDisplay) display, newName);
            case ITEM:
                return cloneItemDisplay((ItemDisplay) display, newName);
        }
        throw new IllegalArgumentException("Unknown display type: " + type.name());
    }

    private TextDisplay cloneTextDisplay(TextDisplay display, String newName) {
        DisplaySettings settings = cloneSettings(display);
        TextDisplay clone = new TextDisplay(newName, display.getLocation(), settings);
        clone.setPages(display.getPages());
        setCommonAttributes(display, clone);
        clone.setLineWidthAttribute(display.getLineWidthAttribute());
        clone.setBackgroundColorAttribute(display.getBackgroundColorAttribute());
        clone.setTextOpacityAttribute(display.getTextOpacityAttribute());
        clone.setTextShadowAttribute(display.getTextShadowAttribute());
        clone.setSeeThroughAttribute(display.getSeeThroughAttribute());
        clone.setAlignmentAttribute(display.getAlignmentAttribute());
        return clone;
    }

    private BlockDisplay cloneBlockDisplay(BlockDisplay display, String newName) {
        DisplaySettings settings = cloneSettings(display);
        BlockDisplay clone = new BlockDisplay(newName, display.getLocation(), settings);
        clone.setMaterial(display.getMaterial());
        setCommonAttributes(display, clone);
        clone.setGlowColorAttribute(display.getGlowColorAttribute());
        return clone;
    }

    private ItemDisplay cloneItemDisplay(ItemDisplay display, String newName) {
        DisplaySettings settings = cloneSettings(display);
        ItemDisplay clone = new ItemDisplay(newName, display.getLocation(), settings);
        clone.setDisplayedItem(display.getDisplayedItem());
        setCommonAttributes(display, clone);
        clone.setDisplayTypeAttribute(display.getDisplayTypeAttribute());
        clone.setGlowColorAttribute(display.getGlowColorAttribute());
        return clone;
    }

    private void setCommonAttributes(DisplayBase source, DisplayBase target) {
        target.setTranslationAttribute(source.getTranslationAttribute());
        target.setScaleAttribute(source.getScaleAttribute());
        target.setBillboardAttribute(source.getBillboardAttribute());
        target.setBrightnessAttribute(source.getBrightnessAttribute());
        target.setShadowRadiusAttribute(source.getShadowRadiusAttribute());
        target.setShadowStrengthAttribute(source.getShadowStrengthAttribute());
    }

    private DisplaySettings cloneSettings(DisplayBase source) {
        DisplaySettings sourceSettings = source.getSettings();
        DisplaySettings settings = new DisplaySettings();
        settings.setEnabled(sourceSettings.isEnabled());
        settings.setDisplayRange(sourceSettings.getDisplayRange());
        settings.setUpdateInterval(sourceSettings.getUpdateInterval());
        return settings;
    }
}

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
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Collection;
import java.util.List;

public abstract class DisplayBase {

    protected final String name;
    protected DecentLocation location;
    protected DisplaySettings settings;
    protected DisplayAttribute<DisplayVector3f> translationAttribute;
    protected DisplayAttribute<DisplayVector3f> scaleAttribute;
    protected DisplayAttribute<DisplayBillboardConstraints> billboardAttribute;
    protected DisplayAttribute<DisplayBrightness> brightnessAttribute;
    protected DisplayAttribute<Float> shadowRadiusAttribute;
    protected DisplayAttribute<Float> shadowStrengthAttribute;

    protected DisplayBase(String name, DecentLocation location, DisplaySettings settings) {
        this.name = name;
        this.location = location;
        this.settings = settings;
    }

    public abstract DisplayType getType();

    public abstract Collection<DisplayAttribute<?>> getAttributes();

    // TODO: rework this
    @MustBeInvokedByOverriders
    public void setAttributes(List<DisplayAttribute<?>> attributes) {
        for (DisplayAttribute<?> attribute : attributes) {
            switch (attribute.getName()) {
                case "translation":
                    this.translationAttribute = (DisplayAttribute<DisplayVector3f>) attribute;
                    break;
                case "scale":
                    this.scaleAttribute = (DisplayAttribute<DisplayVector3f>) attribute;
                    break;
                case "billboard":
                    this.billboardAttribute = (DisplayAttribute<DisplayBillboardConstraints>) attribute;
                    break;
                case "brightness":
                    this.brightnessAttribute = (DisplayAttribute<DisplayBrightness>) attribute;
                    break;
                case "shadow-radius":
                    this.shadowRadiusAttribute = (DisplayAttribute<Float>) attribute;
                    break;
                case "shadow-strength":
                    this.shadowStrengthAttribute = (DisplayAttribute<Float>) attribute;
                    break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public DecentLocation getLocation() {
        return location;
    }

    public void setLocation(DecentLocation location) {
        this.location = location;
    }

    public DisplaySettings getSettings() {
        return settings;
    }

    public void setSettings(DisplaySettings settings) {
        this.settings = settings;
    }

    public DisplayAttribute<DisplayVector3f> getTranslationAttribute() {
        return translationAttribute;
    }

    public void setTranslationAttribute(DisplayAttribute<DisplayVector3f> translationAttribute) {
        this.translationAttribute = translationAttribute;
    }

    public DisplayAttribute<DisplayVector3f> getScaleAttribute() {
        return scaleAttribute;
    }

    public void setScaleAttribute(DisplayAttribute<DisplayVector3f> scaleAttribute) {
        this.scaleAttribute = scaleAttribute;
    }

    public DisplayAttribute<DisplayBillboardConstraints> getBillboardAttribute() {
        return billboardAttribute;
    }

    public void setBillboardAttribute(DisplayAttribute<DisplayBillboardConstraints> billboardAttribute) {
        this.billboardAttribute = billboardAttribute;
    }

    public DisplayAttribute<DisplayBrightness> getBrightnessAttribute() {
        return brightnessAttribute;
    }

    public void setBrightnessAttribute(DisplayAttribute<DisplayBrightness> brightnessAttribute) {
        this.brightnessAttribute = brightnessAttribute;
    }

    public DisplayAttribute<Float> getShadowRadiusAttribute() {
        return shadowRadiusAttribute;
    }

    public void setShadowRadiusAttribute(DisplayAttribute<Float> shadowRadiusAttribute) {
        this.shadowRadiusAttribute = shadowRadiusAttribute;
    }

    public DisplayAttribute<Float> getShadowStrengthAttribute() {
        return shadowStrengthAttribute;
    }

    public void setShadowStrengthAttribute(DisplayAttribute<Float> shadowStrengthAttribute) {
        this.shadowStrengthAttribute = shadowStrengthAttribute;
    }
}

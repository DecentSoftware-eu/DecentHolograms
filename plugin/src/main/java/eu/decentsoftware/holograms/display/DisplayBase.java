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
import eu.decentsoftware.holograms.display.attribute.StaticDisplayAttribute;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;

public abstract class DisplayBase {

    protected final String name;
    protected DecentLocation location;
    protected DisplayAttribute<DisplayVector3f> translationAttribute;
    protected DisplayAttribute<DisplayVector3f> scaleAttribute;
    protected DisplayAttribute<DisplayBillboardConstraints> billboardAttribute;
    protected DisplayAttribute<DisplayBrightness> brightnessAttribute;
    protected DisplayAttribute<Float> shadowRadiusAttribute;
    protected DisplayAttribute<Float> shadowStrengthAttribute;

    protected DisplayBase(String name, DecentLocation location) {
        this.name = name;
        this.location = location;
    }

    public abstract DisplayType getType();

    public String getName() {
        return name;
    }

    public DecentLocation getLocation() {
        return location;
    }

    public void setLocation(DecentLocation location) {
        this.location = location;
    }

    public DisplayVector3f getTranslation() {
        return translationAttribute.getValue();
    }

    public void setTranslation(DisplayVector3f translation) {
        this.translationAttribute = new StaticDisplayAttribute<>(translation);
    }

    public DisplayVector3f getScale() {
        return scaleAttribute.getValue();
    }

    public void setScale(DisplayVector3f scale) {
        this.scaleAttribute = new StaticDisplayAttribute<>(scale);
    }

    public DisplayBillboardConstraints getBillboardConstraints() {
        return billboardAttribute.getValue();
    }

    public void setBillboardConstraints(DisplayBillboardConstraints billboardConstraints) {
        this.billboardAttribute = new StaticDisplayAttribute<>(billboardConstraints);
    }

    public DisplayBrightness getBrightnessOverride() {
        return brightnessAttribute.getValue();
    }

    public void setBrightnessOverride(DisplayBrightness brightnessOverride) {
        this.brightnessAttribute = new StaticDisplayAttribute<>(brightnessOverride);
    }

    public float getShadowRadius() {
        return shadowRadiusAttribute.getValue();
    }

    public void setShadowRadius(float shadowRadius) {
        this.shadowRadiusAttribute = new StaticDisplayAttribute<>(shadowRadius);
    }

    public float getShadowStrength() {
        return shadowStrengthAttribute.getValue();
    }

    public void setShadowStrength(float shadowStrength) {
        this.shadowStrengthAttribute = new StaticDisplayAttribute<>(shadowStrength);
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

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

public class DisplayData {

    private NmsDisplayAttribute<DisplayVector3f> translation;
    private NmsDisplayAttribute<DisplayVector3f> scale;
    private NmsDisplayAttribute<DisplayBillboardConstraints> billboardConstraints;
    private NmsDisplayAttribute<DisplayBrightness> brightnessOverride;
    private NmsDisplayAttribute<Float> shadowRadius;
    private NmsDisplayAttribute<Float> shadowStrength;

    public NmsDisplayAttribute<DisplayVector3f> getTranslation() {
        return translation;
    }

    public void setTranslation(NmsDisplayAttribute<DisplayVector3f> translation) {
        this.translation = translation;
    }

    public NmsDisplayAttribute<DisplayVector3f> getScale() {
        return scale;
    }

    public void setScale(NmsDisplayAttribute<DisplayVector3f> scale) {
        this.scale = scale;
    }

    public NmsDisplayAttribute<DisplayBillboardConstraints> getBillboardConstraints() {
        return billboardConstraints;
    }

    public void setBillboardConstraints(NmsDisplayAttribute<DisplayBillboardConstraints> billboardConstraints) {
        this.billboardConstraints = billboardConstraints;
    }

    public NmsDisplayAttribute<DisplayBrightness> getBrightnessOverride() {
        return brightnessOverride;
    }

    public void setBrightnessOverride(NmsDisplayAttribute<DisplayBrightness> brightnessOverride) {
        this.brightnessOverride = brightnessOverride;
    }

    public NmsDisplayAttribute<Float> getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadiusAttribute(NmsDisplayAttribute<Float> shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public NmsDisplayAttribute<Float> getShadowStrength() {
        return shadowStrength;
    }

    public void setShadowStrengthAttribute(NmsDisplayAttribute<Float> shadowStrength) {
        this.shadowStrength = shadowStrength;
    }
}

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

    private DisplayVector3f translation = new DisplayVector3f(0, 0, 0);
    private DisplayVector3f scale = new DisplayVector3f(1, 1, 1);
    private DisplayBillboardConstraints billboardConstraints = DisplayBillboardConstraints.CENTER;
    private DisplayBrightness brightnessOverride;
    private float viewRange = 1.0f;
    private float shadowRadius = 0.0f;
    private float shadowStrength = 1.0f;

    public DisplayVector3f getTranslation() {
        return translation;
    }

    public void setTranslation(DisplayVector3f translation) {
        this.translation = translation;
    }

    public DisplayVector3f getScale() {
        return scale;
    }

    public void setScale(DisplayVector3f scale) {
        this.scale = scale;
    }

    public DisplayBillboardConstraints getBillboardConstraints() {
        return billboardConstraints;
    }

    public void setBillboardConstraints(DisplayBillboardConstraints billboardConstraints) {
        this.billboardConstraints = billboardConstraints;
    }

    public DisplayBrightness getBrightnessOverride() {
        return brightnessOverride;
    }

    public void setBrightnessOverride(DisplayBrightness brightnessOverride) {
        this.brightnessOverride = brightnessOverride;
    }

    public float getViewRange() {
        return viewRange;
    }

    public void setViewRange(float viewRange) {
        this.viewRange = viewRange;
    }

    public float getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public float getShadowStrength() {
        return shadowStrength;
    }

    public void setShadowStrength(float shadowStrength) {
        this.shadowStrength = shadowStrength;
    }
}

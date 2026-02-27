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

package eu.decentsoftware.holograms.display.render.state;

import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayAlignment;

import java.util.EnumSet;
import java.util.Set;

public class PresentedRenderState {

    private final String id;
    private final DisplayType displayType;
    private DecentLocation location;
    private DisplayContent<?> content;
    private DisplayBillboardConstraints billboardConstraints;
    private DisplayBrightness brightness;
    private DecentColor glowColor;
    private ItemDisplayType itemDisplayType;
    private Float pitch;
    private DecentVector3f scale;
    private Float shadowRadius;
    private Float shadowStrength;
    private TextDisplayAlignment textAlignment;
    private DecentColor textBackgroundColor;
    private Integer textLineWidth;
    private Integer textOpacity;
    private Boolean textSeeThrough;
    private Boolean textShadow;
    private DecentVector3f translation;
    private Float yaw;

    private final EnumSet<MutableStateField> dirtyFields = EnumSet.noneOf(MutableStateField.class);
    private boolean isNew = true;

    public PresentedRenderState(String id, DisplayType displayType) {
        this.id = id;
        this.displayType = displayType;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * Marks the start of a new frame.
     * Clears dirty tracking from the previous frame.
     */
    public void beginFrame() {
        dirtyFields.clear();
    }

    /**
     * Returns which fields were modified this frame.
     */
    public Set<MutableStateField> getDirtyFields() {
        return EnumSet.copyOf(dirtyFields);
    }

    /**
     * Checks if any fields were modified this frame.
     */
    public boolean hasChanges() {
        return isNew || !dirtyFields.isEmpty();
    }

    private void markDirty(MutableStateField field) {
        dirtyFields.add(field);
    }

    public String getId() {
        return id;
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    public DecentLocation getLocation() {
        return location;
    }

    public void setLocation(DecentLocation location) {
        this.location = location;
        markDirty(MutableStateField.LOCATION);
    }

    public DisplayContent<?> getContent() {
        return content;
    }

    public void setContent(DisplayContent<?> content) {
        this.content = content;
        markDirty(MutableStateField.CONTENT);
    }

    public DisplayBillboardConstraints getBillboardConstraints() {
        return billboardConstraints;
    }

    public void setBillboardConstraints(DisplayBillboardConstraints billboardConstraints) {
        this.billboardConstraints = billboardConstraints;
        markDirty(MutableStateField.METADATA_BILLBOARD_CONSTRAINTS);
    }

    public DisplayBrightness getBrightness() {
        return brightness;
    }

    public void setBrightness(DisplayBrightness brightness) {
        this.brightness = brightness;
        markDirty(MutableStateField.METADATA_BRIGHTNESS);
    }

    public DecentColor getGlowColor() {
        return glowColor;
    }

    public void setGlowColor(DecentColor glowColor) {
        this.glowColor = glowColor;
        markDirty(MutableStateField.METADATA_GLOW_COLOR);
    }

    public ItemDisplayType getItemDisplayType() {
        return itemDisplayType;
    }

    public void setItemDisplayType(ItemDisplayType itemDisplayType) {
        this.itemDisplayType = itemDisplayType;
        markDirty(MutableStateField.METADATA_ITEM_DISPLAY_TYPE);
    }

    public Float getPitch() {
        return pitch;
    }

    public void setPitch(Float pitch) {
        this.pitch = pitch;
        markDirty(MutableStateField.LOCATION);
    }

    public DecentVector3f getScale() {
        return scale;
    }

    public void setScale(DecentVector3f scale) {
        this.scale = scale;
        markDirty(MutableStateField.METADATA_SCALE);
    }

    public Float getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(Float shadowRadius) {
        this.shadowRadius = shadowRadius;
        markDirty(MutableStateField.METADATA_SHADOW_RADIUS);
    }

    public Float getShadowStrength() {
        return shadowStrength;
    }

    public void setShadowStrength(Float shadowStrength) {
        this.shadowStrength = shadowStrength;
        markDirty(MutableStateField.METADATA_SHADOW_STRENGTH);
    }

    public TextDisplayAlignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(TextDisplayAlignment textAlignment) {
        this.textAlignment = textAlignment;
        markDirty(MutableStateField.METADATA_TEXT_DISPLAY_PROPERTIES);
    }

    public DecentColor getTextBackgroundColor() {
        return textBackgroundColor;
    }

    public void setTextBackgroundColor(DecentColor textBackgroundColor) {
        this.textBackgroundColor = textBackgroundColor;
        markDirty(MutableStateField.METADATA_TEXT_BACKGROUND_COLOR);
    }

    public Integer getTextLineWidth() {
        return textLineWidth;
    }

    public void setTextLineWidth(Integer textLineWidth) {
        this.textLineWidth = textLineWidth;
        markDirty(MutableStateField.METADATA_TEXT_LINE_WIDTH);
    }

    public Integer getTextOpacity() {
        return textOpacity;
    }

    public void setTextOpacity(Integer textOpacity) {
        this.textOpacity = textOpacity;
        markDirty(MutableStateField.METADATA_TEXT_DISPLAY_OPACITY);
    }

    public Boolean getTextSeeThrough() {
        return textSeeThrough;
    }

    public void setTextSeeThrough(Boolean textSeeThrough) {
        this.textSeeThrough = textSeeThrough;
        markDirty(MutableStateField.METADATA_TEXT_DISPLAY_PROPERTIES);
    }

    public Boolean getTextShadow() {
        return textShadow;
    }

    public void setTextShadow(Boolean textShadow) {
        this.textShadow = textShadow;
        markDirty(MutableStateField.METADATA_TEXT_DISPLAY_PROPERTIES);
    }

    public DecentVector3f getTranslation() {
        return translation;
    }

    public void setTranslation(DecentVector3f translation) {
        this.translation = translation;
        markDirty(MutableStateField.METADATA_TRANSLATION);
    }

    public Float getYaw() {
        return yaw;
    }

    public void setYaw(Float yaw) {
        this.yaw = yaw;
        markDirty(MutableStateField.LOCATION);
    }
}

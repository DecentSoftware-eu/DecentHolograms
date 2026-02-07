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

import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FinalDisplayRenderState {

    private final String id;
    private boolean visible;
    private DisplayType displayType;
    private DecentLocation location;
    private final Map<MetadataKey<?>, MetadataValue<?>> metadataValues = new LinkedHashMap<>();
    private DisplayContent<?> content;

    public FinalDisplayRenderState(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
    }

    public DecentLocation getLocation() {
        return location;
    }

    public void setLocation(DecentLocation location) {
        this.location = location;
    }

    public void addMetadata(MetadataValue<?> value) {
        metadataValues.put(value.getKey(), value);
    }

    public Map<MetadataKey<?>, MetadataValue<?>> getMetadataValues() {
        return metadataValues;
    }

    @SuppressWarnings("unchecked")
    public <T> MetadataValue<T> getMetadataValue(MetadataKey<T> key) {
        return (MetadataValue<T>) metadataValues.get(key);
    }

    public boolean hasMetadataValue(MetadataKey<?> key) {
        return metadataValues.containsKey(key);
    }

    public DisplayContent<?> getContent() {
        return content;
    }

    public void setContent(DisplayContent<?> content) {
        this.content = content;
    }
}

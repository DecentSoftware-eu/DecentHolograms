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

import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;

import java.util.LinkedHashMap;
import java.util.Map;

public final class LogicalRenderState {

    private final String id;
    private final DisplayType displayType;
    private DecentLocation location;
    private final Map<AttributeKey<?>, CompiledAttributeValue<?>> attributeValues = new LinkedHashMap<>();
    private DisplayContent<?> content;

    public LogicalRenderState(String id, DisplayType displayType) {
        this.id = id;
        this.displayType = displayType;
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
    }

    public <T> void addAttribute(AttributeKey<T> key, CompiledAttributeValue<T> value) {
        attributeValues.put(key, value);
    }

    public Map<AttributeKey<?>, CompiledAttributeValue<?>> getAttributeValues() {
        return attributeValues;
    }

    public void clearAttributes() {
        attributeValues.clear();
    }

    public DisplayContent<?> getContent() {
        return content;
    }

    public void setContent(DisplayContent<?> content) {
        this.content = content;
    }
}

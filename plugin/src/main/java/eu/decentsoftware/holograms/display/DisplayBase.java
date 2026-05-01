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

import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class DisplayBase {

    protected final String name;
    protected DecentLocation location;
    protected DisplaySettings settings;
    protected Map<AttributeKey<?>, DisplayAttribute<?>> attributes = new ConcurrentHashMap<>();

    private final AtomicLong lastLogicalUpdateMs = new AtomicLong(0);
    private volatile boolean configDirty = true;
    private volatile boolean contentDirty = true;

    protected DisplayBase(String name, DecentLocation location, DisplaySettings settings) {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(location, "location cannot be null");
        Objects.requireNonNull(settings, "settings cannot be null");
        if (!name.matches(Common.NAME_REGEX)) {
            throw new IllegalArgumentException("Display name '" + name + "' does not match the regex " + Common.NAME_REGEX + ".");
        }

        this.name = name;
        this.location = location;
        this.settings = settings;
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
        markConfigDirty();
    }

    public DisplaySettings getSettings() {
        return settings;
    }

    public void setSettings(DisplaySettings settings) {
        this.settings = settings;
    }

    public <T> void setAttribute(AttributeKey<T> key, DisplayAttribute<T> attribute) {
        attributes.put(key, attribute);
        markConfigDirty();
    }

    @SuppressWarnings("unchecked")
    public <T> DisplayAttribute<T> getAttribute(AttributeKey<T> key) {
        return (DisplayAttribute<T>) attributes.get(key);
    }

    public boolean hasAttribute(AttributeKey<?> key) {
        return attributes.containsKey(key);
    }

    public void setAttributes(Map<AttributeKey<?>, DisplayAttribute<?>> attributes) {
        this.attributes.clear();
        this.attributes.putAll(attributes);
        markConfigDirty();
    }

    public Map<AttributeKey<?>, DisplayAttribute<?>> getAttributesMap() {
        return Collections.unmodifiableMap(attributes);
    }

    public Collection<DisplayAttribute<?>> getAttributes() {
        return Collections.unmodifiableCollection(attributes.values());
    }

    public long getLastLogicalUpdateMs() {
        return lastLogicalUpdateMs.get();
    }

    public void setLastLogicalUpdateMs(long lastLogicalUpdateMs) {
        this.lastLogicalUpdateMs.set(lastLogicalUpdateMs);
    }

    protected void markContentDirty() {
        contentDirty = true;
    }

    protected void markConfigDirty() {
        configDirty = true;
    }

    public boolean checkContentDirty() {
        if (contentDirty) {
            contentDirty = false;
            return true;
        }
        return false;
    }

    public boolean checkConfigDirty() {
        if (configDirty) {
            configDirty = false;
            return true;
        }
        return false;
    }
}

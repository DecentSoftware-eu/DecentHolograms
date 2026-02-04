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

package eu.decentsoftware.holograms.platform.api.render.intent;

import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;

import java.util.Map;

public class SpawnDisplayRenderIntent implements RenderIntent {

    private final DecentLocation location;
    private final Map<MetadataKey<?>, MetadataValue<?>> metadataValues;
    private final Object content;

    public SpawnDisplayRenderIntent(DecentLocation location, Map<MetadataKey<?>, MetadataValue<?>> metadataValues, Object content) {
        this.location = location;
        this.metadataValues = metadataValues;
        this.content = content;
    }

    public DecentLocation getLocation() {
        return location;
    }

    public Map<MetadataKey<?>, MetadataValue<?>> getMetadataValues() {
        return metadataValues;
    }

    public Object getContent() {
        return content;
    }
}

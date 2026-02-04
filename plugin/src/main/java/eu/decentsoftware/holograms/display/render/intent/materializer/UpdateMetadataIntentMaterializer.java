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

package eu.decentsoftware.holograms.display.render.intent.materializer;

import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.intent.IntentDescriptor;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderState;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateMetadataRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;

public class UpdateMetadataIntentMaterializer implements IntentMaterializer<IntentDescriptor.UpdateMetadata> {

    @Override
    public RenderIntent materialize(IntentDescriptor.UpdateMetadata intentDescriptor, DisplayRenderContext context) {
        DisplayRenderState state = intentDescriptor.getRenderState();
        MetadataKey<?> metadataKey = intentDescriptor.getMetadataKey();
        return createUpdateMetadataIntent(state, metadataKey);
    }

    private <T> UpdateMetadataRenderIntent<T> createUpdateMetadataIntent(DisplayRenderState state, MetadataKey<T> metadataKey) {
        MetadataValue<T> metadataValue = state.getMetadataValue(metadataKey);
        return new UpdateMetadataRenderIntent<>(metadataKey, metadataValue);
    }
}

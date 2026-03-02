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

package eu.decentsoftware.holograms.display.render;

import eu.decentsoftware.holograms.display.render.state.PresentedRenderState;
import eu.decentsoftware.holograms.display.render.state.PresentedRenderStateField;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayProperties;
import eu.decentsoftware.holograms.platform.api.render.intent.DespawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.MoveRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.SpawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateDisplayContentRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateMetadataRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import eu.decentsoftware.holograms.profiler.DecentProfiler;
import eu.decentsoftware.holograms.profiler.Metrics;
import eu.decentsoftware.holograms.profiler.TimerHandle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisplayRenderIntentMaterializer {

    public List<RenderIntent> materializeIntents(PresentedRenderState state) {
        try (TimerHandle ignored = DecentProfiler.getInstance().startTimer(Metrics.RENDER_DIFF)) {
            return materializeIntentsInternal(state);
        }
    }

    private List<RenderIntent> materializeIntentsInternal(PresentedRenderState state) {
        if (state == null) {
            return Collections.singletonList(new DespawnDisplayRenderIntent());
        }

        if (state.isNew()) {
            state.setNew(false);
            return Collections.singletonList(new SpawnDisplayRenderIntent(
                    state.getLocation(),
                    getFullMetadata(state),
                    state.getContent()
            ));
        }

        List<RenderIntent> intentList = new ArrayList<>();
        for (PresentedRenderStateField dirtyField : state.getDirtyFields()) {
            if (dirtyField == PresentedRenderStateField.LOCATION) {
                intentList.add(new MoveRenderIntent(state.getLocation()));
            } else if (dirtyField == PresentedRenderStateField.CONTENT) {
                intentList.add(new UpdateDisplayContentRenderIntent(state.getContent()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_BILLBOARD_CONSTRAINTS) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.BILLBOARD_CONSTRAINTS, state.getBillboardConstraints()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_BRIGHTNESS) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.BRIGHTNESS, state.getBrightness()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_GLOW_COLOR) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.GLOW_COLOR_OVERRIDE, state.getGlowColor()));
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.GLOWING, state.getGlowColor() != null));
            } else if (dirtyField == PresentedRenderStateField.METADATA_ITEM_DISPLAY_TYPE) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.ITEM_DISPLAY_TYPE, state.getItemDisplayType()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_SCALE) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.SCALE, state.getScale()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_SHADOW_RADIUS) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.SHADOW_RADIUS, state.getShadowRadius()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_SHADOW_STRENGTH) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.SHADOW_STRENGTH, state.getShadowStrength()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_TEXT_DISPLAY_PROPERTIES) {
                TextDisplayProperties properties = new TextDisplayProperties();
                properties.setAlignment(state.getTextAlignment());
                properties.setSeeThrough(state.getTextSeeThrough());
                properties.setHasShadow(state.getTextShadow());
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.TEXT_DISPLAY_PROPERTIES, properties));
            } else if (dirtyField == PresentedRenderStateField.METADATA_TEXT_DISPLAY_OPACITY) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.TEXT_DISPLAY_OPACITY, state.getTextOpacity()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_TEXT_BACKGROUND_COLOR) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.TEXT_DISPLAY_BACKGROUND, state.getTextBackgroundColor()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_TEXT_LINE_WIDTH) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.TEXT_LINE_WIDTH, state.getTextLineWidth()));
            } else if (dirtyField == PresentedRenderStateField.METADATA_TRANSLATION) {
                intentList.add(createMetadataIntent(BuiltInMetadataKeys.TRANSLATION, state.getTranslation()));
            }
        }

        return intentList;
    }

    private <T> UpdateMetadataRenderIntent<T> createMetadataIntent(MetadataKey<T> key, T value) {
        return new UpdateMetadataRenderIntent<>(key, key.createValue(value));
    }

    private List<MetadataValue<?>> getFullMetadata(PresentedRenderState state) {
        List<MetadataValue<?>> metadata = new ArrayList<>();
        if (state.getBillboardConstraints() != null) {
            metadata.add(BuiltInMetadataKeys.BILLBOARD_CONSTRAINTS.createValue(state.getBillboardConstraints()));
        }
        if (state.getBrightness() != null) {
            metadata.add(BuiltInMetadataKeys.BRIGHTNESS.createValue(state.getBrightness()));
        }
        if (state.getGlowColor() != null) {
            metadata.add(BuiltInMetadataKeys.GLOW_COLOR_OVERRIDE.createValue(state.getGlowColor()));
            metadata.add(BuiltInMetadataKeys.GLOWING.createValue(true));
        }
        if (state.getItemDisplayType() != null) {
            metadata.add(BuiltInMetadataKeys.ITEM_DISPLAY_TYPE.createValue(state.getItemDisplayType()));
        }
        if (state.getScale() != null) {
            metadata.add(BuiltInMetadataKeys.SCALE.createValue(state.getScale()));
        }
        if (state.getShadowRadius() != null) {
            metadata.add(BuiltInMetadataKeys.SHADOW_RADIUS.createValue(state.getShadowRadius()));
        }
        if (state.getShadowStrength() != null) {
            metadata.add(BuiltInMetadataKeys.SHADOW_STRENGTH.createValue(state.getShadowStrength()));
        }
        if (state.getTextAlignment() != null || state.getTextSeeThrough() != null || state.getTextShadow() != null) {
            TextDisplayProperties properties = new TextDisplayProperties();
            properties.setAlignment(state.getTextAlignment());
            properties.setSeeThrough(state.getTextSeeThrough());
            properties.setHasShadow(state.getTextShadow());
            metadata.add(BuiltInMetadataKeys.TEXT_DISPLAY_PROPERTIES.createValue(properties));
        }
        if (state.getTextOpacity() != null) {
            metadata.add(BuiltInMetadataKeys.TEXT_DISPLAY_OPACITY.createValue(state.getTextOpacity()));
        }
        if (state.getTextBackgroundColor() != null) {
            metadata.add(BuiltInMetadataKeys.TEXT_DISPLAY_BACKGROUND.createValue(state.getTextBackgroundColor()));
        }
        if (state.getTextLineWidth() != null) {
            metadata.add(BuiltInMetadataKeys.TEXT_LINE_WIDTH.createValue(state.getTextLineWidth()));
        }
        if (state.getTranslation() != null) {
            metadata.add(BuiltInMetadataKeys.TRANSLATION.createValue(state.getTranslation()));
        }
        return metadata;
    }
}

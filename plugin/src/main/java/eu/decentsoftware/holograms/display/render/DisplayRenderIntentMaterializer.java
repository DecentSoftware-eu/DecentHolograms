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
        if (state.isDirty(PresentedRenderStateField.LOCATION)) {
            intentList.add(new MoveRenderIntent(state.getLocation()));
        }
        if (state.isDirty(PresentedRenderStateField.CONTENT)) {
            intentList.add(new UpdateDisplayContentRenderIntent(state.getContent()));
        }
        List<MetadataValue<?>> metadataValues = getChangedMetadata(state);
        if (!metadataValues.isEmpty()) {
            intentList.add(new UpdateMetadataRenderIntent(metadataValues));
        }

        return intentList;
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
            metadata.add(BuiltInMetadataKeys.TEXT_DISPLAY_PROPERTIES.createValue(getTextDisplayProperties(state)));
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

    private List<MetadataValue<?>> getChangedMetadata(PresentedRenderState state) {
        List<MetadataValue<?>> metadata = new ArrayList<>();
        if (state.isDirty(PresentedRenderStateField.METADATA_BILLBOARD_CONSTRAINTS)) {
            metadata.add(BuiltInMetadataKeys.BILLBOARD_CONSTRAINTS.createValue(state.getBillboardConstraints()));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_BRIGHTNESS)) {
            metadata.add(BuiltInMetadataKeys.BRIGHTNESS.createValue(state.getBrightness()));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_GLOW_COLOR)) {
            metadata.add(BuiltInMetadataKeys.GLOW_COLOR_OVERRIDE.createValue(state.getGlowColor()));
            metadata.add(BuiltInMetadataKeys.GLOWING.createValue(state.getGlowColor() != null));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_ITEM_DISPLAY_TYPE)) {
            metadata.add(BuiltInMetadataKeys.ITEM_DISPLAY_TYPE.createValue(state.getItemDisplayType()));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_SCALE)) {
            metadata.add(BuiltInMetadataKeys.SCALE.createValue(state.getScale()));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_SHADOW_RADIUS)) {
            metadata.add(BuiltInMetadataKeys.SHADOW_RADIUS.createValue(state.getShadowRadius()));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_SHADOW_STRENGTH)) {
            metadata.add(BuiltInMetadataKeys.SHADOW_STRENGTH.createValue(state.getShadowStrength()));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_TEXT_DISPLAY_PROPERTIES)) {
            metadata.add(BuiltInMetadataKeys.TEXT_DISPLAY_PROPERTIES.createValue(getTextDisplayProperties(state)));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_TEXT_DISPLAY_OPACITY)) {
            metadata.add(BuiltInMetadataKeys.TEXT_DISPLAY_OPACITY.createValue(state.getTextOpacity()));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_TEXT_BACKGROUND_COLOR)) {
            metadata.add(BuiltInMetadataKeys.TEXT_DISPLAY_BACKGROUND.createValue(state.getTextBackgroundColor()));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_TEXT_LINE_WIDTH)) {
            metadata.add(BuiltInMetadataKeys.TEXT_LINE_WIDTH.createValue(state.getTextLineWidth()));
        }
        if (state.isDirty(PresentedRenderStateField.METADATA_TRANSLATION)) {
            metadata.add(BuiltInMetadataKeys.TRANSLATION.createValue(state.getTranslation()));
        }
        return metadata;
    }

    private TextDisplayProperties getTextDisplayProperties(PresentedRenderState state) {
        TextDisplayProperties properties = new TextDisplayProperties();
        properties.setAlignment(state.getTextAlignment());
        properties.setSeeThrough(state.getTextSeeThrough());
        properties.setHasShadow(state.getTextShadow());
        return properties;
    }
}

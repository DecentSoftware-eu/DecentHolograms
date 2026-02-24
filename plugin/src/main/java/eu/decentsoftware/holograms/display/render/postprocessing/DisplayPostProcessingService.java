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

package eu.decentsoftware.holograms.display.render.postprocessing;

import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.state.LogicalDisplayRenderState;
import eu.decentsoftware.holograms.display.render.state.MutableRenderState;
import eu.decentsoftware.holograms.profiler.DecentProfiler;
import eu.decentsoftware.holograms.profiler.Metrics;
import eu.decentsoftware.holograms.profiler.TimerHandle;

import java.util.Map;

public class DisplayPostProcessingService {

    private final AttributeDefinitionRegistry attributeDefinitionRegistry;
    private final DisplayContentPostProcessingService contentPostProcessingService;

    public DisplayPostProcessingService(AttributeDefinitionRegistry attributeDefinitionRegistry,
                                        DisplayContentPostProcessingService contentPostProcessingService) {
        this.attributeDefinitionRegistry = attributeDefinitionRegistry;
        this.contentPostProcessingService = contentPostProcessingService;
    }

    public MutableRenderState postProcess(LogicalDisplayRenderState logicalState, MutableRenderState mutableState) {
        try (TimerHandle ignored = DecentProfiler.getInstance().startTimer(Metrics.POST_PROCESS)) {
            return postProcessInternal(logicalState, mutableState);
        }
    }

    private MutableRenderState postProcessInternal(LogicalDisplayRenderState logicalState, MutableRenderState mutableState) {
        if (logicalState == null) {
            return null;
        }

        if (mutableState == null) {
            return createMutableRenderState(logicalState);
        }

        mutableState.beginFrame();

        if (logicalState.getContent().isAnimated()) {
            applyContent(logicalState, mutableState);
        }

        applyDirtyAttributes(logicalState, mutableState);
        return mutableState;
    }

    private MutableRenderState createMutableRenderState(LogicalDisplayRenderState logicalState) {
        MutableRenderState state = new MutableRenderState(logicalState.getId(), logicalState.getDisplayType());
        state.setLocation(logicalState.getLocation());
        applyContent(logicalState, state);
        applyAttributes(logicalState, state);
        return state;
    }

    private void applyContent(LogicalDisplayRenderState logicalState, MutableRenderState mutableState) {
        mutableState.setContent(contentPostProcessingService.postProcessContent(logicalState.getDisplayType(), logicalState.getContent()));
    }

    private void applyDirtyAttributes(LogicalDisplayRenderState logicalState, MutableRenderState mutableState) {
        Map<AttributeKey<?>, CompiledAttributeValue<?>> attributeMap = logicalState.getAttributeValues();
        for (Map.Entry<AttributeKey<?>, CompiledAttributeValue<?>> entry : attributeMap.entrySet()) {
            CompiledAttributeValue<?> value = entry.getValue();
            if (value.isDirty()) {
                applyAttribute(entry.getKey(), value, mutableState);
            }
        }
    }

    private void applyAttributes(LogicalDisplayRenderState logicalState, MutableRenderState mutableState) {
        Map<AttributeKey<?>, CompiledAttributeValue<?>> attributeMap = logicalState.getAttributeValues();
        for (Map.Entry<AttributeKey<?>, CompiledAttributeValue<?>> entry : attributeMap.entrySet()) {
            applyAttribute(entry.getKey(), entry.getValue(), mutableState);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void applyAttribute(AttributeKey<T> key, CompiledAttributeValue<?> attributeValue, MutableRenderState mutableState) {
        AttributeDefinition<T> definition = attributeDefinitionRegistry.getDefinitionByKey(key);
        if (definition != null) {
            definition.apply((CompiledAttributeValue<T>) attributeValue, mutableState);
        }
    }
}

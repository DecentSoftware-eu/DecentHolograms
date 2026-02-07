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
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.display.render.state.LogicalDisplayRenderState;

import java.util.Map;

public class DisplayPostProcessingService {

    private final AttributeDefinitionRegistry attributeDefinitionRegistry;
    private final DisplayContentPostProcessingService contentPostProcessingService;

    public DisplayPostProcessingService(AttributeDefinitionRegistry attributeDefinitionRegistry,
                                        DisplayContentPostProcessingService contentPostProcessingService) {
        this.attributeDefinitionRegistry = attributeDefinitionRegistry;
        this.contentPostProcessingService = contentPostProcessingService;
    }

    public FinalDisplayRenderState postProcess(LogicalDisplayRenderState logicalState) {
        FinalDisplayRenderState state = new FinalDisplayRenderState(logicalState.getId());
        state.setVisible(logicalState.isVisible());
        state.setLocation(logicalState.getLocation());
        state.setDisplayType(logicalState.getDisplayType());
        state.setContent(contentPostProcessingService.postProcessContent(logicalState.getDisplayType(), logicalState.getContent()));
        applyAttributes(logicalState, state);
        return state;
    }

    private void applyAttributes(LogicalDisplayRenderState logicalState, FinalDisplayRenderState state) {
        Map<AttributeKey<?>, AttributeValue<?>> attributeMap = logicalState.getAttributeValues();
        for (AttributeKey<?> attributeKey : attributeMap.keySet()) {
            applyAttribute(attributeKey, logicalState, state);
        }
    }

    private <T> void applyAttribute(AttributeKey<T> key, LogicalDisplayRenderState logicalState, FinalDisplayRenderState state) {
        AttributeValue<T> attribute = logicalState.getAttributeValue(key);
        AttributeDefinition<T> definition = attributeDefinitionRegistry.getDefinitionByKey(key);
        if (definition != null) {
            definition.apply(attribute, state);
        }
    }
}

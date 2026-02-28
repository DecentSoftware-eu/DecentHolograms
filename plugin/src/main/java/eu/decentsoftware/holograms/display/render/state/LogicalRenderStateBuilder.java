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

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.StaticCompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.type.DisplayTypeDefinition;
import eu.decentsoftware.holograms.display.type.DisplayTypeRegistry;
import eu.decentsoftware.holograms.display.render.content.CompiledDisplayContent;

public class LogicalRenderStateBuilder {

    private final DisplayTypeRegistry displayTypeRegistry;

    public LogicalRenderStateBuilder(DisplayTypeRegistry displayTypeRegistry) {
        this.displayTypeRegistry = displayTypeRegistry;
    }

    public LogicalRenderState updateState(DisplayBase display, DisplayRenderContext context, LogicalRenderState currentState) {
        if (currentState == null) {
            return createNewLogicalDisplayRenderState(display, context);
        }

        currentState.setLocation(display.getLocation());
        if (display.checkContentDirty() || currentState.getContent().isDynamic()) {
            applyContent(display, currentState, context);
        }
        if (display.checkConfigDirty()) {
            currentState.clearAttributes();
            applyAttributes(display, currentState, context);
        }

        return currentState;
    }

    private LogicalRenderState createNewLogicalDisplayRenderState(DisplayBase display, DisplayRenderContext context) {
        LogicalRenderState state = new LogicalRenderState(display.getName(), display.getType());
        state.setLocation(display.getLocation());
        applyContent(display, state, context);
        applyAttributes(display, state, context);
        return state;
    }

    private void applyAttributes(DisplayBase display, LogicalRenderState state, DisplayRenderContext context) {
        for (AttributeKey<?> attributeKey : display.getAttributesMap().keySet()) {
            applyAttribute(attributeKey, display, state, context);
        }
    }

    private <T> void applyAttribute(AttributeKey<T> key, DisplayBase display, LogicalRenderState state, DisplayRenderContext context) {
        DisplayAttribute<T> attribute = display.getAttribute(key);
        CompiledAttributeValue<T> value = compileAttribute(attribute, context);
        state.addAttribute(key, value);
    }

    private <T> CompiledAttributeValue<T> compileAttribute(DisplayAttribute<T> attribute, DisplayRenderContext context) {
        AttributeValue<T> value = attribute.getValue();
        if (value == null) {
            return StaticCompiledAttributeValue.empty();
        }
        return value.compile(context);
    }

    private void applyContent(DisplayBase display, LogicalRenderState state, DisplayRenderContext context) {
        DisplayTypeDefinition<?> displayTypeDefinition = displayTypeRegistry.getDefinition(display.getType());
        CompiledDisplayContent<?> content = displayTypeDefinition.resolveContent(display, context);
        state.setContent(content);
    }
}

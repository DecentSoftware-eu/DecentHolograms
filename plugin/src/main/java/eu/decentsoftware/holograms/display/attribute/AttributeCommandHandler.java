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

package eu.decentsoftware.holograms.display.attribute;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.DecentCommandException;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class AttributeCommandHandler {

    private final AttributeDefinitionRegistry attributeDefinitionRegistry;

    public AttributeCommandHandler(AttributeDefinitionRegistry attributeDefinitionRegistry) {
        this.attributeDefinitionRegistry = attributeDefinitionRegistry;
    }

    public <T> void setAttribute(DisplayBase display, AttributeDefinition<T> attributeDefinition, String[] rawValueArgs) {
        T parsed = parseAttributeValue(attributeDefinition, rawValueArgs);

        AttributeKey<T> attributeKey = attributeDefinition.getKey();
        display.setAttribute(attributeKey, new StaticDisplayAttribute<>(attributeDefinition.getName(), parsed));
    }

    private <T> T parseAttributeValue(AttributeDefinition<T> attributeDefinition, String[] rawValueArgs) {
        try {
            return attributeDefinition.parse(rawValueArgs);
        } catch (AttributeParseException e) {
            throw new DecentCommandException(Lang.DISPLAY_ATTRIBUTE_INVALID_VALUE.getValue(), attributeDefinition.getName(), e.getMessage());
        }
    }

    public void resetAttribute(DisplayBase display, AttributeDefinition<?> attributeDefinition) {
        display.removeAttribute(attributeDefinition.getKey());
    }

    public <T> String getAttribute(DisplayBase display, AttributeDefinition<T> attributeDefinition) {
        DisplayAttribute<T> attribute = display.getAttribute(attributeDefinition.getKey());
        if (attribute == null) {
            return null;
        }
        return attributeDefinition.format(attribute.getValue());
    }

    public List<AttributeDefinition<?>> getApplicableAttributes(DisplayBase display) {
        return attributeDefinitionRegistry.getDefinitionsByDisplayType(display.getType());
    }

    public List<String> getApplicableAttributeNames(DisplayBase display) {
        return getApplicableAttributes(display).stream()
                .map(AttributeDefinition::getName)
                .collect(Collectors.toList());
    }

    @Nullable
    public AttributeDefinition<?> getAttributeDefinition(String name, DisplayBase display) {
        AttributeDefinition<?> definition = attributeDefinitionRegistry.getDefinitionByName(name);
        if (definition == null || !definition.applicableTo(display)) {
            return null;
        }
        return definition;
    }
}
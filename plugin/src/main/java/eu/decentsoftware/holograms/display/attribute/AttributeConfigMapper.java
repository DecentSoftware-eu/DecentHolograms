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

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValueType;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValueTypeRegistry;
import eu.decentsoftware.holograms.display.config.dto.ConfigAttribute;

import java.util.Map;

public class AttributeConfigMapper {

    private final AttributeDefinitionRegistry attributeDefinitionRegistry;
    private final AttributeValueTypeRegistry attributeValueTypeRegistry;

    public AttributeConfigMapper(AttributeDefinitionRegistry attributeDefinitionRegistry,
                                 AttributeValueTypeRegistry attributeValueTypeRegistry) {
        this.attributeDefinitionRegistry = attributeDefinitionRegistry;
        this.attributeValueTypeRegistry = attributeValueTypeRegistry;
    }

    public void attributesToDomain(DisplayBase display, Map<String, ConfigAttribute> dto) {
        dto.forEach((name, attribute) -> attributeToDomain(display, name, attribute));
    }

    private void attributeToDomain(DisplayBase display, String name, ConfigAttribute attribute) {
        AttributeDefinition<?> definition = attributeDefinitionRegistry.getDefinitionByName(name);
        if (validateAttributeConfiguration(display, name, attribute, definition)) {
            setAttributeTypeSafe(display, definition, attribute);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void setAttributeTypeSafe(DisplayBase display, AttributeDefinition<T> definition, ConfigAttribute attribute) {
        if (attribute.getValue() == null) {
            Log.warn("Attribute %s in display %s has null value. Skipping.", definition.getName(), display.getName());
            return;
        }
        // This cast is safe because we validated that:
        // attributeValueType.getOutputType() == definition.getValueType()
        AttributeValue<T> typedValue = (AttributeValue<T>) attribute.getValue();
        attributeToDomain(display, definition.getKey(), typedValue);
    }

    private <T> void attributeToDomain(DisplayBase display, AttributeKey<T> attributeKey, AttributeValue<T> value) {
        DisplayAttribute<T> attributeInstance = new DisplayAttribute<>(attributeKey, value);
        display.setAttribute(attributeKey, attributeInstance);
    }

    private boolean validateAttributeConfiguration(DisplayBase display, String name, ConfigAttribute attribute, AttributeDefinition<?> definition) {
        return validateAttributeDefinition(display, name, definition)
                && validateAttributeValueType(display, name, attribute, definition);
    }

    private boolean validateAttributeDefinition(DisplayBase display, String name, AttributeDefinition<?> definition) {
        if (definition == null) {
            Log.warn("Found unknown attribute " + name + " for display " + display.getName() + ". Skipping.");
            return false;
        }
        if (!definition.applicableTo(display)) {
            Log.warn("Found incompatible attribute " + name + " for display " + display.getName() + ". Skipping.");
            return false;
        }
        return true;
    }

    private boolean validateAttributeValueType(DisplayBase display, String name, ConfigAttribute attribute, AttributeDefinition<?> definition) {
        String typeKey = attribute.getValue().getTypeKey();
        AttributeValueType<? extends AttributeValue<?>, ?> attributeValueType = attributeValueTypeRegistry.getByTypeIdUnsafe(typeKey);
        if (attributeValueType == null) {
            Log.warn("Found unknown attribute value type %s for attribute %s in display %s. Skipping.",
                    typeKey, name, display.getName());
            return false;
        }
        if (!attributeValueType.getOutputType().equals(definition.getValueType())) {
            Log.warn("Found incompatible attribute value type %s for attribute %s in display %s. Skipping.",
                    typeKey, name, display.getName());
            return false;
        }
        return true;
    }
}

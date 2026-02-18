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

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;

import java.util.List;

public class DisplayAttributeService {

    private final AttributeDefinitionRegistry attributeDefinitionRegistry;

    public DisplayAttributeService(AttributeDefinitionRegistry attributeDefinitionRegistry) {
        this.attributeDefinitionRegistry = attributeDefinitionRegistry;
    }

    public <T> String getFormattedAttributeValue(DisplayBase display, AttributeDefinition<T> definition) {
        DisplayAttribute<T> attribute = display.getAttribute(definition.getKey());
        if (attribute == null) {
            return null;
        }
        return definition.format(attribute.getValue());
    }

    public List<AttributeDefinition<?>> getApplicableAttributes(DisplayBase display) {
        return attributeDefinitionRegistry.getDefinitionsByDisplayType(display.getType());
    }
}

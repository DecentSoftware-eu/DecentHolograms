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

package eu.decentsoftware.holograms.display.attribute.definition;

import eu.decentsoftware.holograms.display.attribute.definition.general.BillboardAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.BrightnessAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.GlowColorAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.ScaleAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.ShadowRadiusAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.ShadowStrengthAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.general.TranslationAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.item.ItemDisplayTypeAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextAlignmentAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextBackgroundColorAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextLineWidthAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextOpacityAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextSeeThroughAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.text.TextShadowAttributeDefinition;

import java.util.HashMap;
import java.util.Map;

public class AttributeDefinitionRegistry {

    private final Map<String, AttributeDefinition<?>> definitionsByName = new HashMap<>();

    public AttributeDefinitionRegistry() {
        registerDefinition(new TranslationAttributeDefinition());
        registerDefinition(new ScaleAttributeDefinition());
        registerDefinition(new BillboardAttributeDefinition());
        registerDefinition(new BrightnessAttributeDefinition());
        registerDefinition(new ShadowRadiusAttributeDefinition());
        registerDefinition(new ShadowStrengthAttributeDefinition());
        registerDefinition(new GlowColorAttributeDefinition());

        registerDefinition(new TextLineWidthAttributeDefinition());
        registerDefinition(new TextBackgroundColorAttributeDefinition());
        registerDefinition(new TextOpacityAttributeDefinition());
        registerDefinition(new TextShadowAttributeDefinition());
        registerDefinition(new TextSeeThroughAttributeDefinition());
        registerDefinition(new TextAlignmentAttributeDefinition());

        registerDefinition(new ItemDisplayTypeAttributeDefinition());
    }

    public void registerDefinition(AttributeDefinition<?> definition) {
        this.definitionsByName.put(definition.getName(), definition);
    }

    public AttributeDefinition<?> getDefinition(String name) {
        return this.definitionsByName.get(name);
    }
}

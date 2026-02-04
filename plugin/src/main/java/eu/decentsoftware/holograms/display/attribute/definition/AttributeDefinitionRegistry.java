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

import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A registry for managing attribute definitions used in displays.
 *
 * <p>There should only ever be one instance of this class.</p>
 *
 * @author d0by
 * @see AttributeDefinition
 * @since 2.10.0
 */
public class AttributeDefinitionRegistry {

    private final Map<String, AttributeDefinition<?>> definitionsByName = new HashMap<>();
    private final Map<DisplayType, List<AttributeDefinition<?>>> definitionsByDisplayType = new EnumMap<>(DisplayType.class);
    private final Map<AttributeKey<?>, AttributeDefinition<?>> definitionsByKey = new HashMap<>();

    /**
     * Create a new registry and register all built-in attribute definitions.
     */
    public AttributeDefinitionRegistry() {
        registerAllAttributeDefinitions();
        populateDefinitionsByKey();
        populateDefinitionsByDisplayType();
    }

    private void registerAllAttributeDefinitions() {
        // General
        registerDefinition(new YawAttributeDefinition());
        registerDefinition(new PitchAttributeDefinition());
        registerDefinition(new TranslationAttributeDefinition());
        registerDefinition(new ScaleAttributeDefinition());
        registerDefinition(new BillboardAttributeDefinition());
        registerDefinition(new BrightnessAttributeDefinition());
        registerDefinition(new ShadowRadiusAttributeDefinition());
        registerDefinition(new ShadowStrengthAttributeDefinition());
        registerDefinition(new GlowColorAttributeDefinition());

        // Text
        registerDefinition(new TextBackgroundColorAttributeDefinition());
        registerDefinition(new TextOpacityAttributeDefinition());
        registerDefinition(new TextShadowAttributeDefinition());
        registerDefinition(new TextSeeThroughAttributeDefinition());
        registerDefinition(new TextAlignmentAttributeDefinition());

        // Item
        registerDefinition(new ItemDisplayTypeAttributeDefinition());
    }

    private void populateDefinitionsByKey() {
        for (AttributeDefinition<?> definition : definitionsByName.values()) {
            this.definitionsByKey.put(definition.getKey(), definition);
        }
    }

    private void populateDefinitionsByDisplayType() {
        for (AttributeDefinition<?> definition : definitionsByName.values()) {
            for (DisplayType displayType : definition.getApplicableDisplayTypes()) {
                definitionsByDisplayType
                        .computeIfAbsent(displayType, key -> new ArrayList<>())
                        .add(definition);
            }
        }
    }

    private void registerDefinition(@NotNull AttributeDefinition<?> definition) {
        this.definitionsByName.put(definition.getName(), definition);
    }

    /**
     * Get an attribute definition by its key.
     *
     * @param key The key of the definition.
     * @return The definition or null if it wasn't found.
     * @see AttributeDefinition#getKey()
     * @since 2.10.0
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> AttributeDefinition<T> getDefinitionByKey(@NotNull AttributeKey<T> key) {
        return (AttributeDefinition<T>) this.definitionsByKey.get(key);
    }

    /**
     * Get an attribute definition by its name.
     *
     * @param name The name of the definition.
     * @return The definition or null if it wasn't found.
     * @see AttributeDefinition#getName()
     * @since 2.10.0
     */
    @Nullable
    public AttributeDefinition<?> getDefinitionByName(@NotNull String name) {
        return this.definitionsByName.get(name);
    }

    /**
     * Get the definitions of all attributes applicable for a specific display type.
     *
     * @param displayType The display type.
     * @return List of applicable definitions.
     * @see AttributeDefinition#getApplicableDisplayTypes()
     * @since 2.10.0
     */
    @NotNull
    @Unmodifiable
    public List<AttributeDefinition<?>> getDefinitionsByDisplayType(@NotNull DisplayType displayType) {
        return Collections.unmodifiableList(this.definitionsByDisplayType.get(displayType));
    }
}

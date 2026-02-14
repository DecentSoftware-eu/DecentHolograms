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
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.attribute.command.handler.AttributeCommandHandler;
import eu.decentsoftware.holograms.display.attribute.command.handler.AttributeCommandHandlerRegistry;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AttributeCommandService {

    private final AttributeDefinitionRegistry attributeDefinitionRegistry;
    private final AttributeCommandHandlerRegistry commandHandlerRegistry;

    public AttributeCommandService(AttributeDefinitionRegistry attributeDefinitionRegistry,
                                   AttributeCommandHandlerRegistry commandHandlerRegistry) {
        this.attributeDefinitionRegistry = attributeDefinitionRegistry;
        this.commandHandlerRegistry = commandHandlerRegistry;
    }

    public <T> void setAttribute(DisplayBase display, AttributeDefinition<T> definition, String[] args) {
        AttributeCommandHandler<T> handler = commandHandlerRegistry.getHandler(definition.getKey(), args);
        if (handler == null) {
            Log.warn("No command handler registered for attribute: " + definition.getName() + " with args: " + String.join(" ", args));
            return;
        }

        AttributeValue<T> value = parseAttributeValue(definition, handler, args);
        display.setAttribute(definition.getKey(), new DisplayAttribute<>(definition.getKey(), value));
    }

    private <T> AttributeValue<T> parseAttributeValue(AttributeDefinition<T> attributeDefinition, AttributeCommandHandler<T> handler, String[] args) {
        try {
            return handler.parse(getArgsForHandler(handler, args));
        } catch (AttributeParseException e) {
            throw new DecentCommandException(Lang.DISPLAY_ATTRIBUTE_INVALID_VALUE.getValue(), attributeDefinition.getName(), e.getMessage());
        }
    }

    public <T> void resetAttribute(DisplayBase display, AttributeDefinition<T> attributeDefinition) {
        AttributeKey<T> attributeKey = attributeDefinition.getKey();
        display.setAttribute(attributeKey, new DisplayAttribute<>(attributeKey, null));
    }

    public <T> String getAttribute(DisplayBase display, AttributeDefinition<T> attributeDefinition) {
        DisplayAttribute<T> attribute = display.getAttribute(attributeDefinition.getKey());
        if (attribute == null) {
            return null;
        }
        return attributeDefinition.format(attribute.getValue());
    }

    public List<String> getApplicableAttributeNames(DisplayBase display) {
        return getApplicableAttributes(display).stream()
                .map(AttributeDefinition::getName)
                .collect(Collectors.toList());
    }

    private List<AttributeDefinition<?>> getApplicableAttributes(DisplayBase display) {
        return attributeDefinitionRegistry.getDefinitionsByDisplayType(display.getType());
    }

    @Nullable
    public AttributeDefinition<?> getAttributeDefinition(String name, DisplayBase display) {
        AttributeDefinition<?> definition = attributeDefinitionRegistry.getDefinitionByName(name);
        if (definition == null || !definition.applicableTo(display)) {
            return null;
        }
        return definition;
    }

    public <T> List<String> getHints(AttributeDefinition<T> definition, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getHintsForFirstArgument(definition, sender, args);
        }

        AttributeCommandHandler<T> handler = commandHandlerRegistry.getHandler(definition.getKey(), args);
        if (handler == null) {
            Log.warn("No command handler found for attribute: " + definition.getName() + " with args: " + String.join(" ", args));
            return Collections.emptyList();
        }

        return handler.getHints(sender, getArgsForHandler(handler, args));
    }

    private <T> List<String> getHintsForFirstArgument(AttributeDefinition<T> definition, CommandSender sender, String[] args) {
        List<String> hints = new ArrayList<>(commandHandlerRegistry.getKeywords(definition.getKey()));
        AttributeCommandHandler<T> defaultHandler = commandHandlerRegistry.getDefaultHandler(definition.getKey());
        if (defaultHandler != null) {
            // Include hints from the default handler for the first argument, as it may accept direct values without a keyword
            hints.addAll(defaultHandler.getHints(sender, args));
        }
        return hints;
    }

    private String[] getArgsForHandler(AttributeCommandHandler<?> handler, String[] inputArgs) {
        if (handler.isDefault()) {
            return inputArgs; // Pass all args to default handler
        } else {
            // For non-default handlers, the first arg is the keyword, so we pass the rest as value args
            return Arrays.copyOfRange(inputArgs, 1, inputArgs.length);
        }
    }
}
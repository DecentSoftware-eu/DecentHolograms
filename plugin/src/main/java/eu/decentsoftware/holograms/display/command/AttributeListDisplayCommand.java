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

package eu.decentsoftware.holograms.display.command;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.DisplayAttributeService;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.plugin.Validator;

import java.util.Collection;
import java.util.List;

@CommandInfo(
        usage = "/dh d list-attributes <display>",
        description = "Lists all configured attributes on a display.",
        permissions = {"dh.command.displays.listattributes"},
        minArgs = 1
)
class AttributeListDisplayCommand extends DecentCommand {

    private final DisplayService displayService;
    private final DisplayAttributeService displayAttributeService;

    AttributeListDisplayCommand(DisplayService displayService, DisplayAttributeService displayAttributeService) {
        super("list-attributes");
        this.displayService = displayService;
        this.displayAttributeService = displayAttributeService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(1, args);

            String displayName = args[0];
            DisplayBase display = displayService.getDisplay(displayName);
            if (display == null) {
                Lang.DISPLAY_DOES_NOT_EXIST.send(sender, displayName);
                return true;
            }

            Collection<DisplayAttribute<?>> attributes = display.getAttributes();
            if (attributes.isEmpty()) {
                Lang.DISPLAY_ATTRIBUTE_LIST_EMPTY.send(sender, displayName);
                return true;
            }

            Lang.DISPLAY_ATTRIBUTE_LIST_HEADER.send(sender, displayName);
            List<AttributeDefinition<?>> applicableAttributes = displayAttributeService.getApplicableAttributes(display);
            for (AttributeDefinition<?> attributeDefinition : applicableAttributes) {
                String attributeName = attributeDefinition.getName();
                String attributeValue = displayAttributeService.getFormattedAttributeValue(display, attributeDefinition);
                if (attributeValue == null) {
                    attributeValue = "-";
                }

                Lang.DISPLAY_ATTRIBUTE_LIST_ENTRY.send(sender, attributeName, attributeValue);
            }
            Lang.DISPLAY_ATTRIBUTE_LIST_FOOTER.send(sender, displayName);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 1) {
                return TabCompleteHandler.getPartialMatches(args[0], displayService.getRegisteredDisplayNames());
            }
            return null;
        };
    }
}

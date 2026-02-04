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
import eu.decentsoftware.holograms.display.attribute.AttributeCommandHandler;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.plugin.Validator;

import java.util.Arrays;

@CommandInfo(
        usage = "/dh d attribute <name> <attribute> [value]",
        description = "Get or set a display attribute.",
        aliases = {"attr"},
        permissions = {"dh.command.displays.attribute"},
        minArgs = 2
)
class AttributeDisplayCommand extends DecentCommand {

    private final DisplayService displayService;
    private final AttributeCommandHandler attributeCommandHandler;

    AttributeDisplayCommand(DisplayService displayService, AttributeCommandHandler attributeCommandHandler) {
        super("attribute");
        this.displayService = displayService;
        this.attributeCommandHandler = attributeCommandHandler;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(2, args);
            DisplayBase display = Validator.getDisplay(displayService, args[0]);

            AttributeDefinition<?> attributeDefinition = attributeCommandHandler.getAttributeDefinition(args[1], display);
            if (attributeDefinition == null) {
                Lang.DISPLAY_ATTRIBUTE_DOES_NOT_EXIST.send(sender, args[1]);
                return true;
            }

            if (args.length == 2) {
                String attributeValue = attributeCommandHandler.getAttribute(display, attributeDefinition);
                if (attributeValue == null) {
                    Lang.DISPLAY_ATTRIBUTE_GET_NOT_SET.send(sender, attributeDefinition.getName());
                } else {
                    Lang.DISPLAY_ATTRIBUTE_GET.send(sender, attributeDefinition.getName(), attributeValue);
                }
                return true;
            }

            String[] valueArguments = Arrays.copyOfRange(args, 2, args.length);
            attributeCommandHandler.setAttribute(display, attributeDefinition, valueArguments);
            displayService.updateDisplay(display);
            displayService.saveDisplay(display);
            String formattedValue = attributeCommandHandler.getAttribute(display, attributeDefinition);
            Lang.DISPLAY_ATTRIBUTE_SET.send(sender, attributeDefinition.getName(), formattedValue);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 1) {
                return TabCompleteHandler.getPartialMatches(args[0], displayService.getRegisteredDisplayNames());
            } else if (args.length == 2) {
                DisplayBase display = displayService.getDisplay(args[0]);
                if (display == null) {
                    return null;
                }
                return TabCompleteHandler.getPartialMatches(args[1], attributeCommandHandler.getApplicableAttributeNames(display));
            } else if (args.length >= 3) {
                DisplayBase display = displayService.getDisplay(args[0]);
                if (display == null) {
                    return null;
                }
                AttributeDefinition<?> attribute = attributeCommandHandler.getAttributeDefinition(args[1], display);
                if (attribute == null) {
                    return null;
                }
                String[] valueArguments = Arrays.copyOfRange(args, 2, args.length);
                return TabCompleteHandler.getPartialMatches(args[args.length - 1], attribute.getHints(sender, valueArguments));
            }
            return null;
        };
    }
}

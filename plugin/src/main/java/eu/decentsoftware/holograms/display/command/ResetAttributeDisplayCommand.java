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

@CommandInfo(
        usage = "/dh d reset-attribute <name> <attribute>",
        description = "Reset a display attribute to the default value.",
        aliases = {"resetattribute", "unset-attribute", "unsetattribute"},
        permissions = {"dh.command.displays.resetattribute"}
)
class ResetAttributeDisplayCommand extends DecentCommand {

    private final DisplayService displayService;
    private final AttributeCommandHandler attributeCommandHandler;

    ResetAttributeDisplayCommand(DisplayService displayService, AttributeCommandHandler attributeCommandHandler) {
        super("reset-attribute");
        this.displayService = displayService;
        this.attributeCommandHandler = attributeCommandHandler;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(2, args);
            DisplayBase display = Validator.getDisplay(displayService, args[0]);

            AttributeDefinition<?> attributeDefinition = attributeCommandHandler.getAttributeDefinition(args[1]);
            if (attributeDefinition == null) {
                Lang.DISPLAY_ATTRIBUTE_DOES_NOT_EXIST.send(sender, args[1]);
                return true;
            }

            attributeCommandHandler.resetAttribute(display, attributeDefinition);
            displayService.updateDisplayProperties(display);
            displayService.saveDisplay(display);
            Lang.DISPLAY_ATTRIBUTE_RESET.send(sender, attributeDefinition.getName());
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
                if (display != null) {
                    return TabCompleteHandler.getPartialMatches(args[1], attributeCommandHandler.getApplicableAttributeNames(display));
                }
            }
            return null;
        };
    }
}

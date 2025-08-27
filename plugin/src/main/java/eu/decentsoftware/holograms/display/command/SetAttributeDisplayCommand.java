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
import eu.decentsoftware.holograms.display.attribute.DisplayAttributeValidationException;

import java.util.Map;

@CommandInfo(
        usage = "/dd set-attribute <name> <attribute>=<value>",
        description = "Set a display attribute.",
        aliases = {"setattribute", "attribute", "attr"}
)
public class SetAttributeDisplayCommand extends DecentCommand {

    private final DisplayService displayService;
    private final DisplayAttributeService attributeService;

    public SetAttributeDisplayCommand(DisplayService displayService) {
        super("set-attribute");
        this.displayService = displayService;
        this.attributeService = new DisplayAttributeService();
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            DisplayBase display = displayService.getDisplay(args[0]);
            if (display == null) {
                Lang.DISPLAY_DOES_NOT_EXIST.send(sender);
                return true;
            }

            String[] split = args[1].split("=");
            if (split.length != 2) {
                Lang.USE_HELP.send(sender);
                return true;
            }

            Map<String, DisplayAttribute> attributes = attributeService.getAvailableAttributes(display);
            DisplayAttribute attribute = attributes.get(split[0]);
            if (attribute == null) {
                Lang.DISPLAY_ATTRIBUTE_DOES_NOT_EXIST.send(sender, split[0]);
                return true;
            }

            try {
                attribute.applyValue(display, split[1]);
                Lang.DISPLAY_ATTRIBUTE_SET.send(sender, attribute.getName(), split[1]);
                return true;
            } catch (DisplayAttributeValidationException e) {
                Lang.DISPLAY_ATTRIBUTE_INVALID_VALUE.send(sender, split[1], attribute.getName());
                return true;
            }
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
                return TabCompleteHandler.getPartialMatches(args[1], attributeService.getAvailableAttributes(display).keySet());
            }
            return null;
        };
    }
}

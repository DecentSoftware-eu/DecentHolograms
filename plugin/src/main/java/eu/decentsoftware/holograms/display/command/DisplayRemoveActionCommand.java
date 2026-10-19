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

import eu.decentsoftware.holograms.Permissions;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.plugin.Validator;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandInfo(
        usage = "/dh d removeaction <display> <clickType> <index>",
        description = "Remove a click action from a display.",
        aliases = {"remaction"},
        permissions = {Permissions.COMMAND_DISPLAYS_REMOVE_ACTION},
        minArgs = 3
)
class DisplayRemoveActionCommand extends DecentCommand {

    private final DisplayService displayService;

    DisplayRemoveActionCommand(DisplayService displayService) {
        super("removeaction");
        this.displayService = displayService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(3, args);
            DisplayBase display = Validator.getDisplay(displayService, args[0]);
            ClickType clickType = ClickType.fromString(args[1]);
            if (clickType == null) {
                Lang.CLICK_TYPE_DOES_NOT_EXIST.send(sender, args[1]);
                return true;
            }
            int index = Validator.getInteger(args[2], 1, display.getActions(clickType).size(), Lang.ACTION_DOES_NOT_EXIST.getValue()) - 1;
            display.removeAction(clickType, index);
            displayService.saveDisplay(display);
            displayService.refreshClickableEntities(display);
            Lang.ACTION_REMOVED.send(sender);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 1) {
                return TabCompleteHandler.getPartialMatches(args[0], displayService.getRegisteredDisplayNames());
            } else if (args.length == 2) {
                return TabCompleteHandler.getPartialMatches(args[1], Arrays.stream(ClickType.values())
                        .map(ClickType::name)
                        .collect(Collectors.toList()));
            } else if (args.length == 3) {
                DisplayBase display = displayService.getDisplay(args[0]);
                if (display != null) {
                    ClickType clickType = ClickType.fromString(args[1]);
                    if (clickType != null) {
                        return TabCompleteHandler.getPartialMatches(args[2], IntStream
                                .rangeClosed(1, display.getActions(clickType).size())
                                .boxed().map(String::valueOf)
                                .collect(Collectors.toList()));
                    }
                }
            }
            return null;
        };
    }
}

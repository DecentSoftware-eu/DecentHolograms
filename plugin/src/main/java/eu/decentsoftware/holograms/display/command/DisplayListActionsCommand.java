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
import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.utils.message.Message;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.plugin.Validator;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@CommandInfo(
        usage = "/dh d actions <display> <clickType> [listPage]",
        description = "List click actions of a display.",
        permissions = {Permissions.COMMAND_DISPLAYS_ACTIONS},
        playerOnly = true,
        minArgs = 2
)
class DisplayListActionsCommand extends DecentCommand {

    private final DisplayService displayService;

    DisplayListActionsCommand(DisplayService displayService) {
        super("actions");
        this.displayService = displayService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(2, args);
            DisplayBase display = Validator.getDisplay(displayService, args[0]);
            ClickType clickType = ClickType.fromString(args[1]);
            if (clickType == null) {
                Lang.CLICK_TYPE_DOES_NOT_EXIST.send(sender, args[1]);
                return true;
            }
            List<Action> actions = display.getActions(clickType);
            if (actions == null || actions.isEmpty()) {
                Lang.ACTION_NO_ACTIONS.send(sender);
                return true;
            }
            int currentPage = args.length >= 3 ? Validator.getInteger(args[2], "Page must be a valid integer.") - 1 : 0;
            List<String> header = Lists.newArrayList("", " &3&lDISPLAY ACTIONS", " &fList of all actions on display '" + display.getName() + "'.", "");
            Function<Action, String> parseItem = action -> String.format(" &8• &b%s", action.toString());
            String commandFormat = "/dh d actions " + args[0] + " " + args[1] + " %d";
            Message.sendPaginatedMessage((Player) sender, currentPage, commandFormat, 15, header, null, actions, parseItem);
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
            }
            return null;
        };
    }
}

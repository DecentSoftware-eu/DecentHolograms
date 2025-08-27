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
import eu.decentsoftware.holograms.display.DisplayService;

@CommandInfo(
        usage = "/dh d delete <name>",
        description = "Delete an existing display",
        permissions = {"dh.command.displays.delete"},
        aliases = {"del"}
)
class DeleteDisplayCommand extends DecentCommand {

    private final DisplayService displayService;

    DeleteDisplayCommand(DisplayService displayService) {
        super("delete");
        this.displayService = displayService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            if (args.length < 1) {
                Lang.USE_HELP.send(sender);
                return true;
            }

            String name = args[0];
            boolean deleted = displayService.deleteDisplay(name);
            if (deleted) {
                Lang.DISPLAY_DELETED.send(sender, name);
                return true;
            }

            Lang.DISPLAY_DOES_NOT_EXIST.send(sender, name);
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

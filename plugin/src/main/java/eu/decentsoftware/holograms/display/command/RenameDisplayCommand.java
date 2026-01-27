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
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayCloneService;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.plugin.Validator;

@CommandInfo(
        usage = "/dh d rename <name> <new_name>",
        description = "Rename an existing display.",
        permissions = "dh.command.displays.rename",
        minArgs = 2
)
class RenameDisplayCommand extends DecentCommand {

    private final DisplayService displayService;
    private final DisplayCloneService displayCloneService;

    RenameDisplayCommand(DisplayService displayService, DisplayCloneService displayCloneService) {
        super("rename");
        this.displayService = displayService;
        this.displayCloneService = displayCloneService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(2, args);
            DisplayBase display = Validator.getDisplay(displayService, args[0]);
            String name = args[1];
            if (!name.matches(Common.NAME_REGEX)) {
                Lang.DISPLAY_INVALID_NAME.send(sender, name);
                return true;
            }
            if (displayService.getDisplay(name) != null) {
                Lang.DISPLAY_ALREADY_EXISTS.send(sender, name);
                return true;
            }

            DisplayBase renamedDisplay = displayCloneService.cloneDisplay(display, name);
            displayService.saveDisplay(renamedDisplay);
            displayService.deleteDisplay(display.getName());

            Lang.DISPLAY_RENAMED.send(sender, display.getName());
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

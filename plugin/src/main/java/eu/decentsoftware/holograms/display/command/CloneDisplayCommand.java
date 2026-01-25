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
import eu.decentsoftware.holograms.location.DecentLocation;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayCloneService;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.plugin.Validator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandInfo(
        usage = "/dh d clone <name> <new_name>",
        description = "Clone an existing display.",
        permissions = "dh.command.displays.clone",
        playerOnly = true,
        aliases = {"cp", "copy"}
)
class CloneDisplayCommand extends DecentCommand {

    private final DisplayService displayService;
    private final DisplayCloneService displayCloneService;

    CloneDisplayCommand(DisplayService displayService, DisplayCloneService displayCloneService) {
        super("clone");
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

            DisplayBase clonedDisplay = displayCloneService.cloneDisplay(display, name);
            DecentLocation displayLocation = clonedDisplay.getLocation();
            Location playerLocation = ((Player) sender).getLocation();
            clonedDisplay.setLocation(new DecentLocation(
                    playerLocation.getWorld().getName(),
                    playerLocation.getX(),
                    playerLocation.getY(),
                    playerLocation.getZ(),
                    displayLocation.getYaw(),
                    displayLocation.getPitch()
            ));
            displayService.updateDisplayLocation(clonedDisplay);
            displayService.saveDisplay(clonedDisplay);

            Lang.DISPLAY_CLONED.send(sender, display.getName());
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

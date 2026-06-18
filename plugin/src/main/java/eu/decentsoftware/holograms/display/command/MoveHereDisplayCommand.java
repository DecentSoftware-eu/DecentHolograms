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
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.plugin.Validator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandInfo(
        usage = "/dh d movehere <name>",
        description = "Move a display to your current location.",
        permissions = {"dh.command.displays.movehere"},
        aliases = {"mvhr"},
        playerOnly = true,
        minArgs = 1
)
class MoveHereDisplayCommand extends DecentCommand {

    private final DisplayService displayService;

    public MoveHereDisplayCommand(DisplayService displayService) {
        super("movehere");
        this.displayService = displayService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(1, args);
            DisplayBase display = Validator.getDisplay(displayService, args[0]);

            DecentLocation displayLocation = display.getLocation();
            final Player player = (Player) sender;
            Location playerLocation = Settings.DISPLAYS_EYE_LEVEL_POSITIONING ? player.getEyeLocation() : player.getLocation();
            display.setLocation(new DecentLocation(
                    playerLocation.getWorld().getName(),
                    playerLocation.getX(),
                    playerLocation.getY(),
                    playerLocation.getZ(),
                    displayLocation.getYaw(),
                    displayLocation.getPitch()
            ));
            displayService.updateDisplay(display);
            displayService.saveDisplay(display);
            Lang.DISPLAY_MOVED.send(sender, display.getName());
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

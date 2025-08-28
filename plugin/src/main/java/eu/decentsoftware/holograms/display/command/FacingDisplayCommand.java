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
import eu.decentsoftware.holograms.display.DecentLocation;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.plugin.Validator;

@CommandInfo(
        usage = "/dh d set-facing <name> <yaw> [pitch]",
        description = "Set the facing direction of a display.",
        permissions = {"dh.command.displays.facing"},
        aliases = {"setfacing", "facing", "setface", "face"}
)
class FacingDisplayCommand extends DecentCommand {

    private final DisplayService displayService;

    FacingDisplayCommand(DisplayService displayService) {
        super("set-facing");
        this.displayService = displayService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            if (args.length < 2) {
                Lang.USE_HELP.send(sender);
                return true;
            }

            String name = args[0];
            DisplayBase<?> display = displayService.getDisplay(name);
            if (display == null) {
                Lang.DISPLAY_DOES_NOT_EXIST.send(sender, name);
                return true;
            }

            DecentLocation location = display.getLocation();
            float yaw = Validator.getFloat(args[1], -180.0f, 180.0f, "Yaw must be a valid number between -180 and 180.");
            float pitch = args.length > 2
                    ? Validator.getFloat(args[2], -90.0f, 90.0f, "Pitch must be a valid number between -90 and 90.")
                    : location.getPitch();
            display.setLocation(new DecentLocation(
                    location.getWorldName(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    yaw,
                    pitch
            ));
            displayService.updateDisplayLocation(display);
            displayService.saveDisplay(display);
            Lang.DISPLAY_FACING_SET.send(sender, name);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 1) {
                return TabCompleteHandler.getPartialMatches(args[0], displayService.getRegisteredDisplayNames());
            } else if (args.length == 2) {
                return TabCompleteHandler.getPartialMatches(args[1], "0", "45", "90", "135", "180", "-45", "-90", "-135");
            } else if (args.length == 3) {
                return TabCompleteHandler.getPartialMatches(args[2], "0", "45", "90", "-45", "-90");
            }
            return null;
        };
    }
}

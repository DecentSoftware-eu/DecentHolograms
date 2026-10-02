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

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.Permissions;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.plugin.Validator;

import java.util.Collections;
import java.util.List;
import java.util.function.ToDoubleFunction;

@CommandInfo(
        usage = "/dh d move <name> <x> <y> <z>",
        description = "Move an existing display",
        permissions = {Permissions.COMMAND_DISPLAYS_MOVE},
        aliases = {"mv"},
        minArgs = 4
)
class MoveDisplayCommand extends DecentCommand {

    private final DisplayService displayService;

    MoveDisplayCommand(DisplayService displayService) {
        super("move");
        this.displayService = displayService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(4, args);
            DisplayBase display = Validator.getDisplay(displayService, args[0]);

            DecentLocation location = display.getLocation();
            double x = Validator.getLocationValue(args[1], location.getX());
            double y = Validator.getLocationValue(args[2], location.getY());
            double z = Validator.getLocationValue(args[3], location.getZ());
            display.setLocation(new DecentLocation(location.getWorldName(), x, y, z, location.getYaw(), location.getPitch()));
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
            } else if (args.length == 2 && Validator.isPlayer(sender)) {
                return getCoordinateSuggestions(args, DecentLocation::getX);
            } else if (args.length == 3 && Validator.isPlayer(sender)) {
                return getCoordinateSuggestions(args, DecentLocation::getY);
            } else if (args.length == 4 && Validator.isPlayer(sender)) {
                return getCoordinateSuggestions(args, DecentLocation::getZ);
            }
            return null;
        };
    }

    private List<String> getCoordinateSuggestions(String[] args, ToDoubleFunction<DecentLocation> getCurrentCoordinate) {
        DisplayBase display = displayService.getDisplay(args[0]);
        DecentLocation location = display == null ? null : display.getLocation();
        if (location != null) {
            return Lists.newArrayList(String.valueOf(getCurrentCoordinate.applyAsDouble(location)), "~");
        }
        return Collections.emptyList();
    }
}

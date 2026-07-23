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
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.platform.api.capability.PlatformMaterialService;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.plugin.Validator;

@CommandInfo(
        usage = "/dh d set-block <name> <block_type>",
        description = "Set the displayed block in a Block Display.",
        permissions = {Permissions.COMMAND_DISPLAYS_SET_BLOCK},
        aliases = {"setblock", "block"},
        minArgs = 2
)
class BlockDisplaySetBlockCommand extends DecentCommand {

    private final DisplayService displayService;
    private final PlatformMaterialService materialService;

    BlockDisplaySetBlockCommand(DisplayService displayService, PlatformMaterialService materialService) {
        super("set-block");
        this.displayService = displayService;
        this.materialService = materialService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(2, args);
            DisplayBase display = Validator.getDisplayOfType(displayService, args[0], DisplayType.BLOCK);

            String materialNamespacedKey = materialService.toMojangNamespacedKey(args[1]);
            if (materialNamespacedKey == null || !materialService.isBlock(materialNamespacedKey)) {
                Lang.DISPLAY_INVALID_BLOCK_TYPE.send(sender, materialNamespacedKey);
                return true;
            }

            BlockDisplay blockDisplay = (BlockDisplay) display;
            blockDisplay.setMaterial(materialNamespacedKey);
            displayService.updateDisplay(display);
            displayService.saveDisplay(display);
            Lang.DISPLAY_BLOCK_SET.send(sender, display.getName(), materialNamespacedKey);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 1) {
                return TabCompleteHandler.getPartialMatches(args[0], displayService.getRegisteredDisplayNames());
            } else if (args.length == 2) {
                return TabCompleteHandler.getPartialMatches(args[1], materialService.getBlockMaterialNames());
            }
            return null;
        };
    }
}

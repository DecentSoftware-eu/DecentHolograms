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
import eu.decentsoftware.holograms.display.DisplayCloneService;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.display.command.attribute.CommandAttributeService;
import eu.decentsoftware.holograms.display.text.TextDisplayViewService;

@CommandInfo(
        usage = "/dh displays help",
        description = "All commands for editing displays.",
        permissions = {"dh.command.displays"},
        aliases = {"display", "d"}
)
public class DisplaysCommand extends DecentCommand {

    private static DisplaysCommand instance;

    public static DisplaysCommand getInstance() {
        return instance;
    }

    public DisplaysCommand(DisplayService displayService, TextDisplayViewService textDisplayViewService) {
        super("displays");
        instance = this;

        addSubCommand(new DisplaysHelpCommand());
        addSubCommand(new CreateDisplayCommand(displayService));
        addSubCommand(new DeleteDisplayCommand(displayService));
        addSubCommand(new MoveDisplayCommand(displayService));
        DisplayCloneService displayCloneService = new DisplayCloneService();
        addSubCommand(new RenameDisplayCommand(displayService, displayCloneService));
        addSubCommand(new CloneDisplayCommand(displayService, displayCloneService));
        addSubCommand(new EnableDisplayCommand(displayService));
        addSubCommand(new DisableDisplayCommand(displayService));
        addSubCommand(new DisplayRangeDisplayCommand(displayService));
        addSubCommand(new UpdateIntervalDisplayCommand(displayService));
        CommandAttributeService attributeService = new CommandAttributeService();
        addSubCommand(new SetAttributeDisplayCommand(displayService, attributeService));
        addSubCommand(new ResetAttributeDisplayCommand(displayService, attributeService));
        addSubCommand(new GetAttributeDisplayCommand(displayService, attributeService));
        addSubCommand(new CenterDisplayCommand(displayService));
        addSubCommand(new MoveHereDisplayCommand(displayService));
        addSubCommand(new TeleportDisplayCommand(displayService));
        addSubCommand(new FacingDisplayCommand(displayService));
        addSubCommand(new ListDisplaysCommand(displayService));
        addSubCommand(new NearbyDisplaysCommand(displayService));
        addSubCommand(new BlockDisplaySetBlockCommand(displayService));
        addSubCommand(new ItemDisplaySetItemCommand(displayService));
        DisplayTabCompleteHelper tabCompleteHelper = new DisplayTabCompleteHelper(displayService);
        addSubCommand(new TextDisplayAddLineCommand(displayService, tabCompleteHelper));
        addSubCommand(new TextDisplayInsertLineCommand(displayService, tabCompleteHelper));
        addSubCommand(new TextDisplayRemoveLineCommand(displayService, tabCompleteHelper));
        addSubCommand(new TextDisplaySetLineCommand(displayService, tabCompleteHelper));
        // Pages disabled
//        addSubCommand(new TextDisplayAddPageCommand(displayService, tabCompleteHelper));
//        addSubCommand(new TextDisplayInsertPageCommand(displayService, tabCompleteHelper));
//        addSubCommand(new TextDisplayRemovePageCommand(displayService, tabCompleteHelper));
//        addSubCommand(new TextDisplaySwitchPageCommand(displayService, tabCompleteHelper, textDisplayViewService));
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            if (args.length == 0) {
                Lang.USE_HELP.send(sender);
                return true;
            }
            Lang.UNKNOWN_SUB_COMMAND.send(sender);
            Lang.USE_HELP.send(sender);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return null;
    }
}

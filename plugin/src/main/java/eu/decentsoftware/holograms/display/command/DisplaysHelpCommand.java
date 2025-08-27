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

import eu.decentsoftware.holograms.api.commands.CommandBase;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.utils.Common;

@CommandInfo(
        usage = "/dh d help",
        description = "Show general displays help.",
        permissions = {"dh.display.command.help"},
        aliases = {"?"}
)
class DisplaysHelpCommand extends DecentCommand {

    DisplaysHelpCommand() {
        super("help");
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            sender.sendMessage("");
            Common.tell(sender, " &3&lDECENT DISPLAYS HELP");
            Common.tell(sender, " All general commands.");
            sender.sendMessage("");
            CommandBase command = DisplaysCommand.getInstance();
            printHelpSubCommandsAndAliases(sender, command);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return null;
    }
}

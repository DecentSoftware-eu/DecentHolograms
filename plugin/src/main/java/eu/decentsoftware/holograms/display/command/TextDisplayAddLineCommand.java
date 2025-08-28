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
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.TextDisplay;

import java.util.Arrays;

@CommandInfo(
        usage = "/dh d addline <name> <text>",
        description = "Add a line of text to a Text Display.",
        permissions = {"dh.command.displays.text.addline"},
        aliases = {"appendline"}
)
class TextDisplayAddLineCommand extends DecentCommand {

    private final DisplayService displayService;

    TextDisplayAddLineCommand(DisplayService displayService) {
        super("addline");
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
            if (!(display instanceof TextDisplay)) {
                Lang.DISPLAY_WRONG_TYPE.send(sender, DisplayType.TEXT.name());
                return true;
            }

            String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            TextDisplay textDisplay = (TextDisplay) display;
            textDisplay.addLine(text);
            displayService.updateDisplayContent(display);
            displayService.saveDisplay(display);
            Lang.DISPLAY_TEXT_LINE_ADDED.send(sender, name);
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

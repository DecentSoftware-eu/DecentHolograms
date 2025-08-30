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
import eu.decentsoftware.holograms.plugin.Validator;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandInfo(
        usage = "/dh d setline <name> <index> <text>",
        description = "Set a line of text in a Text Display.",
        permissions = {"dh.command.displays.text.setline"}
)
class TextDisplaySetLineCommand extends DecentCommand {

    private final DisplayService displayService;

    TextDisplaySetLineCommand(DisplayService displayService) {
        super("setline");
        this.displayService = displayService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(3, args);
            DisplayBase<?> display = Validator.getDisplayOfType(displayService, args[0], DisplayType.TEXT);

            TextDisplay textDisplay = (TextDisplay) display;
            int index = Validator.getInteger(args[1], 1, textDisplay.getLines().size(), "Line index out of bounds.");
            String text = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            textDisplay.setLine(index - 1, text);
            displayService.updateDisplayContent(display);
            displayService.saveDisplay(display);
            Lang.DISPLAY_TEXT_LINE_SET.send(sender, display.getName(), index);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 1) {
                return TabCompleteHandler.getPartialMatches(args[0], displayService.getRegisteredDisplayNames());
            } else if (args.length == 2) {
                DisplayBase<?> display = displayService.getDisplay(args[0]);
                if (!(display instanceof TextDisplay)) {
                    return null;
                }
                TextDisplay textDisplay = (TextDisplay) display;
                return TabCompleteHandler.getPartialMatches(args[1], TabCompleteHandler.getPartialMatches(args[1], IntStream
                        .rangeClosed(1, textDisplay.getLines().size())
                        .boxed().map(String::valueOf)
                        .collect(Collectors.toList())));
            }
            return null;
        };
    }
}

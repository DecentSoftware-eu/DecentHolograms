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
import eu.decentsoftware.holograms.display.TextDisplayPage;
import eu.decentsoftware.holograms.plugin.Validator;

import java.util.Arrays;

@CommandInfo(
        usage = "/dh d insertpage <name> <index> [text]",
        description = "Insert a page of text to a Text Display.",
        permissions = {"dh.command.displays.text.insertpage"},
        minArgs = 2
)
class TextDisplayInsertPageCommand extends DecentCommand {

    private final DisplayService displayService;
    private final DisplayTabCompleteHelper tabCompleteHelper;

    TextDisplayInsertPageCommand(DisplayService displayService, DisplayTabCompleteHelper tabCompleteHelper) {
        super("insertpage");
        this.displayService = displayService;
        this.tabCompleteHelper = tabCompleteHelper;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(2, args);
            DisplayBase display = Validator.getDisplayOfType(displayService, args[0], DisplayType.TEXT);

            TextDisplay textDisplay = (TextDisplay) display;
            int pageIndex = Validator.getInteger(args[1], 1, textDisplay.getPages().size(), "Page index out of bounds.");
            String text = Validator.getLineContent(args, 2);
            TextDisplayPage page = new TextDisplayPage();
            page.setLines(Arrays.asList(text.split("\\\\n")));
            textDisplay.addPage(pageIndex, page);
            displayService.updateDisplayContent(display);
            displayService.saveDisplay(display);
            Lang.DISPLAY_TEXT_PAGE_INSERTED.send(sender, display.getName());
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 1) {
                return tabCompleteHelper.getDisplayNames(args[0]);
            } else if (args.length == 2) {
                return tabCompleteHelper.getPageIndexes(args[0], args[1]);
            }
            return null;
        };
    }
}

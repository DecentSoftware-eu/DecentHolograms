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

import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.display.TextDisplayPage;
import eu.decentsoftware.holograms.plugin.Validator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DisplayTabCompleteHelper {

    private final DisplayService displayService;

    public DisplayTabCompleteHelper(DisplayService displayService) {
        this.displayService = displayService;
    }

    public List<String> getDisplayNames(String input) {
        return TabCompleteHandler.getPartialMatches(input, displayService.getRegisteredDisplayNames());
    }

    public List<String> getPageIndexes(String displayName, String input) {
        DisplayBase display = displayService.getDisplay(displayName);
        if (!(display instanceof TextDisplay)) {
            return Collections.emptyList();
        }
        TextDisplay textDisplay = (TextDisplay) display;
        return TabCompleteHandler.getPartialMatches(input, TabCompleteHandler.getPartialMatches(input, IntStream
                .rangeClosed(1, textDisplay.getPages().size())
                .boxed().map(String::valueOf)
                .collect(Collectors.toList())));
    }

    public List<String> getLineIndexes(String displayName, String pageIndex, String input) {
        DisplayBase display = displayService.getDisplay(displayName);
        if (!(display instanceof TextDisplay)) {
            return Collections.emptyList();
        }
        TextDisplay textDisplay = (TextDisplay) display;
        TextDisplayPage page = textDisplay.getPage(Validator.getInteger(pageIndex));
        if (page == null) {
            return Collections.emptyList();
        }
        return TabCompleteHandler.getPartialMatches(input, TabCompleteHandler.getPartialMatches(input, IntStream
                .rangeClosed(1, page.getLines().size())
                .boxed().map(String::valueOf)
                .collect(Collectors.toList())));
    }
}

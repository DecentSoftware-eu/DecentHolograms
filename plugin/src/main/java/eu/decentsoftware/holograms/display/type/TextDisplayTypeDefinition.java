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

package eu.decentsoftware.holograms.display.type;

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.display.TextDisplayPage;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.placeholder.DisplayPlaceholderService;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayContent;
import org.bukkit.ChatColor;

public class TextDisplayTypeDefinition implements DisplayTypeDefinition<String> {

    private final DisplayPlaceholderService displayPlaceholderService;

    public TextDisplayTypeDefinition(DisplayPlaceholderService displayPlaceholderService) {
        this.displayPlaceholderService = displayPlaceholderService;
    }

    @Override
    public DisplayType getType() {
        return DisplayType.TEXT;
    }

    @Override
    public DisplayContent<String> resolveContent(DisplayBase display, DisplayRenderContext context) {
        TextDisplay textDisplay = getTextDisplay(display);

        TextDisplayPage page = textDisplay.getPage(context.getPage());
        String contentString = String.join(ChatColor.RESET + "\n", page.getLines());
        String resolvedContent = displayPlaceholderService.replacePlaceholders(contentString, context);
        return new TextDisplayContent(resolvedContent);
    }

    private TextDisplay getTextDisplay(DisplayBase displayBase) {
        if (!(displayBase instanceof TextDisplay)) {
            throw new IllegalArgumentException("Display is not a text display");
        }
        return (TextDisplay) displayBase;
    }
}

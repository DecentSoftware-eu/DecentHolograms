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

package eu.decentsoftware.holograms.display.text;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.display.TextDisplay;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class TextProcessingService {

    private final TextDisplayViewService viewService;
    private final AnimationManager animationManager;

    public TextProcessingService(TextDisplayViewService viewService, AnimationManager animationManager) {
        this.viewService = viewService;
        this.animationManager = animationManager;
    }

    public String processText(TextDisplay display, Player player, boolean updatePlaceholders) {
        TextDisplayView view = viewService.getView(display.getName(), player.getUniqueId());
        updateCachedProcessedTextIfNecessary(display, player, updatePlaceholders, view);

        CachedProcessedText cachedProcessedText = view.getCachedProcessedText();
        String processedText = cachedProcessedText.getProcessedText();
        if (cachedProcessedText.containsAnimations()) {
            processedText = animationManager.parseTextAnimations(processedText);
        }
        processedText = Common.colorize(processedText);
        return processedText;
    }

    private void updateCachedProcessedTextIfNecessary(TextDisplay display, Player player, boolean updatePlaceholders, TextDisplayView view) {
        CachedProcessedText cachedProcessedText = view.getCachedProcessedText();
        if (updatePlaceholders || cachedProcessedText == null) {
            List<String> lines = display.getPage(view.getCurrentPage()).getLines();
            CachedProcessedText updatedCachedProcessedText = processText(player, lines);
            view.setCachedProcessedText(updatedCachedProcessedText);
        }
    }

    private CachedProcessedText processText(Player player, List<String> lines) {
        String processedText = linesToString(lines);
        processedText = replacePlaceholders(player, processedText);
        boolean containsAnimations = animationManager.containsAnimations(processedText);
        return CachedProcessedText.of(processedText, containsAnimations);
    }

    private String replacePlaceholders(Player player, String processedText) {
        return PAPI.setPlaceholders(player, processedText);
    }

    private String linesToString(List<String> lines) {
        return String.join(ChatColor.RESET + "\n", lines);
    }
}

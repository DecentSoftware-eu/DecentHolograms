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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextProcessingService {

    private final TextDisplayViewService viewService;
    private final AnimationManager animationManager;

    public TextProcessingService(TextDisplayViewService viewService, AnimationManager animationManager) {
        this.viewService = viewService;
        this.animationManager = animationManager;
    }

    public List<String> processText(TextDisplay display, Player player, boolean updatePlaceholders) {
        TextDisplayView view = viewService.getView(display.getName(), player.getUniqueId());
        CachedProcessedText cachedProcessedText = view.getCachedProcessedText();
        List<String> processedText;
        if (updatePlaceholders || cachedProcessedText == null) {
            boolean containsAnimations = false;
            List<String> processedLines = new ArrayList<>();
            for (String line : display.getPage(view.getCurrentPage()).getLines()) {
                processedLines.add(PAPI.setPlaceholders(player, line));
                if (animationManager.containsAnimations(line)) {
                    containsAnimations = true;
                }
            }
            processedText = processedLines;
            cachedProcessedText = CachedProcessedText.of(processedText, containsAnimations);
            view.setCachedProcessedText(cachedProcessedText);
        }

        boolean containsAnimations = cachedProcessedText.containsAnimations();
        return cachedProcessedText.getProcessedText().stream()
                .map(line -> containsAnimations ? animationManager.parseTextAnimations(line) : line)
                .map(Common::colorize)
                .collect(Collectors.toList());
    }
}

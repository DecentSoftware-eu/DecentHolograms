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

package eu.decentsoftware.holograms.display.rendering;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.display.TextDisplay;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TextProcessingService {

    private final Map<DisplayAndPlayerKey, CachedProcessedText> processedTextCache = new ConcurrentHashMap<>();
    private final AnimationManager animationManager;

    public TextProcessingService(AnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    public List<String> processText(TextDisplay display, Player player, boolean updatePlaceholders) {
        DisplayAndPlayerKey key = DisplayAndPlayerKey.of(display, player);
        CachedProcessedText cachedProcessedText = processedTextCache.get(key);
        List<String> processedText;
        if (updatePlaceholders || cachedProcessedText == null) {
            boolean containsAnimations = false;
            List<String> processedLines = new ArrayList<>();
            for (String line : display.getLines()) {
                processedLines.add(PAPI.setPlaceholders(player, line));
                if (animationManager.containsAnimations(line)) {
                    containsAnimations = true;
                }
            }
            processedText = processedLines;
            cachedProcessedText = CachedProcessedText.of(processedText, containsAnimations);
            processedTextCache.put(key, cachedProcessedText);
        }

        boolean containsAnimations = cachedProcessedText.containsAnimations();
        return cachedProcessedText.getProcessedText().stream()
                .map(line -> containsAnimations ? animationManager.parseTextAnimations(line) : line)
                .map(Common::colorize)
                .collect(Collectors.toList());
    }

    private static class DisplayAndPlayerKey {
        private final String displayName;
        private final UUID playerUniqueId;

        private DisplayAndPlayerKey(String displayName, UUID playerUniqueId) {
            this.displayName = displayName;
            this.playerUniqueId = playerUniqueId;
        }

        public static DisplayAndPlayerKey of(TextDisplay display, Player player) {
            return new DisplayAndPlayerKey(display.getName(), player.getUniqueId());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DisplayAndPlayerKey that = (DisplayAndPlayerKey) o;
            return displayName.equals(that.displayName) && playerUniqueId.equals(that.playerUniqueId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(displayName, playerUniqueId);
        }
    }

    private static class CachedProcessedText {
        private final List<String> processedText;
        private final boolean containsAnimations;

        private CachedProcessedText(List<String> processedText, boolean containsAnimations) {
            this.processedText = processedText;
            this.containsAnimations = containsAnimations;
        }

        public static CachedProcessedText of(List<String> processedText, boolean containsAnimations) {
            return new CachedProcessedText(processedText, containsAnimations);
        }

        public List<String> getProcessedText() {
            return processedText;
        }

        public boolean containsAnimations() {
            return containsAnimations;
        }
    }
}

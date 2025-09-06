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

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TextDisplayViewService {

    private final Map<DisplayViewKey, TextDisplayView> views = new ConcurrentHashMap<>();

    public void shutdown() {
        views.clear();
    }

    public void createView(String displayName, UUID playerUniqueId) {
        views.put(new DisplayViewKey(displayName, playerUniqueId), new TextDisplayView());
    }

    public boolean hasView(String displayName, UUID playerUniqueId) {
        return views.containsKey(new DisplayViewKey(displayName, playerUniqueId));
    }

    @NotNull
    public TextDisplayView getView(String displayName, UUID playerUniqueId) {
        return views.computeIfAbsent(new DisplayViewKey(displayName, playerUniqueId), key -> new TextDisplayView());
    }

    public void clearView(String displayName, UUID playerUniqueId) {
        views.remove(new DisplayViewKey(displayName, playerUniqueId));
    }

    private static class DisplayViewKey {
        private final String displayName;
        private final UUID playerUniqueId;

        private DisplayViewKey(String displayName, UUID playerUniqueId) {
            this.displayName = displayName;
            this.playerUniqueId = playerUniqueId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DisplayViewKey that = (DisplayViewKey) o;
            return displayName.equals(that.displayName) && playerUniqueId.equals(that.playerUniqueId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(displayName, playerUniqueId);
        }
    }
}

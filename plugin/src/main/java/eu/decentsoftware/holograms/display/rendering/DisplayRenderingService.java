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

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayVisibilityService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class DisplayRenderingService {

    private final DisplayVisibilityService visibilityService;
    private final Map<String, DisplayRenderingAdapter<? extends DisplayBase>> renderingContexts;
    private final DisplayRenderingAdapterFactory renderingAdapterFactory;

    public DisplayRenderingService(DisplayVisibilityService visibilityService, DisplayRenderingAdapterFactory renderingAdapterFactory) {
        this.visibilityService = visibilityService;
        this.renderingAdapterFactory = renderingAdapterFactory;
        this.renderingContexts = new ConcurrentHashMap<>();
    }

    public void hideDisplayForPlayer(DisplayBase display, Player player) {
        if (visibilityService.isShownToPlayer(display, player)) {
            hideForPlayer(display, player);
        }
    }

    public void hideForEveryone(DisplayBase display) {
        Bukkit.getOnlinePlayers().forEach(player -> hideForPlayer(display, player));
    }

    public void updateVisibility(DisplayBase display) {
        Bukkit.getOnlinePlayers().forEach(player -> updateVisibility(display, player));
    }

    public void updateVisibility(DisplayBase display, Player player) {
        boolean shouldBeShownToPlayer = visibilityService.shouldBeShownToPlayer(display, player);
        boolean isShownToPlayer = visibilityService.isShownToPlayer(display, player);
        if (shouldBeShownToPlayer && !isShownToPlayer) {
            showForPlayer(display, player);
        } else if (!shouldBeShownToPlayer && isShownToPlayer) {
            hideForPlayer(display, player);
        }
    }

    public void updateContent(DisplayBase display) {
        updateContent(display, true);
    }

    public void updateContentAnimations(DisplayBase display) {
        updateContent(display, false);
    }

    private void updateContent(DisplayBase display, boolean fullUpdate) {
        performForAllViewers(display, (player, adapter) -> adapter.updateContent(display, player, fullUpdate));
    }

    public void updateProperties(DisplayBase display) {
        performForAllViewers(display, (player, adapter) -> adapter.updateProperties(display, player));
    }

    public void updateDisplayLocation(DisplayBase display) {
        performForAllViewers(display, (player, adapter) -> adapter.move(display, player));
    }

    private <T extends DisplayBase> void performForAllViewers(T display, BiConsumer<Player, DisplayRenderingAdapter<T>> consumer) {
        for (Player onlinePlayer : visibilityService.getViewersAsPlayers(display)) {
            DisplayRenderingAdapter<T> adapter = getAdapter(display);
            consumer.accept(onlinePlayer, adapter);
        }
    }

    private <T extends DisplayBase> void showForPlayer(T display, Player player) {
        DisplayRenderingAdapter<T> adapter = getAdapter(display);
        adapter.display(display, player);
        visibilityService.addViewer(display, player);
    }

    private <T extends DisplayBase> void hideForPlayer(T display, Player player) {
        DisplayRenderingAdapter<T> adapter = getAdapter(display);
        adapter.hide(display, player);
        visibilityService.removeViewer(display, player);
    }

    @SuppressWarnings("unchecked")
    private <T extends DisplayBase> DisplayRenderingAdapter<T> getAdapter(T display) {
        return (DisplayRenderingAdapter<T>) renderingContexts.computeIfAbsent(display.getName(),
                name -> renderingAdapterFactory.createAdapter(display));
    }
}

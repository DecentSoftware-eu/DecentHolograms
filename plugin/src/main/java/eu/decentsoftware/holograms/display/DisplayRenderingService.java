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

package eu.decentsoftware.holograms.display;

import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsDisplayRenderer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class DisplayRenderingService {

    private final DisplayVisibilityService visibilityService;
    private final DisplayDataMapper displayDataMapper;

    public DisplayRenderingService(DisplayVisibilityService visibilityService, DisplayDataMapper displayDataMapper) {
        this.visibilityService = visibilityService;
        this.displayDataMapper = displayDataMapper;
    }

    public void hideDisplayForPlayer(DisplayBase<?> display, Player player) {
        if (visibilityService.isShownToPlayer(display, player)) {
            hideForPlayer(display, player);
        }
    }

    public void hideForEveryone(DisplayBase<?> display) {
        Bukkit.getOnlinePlayers().forEach(player -> hideForPlayer(display, player));
    }

    public void updateVisibility(DisplayBase<?> display) {
        Bukkit.getOnlinePlayers().forEach(player -> updateVisibility(display, player));
    }

    public void updateVisibility(DisplayBase<?> display, Player player) {
        boolean shouldBeShownToPlayer = visibilityService.shouldBeShownToPlayer(display, player);
        boolean isShownToPlayer = visibilityService.isShownToPlayer(display, player);
        if (shouldBeShownToPlayer && !isShownToPlayer) {
            showForPlayer(display, player);
        } else if (!shouldBeShownToPlayer && isShownToPlayer) {
            hideForPlayer(display, player);
        }
    }

    private <T> void showForPlayer(DisplayBase<T> display, Player player) {
        NmsDisplayRenderer<T> renderer = display.getDisplayRenderer();
        renderer.display(player, getPartData(display, player));
        visibilityService.addViewer(display, player);
    }

    public <T> void updateContent(DisplayBase<T> display) {
        performForAllViewers(display, (player, renderer) -> renderer.updateContent(player, getPartData(display, player)));
    }

    public <T> void updateProperties(DisplayBase<T> display) {
        performForAllViewers(display, (player, renderer) -> renderer.updateProperties(player, getPartData(display, player)));
    }

    public <T> void updateDisplayLocation(DisplayBase<T> display) {
        performForAllViewers(display, (player, renderer) -> renderer.move(player, getPartData(display, player)));
    }

    private <T> void performForAllViewers(DisplayBase<T> display, BiConsumer<Player, NmsDisplayRenderer<T>> consumer) {
        for (Player onlinePlayer : visibilityService.getViewersAsPlayers(display)) {
            NmsDisplayRenderer<T> renderer = display.getDisplayRenderer();
            consumer.accept(onlinePlayer, renderer);
        }
    }

    private void hideForPlayer(DisplayBase<?> display, Player player) {
        display.getDisplayRenderer().hide(player);
        visibilityService.removeViewer(display, player);
    }

    private <T> NmsHologramPartData<T> getPartData(DisplayBase<T> display, Player player) {
        return new NmsHologramPartData<>(
                () -> display.getLocation().toDecentPosition(),
                () -> displayDataMapper.map(display, player)
        );
    }
}

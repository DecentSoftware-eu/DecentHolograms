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
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class DisplayRenderingService {

    private final DisplayVisibilityService visibilityService;

    public DisplayRenderingService(DisplayVisibilityService visibilityService) {
        this.visibilityService = visibilityService;
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
        if (visibilityService.shouldBeShownToPlayer(display, player)) {
            if (!visibilityService.isShownToPlayer(display, player)) {
                showForPlayer(display, player);
            }
        } else {
            if (visibilityService.isShownToPlayer(display, player)) {
                hideForPlayer(display, player);
            }
        }
    }

    private void showForPlayer(DisplayBase display, Player player) {
        NmsDisplayRenderer renderer = display.getDisplayRenderer();
        renderer.display(player, getPartData(display, player));
        visibilityService.addViewer(display, player);
    }

    public void updateContent(DisplayBase display) {
        performForAllViewers(display, (player, renderer) -> renderer.updateContent(player, getPartData(display, player)));
    }

    public void updateProperties(DisplayBase display) {
        performForAllViewers(display, (player, renderer) -> renderer.updateProperties(player, getPartData(display, player)));
    }

    public void updateDisplayLocation(DisplayBase display) {
        performForAllViewers(display, (player, renderer) -> renderer.move(player, getPartData(display, player)));
    }

    private void performForAllViewers(DisplayBase display, BiConsumer<Player, NmsDisplayRenderer> consumer) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (visibilityService.isShownToPlayer(display, onlinePlayer)) {
                NmsDisplayRenderer renderer = display.getDisplayRenderer();
                consumer.accept(onlinePlayer, renderer);
            }
        }
    }

    private void hideForPlayer(DisplayBase display, Player player) {
        display.getDisplayRenderer().hide(player);
        visibilityService.removeViewer(display, player);
    }

    private NmsHologramPartData getPartData(DisplayBase display, Player player) {
        return new NmsHologramPartData<>(
                () -> {
                    DecentLocation location = display.getLocation();
                    return new DecentPosition(
                            location.getX(),
                            location.getY(),
                            location.getZ(),
                            location.getYaw(),
                            location.getPitch()
                    );
                },
                () -> display.createDisplayData(player)
        );
    }
}

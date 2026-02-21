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

package eu.decentsoftware.holograms.display.render;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayVisibilityService;
import eu.decentsoftware.holograms.display.TextDisplayPlayerPageManager;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderStateService;
import eu.decentsoftware.holograms.display.render.state.LogicalDisplayRenderState;
import eu.decentsoftware.holograms.display.render.state.LogicalDisplayRenderStateManager;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayerService;
import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;

public class DisplayRenderingService {

    private final DisplayVisibilityService visibilityService;
    private final PlatformPlayerService playerService;
    private final DisplayRenderStateService stateService;
    private final DisplayRenderService renderService;
    private final TextDisplayPlayerPageManager pageManager;
    private final LogicalDisplayRenderStateManager logicalDisplayRenderStateManager;

    public DisplayRenderingService(DisplayVisibilityService visibilityService,
                                   PlatformPlayerService playerService,
                                   DisplayRenderStateService stateService,
                                   DisplayRenderService renderService,
                                   TextDisplayPlayerPageManager pageManager,
                                   LogicalDisplayRenderStateManager logicalDisplayRenderStateManager) {
        this.visibilityService = visibilityService;
        this.playerService = playerService;
        this.stateService = stateService;
        this.renderService = renderService;
        this.pageManager = pageManager;
        this.logicalDisplayRenderStateManager = logicalDisplayRenderStateManager;
    }

    public void hideDisplayForPlayer(DisplayBase display, PlatformPlayer player) {
        if (visibilityService.isShownToPlayer(display, player)) {
            hideForPlayer(display, player);
        }
    }

    public void hideForEveryone(DisplayBase display) {
        for (PlatformPlayer onlinePlayer : playerService.getOnlinePlayers()) {
            hideForPlayer(display, onlinePlayer);
        }
    }

    public void updateVisibility(DisplayBase display) {
        for (PlatformPlayer onlinePlayer : playerService.getOnlinePlayers()) {
            updateVisibility(display, onlinePlayer);
        }
    }

    public void updateVisibility(DisplayBase display, PlatformPlayer player) {
        boolean shouldBeShownToPlayer = visibilityService.shouldBeShownToPlayer(display, player);
        boolean isShownToPlayer = visibilityService.isShownToPlayer(display, player);
        if (shouldBeShownToPlayer && !isShownToPlayer) {
            renderForPlayer(display, player);
        } else if (!shouldBeShownToPlayer && isShownToPlayer) {
            hideForPlayer(display, player);
        }
    }

    public void update(DisplayBase display) {
        for (PlatformPlayer onlinePlayer : playerService.getOnlinePlayers()) {
            if (visibilityService.isShownToPlayer(display, onlinePlayer)) {
                renderForPlayer(display, onlinePlayer);
            }
        }
    }

    public void postProcess(DisplayBase display) {
        for (PlatformPlayer onlinePlayer : playerService.getOnlinePlayers()) {
            if (visibilityService.isShownToPlayer(display, onlinePlayer)) {
                renderLogicalState(display, onlinePlayer);
            }
        }
    }

    public void hideForPlayer(DisplayBase display, PlatformPlayer player) {
        updateLogicalState(display, player, false);
        visibilityService.removeViewer(display, player);
    }

    public void renderForPlayer(DisplayBase display, PlatformPlayer player) {
        updateLogicalState(display, player, true);
        visibilityService.addViewer(display, player);
    }

    private void updateLogicalState(DisplayBase display, PlatformPlayer player, boolean visible) {
        try {
            RenderObjectHandle handle = getRenderObjectHandle(display);
            DisplayRenderContext context = getDisplayRenderContext(display, player);
            LogicalDisplayRenderState state = stateService.buildRenderState(display, context);
            state.setVisible(visible);
            state.setChanged(true);

            logicalDisplayRenderStateManager.updateState(handle, context, state);
        } catch (Exception e) {
            Log.warn("Failed to update logical state of display '%s' for player '%s'.", e, display.getName(), player.getName());
        }
    }

    private void renderLogicalState(DisplayBase display, PlatformPlayer player) {
        try {
            RenderObjectHandle handle = getRenderObjectHandle(display);
            DisplayRenderContext context = getDisplayRenderContext(display, player);
            LogicalDisplayRenderState state = logicalDisplayRenderStateManager.getCurrentState(handle, context);
            if (state == null || (!state.isChanged() && !state.isNeedsPostProcessing())) {
                return;
            }

            if (state.isChanged()) {
                state.setChanged(false);
            }

            renderService.render(handle, state, context);
        } catch (Exception e) {
            Log.warn("Failed to render display '%s' for player '%s'.", e, display.getName(), player.getName());
        }
    }

    private RenderObjectHandle getRenderObjectHandle(DisplayBase display) {
        return new RenderObjectHandle(display.getName(), display.getType());
    }

    private DisplayRenderContext getDisplayRenderContext(DisplayBase display, PlatformPlayer player) {
        Integer page = pageManager.getPage(display.getName(), player.getUniqueId());
        return new DisplayRenderContext(player, page);
    }
}

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

import eu.decentsoftware.holograms.display.render.postprocessing.DisplayPostProcessingService;
import eu.decentsoftware.holograms.display.render.state.LogicalDisplayRenderState;
import eu.decentsoftware.holograms.display.render.state.MutableRenderState;
import eu.decentsoftware.holograms.display.render.state.MutableRenderStateManager;
import eu.decentsoftware.holograms.platform.api.render.PlatformRenderService;
import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.profiler.DecentProfiler;
import eu.decentsoftware.holograms.profiler.Metrics;
import eu.decentsoftware.holograms.profiler.TimerHandle;

import java.util.List;
import java.util.UUID;

public class DisplayRenderService {

    private final DisplayRenderIntentMaterializer diffService;
    private final PlatformRenderService platformRenderService;
    private final MutableRenderStateManager finalStateManager;
    private final DisplayPostProcessingService postProcessingService;

    public DisplayRenderService(DisplayRenderIntentMaterializer diffService,
                                PlatformRenderService platformRenderService,
                                MutableRenderStateManager finalStateManager,
                                DisplayPostProcessingService postProcessingService) {
        this.diffService = diffService;
        this.platformRenderService = platformRenderService;
        this.finalStateManager = finalStateManager;
        this.postProcessingService = postProcessingService;
    }

    public void render(RenderObjectHandle handle, LogicalDisplayRenderState logicalState, DisplayRenderContext context) {
        MutableRenderState previousState = getPreviousState(handle.getId(), context);
        MutableRenderState currentState = postProcessingService.postProcess(logicalState, previousState);
        if (logicalState != null && currentState != null && !currentState.hasChanges()) {
            return;
        }
        List<RenderIntent> intents = diffService.materializeIntents(currentState);
        if (intents.isEmpty()) {
            return; // No changes, skip rendering
        }

        try (TimerHandle ignored = DecentProfiler.getInstance().startTimer(Metrics.RENDER_PLATFORM)) {
            platformRenderService.render(context.getPlayer(), handle, intents);
        }

        if (previousState == null || currentState == null) {
            saveCurrentState(handle.getId(), currentState, context);
        }
    }

    private void saveCurrentState(String displayName, MutableRenderState state, DisplayRenderContext context) {
        UUID playerUniqueId = context.getPlayer().getUniqueId();
        finalStateManager.updateState(displayName, playerUniqueId, state);
    }

    private MutableRenderState getPreviousState(String displayName, DisplayRenderContext context) {
        UUID playerUniqueId = context.getPlayer().getUniqueId();
        return finalStateManager.getCurrentState(displayName, playerUniqueId);
    }
}

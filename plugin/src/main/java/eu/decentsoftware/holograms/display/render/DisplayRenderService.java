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
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderStateManager;
import eu.decentsoftware.holograms.display.render.state.LogicalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.PlatformAdapter;
import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.profiler.DecentProfiler;
import eu.decentsoftware.holograms.profiler.Metrics;
import eu.decentsoftware.holograms.profiler.TimerHandle;

import java.util.List;
import java.util.UUID;

public class DisplayRenderService {

    private final DisplayRenderDiffService diffService;
    private final PlatformAdapter platformAdapter;
    private final FinalDisplayRenderStateManager finalStateManager;
    private final DisplayPostProcessingService postProcessingService;

    public DisplayRenderService(DisplayRenderDiffService diffService,
                                PlatformAdapter platformAdapter,
                                FinalDisplayRenderStateManager finalStateManager,
                                DisplayPostProcessingService postProcessingService) {
        this.diffService = diffService;
        this.platformAdapter = platformAdapter;
        this.finalStateManager = finalStateManager;
        this.postProcessingService = postProcessingService;
    }

    public void render(RenderObjectHandle handle, LogicalDisplayRenderState state, DisplayRenderContext context) {
        FinalDisplayRenderState currentState = postProcessingService.postProcess(state);
        FinalDisplayRenderState previousState = getPreviousState(handle, context);
        List<RenderIntent> intents = diffService.diff(currentState, previousState);

        try (TimerHandle ignored = DecentProfiler.getInstance().startTimer(Metrics.RENDER_PLATFORM)) {
            platformAdapter.getRenderService().render(context.getPlayer(), handle, intents);
        }
        saveCurrentState(handle, currentState, context);
    }

    private void saveCurrentState(RenderObjectHandle handle, FinalDisplayRenderState state, DisplayRenderContext context) {
        UUID playerUniqueId = context.getPlayer().getUniqueId();
        finalStateManager.setState(playerUniqueId, handle, state);
    }

    private FinalDisplayRenderState getPreviousState(RenderObjectHandle handle, DisplayRenderContext context) {
        UUID playerUniqueId = context.getPlayer().getUniqueId();
        return finalStateManager.getState(playerUniqueId, handle);
    }
}

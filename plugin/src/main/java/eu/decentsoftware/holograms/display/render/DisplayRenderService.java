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

import eu.decentsoftware.holograms.display.render.intent.IntentDescriptor;
import eu.decentsoftware.holograms.display.render.intent.materializer.IntentMaterializerService;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderState;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderStateManager;
import eu.decentsoftware.holograms.platform.api.PlatformAdapter;
import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;

import java.util.List;
import java.util.UUID;

public class DisplayRenderService {

    private final DisplayRenderDiffService diffService;
    private final PlatformAdapter platformAdapter;
    private final DisplayRenderStateManager stateManager;
    private final IntentMaterializerService intentMaterializerService;

    public DisplayRenderService(DisplayRenderDiffService diffService,
                                PlatformAdapter platformAdapter,
                                DisplayRenderStateManager stateManager,
                                IntentMaterializerService intentMaterializerService) {
        this.diffService = diffService;
        this.platformAdapter = platformAdapter;
        this.stateManager = stateManager;
        this.intentMaterializerService = intentMaterializerService;
    }

    public void render(RenderObjectHandle handle, DisplayRenderState state, DisplayRenderContext context) {
        DisplayRenderState previousState = getPreviousState(handle, context);
        List<IntentDescriptor> intentDescriptors = diffService.diff(state, previousState);
        List<RenderIntent> intents = intentMaterializerService.materializeIntents(intentDescriptors, context);

        platformAdapter.getRenderService().render(context.getPlayer(), handle, intents);
        saveCurrentState(handle, state, context);
    }

    private void saveCurrentState(RenderObjectHandle handle, DisplayRenderState state, DisplayRenderContext context) {
        UUID playerUniqueId = context.getPlayer().getUniqueId();
        stateManager.setState(playerUniqueId, handle, state);
    }

    private DisplayRenderState getPreviousState(RenderObjectHandle handle, DisplayRenderContext context) {
        UUID playerUniqueId = context.getPlayer().getUniqueId();
        return stateManager.getState(playerUniqueId, handle);
    }
}

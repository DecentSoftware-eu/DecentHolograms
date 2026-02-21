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

import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.render.intent.DespawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.MoveRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.SpawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateDisplayContentRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateMetadataRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import eu.decentsoftware.holograms.profiler.DecentProfiler;
import eu.decentsoftware.holograms.profiler.Metrics;
import eu.decentsoftware.holograms.profiler.TimerHandle;

import java.util.ArrayList;
import java.util.List;

public class DisplayRenderDiffService {

    public List<RenderIntent> diff(FinalDisplayRenderState currentState, FinalDisplayRenderState previousState) {
        try (TimerHandle ignored = DecentProfiler.getInstance().startTimer(Metrics.RENDER_DIFF)) {
            return diffInternal(currentState, previousState);
        }
    }

    private List<RenderIntent> diffInternal(FinalDisplayRenderState currentState, FinalDisplayRenderState previousState) {
        List<RenderIntent> intentList = new ArrayList<>();
        if (previousState == null) {
            if (currentState != null) {
                intentList.add(new SpawnDisplayRenderIntent(
                        currentState.getLocation(),
                        currentState.getMetadataValues(),
                        currentState.getContent()
                ));
            }
            return intentList;
        } else if (currentState == null) {
            intentList.add(new DespawnDisplayRenderIntent());
            return intentList;
        }

        diffLocation(currentState, previousState, intentList);
        diffContent(currentState, previousState, intentList);
        diffMetadata(currentState, previousState, intentList);
        return intentList;
    }

    private void diffLocation(FinalDisplayRenderState currentState, FinalDisplayRenderState previousState, List<RenderIntent> intentList) {
        if (!currentState.getLocation().equals(previousState.getLocation())) {
            intentList.add(new MoveRenderIntent(currentState.getLocation()));
        }
    }

    private void diffContent(FinalDisplayRenderState currentState, FinalDisplayRenderState previousState, List<RenderIntent> intentList) {
        if (!currentState.getContent().equals(previousState.getContent())) {
            intentList.add(new UpdateDisplayContentRenderIntent(currentState.getContent()));
        }
    }

    private void diffMetadata(FinalDisplayRenderState currentState, FinalDisplayRenderState previousState, List<RenderIntent> intentList) {
        for (MetadataKey<?> metadataKey : currentState.getMetadataValues().keySet()) {
            diffMetadataValues(metadataKey, currentState, previousState, intentList);
        }
    }

    private <T> void diffMetadataValues(MetadataKey<T> key,
                                        FinalDisplayRenderState currentState,
                                        FinalDisplayRenderState previousState,
                                        List<RenderIntent> intentList) {
        MetadataValue<T> currentValue = currentState.getMetadataValue(key);
        MetadataValue<T> previousValue = previousState.getMetadataValue(key);
        if (previousValue == null || !key.areValuesEqual(currentValue, previousValue)) {
            intentList.add(new UpdateMetadataRenderIntent<>(key, currentValue));
        }
    }
}

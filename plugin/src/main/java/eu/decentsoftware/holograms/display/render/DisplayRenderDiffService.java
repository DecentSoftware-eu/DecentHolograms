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
import eu.decentsoftware.holograms.display.render.state.DisplayRenderState;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DisplayRenderDiffService {

    public List<IntentDescriptor> diff(@NotNull DisplayRenderState currentState, @Nullable DisplayRenderState previousState) {
        List<IntentDescriptor> intentList = new ArrayList<>();
        if (previousState == null || !previousState.isVisible()) {
            if (currentState.isVisible()) {
                intentList.add(new IntentDescriptor.SpawnDisplay(currentState));
            }
            return intentList;
        } else if (!currentState.isVisible()) {
            intentList.add(new IntentDescriptor.DespawnDisplay(currentState));
            return intentList;
        }

        diffLocation(currentState, previousState, intentList);
        diffContent(currentState, previousState, intentList);
        diffMetadata(currentState, previousState, intentList);
        return intentList;
    }

    private void diffLocation(DisplayRenderState currentState, DisplayRenderState previousState, List<IntentDescriptor> intentList) {
        if (!currentState.getLocation().equals(previousState.getLocation())) {
            intentList.add(new IntentDescriptor.Move(currentState));
        }
    }

    private void diffContent(DisplayRenderState currentState, DisplayRenderState previousState, List<IntentDescriptor> intentList) {
        if (!currentState.getContent().equals(previousState.getContent())) {
            intentList.add(new IntentDescriptor.UpdateDisplayContent(currentState));
        }
    }

    private void diffMetadata(DisplayRenderState currentState, DisplayRenderState previousState, List<IntentDescriptor> intentList) {
        for (MetadataKey<?> metadataKey : currentState.getMetadataValues().keySet()) {
            diffMetadataValues(metadataKey, currentState, previousState, intentList);
        }
    }

    private <T> void diffMetadataValues(MetadataKey<T> key,
                                        DisplayRenderState currentState,
                                        DisplayRenderState previousState,
                                        List<IntentDescriptor> intentList) {
        MetadataValue<T> currentValue = currentState.getMetadataValue(key);
        MetadataValue<T> previousValue = previousState.getMetadataValue(key);
        if (previousValue == null || !key.areValuesEqual(currentValue, previousValue)) {
            intentList.add(new IntentDescriptor.UpdateMetadata<>(currentState, key));
        }
    }
}

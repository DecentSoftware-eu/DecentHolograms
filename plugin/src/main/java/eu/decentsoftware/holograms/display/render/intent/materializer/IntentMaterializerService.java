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

package eu.decentsoftware.holograms.display.render.intent.materializer;

import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.intent.IntentDescriptor;
import eu.decentsoftware.holograms.display.render.postprocessing.DisplayContentPostProcessingService;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IntentMaterializerService {

    private final Map<Class<?>, IntentMaterializer<?>> materializers = new HashMap<>();

    public IntentMaterializerService(DisplayContentPostProcessingService postProcessingService) {
        registerDefaultMaterializers(postProcessingService);
    }

    private void registerDefaultMaterializers(DisplayContentPostProcessingService postProcessingService) {
        registerMaterializer(IntentDescriptor.UpdateMetadata.class, new UpdateMetadataIntentMaterializer());
        registerMaterializer(IntentDescriptor.UpdateDisplayContent.class, new UpdateDisplayContentIntentMaterializer(postProcessingService));
        registerMaterializer(IntentDescriptor.Move.class, new MoveIntentMaterializer());
        registerMaterializer(IntentDescriptor.SpawnDisplay.class, new SpawnDisplayIntentMaterializer(postProcessingService));
        registerMaterializer(IntentDescriptor.DespawnDisplay.class, new DespawnDisplayIntentMaterializer());
    }

    private <T extends IntentDescriptor> void registerMaterializer(Class<T> clazz, IntentMaterializer<T> materializer) {
        materializers.put(clazz, materializer);
    }

    public List<RenderIntent> materializeIntents(List<IntentDescriptor> intentDescriptors, DisplayRenderContext context) {
        return intentDescriptors.stream()
                .map(descriptor -> materializeIntent(descriptor, context))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked") // Safe because we control the registration of materializers
    private <T extends IntentDescriptor> IntentMaterializer<T> getMaterializer(Class<T> clazz) {
        return (IntentMaterializer<T>) materializers.get(clazz);
    }

    private <T extends IntentDescriptor> RenderIntent materializeIntent(T intentDescriptor, DisplayRenderContext context) {
        IntentMaterializer<T> materializer = getMaterializer(getIntentDescriptorClass(intentDescriptor));
        return materializer.materialize(intentDescriptor, context);
    }

    @SuppressWarnings("unchecked") // Safe because we are getting the class from the instance itself
    private <T extends IntentDescriptor> Class<T> getIntentDescriptorClass(T intentDescriptor) {
        return (Class<T>) intentDescriptor.getClass();
    }
}

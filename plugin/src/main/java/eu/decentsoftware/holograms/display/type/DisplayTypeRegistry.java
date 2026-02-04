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

package eu.decentsoftware.holograms.display.type;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.display.render.placeholder.DisplayPlaceholderService;
import eu.decentsoftware.holograms.display.render.postprocessing.processor.DisplayContentPostProcessor;
import eu.decentsoftware.holograms.display.render.postprocessing.processor.TextDisplayAnimationPostProcessor;
import eu.decentsoftware.holograms.display.render.postprocessing.processor.TextDisplayFormatPostProcessor;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DisplayTypeRegistry {

    private final Map<DisplayType, DisplayTypeDefinition<?>> definitions = new HashMap<>();

    public DisplayTypeRegistry(DisplayPlaceholderService displayPlaceholderService, AnimationManager animationManager) {
        registerDefaultTypes(displayPlaceholderService, animationManager);
    }

    private void registerDefaultTypes(DisplayPlaceholderService displayPlaceholderService, AnimationManager animationManager) {
        registerDisplayType(DisplayType.TEXT, initializeTextDisplayType(displayPlaceholderService, animationManager));
        registerDisplayType(DisplayType.ITEM, new ItemDisplayTypeDefinition());
        registerDisplayType(DisplayType.BLOCK, new BlockDisplayTypeDefinition());
    }

    private TextDisplayTypeDefinition initializeTextDisplayType(DisplayPlaceholderService displayPlaceholderService,
                                                                AnimationManager animationManager) {
        List<DisplayContentPostProcessor<String, DisplayContent<String>>> postProcessors = Collections.unmodifiableList(Arrays.asList(
                new TextDisplayAnimationPostProcessor(animationManager),
                new TextDisplayFormatPostProcessor()
        ));
        return new TextDisplayTypeDefinition(displayPlaceholderService, postProcessors, animationManager);
    }

    private <T extends DisplayTypeDefinition<?>> void registerDisplayType(DisplayType type, T definition) {
        this.definitions.put(type, definition);
    }

    @NotNull
    public DisplayTypeDefinition<?> getDefinition(@NotNull DisplayType type) {
        DisplayTypeDefinition<?> definition = this.definitions.get(type);
        if (definition == null) {
            throw new IllegalStateException("Unknown display type: " + type.name());
        }
        return definition;
    }
}

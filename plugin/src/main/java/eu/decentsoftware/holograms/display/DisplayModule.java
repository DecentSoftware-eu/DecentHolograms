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

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.display.attribute.AttributeCommandHandler;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import eu.decentsoftware.holograms.display.command.DisplaysCommand;
import eu.decentsoftware.holograms.display.config.DisplayConfigMapper;
import eu.decentsoftware.holograms.display.config.DisplayConfigService;
import eu.decentsoftware.holograms.display.config.DisplayPersistenceService;
import eu.decentsoftware.holograms.display.render.DisplayRenderDiffService;
import eu.decentsoftware.holograms.display.render.DisplayRenderService;
import eu.decentsoftware.holograms.display.render.DisplayRenderingService;
import eu.decentsoftware.holograms.display.render.intent.materializer.IntentMaterializerService;
import eu.decentsoftware.holograms.display.render.placeholder.DisplayPlaceholderService;
import eu.decentsoftware.holograms.display.render.postprocessing.DisplayContentPostProcessingService;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderStateManager;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderStateService;
import eu.decentsoftware.holograms.display.type.DisplayTypeRegistry;
import eu.decentsoftware.holograms.platform.api.PlatformAdapter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages the display module, including initialization, reloading, and shutdown.
 *
 * @author d0by
 */
public class DisplayModule {

    private final JavaPlugin plugin;
    private final DisplayService displayService;
    private final DisplayUpdater displayUpdater;
    private final DisplayListener displayListener;
    private final TextDisplayPlayerPageManager playerPageManager;
    private final DisplaysCommand displaysCommand;

    public DisplayModule(JavaPlugin plugin, AnimationManager animationManager, PlatformAdapter platformAdapter) {
        this.plugin = plugin;
        DisplayVisibilityService visibilityService = new DisplayVisibilityService();
        DisplayRenderDiffService renderDiffService = new DisplayRenderDiffService();
        DisplayRenderStateManager renderStateManager = new DisplayRenderStateManager();
        DisplayPlaceholderService displayPlaceholderService = new DisplayPlaceholderService(platformAdapter);
        DisplayTypeRegistry displayTypeRegistry = new DisplayTypeRegistry(displayPlaceholderService, animationManager);
        DisplayContentPostProcessingService postProcessingService = new DisplayContentPostProcessingService(displayTypeRegistry);
        IntentMaterializerService intentMaterializerService = new IntentMaterializerService(postProcessingService);
        DisplayRenderService renderService = new DisplayRenderService(renderDiffService, platformAdapter, renderStateManager, intentMaterializerService);
        AttributeDefinitionRegistry attributeDefinitionRegistry = new AttributeDefinitionRegistry();
        DisplayRenderStateService stateService = new DisplayRenderStateService(attributeDefinitionRegistry, displayTypeRegistry);
        TextDisplayPlayerPageManager playerPageManager = new TextDisplayPlayerPageManager();
        DisplayRenderingService renderingService = new DisplayRenderingService(
                visibilityService, platformAdapter.getPlayerService(), stateService, renderService, playerPageManager);
        DisplayConfigService configService = new DisplayConfigService(plugin);
        DisplayConfigMapper configMapper = new DisplayConfigMapper(attributeDefinitionRegistry);
        DisplayPersistenceService persistenceService = new DisplayPersistenceService(configService, configMapper);
        DisplayCloneService displayCloneService = new DisplayCloneService();
        this.playerPageManager = new TextDisplayPlayerPageManager();
        this.displayService = new DisplayService(persistenceService, renderingService, playerPageManager);
        this.displayUpdater = new DisplayUpdater(displayService, renderingService);
        this.displayListener = new DisplayListener(displayService, platformAdapter.getPlayerService());
        AttributeCommandHandler attributeCommandHandler = new AttributeCommandHandler(attributeDefinitionRegistry);
        this.displaysCommand = new DisplaysCommand(displayService, displayCloneService, attributeCommandHandler);
    }

    public void initialize() {
        if (Version.before(Version.v1_19_R3)) {
            return;
        }
        this.displayService.reload();
        this.displayUpdater.register();
        Bukkit.getPluginManager().registerEvents(displayListener, plugin);
    }

    public void reload() {
        if (Version.before(Version.v1_19_R3)) {
            return;
        }
        this.displayService.reload();
    }

    public void shutdown() {
        if (Version.before(Version.v1_19_R3)) {
            return;
        }
        HandlerList.unregisterAll(displayListener);
        this.displayUpdater.unregister();
        this.playerPageManager.shutdown();
        this.displayService.shutdown();
    }

    public DisplayService getDisplayService() {
        return displayService;
    }

    public DisplaysCommand getDisplaysCommand() {
        return displaysCommand;
    }
}

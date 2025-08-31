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
import eu.decentsoftware.holograms.display.rendering.DisplayDataMapper;
import eu.decentsoftware.holograms.display.rendering.DisplayRenderingAdapterFactory;
import eu.decentsoftware.holograms.display.rendering.DisplayRenderingService;
import eu.decentsoftware.holograms.display.rendering.TextProcessingService;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsDisplayRendererFactory;
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

    public DisplayModule(JavaPlugin plugin, NmsDisplayRendererFactory rendererFactory, AnimationManager animationManager) {
        this.plugin = plugin;
        DisplayDataMapper dataMapper = new DisplayDataMapper();
        TextProcessingService textProcessingService = new TextProcessingService(animationManager);
        DisplayRenderingAdapterFactory renderingAdapterFactory = new DisplayRenderingAdapterFactory(
                dataMapper, rendererFactory, textProcessingService);
        DisplayRenderingService renderingService = new DisplayRenderingService(new DisplayVisibilityService(), renderingAdapterFactory);
        this.displayService = new DisplayService(renderingService);
        this.displayUpdater = new DisplayUpdater(displayService, renderingService);
        this.displayListener = new DisplayListener(displayService);
    }

    public void initialize() {
        if (Version.before(Version.v1_19_R3)) {
            return;
        }
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
        this.displayService.shutdown();
    }

    public DisplayService getDisplayService() {
        return displayService;
    }
}

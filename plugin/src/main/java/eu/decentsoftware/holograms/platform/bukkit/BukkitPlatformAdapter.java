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

package eu.decentsoftware.holograms.platform.bukkit;

import eu.decentsoftware.holograms.nms.api.display.NmsDisplayRendererFactory;
import eu.decentsoftware.holograms.platform.api.PlatformAdapter;
import eu.decentsoftware.holograms.platform.api.PlatformEventListener;
import eu.decentsoftware.holograms.platform.api.capability.PlatformCapabilities;
import eu.decentsoftware.holograms.platform.api.capability.PlatformMaterialService;
import eu.decentsoftware.holograms.platform.api.placeholder.PlaceholderProvider;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayerService;
import eu.decentsoftware.holograms.platform.api.render.PlatformRenderService;
import eu.decentsoftware.holograms.platform.api.resource.SaveResourceService;
import eu.decentsoftware.holograms.platform.bukkit.placeholder.BukkitPlaceholderApiProvider;
import eu.decentsoftware.holograms.platform.bukkit.player.BukkitPlayerService;
import eu.decentsoftware.holograms.platform.bukkit.render.BukkitItemFactory;
import eu.decentsoftware.holograms.platform.bukkit.render.BukkitRenderService;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BukkitPlatformAdapter implements PlatformAdapter {

    private final BukkitPlatformCapabilities capabilities;
    private final BukkitMaterialService materialService;
    private final BukkitPlayerService playerService;
    private final BukkitRenderService renderService;
    private final BukkitEventListener eventListener;
    private final List<PlaceholderProvider> placeholderProviders;
    private final BukkitSaveResourceService saveResourceService;

    public BukkitPlatformAdapter(JavaPlugin plugin, NmsDisplayRendererFactory rendererFactory) {
        capabilities = new BukkitPlatformCapabilities();
        materialService = new BukkitMaterialService();
        playerService = new BukkitPlayerService();
        renderService = new BukkitRenderService(rendererFactory, new BukkitItemFactory());
        eventListener = new BukkitEventListener(renderService);
        placeholderProviders = Collections.singletonList(
                new BukkitPlaceholderApiProvider()
        );
        saveResourceService = new BukkitSaveResourceService(plugin);
    }

    @NotNull
    @Override
    public PlatformCapabilities getCapabilities() {
        return capabilities;
    }

    @NotNull
    @Override
    public PlatformEventListener getEventListener() {
        return eventListener;
    }

    @NotNull
    @Override
    public PlatformMaterialService getMaterialService() {
        return materialService;
    }

    @NotNull
    @Override
    public PlatformPlayerService getPlayerService() {
        return playerService;
    }

    @NotNull
    @Override
    public PlatformRenderService getRenderService() {
        return renderService;
    }

    @NotNull
    @Override
    public List<PlaceholderProvider> getPlaceholderProviders() {
        return placeholderProviders;
    }

    @NotNull
    @Override
    public SaveResourceService getSaveResourceService() {
        return saveResourceService;
    }
}

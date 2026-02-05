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

import eu.decentsoftware.holograms.nms.api.renderer.display.NmsDisplayRendererFactory;
import eu.decentsoftware.holograms.platform.api.PlatformAdapter;
import eu.decentsoftware.holograms.platform.api.capability.PlatformCapabilities;
import eu.decentsoftware.holograms.platform.api.placeholder.PlaceholderProvider;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayerService;
import eu.decentsoftware.holograms.platform.api.render.PlatformRenderService;
import eu.decentsoftware.holograms.platform.bukkit.placeholder.BukkitPlaceholderApiProvider;
import eu.decentsoftware.holograms.platform.bukkit.player.BukkitPlayerService;
import eu.decentsoftware.holograms.platform.bukkit.render.BukkitRenderService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BukkitPlatformAdapter implements PlatformAdapter {

    private final PlatformCapabilities capabilities;
    private final BukkitPlayerService playerService;
    private final BukkitRenderService renderService;
    private final List<PlaceholderProvider> placeholderProviders;

    public BukkitPlatformAdapter(NmsDisplayRendererFactory rendererFactory) {
        capabilities = new BukkitPlatformCapabilities();
        playerService = new BukkitPlayerService();
        renderService = new BukkitRenderService(rendererFactory);
        placeholderProviders = Collections.singletonList(
                new BukkitPlaceholderApiProvider()
        );
    }

    @NotNull
    @Override
    public PlatformCapabilities getCapabilities() {
        return capabilities;
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
}

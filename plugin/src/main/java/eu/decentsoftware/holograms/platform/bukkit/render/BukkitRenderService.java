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

package eu.decentsoftware.holograms.platform.bukkit.render;

import eu.decentsoftware.holograms.nms.api.renderer.display.NmsDisplayRendererFactory;
import eu.decentsoftware.holograms.nms.api.renderer.NmsDisplayRenderer;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import eu.decentsoftware.holograms.platform.api.render.PlatformRenderService;
import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.platform.bukkit.player.BukkitPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitRenderService implements PlatformRenderService {

    private final Map<String, NmsDisplayRenderer> renderers = new HashMap<>();
    private final NmsDisplayRendererFactory rendererFactory;

    public BukkitRenderService(NmsDisplayRendererFactory rendererFactory) {
        this.rendererFactory = rendererFactory;
    }

    @Override
    public void render(PlatformPlayer player, RenderObjectHandle handle, List<RenderIntent> intents) {
        Player bukkitPlayer = ((BukkitPlayer) player).getBukkitPlayer();
        NmsDisplayRenderer renderer = renderers.computeIfAbsent(handle.getId(),
                k -> rendererFactory.createDisplayRenderer(handle.getDisplayType()));
        renderer.accept(bukkitPlayer, intents);
    }
}

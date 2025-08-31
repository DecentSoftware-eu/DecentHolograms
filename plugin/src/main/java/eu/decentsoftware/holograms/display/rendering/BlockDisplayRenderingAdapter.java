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

package eu.decentsoftware.holograms.display.rendering;

import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.display.data.BlockDisplayData;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsDisplayRenderer;
import org.bukkit.entity.Player;

public class BlockDisplayRenderingAdapter implements DisplayRenderingAdapter<BlockDisplay> {

    private final DisplayDataMapper dataMapper;
    private final NmsDisplayRenderer<BlockDisplayData> renderer;

    public BlockDisplayRenderingAdapter(DisplayDataMapper dataMapper, NmsDisplayRenderer<BlockDisplayData> renderer) {
        this.dataMapper = dataMapper;
        this.renderer = renderer;
    }

    @Override
    public void display(BlockDisplay display, Player player) {
        renderer.display(player, getPartData(display));
    }

    @Override
    public void updateProperties(BlockDisplay display, Player player) {
        renderer.updateProperties(player, getPartData(display));
    }

    @Override
    public void updateContent(BlockDisplay display, Player player, boolean fullUpdate) {
        renderer.updateContent(player, getPartData(display));
    }

    @Override
    public void move(BlockDisplay display, Player player) {
        renderer.move(player, getPartData(display));
    }

    @Override
    public void hide(BlockDisplay display, Player player) {
        renderer.hide(player);
    }

    private NmsHologramPartData<BlockDisplayData> getPartData(BlockDisplay display) {
        return new NmsHologramPartData<>(
                () -> display.getLocation().toDecentPosition(),
                () -> dataMapper.mapBlockDisplay(display)
        );
    }
}

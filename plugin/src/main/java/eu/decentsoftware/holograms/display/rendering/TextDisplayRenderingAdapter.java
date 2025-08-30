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

import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayData;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsDisplayRenderer;
import org.bukkit.entity.Player;

public class TextDisplayRenderingAdapter implements DisplayRenderingAdapter<TextDisplay> {

    private final DisplayDataMapper dataMapper;
    private final NmsDisplayRenderer<TextDisplayData> renderer;

    public TextDisplayRenderingAdapter(DisplayDataMapper dataMapper, NmsDisplayRenderer<TextDisplayData> renderer) {
        this.dataMapper = dataMapper;
        this.renderer = renderer;
    }

    @Override
    public void display(TextDisplay display, Player player) {
        renderer.display(player, getPartData(display, player));
    }

    @Override
    public void updateProperties(TextDisplay display, Player player) {
        renderer.updateProperties(player, getPartData(display, player));
    }

    @Override
    public void updateContent(TextDisplay display, Player player) {
        renderer.updateContent(player, getPartData(display, player));
    }

    @Override
    public void move(TextDisplay display, Player player) {
        renderer.move(player, getPartData(display, player));
    }

    @Override
    public void hide(TextDisplay display, Player player) {
        renderer.hide(player);
    }

    private NmsHologramPartData<TextDisplayData> getPartData(TextDisplay display, Player player) {
        return new NmsHologramPartData<>(
                () -> display.getLocation().toDecentPosition(),
                () -> dataMapper.mapTextDisplay(display, player)
        );
    }
}

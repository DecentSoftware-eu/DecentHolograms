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
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.ItemDisplay;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsDisplayRendererFactory;

public class DisplayRenderingAdapterFactory {

    private final DisplayDataMapper dataMapper;
    private final NmsDisplayRendererFactory rendererFactory;
    private final TextProcessingService textProcessingService;

    public DisplayRenderingAdapterFactory(DisplayDataMapper dataMapper,
                                          NmsDisplayRendererFactory rendererFactory,
                                          TextProcessingService textProcessingService) {
        this.dataMapper = dataMapper;
        this.rendererFactory = rendererFactory;
        this.textProcessingService = textProcessingService;
    }

    public DisplayRenderingAdapter<? extends DisplayBase> createAdapter(DisplayBase display) {
        if (display instanceof TextDisplay) {
            return new TextDisplayRenderingAdapter(dataMapper, textProcessingService, rendererFactory.createTextDisplayRenderer());
        } else if (display instanceof ItemDisplay) {
            return new ItemDisplayRenderingAdapter(dataMapper, rendererFactory.createItemDisplayRenderer());
        } else if (display instanceof BlockDisplay) {
            return new BlockDisplayRenderingAdapter(dataMapper, rendererFactory.createBlockDisplayRenderer());
        }
        return null;
    }
}

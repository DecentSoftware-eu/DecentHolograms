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

import eu.decentsoftware.holograms.api.utils.tick.Ticked;
import eu.decentsoftware.holograms.display.rendering.DisplayRenderingService;

import java.util.concurrent.atomic.AtomicLong;

public class DisplayUpdater extends Ticked {

    private final DisplayService displayService;
    private final DisplayRenderingService renderingService;
    private final AtomicLong tickCounter;

    public DisplayUpdater(DisplayService displayService, DisplayRenderingService renderingService) {
        super("display_updater", 1L);
        this.displayService = displayService;
        this.renderingService = renderingService;
        this.tickCounter = new AtomicLong(0);
    }

    @Override
    public void tick() {
        long currentTick = tickCounter.getAndIncrement();
        if (currentTick % 20 == 0) {
            for (DisplayBase display : displayService.getRegisteredDisplays()) {
                renderingService.updateVisibility(display);
                renderingService.updateContent(display);
            }
        }

        // TODO: update animations
    }
}

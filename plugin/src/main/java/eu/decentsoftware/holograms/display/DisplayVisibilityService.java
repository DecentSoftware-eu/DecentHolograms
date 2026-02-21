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

import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;

public class DisplayVisibilityService {

    public boolean shouldBeShownToPlayer(DisplayBase display, PlatformPlayer player) {
        return isDisplayEnabled(display) && isPlayerWithinDisplayRange(display, player);
    }

    private boolean isDisplayEnabled(DisplayBase display) {
        return display.getSettings().isEnabled();
    }

    private boolean isPlayerWithinDisplayRange(DisplayBase display, PlatformPlayer player) {
        double displayRange = display.getSettings().getDisplayRange();
        DecentLocation displayLocation = display.getLocation();
        DecentLocation playerLocation = player.getLocation();
        return displayLocation.isSameWorld(playerLocation)
                && displayLocation.distanceSquared(playerLocation) <= displayRange * displayRange;
    }
}

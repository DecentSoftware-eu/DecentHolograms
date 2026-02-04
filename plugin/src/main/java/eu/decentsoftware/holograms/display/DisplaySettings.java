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

import lombok.Getter;
import lombok.Setter;

/**
 * Settings of a Display.
 *
 * @author d0by
 * @see DisplayBase
 * @since 2.10.0
 */
@Getter
@Setter
public class DisplaySettings {

    /**
     * Whether the display is enabled or not.
     *
     * <p>If the display is disabled, it won't be rendered.</p>
     */
    private boolean enabled;
    /**
     * The maximum range in blocks at which the display is visible for a player.
     */
    private double displayRange;
    /**
     * The interval in ticks at which the display is updated for players within the update range.
     */
    private int updateInterval;

    public DisplaySettings() {
        this.enabled = true;
        this.displayRange = 256;
        this.updateInterval = 20;
    }

    public DisplaySettings copy() {
        DisplaySettings copy = new DisplaySettings();
        copy.setEnabled(this.isEnabled());
        copy.setDisplayRange(this.getDisplayRange());
        copy.setUpdateInterval(this.getUpdateInterval());
        return copy;
    }
}

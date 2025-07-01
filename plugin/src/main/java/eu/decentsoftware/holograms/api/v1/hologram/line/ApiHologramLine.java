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

package eu.decentsoftware.holograms.api.v1.hologram.line;

import eu.decentsoftware.holograms.api.v1.hologram.HologramLine;
import eu.decentsoftware.holograms.api.v1.location.DecentOffsets;
import eu.decentsoftware.holograms.utils.Validate;
import org.jetbrains.annotations.NotNull;

public abstract class ApiHologramLine implements HologramLine {

    private double height = 0.0d;
    private DecentOffsets offsets = DecentOffsets.ZERO;

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    @NotNull
    @Override
    public DecentOffsets getOffsets() {
        return offsets;
    }

    @Override
    public void setOffsets(@NotNull DecentOffsets offsets) {
        Validate.notNull(offsets, "offsets cannot be null");
        this.offsets = offsets;
    }
}

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

package eu.decentsoftware.holograms.api.v1.hologram;

import eu.decentsoftware.holograms.Validate;
import eu.decentsoftware.holograms.api.v1.hologram.content.HologramLineContent;
import eu.decentsoftware.holograms.api.v1.location.DecentOffsets;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ApiHologramLineBuilder implements HologramLineBuilder {

    private final ApiHologramPageBuilder pageBuilder;
    private final HologramLineContent content;
    private double height = 0.0;
    private DecentOffsets offsets = DecentOffsets.ZERO;
    private float facing = 0.0f;

    public ApiHologramLineBuilder(@NotNull ApiHologramPageBuilder pageBuilder, @NotNull HologramLineContent content) {
        this.pageBuilder = pageBuilder;
        this.content = content;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramLineBuilder withHeight(double height) {
        this.height = height;
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramLineBuilder withOffsets(@NotNull DecentOffsets offsets) {
        Validate.notNull(offsets, "offsets cannot be null");
        this.offsets = offsets;
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramLineBuilder withFacing(float facing) {
        Validate.isTrue(facing >= 0.0f && facing <= 360.0f, "facing must be between 0 and 360");
        this.facing = facing;
        return this;
    }

    @NotNull
    @Override
    public HologramPageBuilder and() {
        return pageBuilder;
    }

    ApiHologramLine build() {
        ApiHologramLine line = new ApiHologramLine(content);
        line.setHeight(height);
        line.setOffsets(offsets);
        line.setFacing(facing);
        return line;
    }
}

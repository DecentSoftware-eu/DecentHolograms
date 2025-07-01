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

import eu.decentsoftware.holograms.api.v1.location.DecentOffsets;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Builder for configuring hologram lines.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface HologramLineBuilder {

    /**
     * Sets the vertical height/spacing of this line.
     *
     * @param height The height value to set.
     * @return This builder for chaining.
     * @since 2.10.0
     */
    @NotNull
    @Contract("_ -> this")
    HologramLineBuilder withHeight(double height);

    /**
     * Sets the positional offsets for this line.
     *
     * @param offsets The offsets to apply.
     * @return This builder for chaining.
     * @since 2.10.0
     */
    @NotNull
    @Contract("_ -> this")
    HologramLineBuilder withOffsets(@NotNull DecentOffsets offsets);

    /**
     * Sets the facing angle/rotation of this line.
     *
     * @param facing The facing angle in degrees. (0-360)
     * @return This builder for chaining.
     * @since 2.10.0
     */
    @NotNull
    @Contract("_ -> this")
    HologramLineBuilder withFacing(float facing);

    /**
     * Returns to the parent page builder.
     *
     * @return The parent HologramPageBuilder.
     * @since 2.10.0
     */
    @NotNull
    HologramPageBuilder and();

}
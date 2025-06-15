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

package eu.decentsoftware.holograms.api.hologram.line;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public interface EntityHologramLine extends HologramLine {

    /**
     * Get the entity type of this line.
     *
     * @return The entity type of this line.
     * @since 2.10.0
     */
    @NotNull
    EntityType getEntityType();

    /**
     * Set the entity type of this line. This method also updates the line accordingly.
     *
     * @param entityType The entity type of this line.
     * @since 2.10.0
     */
    void setEntityType(@NotNull EntityType entityType);

    /**
     * Get the facing direction of the entity. This is the angle in degrees
     * that the entity is facing.
     * <p>
     * This value overrides the default facing direction of the hologram.
     *
     * @return The facing direction of the entity in degrees.
     * @since 2.10.0
     */
    float getFacing();

    /**
     * Set the facing direction of the entity. This is the angle in degrees
     * that the entity is facing.
     * <p>
     * This value overrides the default facing direction of the hologram.
     *
     * @param facing The facing direction of the entity in degrees. Must be between 0 and 360.
     * @since 2.10.0
     */
    void setFacing(float facing);

}

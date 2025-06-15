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

import eu.decentsoftware.holograms.utils.Validate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ApiHeadHologramLine extends ApiHologramLine implements HeadHologramLine {

    private ItemStack itemStack;
    private final boolean small;
    private float facing = 0.0f;

    protected ApiHeadHologramLine(ItemStack itemStack, boolean small) {
        this.itemStack = itemStack;
        this.small = small;
    }

    public ApiHeadHologramLine(ItemStack itemStack) {
        this(itemStack, false);
    }

    @NotNull
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void setItemStack(@NotNull ItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        this.itemStack = itemStack;
    }

    @Override
    public float getFacing() {
        return facing;
    }

    @Override
    public void setFacing(float facing) {
        Validate.isTrue(facing >= 0.0f && facing <= 360.0f, "facing must be between 0 and 360 degrees");
        this.facing = facing;
    }

    public boolean isSmall() {
        return small;
    }
}

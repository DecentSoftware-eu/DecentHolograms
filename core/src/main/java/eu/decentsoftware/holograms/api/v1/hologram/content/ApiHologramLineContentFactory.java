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

package eu.decentsoftware.holograms.api.v1.hologram.content;

import eu.decentsoftware.holograms.shared.Validate;
import eu.decentsoftware.holograms.api.v1.DecentEntityType;
import eu.decentsoftware.holograms.api.v1.platform.DecentItemStack;
import org.jetbrains.annotations.NotNull;

public class ApiHologramLineContentFactory implements HologramLineContentFactory {

    @Override
    public TextHologramLineContent createTextContent(@NotNull String text) {
        Validate.notNull(text, "text cannot be null");
        return new ApiTextHologramLineContent(text);
    }

    @Override
    public IconHologramLineContent createIconContent(@NotNull DecentItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        return new ApiIconHologramLineContent(itemStack);
    }

    @Override
    public HeadHologramLineContent createHeadContent(@NotNull DecentItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        return new ApiHeadHologramLineContent(itemStack);
    }

    @Override
    public SmallHeadHologramLineContent createSmallHeadContent(@NotNull DecentItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        return new ApiSmallHeadHologramLineContent(itemStack);
    }

    @Override
    public EntityHologramLineContent createEntityContent(@NotNull DecentEntityType entityType) {
        Validate.notNull(entityType, "entityType cannot be null");
        return new ApiEntityHologramLineContent(entityType);
    }
}

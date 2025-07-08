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

import eu.decentsoftware.holograms.shared.Validate;
import eu.decentsoftware.holograms.api.v1.DecentEntityType;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiEntityHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiHeadHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiIconHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiSmallHeadHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiTextHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.HologramLineContent;
import eu.decentsoftware.holograms.api.v1.location.DecentOffsets;
import eu.decentsoftware.holograms.api.v1.platform.DecentItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ApiHologramLineBuilder implements HologramLineBuilder {

    private final ApiHologramPageBuilder pageBuilder;
    private HologramLineContent content;
    private double height = 0.0;
    private DecentOffsets offsets = DecentOffsets.ZERO;
    private float facing = 0.0f;

    public ApiHologramLineBuilder(@NotNull ApiHologramPageBuilder pageBuilder) {
        this.pageBuilder = pageBuilder;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramLineBuilder withContent(@NotNull HologramLineContent content) {
        Validate.notNull(content, "content cannot be null");
        this.content = content;
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramLineBuilder withTextContent(@NotNull String text) {
        Validate.notNull(text, "text cannot be null");
        this.content = new ApiTextHologramLineContent(text);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramLineBuilder withIconContent(@NotNull DecentItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        this.content = new ApiIconHologramLineContent(itemStack);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramLineBuilder withHeadContent(@NotNull DecentItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        this.content = new ApiHeadHologramLineContent(itemStack);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramLineBuilder withSmallHeadContent(@NotNull DecentItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        this.content = new ApiSmallHeadHologramLineContent(itemStack);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramLineBuilder withEntityContent(@NotNull DecentEntityType entityType) {
        Validate.notNull(entityType, "entityType cannot be null");
        this.content = new ApiEntityHologramLineContent(entityType);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramLineBuilder withHeight(double height) {
        this.height = height;
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramLineBuilder withOffsets(@NotNull DecentOffsets offsets) {
        Validate.notNull(offsets, "offsets cannot be null");
        this.offsets = offsets;
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramLineBuilder withFacing(float facing) {
        Validate.isTrue(facing >= 0.0f && facing <= 360.0f, "facing must be between 0 and 360");
        this.facing = facing;
        return this;
    }

    @NotNull
    @Override
    public ApiHologramPageBuilder and() {
        return pageBuilder;
    }

    ApiHologramLine build() {
        Validate.notNull(content, "Cannot build a hologram line without content");

        ApiHologramLine line = new ApiHologramLine(content);
        line.setHeight(height);
        line.setOffsets(offsets);
        line.setFacing(facing);
        return line;
    }
}

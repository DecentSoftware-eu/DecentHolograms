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
import eu.decentsoftware.holograms.api.v1.DecentEntityType;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiEntityHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiHeadHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiIconHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiSmallHeadHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.ApiTextHologramLineContent;
import eu.decentsoftware.holograms.api.v1.hologram.content.HologramLineContent;
import eu.decentsoftware.holograms.api.v1.platform.GenericItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ApiHologramPageBuilder implements HologramPageBuilder {

    private final ApiHologramBuilder hologramBuilder;
    private final List<ApiHologramLineBuilder> lineBuilders = new ArrayList<>();

    public ApiHologramPageBuilder(@NotNull ApiHologramBuilder hologramBuilder) {
        this.hologramBuilder = hologramBuilder;
    }

    @NotNull
    @Override
    public HologramLineBuilder addLine(@NotNull HologramLineContent content) {
        Validate.notNull(content, "content cannot be null");
        return createHologramLineBuilder(content);
    }

    @NotNull
    @Override
    public HologramLineBuilder addTextLine(@NotNull String text) {
        Validate.notNull(text, "text cannot be null");
        ApiTextHologramLineContent lineContent = new ApiTextHologramLineContent(text);
        return createHologramLineBuilder(lineContent);
    }

    @NotNull
    @Override
    public HologramLineBuilder addIconLine(@NotNull GenericItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        HologramLineContent lineContent = new ApiIconHologramLineContent(itemStack);
        return createHologramLineBuilder(lineContent);
    }

    @NotNull
    @Override
    public HologramLineBuilder addHeadLine(@NotNull GenericItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        HologramLineContent lineContent = new ApiHeadHologramLineContent(itemStack);
        return createHologramLineBuilder(lineContent);
    }

    @NotNull
    @Override
    public HologramLineBuilder addSmallHeadLine(@NotNull GenericItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        HologramLineContent lineContent = new ApiSmallHeadHologramLineContent(itemStack);
        return createHologramLineBuilder(lineContent);
    }

    @NotNull
    @Override
    public HologramLineBuilder addEntityLine(@NotNull DecentEntityType entityType) {
        Validate.notNull(entityType, "entityType cannot be null");
        HologramLineContent lineContent = new ApiEntityHologramLineContent(entityType);
        return createHologramLineBuilder(lineContent);
    }

    @NotNull
    @Override
    public HologramBuilder and() {
        return hologramBuilder;
    }

    ApiHologramPage build(ApiHologram hologram) {
        Validate.isTrue(!lineBuilders.isEmpty(), "Cannot build a hologram page with no lines");

        ApiHologramPage page = new ApiHologramPage(hologram);
        for (ApiHologramLineBuilder lineBuilder : lineBuilders) {
            ApiHologramLine line = lineBuilder.build();
            page.appendLine(line);
        }
        return page;
    }

    private ApiHologramLineBuilder createHologramLineBuilder(HologramLineContent content) {
        ApiHologramLineBuilder lineBuilder = new ApiHologramLineBuilder(this, content);
        lineBuilders.add(lineBuilder);
        return lineBuilder;
    }
}

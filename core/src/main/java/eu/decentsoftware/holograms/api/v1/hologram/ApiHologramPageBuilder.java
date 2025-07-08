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
import eu.decentsoftware.holograms.api.v1.hologram.content.HologramLineContent;
import eu.decentsoftware.holograms.api.v1.platform.DecentItemStack;
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
    public HologramLineBuilder withLine() {
        ApiHologramLineBuilder lineBuilder = new ApiHologramLineBuilder(this);
        lineBuilders.add(lineBuilder);
        return lineBuilder;
    }

    @NotNull
    @Override
    public ApiHologramPageBuilder withLine(@NotNull HologramLineContent content) {
        Validate.notNull(content, "content cannot be null");
        ApiHologramLineBuilder lineBuilder = new ApiHologramLineBuilder(this)
                .withContent(content);
        lineBuilders.add(lineBuilder);
        return this;
    }

    @NotNull
    @Override
    public ApiHologramPageBuilder withTextLine(@NotNull String text) {
        Validate.notNull(text, "text cannot be null");
        ApiHologramLineBuilder lineBuilder = new ApiHologramLineBuilder(this)
                .withTextContent(text);
        lineBuilders.add(lineBuilder);
        return this;
    }

    @NotNull
    @Override
    public ApiHologramPageBuilder withIconLine(@NotNull DecentItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        ApiHologramLineBuilder lineBuilder = new ApiHologramLineBuilder(this)
                .withIconContent(itemStack);
        lineBuilders.add(lineBuilder);
        return this;
    }

    @NotNull
    @Override
    public ApiHologramPageBuilder withHeadLine(@NotNull DecentItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        ApiHologramLineBuilder lineBuilder = new ApiHologramLineBuilder(this)
                .withHeadContent(itemStack);
        lineBuilders.add(lineBuilder);
        return this;
    }

    @NotNull
    @Override
    public ApiHologramPageBuilder withSmallHeadLine(@NotNull DecentItemStack itemStack) {
        Validate.notNull(itemStack, "itemStack cannot be null");
        ApiHologramLineBuilder lineBuilder = new ApiHologramLineBuilder(this)
                .withSmallHeadContent(itemStack);
        lineBuilders.add(lineBuilder);
        return this;
    }

    @NotNull
    @Override
    public ApiHologramPageBuilder withEntityLine(@NotNull DecentEntityType entityType) {
        Validate.notNull(entityType, "entityType cannot be null");
        ApiHologramLineBuilder lineBuilder = new ApiHologramLineBuilder(this)
                .withEntityContent(entityType);
        lineBuilders.add(lineBuilder);
        return this;
    }

    @NotNull
    @Override
    public ApiHologramBuilder and() {
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
}

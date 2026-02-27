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

package eu.decentsoftware.holograms.display.render.placeholder;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.platform.api.PlatformAdapter;
import eu.decentsoftware.holograms.platform.api.placeholder.PlaceholderContext;
import eu.decentsoftware.holograms.platform.api.placeholder.PlaceholderProvider;

public class DisplayPlaceholderService {

    private final PlatformAdapter platformAdapter;

    public DisplayPlaceholderService(PlatformAdapter platformAdapter) {
        this.platformAdapter = platformAdapter;
    }

    public String replacePlaceholders(String content, DisplayRenderContext context) {
        content = replaceInternalPlaceholders(content, context);
        content = replacePlatformPlaceholders(content, context);
        return content;
    }

    private String replacePlatformPlaceholders(String content, DisplayRenderContext context) {
        PlaceholderContext placeholderContext = createPlaceholderContext(context);
        for (PlaceholderProvider placeholderProvider : platformAdapter.getPlaceholderProviders()) {
            try {
                content = placeholderProvider.replace(content, placeholderContext);
            } catch (Exception e) {
                Log.warn("Failed to resolve placeholders using provider '%s'.", placeholderProvider.getClass().getName(), e);
            }
        }
        return content;
    }

    private PlaceholderContext createPlaceholderContext(DisplayRenderContext displayRenderContext) {
        return new DisplayPlaceholderContext(displayRenderContext.getPlayer());
    }

    private String replaceInternalPlaceholders(String content, DisplayRenderContext context) {
        return content.replace("{player}", context.getPlayer().getName());
    }
}

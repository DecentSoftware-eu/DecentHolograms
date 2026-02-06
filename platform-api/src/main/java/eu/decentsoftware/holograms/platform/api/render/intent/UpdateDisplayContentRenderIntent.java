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

package eu.decentsoftware.holograms.platform.api.render.intent;

import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a rendering operation for updating the content of an existing display element.
 *
 * <p>This render intent encapsulates the new content that should be applied to the display.
 * It is used by the rendering system to process and execute content updates on displays.</p>
 *
 * <p>Instances of this class are immutable and carry the necessary data for the "update display content"
 * rendering intent.</p>
 *
 * @author d0by
 * @see RenderIntent
 * @since 2.10.0
 */
public final class UpdateDisplayContentRenderIntent implements RenderIntent {

    private final DisplayContent<?> content;

    public UpdateDisplayContentRenderIntent(@NotNull DisplayContent<?> content) {
        this.content = content;
    }

    /**
     * Get the new content that should be applied to the display.
     *
     * @return The new content.
     */
    @NotNull
    public DisplayContent<?> getContent() {
        return content;
    }
}

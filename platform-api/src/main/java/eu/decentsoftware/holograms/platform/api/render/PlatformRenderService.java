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

package eu.decentsoftware.holograms.platform.api.render;

import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a service for rendering objects specific to the platform.
 *
 * <p>Implementations must be able to identify a specific rendered object by {@link RenderObjectHandle}.
 * The intents provided for processing may update an existing object or create a new one.</p>
 *
 * @author d0by
 * @since 2.10.0
 */
public interface PlatformRenderService {

    /**
     * Apply the given rendering intents to the specified render object.
     *
     * <p>The rendering intents are applied in the order they are provided.</p>
     *
     * @param player  The platform player for whom the rendering is performed.
     * @param handle  The handle representing the render object to render.
     * @param intents The list of intents describing the rendering operations to perform.
     * @see RenderIntent
     * @see RenderObjectHandle
     * @since 2.10.0
     */
    void render(@NotNull PlatformPlayer player, @NotNull RenderObjectHandle handle, @NotNull List<RenderIntent> intents);
}

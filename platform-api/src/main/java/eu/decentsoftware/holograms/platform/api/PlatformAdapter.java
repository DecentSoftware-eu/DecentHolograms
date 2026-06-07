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

package eu.decentsoftware.holograms.platform.api;

import eu.decentsoftware.holograms.platform.api.capability.PlatformCapabilities;
import eu.decentsoftware.holograms.platform.api.capability.PlatformMaterialService;
import eu.decentsoftware.holograms.platform.api.placeholder.PlaceholderProvider;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayerService;
import eu.decentsoftware.holograms.platform.api.render.PlatformRenderService;
import eu.decentsoftware.holograms.platform.api.resource.SaveResourceService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an adapter that bridges platform-specific functionality with the DecentHolograms core.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface PlatformAdapter {

    /**
     * Get the capabilities of this platform adapter.
     *
     * <p>This allows querying available features or behaviors.</p>
     *
     * @return The capabilities.
     * @see PlatformCapabilities
     * @since 2.10.0
     */
    @NotNull
    PlatformCapabilities getCapabilities();

    /**
     * Get the event listener of this platform adapter.
     *
     * <p>The core uses this to propagate certain events to the platform implementation.</p>
     *
     * @return The event listener.
     * @see PlatformEventListener
     * @since 2.10.0
     */
    @NotNull
    PlatformEventListener getEventListener();

    /**
     * Get the material service of this platform adapter.
     *
     * <p>This service allows querying and managing material names on the server.</p>
     *
     * @return The material service.
     * @see PlatformMaterialService
     * @since 2.10.0
     */
    @NotNull
    PlatformMaterialService getMaterialService();

    /**
     * Get the player service of this platform adapter.
     *
     * <p>This service allows querying and managing players on the server.</p>
     *
     * @return The player service.
     * @see PlatformPlayerService
     * @since 2.10.0
     */
    @NotNull
    PlatformPlayerService getPlayerService();

    /**
     * Get the render service of this platform adapter.
     *
     * <p>This service is responsible for rendering holograms on the server.</p>
     *
     * @return The render service.
     * @see PlatformRenderService
     * @since 2.10.0
     */
    @NotNull
    PlatformRenderService getRenderService();

    /**
     * Get the placeholder providers of this platform adapter.
     *
     * <p>The platform may implement any placeholder providers relevant to the platform.
     * For example, a placeholder provider could be implemented for PlaceholderAPI on Bukkit-based servers.</p>
     *
     * @return The placeholder providers.
     * @see PlaceholderProvider
     * @since 2.10.0
     */
    @NotNull
    List<PlaceholderProvider> getPlaceholderProviders();

    /**
     * Get the save resource service of this platform adapter.
     *
     * <p>This service allows saving resources to the plugin's data folder. It can be used by the core or by other services
     * to save default configuration files or other resources bundled with the plugin.</p>
     *
     * @return The save resource service.
     * @see SaveResourceService
     * @since 2.10.1
     */
    @NotNull
    SaveResourceService getSaveResourceService();
}

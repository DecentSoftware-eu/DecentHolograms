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

import eu.decentsoftware.holograms.platform.api.render.PlatformRenderService;

/**
 * Represents a specific rendering operation that can be performed.
 *
 * <p>Classes implementing this interface define specific actions such as
 * spawning, moving, despawning, or updating display elements and their
 * associated metadata or content.</p>
 *
 * <p>Render intents are used to encapsulate rendering logic in a modular
 * and extensible way, allowing the rendering system to process and
 * execute these actions accordingly.</p>
 *
 * @author d0by
 * @see PlatformRenderService
 * @since 2.10.0
 */
public interface RenderIntent {
}

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
import org.jetbrains.annotations.NotNull;

/**
 * The result of turning render intents into something that can be shown to a player.
 *
 * <p>Separating this from the act of showing it is what lets one result serve several viewers:
 * everything that does not depend on who is watching - resolving the intents, building the
 * payload - happens once, and only the delivery happens per player. Viewers seeing identical
 * content can therefore share a single instance rather than repeating the work for each of them.</p>
 *
 * <h3>Contract</h3>
 * <ul>
 *   <li>{@link #apply(PlatformPlayer)} may be called any number of times, with a different player
 *       each time, and must not consume or invalidate the instance.</li>
 *   <li>Applying is independent per player: one player failing must not affect the others.</li>
 *   <li>Instances may be applied from any thread, including one that does not own the player.
 *       Implementations are responsible for reaching the right thread if their platform needs it.</li>
 * </ul>
 *
 * <p><b>Note:</b> how much work an implementation actually moves out of {@code apply} is up to it.
 * An implementation that simply defers everything is a valid, if unhelpful, one - the contract
 * above is what callers may rely on, not any particular saving.</p>
 *
 * @author d0by
 * @see PlatformRenderService
 * @since 2.10.2
 */
@FunctionalInterface
public interface PreparedRender {

    /**
     * Shows this to the given player.
     *
     * @param player The player to show it to.
     * @since 2.10.2
     */
    void apply(@NotNull PlatformPlayer player);
}

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

package eu.decentsoftware.holograms.nms.api.render;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A finished piece of rendering work, ready to be sent to players.
 *
 * <p>What it holds is deliberately not part of this contract. In practice, it is a list of built
 * packets, but nothing outside the NMS module needs to know that - the caller only ever asks for
 * it to be sent. Keeping it opaque is what allows the packet types, which differ between every
 * supported version, to stay behind this boundary.</p>
 *
 * <p>Sending is the only per-player step. Everything that does not depend on the viewer has
 * already happened by the time an instance exists, so showing the same thing to a hundred players
 * costs one build and a hundred sends. That is the entire reason renderers return a value instead
 * of taking a player.</p>
 *
 * <h3>Contract</h3>
 * <ul>
 *   <li>May be applied any number of times, to any number of players, in any order.</li>
 *   <li>Applying must not modify the instance, so concurrent applications are safe.</li>
 *   <li>Applying for one player must be unaffected by another player having failed.</li>
 *   <li>Instances may be applied from any thread.</li>
 * </ul>
 *
 * @author d0by
 * @since 2.10.2
 */
@FunctionalInterface
public interface NmsPreparedRender {

    /**
     * Sends this to the given player.
     *
     * @param player The player to send it to.
     * @since 2.10.2
     */
    void apply(@NotNull Player player);
}

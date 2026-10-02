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

package eu.decentsoftware.holograms.nms.api.display;

import eu.decentsoftware.holograms.nms.api.render.NmsPreparedRender;
import org.jetbrains.annotations.NotNull;

/**
 * One display that exists in the world, for as long as it is being shown to anyone.
 *
 * <p>Each method describes something that should happen to it and hands back the work needed to
 * make that so, without performing it. Who sees it is decided afterward, by applying the result
 * to whichever players should - so a change that looks the same to everyone is built once and
 * sent many times.</p>
 *
 * <h3>Identity</h3>
 *
 * <p>An instance owns the entity ids behind the display, which the server allocates and the caller
 * has no way to invent. Because those ids are shared between viewers, the packets describing them
 * are identical for every viewer, which is what makes one result usable for all of them.</p>
 *
 * <p>How many entities there are is not the caller's concern, and is not constant across versions:
 * a display may be one entity on one version and two on another. Create one instance per display
 * and keep it for that display's lifetime; a second instance would be a second display.</p>
 *
 * <h3>Content and metadata</h3>
 *
 * <p>Content is typed, so a text display cannot be handed an item. Metadata is not, because it is
 * open-ended - properties are added as Minecraft gains them, and an implementation ignores the
 * ones its version has no notion of rather than failing.</p>
 *
 * @param <C> What this display shows.
 * @author d0by
 * @see NmsPreparedRender
 * @since 2.10.0
 */
public interface NmsDisplayRenderer<C> {

    /**
     * The display comes into existence, with everything it needs to be shown correctly at once.
     *
     * @param data Where it is, what it shows, and how it looks.
     * @return The work needed to bring it into existence.
     * @since 2.10.0
     */
    @NotNull
    NmsPreparedRender spawn(@NotNull NmsSpawnDisplayData<C> data);

    /**
     * The display ceases to exist.
     *
     * @return The work needed to remove it.
     * @since 2.10.0
     */
    @NotNull
    NmsPreparedRender despawn();

    /**
     * The display is somewhere else.
     *
     * @param data Where it now is.
     * @return The work needed to move it.
     * @since 2.10.0
     */
    @NotNull
    NmsPreparedRender move(@NotNull NmsMoveDisplayData data);

    /**
     * The display shows something else.
     *
     * @param data What it now shows.
     * @return The work needed to change it.
     * @since 2.10.0
     */
    @NotNull
    NmsPreparedRender updateContent(@NotNull NmsUpdateDisplayContentData<C> data);

    /**
     * Some of the display's properties have changed.
     *
     * <p>Only the given properties are affected; anything omitted keeps its current value.</p>
     *
     * @param data The properties that changed, and their values.
     * @return The work needed to change them.
     * @since 2.10.0
     */
    @NotNull
    NmsPreparedRender updateMetadata(@NotNull NmsUpdateDisplayMetadataData data);
}

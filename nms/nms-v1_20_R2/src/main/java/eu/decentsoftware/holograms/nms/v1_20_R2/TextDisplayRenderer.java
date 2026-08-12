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

package eu.decentsoftware.holograms.nms.v1_20_R2;

import eu.decentsoftware.holograms.nms.api.display.NmsSpawnDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsTextDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayContentData;
import eu.decentsoftware.holograms.nms.api.render.NmsPreparedRender;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class TextDisplayRenderer extends AbstractDisplayRenderer<List<String>> implements NmsTextDisplayRenderer {

    TextDisplayRenderer(int entityId) {
        super(entityId);
    }

    @NotNull
    @Override
    public NmsPreparedRender spawn(@NotNull NmsSpawnDisplayData<List<String>> data) {
        EntityMetadataBuilder metadataBuilder = EntityMetadataBuilder.create();
        applyMetadata(data.getMetadata(), metadataBuilder);
        metadataBuilder.withTextDisplayText(data.getContent());

        return EntityPacketsBuilder.create()
                .withSpawnEntity(entityId, EntityType.TEXT_DISPLAY, data.getPosition())
                .withEntityMetadata(entityId, metadataBuilder.toWatchableObjects())
                .build();
    }

    @NotNull
    @Override
    public NmsPreparedRender updateContent(@NotNull NmsUpdateDisplayContentData<List<String>> data) {
        EntityMetadataBuilder metadataBuilder = EntityMetadataBuilder.create()
                .withTextDisplayText(data.getContent());

        return EntityPacketsBuilder.create()
                .withEntityMetadata(entityId, metadataBuilder.toWatchableObjects())
                .build();
    }
}

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

package eu.decentsoftware.holograms.platform.bukkit.render.display;

import eu.decentsoftware.holograms.nms.api.display.NmsDisplayMetadata;
import eu.decentsoftware.holograms.nms.api.display.NmsDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.NmsMoveDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsSpawnDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayContentData;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayMetadataData;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.render.intent.DespawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.MoveRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.SpawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateDisplayContentRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateMetadataRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BukkitDisplayRenderService<C> {

    private final NmsDisplayRenderer<C> renderer;

    protected BukkitDisplayRenderService(NmsDisplayRenderer<C> renderer) {
        this.renderer = renderer;
    }

    public void apply(Player player, List<RenderIntent> intents) {
        for (RenderIntent intent : intents) {
            if (intent instanceof SpawnDisplayRenderIntent) {
                handleSpawnIntent(player, (SpawnDisplayRenderIntent) intent);
            } else if (intent instanceof UpdateDisplayContentRenderIntent) {
                handleUpdateContentIntent(player, (UpdateDisplayContentRenderIntent) intent);
            } else if (intent instanceof UpdateMetadataRenderIntent) {
                handleUpdateMetadataIntent(player, (UpdateMetadataRenderIntent) intent);
            } else if (intent instanceof MoveRenderIntent) {
                handleMoveIntent(player, (MoveRenderIntent) intent);
            } else if (intent instanceof DespawnDisplayRenderIntent) {
                handleDespawnIntent(player);
            }
        }
    }

    protected abstract NmsSpawnDisplayData<C> createNmsSpawnDisplayData(SpawnDisplayRenderIntent intent,
                                                                        DecentPosition position,
                                                                        List<NmsDisplayMetadata<?>> metadata);

    protected abstract NmsUpdateDisplayContentData<C> createNmsUpdateDisplayContentData(UpdateDisplayContentRenderIntent intent);

    private void handleSpawnIntent(Player player, SpawnDisplayRenderIntent intent) {
        DecentPosition position = mapPosition(intent.getLocation());
        List<NmsDisplayMetadata<?>> metadata = mapMetadata(intent.getMetadataValues());
        NmsSpawnDisplayData<C> spawnDisplayData = createNmsSpawnDisplayData(intent, position, metadata);
        renderer.spawn(player, spawnDisplayData);
    }

    private void handleUpdateContentIntent(Player player, UpdateDisplayContentRenderIntent intent) {
        NmsUpdateDisplayContentData<C> updateDisplayData = createNmsUpdateDisplayContentData(intent);
        renderer.updateContent(player, updateDisplayData);
    }

    private void handleUpdateMetadataIntent(Player player, UpdateMetadataRenderIntent intent) {
        List<MetadataValue<?>> metadataValues = intent.getMetadataValues();
        List<NmsDisplayMetadata<?>> metadataToUpdate = new ArrayList<>(metadataValues.size());
        for (MetadataValue<?> metadataValue : metadataValues) {
            NmsDisplayMetadata<?> metadatum = mapMetadatum(metadataValue);
            metadataToUpdate.add(metadatum);
        }
        sendCollectedMetadata(player, metadataToUpdate);
    }

    private void sendCollectedMetadata(Player player, List<NmsDisplayMetadata<?>> metadataToUpdate) {
        if (!metadataToUpdate.isEmpty()) {
            NmsUpdateDisplayMetadataData metadataData = new NmsUpdateDisplayMetadataData(metadataToUpdate);
            renderer.updateMetadata(player, metadataData);
        }
    }

    private void handleMoveIntent(Player player, MoveRenderIntent intent) {
        DecentPosition position = mapPosition(intent.getLocation());

        NmsMoveDisplayData moveDisplayData = new NmsMoveDisplayData(position);
        renderer.move(player, moveDisplayData);
    }

    private void handleDespawnIntent(Player player) {
        renderer.despawn(player);
    }

    private DecentPosition mapPosition(DecentLocation location) {
        return new DecentPosition(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    private List<NmsDisplayMetadata<?>> mapMetadata(List<MetadataValue<?>> metadataValues) {
        List<NmsDisplayMetadata<?>> mappedValues = new ArrayList<>(metadataValues.size());
        for (MetadataValue<?> value : metadataValues) {
            mappedValues.add(mapMetadatum(value));
        }
        return mappedValues;
    }

    private <T> NmsDisplayMetadata<T> mapMetadatum(MetadataValue<T> metadataValue) {
        return new NmsDisplayMetadata<>(metadataValue.getKey(), metadataValue.getValue());
    }
}

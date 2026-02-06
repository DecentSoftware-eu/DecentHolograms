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
import eu.decentsoftware.holograms.nms.api.display.NmsItemDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.NmsMoveDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsSpawnDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayContentData;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayMetadataData;
import eu.decentsoftware.holograms.platform.api.data.ItemDescriptor;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayContent;
import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;
import eu.decentsoftware.holograms.platform.api.render.intent.DespawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.MoveRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.SpawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateDisplayContentRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateMetadataRenderIntent;
import eu.decentsoftware.holograms.platform.bukkit.render.BukkitItemFactory;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BukkitItemDisplayRenderService extends BukkitDisplayRenderService {

    private final NmsItemDisplayRenderer renderer;
    private final BukkitItemFactory itemFactory;

    public BukkitItemDisplayRenderService(NmsItemDisplayRenderer renderer, BukkitItemFactory itemFactory) {
        this.renderer = renderer;
        this.itemFactory = itemFactory;
    }

    @Override
    public void apply(Player player, RenderObjectHandle handle, List<RenderIntent> intents) {
        List<NmsDisplayMetadata<?>> metadataToUpdate = new ArrayList<>();
        for (RenderIntent intent : intents) {
            if (intent instanceof SpawnDisplayRenderIntent) {
                handleSpawnIntent(player, (SpawnDisplayRenderIntent) intent);
            } else if (intent instanceof UpdateDisplayContentRenderIntent) {
                handleUpdateContentIntent(player, (UpdateDisplayContentRenderIntent) intent);
            } else if (intent instanceof UpdateMetadataRenderIntent<?>) {
                handleUpdateMetadataIntent((UpdateMetadataRenderIntent<?>) intent, metadataToUpdate);
            } else if (intent instanceof MoveRenderIntent) {
                handleMoveIntent(player, (MoveRenderIntent) intent);
            } else if (intent instanceof DespawnDisplayRenderIntent) {
                handleDespawnIntent(player);
            }
        }

        // Send all metadata at once; it can be in a single packet
        sendCollectedMetadata(player, metadataToUpdate);
    }

    private void handleSpawnIntent(Player player, SpawnDisplayRenderIntent intent) {
        DecentPosition position = mapPosition(intent.getLocation());
        List<NmsDisplayMetadata<?>> metadata = mapMetadata(intent.getMetadataValues());
        ItemStack itemStack = getItemStackFromContent(intent.getContent());

        NmsSpawnDisplayData<ItemStack> spawnDisplayData = new NmsSpawnDisplayData<>(position, metadata, itemStack);
        renderer.spawn(player, spawnDisplayData);
    }

    private void handleUpdateContentIntent(Player player, UpdateDisplayContentRenderIntent intent) {
        ItemStack itemStack = getItemStackFromContent(intent.getContent());

        NmsUpdateDisplayContentData<ItemStack> updateDisplayData = new NmsUpdateDisplayContentData<>(itemStack);
        renderer.updateContent(player, updateDisplayData);
    }

    private void handleUpdateMetadataIntent(UpdateMetadataRenderIntent<?> intent, List<NmsDisplayMetadata<?>> metadataToUpdate) {
        NmsDisplayMetadata<?> metadatum = mapMetadatum(intent.getValue());
        metadataToUpdate.add(metadatum);
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

    private ItemStack getItemStackFromContent(DisplayContent<?> content) {
        if (!(content instanceof ItemDisplayContent)) {
            throw new IllegalArgumentException("Unsupported content type for Item display: " + content.getClass().getName());
        }
        ItemDescriptor itemDescriptor = ((ItemDisplayContent) content).getContent();
        return itemFactory.createItemStack(itemDescriptor);
    }
}

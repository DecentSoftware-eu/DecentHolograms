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

package eu.decentsoftware.holograms.nms.v1_21_R5;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.renderer.NmsDisplayRenderer;
import eu.decentsoftware.holograms.platform.api.data.ItemDescriptor;
import eu.decentsoftware.holograms.platform.api.data.display.BlockDisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayContent;
import eu.decentsoftware.holograms.platform.api.render.intent.DespawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.MoveRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.SpawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateDisplayContentRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateMetadataRenderIntent;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.network.syncher.DataWatcher;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

class DisplayRenderer implements NmsDisplayRenderer {

    private final int entityId;
    private final DisplayType displayType;

    DisplayRenderer(EntityIdGenerator entityIdGenerator, DisplayType displayType) {
        this.entityId = entityIdGenerator.getFreeEntityId();
        this.displayType = displayType;
    }

    @Override
    public void accept(Player player, List<RenderIntent> intents) {
        EntityPacketsBuilder packetsBuilder = EntityPacketsBuilder.create();
        EntityMetadataBuilder metadataBuilder = EntityMetadataBuilder.create();

        boolean despawn = false;
        for (RenderIntent intent : intents) {
            if (intent instanceof UpdateMetadataRenderIntent<?> updateMetadataIntent) {
                // TODO: handle metadata updates
            } else if (intent instanceof UpdateDisplayContentRenderIntent updateContentIntent) {
                applyContent(updateContentIntent, metadataBuilder);
            } else if (intent instanceof MoveRenderIntent moveIntent) {
                DecentPosition position = moveIntent.getLocation().toDecentPosition();
                packetsBuilder.withTeleportEntity(entityId, position);
            } else if (intent instanceof SpawnDisplayRenderIntent spawnIntent) {
                DecentPosition position = spawnIntent.getLocation().toDecentPosition();
                packetsBuilder.withSpawnEntity(entityId, getEntityType(), position);
            } else if (intent instanceof DespawnDisplayRenderIntent) {
                despawn = true;
            }
        }

        List<DataWatcher.Item<?>> watchableObjects = metadataBuilder.toWatchableObjects();
        if (!watchableObjects.isEmpty()) {
            packetsBuilder.withEntityMetadata(entityId, watchableObjects);
        }

        if (despawn) {
            // Remove entity packet should be sent last
            packetsBuilder.withRemoveEntity(entityId);
        }

        packetsBuilder.sendTo(player);
    }

    private void applyContent(UpdateDisplayContentRenderIntent updateContentIntent, EntityMetadataBuilder metadataBuilder) {
        if (displayType == DisplayType.TEXT) {
            if (!(updateContentIntent.getContent() instanceof TextDisplayContent textContent)) {
                throw unsupportedContentException(updateContentIntent, "TextDisplayContent");
            }
            metadataBuilder.withTextDisplayText(textContent.getContent());
        } else if (displayType == DisplayType.ITEM) {
            if (!(updateContentIntent.getContent() instanceof ItemDisplayContent itemContent)) {
                throw unsupportedContentException(updateContentIntent, "ItemDisplayContent");
            }
            ItemDescriptor itemDescriptor = itemContent.getContent();
            String type = itemDescriptor.getType();
            Material material = Material.matchMaterial(type);
            if (material == null) {
                throw new DecentHologramsNmsException("Invalid item type: " + type);
            }
            ItemStack itemStack = new ItemStack(material);
            metadataBuilder.withItemDisplayItemStack(itemStack);
        } else if (displayType == DisplayType.BLOCK) {
            if (!(updateContentIntent.getContent() instanceof BlockDisplayContent blockContent)) {
                throw unsupportedContentException(updateContentIntent, "BlockDisplayContent");
            }
            String type = blockContent.getContent().getType();
            Material material = Material.matchMaterial(type);
            if (material == null) {
                throw new DecentHologramsNmsException("Invalid block type: " + type);
            }
            metadataBuilder.withBlockDisplayBlockData(material);
        }
    }

    private DecentHologramsNmsException unsupportedContentException(UpdateDisplayContentRenderIntent updateContentIntent,
                                                                    String expectedContent) {
        return new DecentHologramsNmsException("Unsupported display content type: " + updateContentIntent.getContent().getClass().getName()
                + " (expected " + expectedContent + ")");
    }

    private EntityType getEntityType() {
        return switch (displayType) {
            case TEXT -> EntityType.TEXT_DISPLAY;
            case ITEM -> EntityType.ITEM_DISPLAY;
            case BLOCK -> EntityType.BLOCK_DISPLAY;
        };
    }
}

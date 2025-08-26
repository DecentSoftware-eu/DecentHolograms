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

import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayData;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsItemDisplayRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class ItemDisplayRenderer implements NmsItemDisplayRenderer {

    private final int entityId;

    ItemDisplayRenderer(EntityIdGenerator entityIdGenerator) {
        this.entityId = entityIdGenerator.getFreeEntityId();
    }

    @Override
    public void display(Player player, NmsHologramPartData<ItemDisplayData> data) {
        ItemDisplayData itemDisplayData = data.getContent();
        DecentPosition position = data.getPosition();
        EntityPacketsBuilder.create()
                .withSpawnEntity(entityId, EntityType.ITEM_DISPLAY, offsetPosition(position))
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withNoGravity()
                        .withEntityProperties(false, false, false, false, false, itemDisplayData.getGlowColor() != null, false)
                        .withItemDisplayData(itemDisplayData.getDisplayType())
                        .withItemDisplayItemStack(itemDisplayData.getDisplayedItem())
                        .withDisplayTranslation(itemDisplayData.getTranslation())
                        .withDisplayScale(itemDisplayData.getScale())
                        .withDisplayBillboardConstraints(itemDisplayData.getBillboardConstraints())
                        .withDisplayBrightness(itemDisplayData.getBrightnessOverride())
                        .withDisplayViewRange(itemDisplayData.getViewRange())
                        .withDisplayShadowRadius(itemDisplayData.getShadowRadius())
                        .withDisplayShadowStrength(itemDisplayData.getShadowStrength())
                        .withDisplayGlowColorOverride(itemDisplayData.getGlowColor())
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void updateProperties(Player player, NmsHologramPartData<ItemDisplayData> data) {
        ItemDisplayData itemDisplayData = data.getContent();
        EntityPacketsBuilder.create()
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withEntityProperties(false, false, false, false, false, itemDisplayData.getGlowColor() != null, false)
                        .withItemDisplayData(itemDisplayData.getDisplayType())
                        .withDisplayTranslation(itemDisplayData.getTranslation())
                        .withDisplayScale(itemDisplayData.getScale())
                        .withDisplayBillboardConstraints(itemDisplayData.getBillboardConstraints())
                        .withDisplayBrightness(itemDisplayData.getBrightnessOverride())
                        .withDisplayViewRange(itemDisplayData.getViewRange())
                        .withDisplayShadowRadius(itemDisplayData.getShadowRadius())
                        .withDisplayShadowStrength(itemDisplayData.getShadowStrength())
                        .withDisplayGlowColorOverride(itemDisplayData.getGlowColor())
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, NmsHologramPartData<ItemDisplayData> data) {
        EntityPacketsBuilder.create()
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withItemDisplayItemStack(data.getContent().getDisplayedItem())
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void move(Player player, NmsHologramPartData<ItemDisplayData> data) {
        EntityPacketsBuilder.create()
                .withTeleportEntity(entityId, offsetPosition(data.getPosition()))
                .sendTo(player);
    }

    @Override
    public void hide(Player player) {
        EntityPacketsBuilder.create()
                .withRemoveEntity(entityId)
                .sendTo(player);
    }

    @Override
    public double getHeight(NmsHologramPartData<ItemDisplayData> data) {
        return 0;
    }

    @Override
    public int[] getEntityIds() {
        return new int[]{entityId};
    }

    private DecentPosition offsetPosition(DecentPosition position) {
        return position;
    }
}

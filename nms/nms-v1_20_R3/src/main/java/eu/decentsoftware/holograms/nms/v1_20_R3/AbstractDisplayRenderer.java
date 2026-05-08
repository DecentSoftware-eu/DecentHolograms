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

package eu.decentsoftware.holograms.nms.v1_20_R3;

import eu.decentsoftware.holograms.nms.api.display.NmsDisplayMetadata;
import eu.decentsoftware.holograms.nms.api.display.NmsDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.NmsMoveDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayMetadataData;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayProperties;
import eu.decentsoftware.holograms.platform.api.render.metadata.DisplayMetadataType;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.network.syncher.DataWatcher;
import org.bukkit.entity.Player;

import java.util.List;

abstract class AbstractDisplayRenderer<C> implements NmsDisplayRenderer<C> {

    protected final int entityId;

    protected AbstractDisplayRenderer(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void despawn(Player player) {
        EntityPacketsBuilder.create()
                .withRemoveEntity(entityId)
                .sendTo(player);
    }

    @Override
    public void move(Player player, NmsMoveDisplayData data) {
        DecentPosition position = data.getPosition();
        EntityPacketsBuilder.create()
                .withTeleportEntity(entityId, position)
                .sendTo(player);
    }

    @Override
    public void updateMetadata(Player player, NmsUpdateDisplayMetadataData data) {
        EntityMetadataBuilder metadataBuilder = EntityMetadataBuilder.create();
        List<NmsDisplayMetadata<?>> metadata = data.getMetadata();
        applyMetadata(metadata, metadataBuilder);

        List<DataWatcher.Item<?>> watchableObjects = metadataBuilder.toWatchableObjects();
        if (!watchableObjects.isEmpty()) {
            EntityPacketsBuilder.create()
                    .withEntityMetadata(entityId, watchableObjects)
                    .sendTo(player);
        }
    }

    protected void applyMetadata(List<NmsDisplayMetadata<?>> metadata, EntityMetadataBuilder metadataBuilder) {
        for (NmsDisplayMetadata<?> metadatum : metadata) {
            applyMetadatum(metadatum, metadataBuilder);
        }
    }

    private <T> void applyMetadatum(NmsDisplayMetadata<T> metadatum, EntityMetadataBuilder metadataBuilder) {
        MetadataKey<T> key = metadatum.getKey();
        if (key.getType().equals(DisplayMetadataType.TRANSLATION)) {
            metadataBuilder.withDisplayTranslation((DecentVector3f) metadatum.getValue());
        } else if (key.getType().equals(DisplayMetadataType.SCALE)) {
            metadataBuilder.withDisplayScale((DecentVector3f) metadatum.getValue());
        } else if (key.getType().equals(DisplayMetadataType.BILLBOARD_CONSTRAINTS)) {
            metadataBuilder.withDisplayBillboardConstraints((DisplayBillboardConstraints) metadatum.getValue());
        } else if (key.getType().equals(DisplayMetadataType.BRIGHTNESS)) {
            metadataBuilder.withDisplayBrightness((DisplayBrightness) metadatum.getValue());
        } else if (key.getType().equals(DisplayMetadataType.SHADOW_RADIUS)) {
            metadataBuilder.withDisplayShadowRadius((Float) metadatum.getValue());
        } else if (key.getType().equals(DisplayMetadataType.SHADOW_STRENGTH)) {
            metadataBuilder.withDisplayShadowStrength((Float) metadatum.getValue());
        } else if (key.getType().equals(DisplayMetadataType.GLOWING)) {
            boolean glowing = (Boolean) metadatum.getValue();
            metadataBuilder.withGlowing(glowing);
        } else if (key.getType().equals(DisplayMetadataType.GLOW_COLOR_OVERRIDE)) {
            DecentColor colorOverride = (DecentColor) metadatum.getValue();
            metadataBuilder.withDisplayGlowColorOverride(colorOverride);
        } else if (key.getType().equals(DisplayMetadataType.TEXT_DISPLAY_BACKGROUND)) {
            metadataBuilder.withTextDisplayBackground((DecentColor) metadatum.getValue());
        } else if (key.getType().equals(DisplayMetadataType.TEXT_DISPLAY_OPACITY)) {
            metadataBuilder.withTextDisplayTextOpacity((Integer) metadatum.getValue());
        } else if (key.getType().equals(DisplayMetadataType.TEXT_DISPLAY_PROPERTIES)) {
            TextDisplayProperties properties = (TextDisplayProperties) metadatum.getValue();
            metadataBuilder.withTextDisplayProperties(
                    properties.hasShadow(),
                    properties.isSeeThrough(),
                    properties.getAlignment()
            );
        } else if (key.getType().equals(DisplayMetadataType.TEXT_LINE_WIDTH)) {
            metadataBuilder.withTextDisplayLineWidth((Integer) metadatum.getValue());
        } else if (key.getType().equals(DisplayMetadataType.ITEM_DISPLAY_TYPE)) {
            ItemDisplayType itemDisplayType = (ItemDisplayType) metadatum.getValue();
            metadataBuilder.withItemDisplayData(itemDisplayType);
        } else {
            throw new IllegalStateException("Unexpected value: " + key.getType());
        }
    }
}

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
import eu.decentsoftware.holograms.nms.api.display.NmsMoveDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsSpawnDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsTextDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayContentData;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayMetadataData;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayContent;
import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;
import eu.decentsoftware.holograms.platform.api.render.intent.DespawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.MoveRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.SpawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateDisplayContentRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateMetadataRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BukkitTextDisplayRenderService extends BukkitDisplayRenderService {

    private static final String LINE_SEPARATOR = ChatColor.RESET + "\n";
    private final NmsTextDisplayRenderer renderer;

    public BukkitTextDisplayRenderService(NmsTextDisplayRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void apply(Player player, RenderObjectHandle handle, List<RenderIntent> intents) {
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

    private void handleSpawnIntent(Player player, SpawnDisplayRenderIntent intent) {
        DecentPosition position = mapPosition(intent.getLocation());
        List<NmsDisplayMetadata<?>> metadata = mapMetadata(intent.getMetadataValues());
        String text = getTextFromContent(intent.getContent());

        NmsSpawnDisplayData<String> spawnDisplayData = new NmsSpawnDisplayData<>(position, metadata, text);
        renderer.spawn(player, spawnDisplayData);
    }

    private void handleUpdateContentIntent(Player player, UpdateDisplayContentRenderIntent intent) {
        String text = getTextFromContent(intent.getContent());

        NmsUpdateDisplayContentData<String> updateDisplayData = new NmsUpdateDisplayContentData<>(text);
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

    private String getTextFromContent(DisplayContent<?> content) {
        if (!(content instanceof TextDisplayContent)) {
            throw new IllegalArgumentException("Unsupported content type for Text display: " + content.getClass().getName());
        }

        List<String> lines = ((TextDisplayContent) content).getContent();
        boolean firstLine = true;
        StringBuilder textBuilder = new StringBuilder();
        for (String line : lines) {
            if (firstLine) {
                firstLine = false;
            } else {
                textBuilder.append(LINE_SEPARATOR);
            }
            textBuilder.append(line);
        }
        return textBuilder.toString();
    }
}

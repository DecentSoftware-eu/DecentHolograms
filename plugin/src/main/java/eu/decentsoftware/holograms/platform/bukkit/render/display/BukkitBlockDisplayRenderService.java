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

import eu.decentsoftware.holograms.api.utils.items.DecentMaterial;
import eu.decentsoftware.holograms.nms.api.display.NmsBlockDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.NmsDisplayMetadata;
import eu.decentsoftware.holograms.nms.api.display.NmsSpawnDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayContentData;
import eu.decentsoftware.holograms.platform.api.data.BlockDescriptor;
import eu.decentsoftware.holograms.platform.api.data.display.BlockDisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.render.intent.SpawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateDisplayContentRenderIntent;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.Material;

import java.util.List;

public class BukkitBlockDisplayRenderService extends BukkitDisplayRenderService<Material> {

    public BukkitBlockDisplayRenderService(NmsBlockDisplayRenderer renderer) {
        super(renderer);
    }

    @Override
    protected NmsSpawnDisplayData<Material> createNmsSpawnDisplayData(SpawnDisplayRenderIntent intent,
                                                                      DecentPosition position,
                                                                      List<NmsDisplayMetadata<?>> metadata) {
        Material material = getMaterialFromContent(intent.getContent());
        return new NmsSpawnDisplayData<>(position, metadata, material);
    }

    @Override
    protected NmsUpdateDisplayContentData<Material> createNmsUpdateDisplayContentData(UpdateDisplayContentRenderIntent intent) {
        Material material = getMaterialFromContent(intent.getContent());
        return new NmsUpdateDisplayContentData<>(material);
    }

    private Material getMaterialFromContent(DisplayContent<?> content) {
        if (!(content instanceof BlockDisplayContent)) {
            throw new IllegalArgumentException("Unsupported content type for Block display: " + content.getClass().getName());
        }
        BlockDisplayContent blockDisplayContent = (BlockDisplayContent) content;
        BlockDescriptor blockDescriptor = blockDisplayContent.getContent();
        Material material = DecentMaterial.parseMaterial(blockDescriptor.getType());
        if (material == null) {
            return Material.STONE;
        }
        return material;
    }
}

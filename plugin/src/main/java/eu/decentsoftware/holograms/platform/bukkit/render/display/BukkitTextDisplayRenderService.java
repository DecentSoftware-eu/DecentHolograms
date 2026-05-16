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
import eu.decentsoftware.holograms.nms.api.display.NmsSpawnDisplayData;
import eu.decentsoftware.holograms.nms.api.display.NmsTextDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.NmsUpdateDisplayContentData;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayContent;
import eu.decentsoftware.holograms.platform.api.render.intent.SpawnDisplayRenderIntent;
import eu.decentsoftware.holograms.platform.api.render.intent.UpdateDisplayContentRenderIntent;
import eu.decentsoftware.holograms.shared.DecentPosition;

import java.util.List;

public class BukkitTextDisplayRenderService extends BukkitDisplayRenderService<List<String>> {

    public BukkitTextDisplayRenderService(NmsTextDisplayRenderer renderer) {
        super(renderer);
    }

    @Override
    protected NmsSpawnDisplayData<List<String>> createNmsSpawnDisplayData(SpawnDisplayRenderIntent intent,
                                                                          DecentPosition position,
                                                                          List<NmsDisplayMetadata<?>> metadata) {
        List<String> text = getTextFromContent(intent.getContent());
        return new NmsSpawnDisplayData<>(position, metadata, text);
    }

    @Override
    protected NmsUpdateDisplayContentData<List<String>> createNmsUpdateDisplayContentData(UpdateDisplayContentRenderIntent intent) {
        List<String> text = getTextFromContent(intent.getContent());
        return new NmsUpdateDisplayContentData<>(text);
    }

    private List<String> getTextFromContent(DisplayContent<?> content) {
        if (!(content instanceof TextDisplayContent)) {
            throw new IllegalArgumentException("Unsupported content type for Text display: " + content.getClass().getName());
        }
        return ((TextDisplayContent) content).getContent();
    }
}

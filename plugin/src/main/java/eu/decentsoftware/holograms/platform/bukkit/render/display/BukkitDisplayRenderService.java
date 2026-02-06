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
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;
import eu.decentsoftware.holograms.platform.api.render.intent.RenderIntent;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BukkitDisplayRenderService {

    public abstract void apply(Player player, RenderObjectHandle handle, List<RenderIntent> intents);

    protected DecentPosition mapPosition(DecentLocation location) {
        return new DecentPosition(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    protected List<NmsDisplayMetadata<?>> mapMetadata(Map<MetadataKey<?>, MetadataValue<?>> metadataValues) {
        return metadataValues.values().stream()
                .map(this::mapMetadatum)
                .collect(Collectors.toList());
    }

    protected <T> NmsDisplayMetadata<T> mapMetadatum(MetadataValue<T> metadataValue) {
        return new NmsDisplayMetadata<>(metadataValue.getKey(), metadataValue.getValue());
    }
}

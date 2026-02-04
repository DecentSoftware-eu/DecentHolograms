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

package eu.decentsoftware.holograms.display.config.serializer;

import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class DisplayBrightnessSerializer implements TypeSerializer<DisplayBrightness> {

    @Override
    public DisplayBrightness deserialize(Type type, ConfigurationNode node) {
        int blockLight = node.node("block-light").getInt();
        int skyLight = node.node("sky-light").getInt();
        return DisplayBrightness.of(blockLight, skyLight);
    }

    @Override
    public void serialize(Type type, @Nullable DisplayBrightness obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }
        node.node("block-light").set(obj.getBlockLight());
        node.node("sky-light").set(obj.getSkyLight());
    }
}

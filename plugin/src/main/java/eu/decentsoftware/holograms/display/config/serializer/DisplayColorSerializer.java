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

import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class DisplayColorSerializer implements TypeSerializer<DisplayColor> {

    @Override
    public DisplayColor deserialize(Type type, ConfigurationNode node) {
        int alpha = node.node("alpha").getInt(255);
        int red = node.node("red").getInt();
        int green = node.node("green").getInt();
        int blue = node.node("blue").getInt();
        return new DisplayColor(alpha, red, green, blue);
    }

    @Override
    public void serialize(Type type, @Nullable DisplayColor obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node("alpha").set(obj.getAlpha());
        node.node("red").set(obj.getRed());
        node.node("green").set(obj.getGreen());
        node.node("blue").set(obj.getBlue());
    }
}

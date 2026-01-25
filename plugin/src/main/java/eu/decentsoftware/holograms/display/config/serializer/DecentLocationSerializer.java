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

import eu.decentsoftware.holograms.location.DecentLocation;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class DecentLocationSerializer implements TypeSerializer<DecentLocation> {

    @Override
    public DecentLocation deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String world = node.node("world").getString();
        if (world == null) {
            throw new SerializationException("World name is missing.");
        }
        double x = node.node("x").getDouble();
        double y = node.node("y").getDouble();
        double z = node.node("z").getDouble();
        float yaw = node.node("yaw").getFloat(0f);
        float pitch = node.node("pitch").getFloat(0f);
        return new DecentLocation(world, x, y, z, yaw, pitch);
    }

    @Override
    public void serialize(Type type, @Nullable DecentLocation obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node("world").set(obj.getWorldName());
        node.node("x").set(obj.getX());
        node.node("y").set(obj.getY());
        node.node("z").set(obj.getZ());
        node.node("yaw").set(obj.getYaw());
        node.node("pitch").set(obj.getPitch());
    }
}

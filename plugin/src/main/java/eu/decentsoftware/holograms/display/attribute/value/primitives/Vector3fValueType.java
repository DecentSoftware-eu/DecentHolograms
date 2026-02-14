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

package eu.decentsoftware.holograms.display.attribute.value.primitives;

import eu.decentsoftware.holograms.display.attribute.value.AttributeValueType;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public final class Vector3fValueType implements AttributeValueType<Vector3fValue, DecentVector3f> {

    public static final String TYPE_ID = "vector3f";

    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    @Override
    public Class<Vector3fValue> getValueClass() {
        return Vector3fValue.class;
    }

    @Override
    public Class<DecentVector3f> getOutputType() {
        return DecentVector3f.class;
    }

    @Override
    public void serialize(Vector3fValue value, ConfigurationNode node) throws SerializationException {
        node.node("x").set(value.getX());
        node.node("y").set(value.getY());
        node.node("z").set(value.getZ());
    }

    @Override
    public Vector3fValue deserialize(ConfigurationNode node) throws SerializationException {
        float x = node.node("x").getFloat();
        float y = node.node("y").getFloat();
        float z = node.node("z").getFloat();
        return new Vector3fValue(x, y, z);
    }
}

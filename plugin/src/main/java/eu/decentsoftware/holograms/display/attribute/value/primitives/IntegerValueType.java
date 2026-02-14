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
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public final class IntegerValueType implements AttributeValueType<IntegerValue, Integer> {

    public static final String TYPE_ID = "integer";

    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    @Override
    public Class<IntegerValue> getValueClass() {
        return IntegerValue.class;
    }

    @Override
    public Class<Integer> getOutputType() {
        return Integer.class;
    }

    @Override
    public void serialize(IntegerValue value, ConfigurationNode node) throws SerializationException {
        node.set(value.getValue());
    }

    @Override
    public IntegerValue deserialize(ConfigurationNode node) throws SerializationException {
        int value = node.getInt();
        return new IntegerValue(value);
    }
}

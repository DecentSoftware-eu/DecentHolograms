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

package eu.decentsoftware.holograms.display.attribute.definition;

import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.primitives.FloatValue;
import eu.decentsoftware.holograms.display.render.state.MutableRenderState;
import org.jetbrains.annotations.NotNull;

public class ShadowRadiusAttributeDefinition implements AttributeDefinition<Float> {

    public static final AttributeKey<Float> KEY = AttributeKey.of("shadow-radius", Float.class);

    @Override
    public @NotNull AttributeKey<Float> getKey() {
        return KEY;
    }

    @Override
    public AttributeValue<Float> getDefaultValue() {
        return new FloatValue(0.0f);
    }

    @Override
    public void apply(CompiledAttributeValue<Float> value, MutableRenderState state) {
        state.setShadowRadius(value.evaluate());
    }
}

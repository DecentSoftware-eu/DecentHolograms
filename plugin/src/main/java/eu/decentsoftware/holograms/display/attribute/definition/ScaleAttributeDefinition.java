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
import eu.decentsoftware.holograms.display.attribute.value.primitives.Vector3fValue;
import eu.decentsoftware.holograms.display.render.state.PresentedRenderState;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;
import org.jetbrains.annotations.NotNull;

public class ScaleAttributeDefinition implements AttributeDefinition<DecentVector3f> {

    public static final AttributeKey<DecentVector3f> KEY = AttributeKey.of("scale", DecentVector3f.class);
    private static final Vector3fValue DEFAULT_VALUE = new Vector3fValue(1, 1, 1);

    @Override
    public @NotNull AttributeKey<DecentVector3f> getKey() {
        return KEY;
    }

    @Override
    public AttributeValue<DecentVector3f> getDefaultValue() {
        return DEFAULT_VALUE;
    }

    @Override
    public void apply(CompiledAttributeValue<DecentVector3f> value, PresentedRenderState state) {
        state.setScale(value.evaluate());
    }
}

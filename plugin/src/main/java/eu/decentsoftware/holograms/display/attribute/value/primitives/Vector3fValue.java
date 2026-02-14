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

import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.StaticCompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;

public final class Vector3fValue implements AttributeValue<DecentVector3f> {

    private final float x;
    private final float y;
    private final float z;

    public Vector3fValue(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String getTypeKey() {
        return Vector3fValueType.TYPE_ID;
    }

    @Override
    public CompiledAttributeValue<DecentVector3f> compile(DisplayRenderContext context) {
        DecentVector3f vector3f = new DecentVector3f(x, y, z);
        return new StaticCompiledAttributeValue<>(vector3f);
    }

    @Override
    public String toHumanReadableString() {
        return String.format("(%s, %s, %s)", x, y, z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}

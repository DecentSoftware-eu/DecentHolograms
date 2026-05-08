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

package eu.decentsoftware.holograms.display.attribute.value.display;

import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.StaticCompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;

public final class BillboardConstraintsValue implements AttributeValue<DisplayBillboardConstraints> {

    private final DisplayBillboardConstraints billboardConstraints;

    public BillboardConstraintsValue(DisplayBillboardConstraints billboardConstraints) {
        this.billboardConstraints = billboardConstraints;
    }

    @Override
    public String getTypeKey() {
        return BillboardConstraintsValueType.TYPE_ID;
    }

    @Override
    public CompiledAttributeValue<DisplayBillboardConstraints> compile(DisplayRenderContext context) {
        return new StaticCompiledAttributeValue<>(billboardConstraints);
    }

    @Override
    public String toHumanReadableString() {
        return billboardConstraints.name();
    }

    public DisplayBillboardConstraints getBillboardConstraints() {
        return billboardConstraints;
    }
}

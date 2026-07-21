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
import eu.decentsoftware.holograms.display.attribute.value.display.BillboardConstraintsValue;
import eu.decentsoftware.holograms.display.render.state.PresentedRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;
import org.jetbrains.annotations.NotNull;

public class BillboardAttributeDefinition implements AttributeDefinition<DisplayBillboardConstraints> {

    public static final AttributeKey<DisplayBillboardConstraints> KEY = AttributeKey.of("billboard", DisplayBillboardConstraints.class);
    private static final BillboardConstraintsValue DEFAULT_VALUE = new BillboardConstraintsValue(DisplayBillboardConstraints.FIXED);

    @Override
    public @NotNull AttributeKey<DisplayBillboardConstraints> getKey() {
        return KEY;
    }

    @Override
    public AttributeValue<DisplayBillboardConstraints> getDefaultValue() {
        return DEFAULT_VALUE;
    }

    @Override
    public void apply(CompiledAttributeValue<DisplayBillboardConstraints> value, PresentedRenderState state) {
        state.setBillboardConstraints(value.evaluate());
    }
}

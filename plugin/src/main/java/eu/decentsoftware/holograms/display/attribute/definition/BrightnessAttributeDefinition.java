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
import eu.decentsoftware.holograms.display.render.state.MutableRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import org.jetbrains.annotations.NotNull;

public class BrightnessAttributeDefinition implements AttributeDefinition<DisplayBrightness> {

    public static final AttributeKey<DisplayBrightness> KEY = AttributeKey.of("brightness", DisplayBrightness.class);

    @Override
    public @NotNull AttributeKey<DisplayBrightness> getKey() {
        return KEY;
    }

    @Override
    public AttributeValue<DisplayBrightness> getDefaultValue() {
        return null;
    }

    @Override
    public void apply(CompiledAttributeValue<DisplayBrightness> value, MutableRenderState state) {
        state.setBrightness(value.evaluate());
    }
}

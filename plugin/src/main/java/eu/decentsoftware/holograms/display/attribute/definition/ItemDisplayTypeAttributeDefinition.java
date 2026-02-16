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
import eu.decentsoftware.holograms.display.attribute.value.display.ItemDisplayTypeValue;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayType;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import org.jetbrains.annotations.NotNull;

public class ItemDisplayTypeAttributeDefinition implements AttributeDefinition<ItemDisplayType> {

    public static final AttributeKey<ItemDisplayType> KEY = AttributeKey.of("display-type", ItemDisplayType.class);
    private static final ItemDisplayTypeValue DEFAULT_VALUE = new ItemDisplayTypeValue(ItemDisplayType.NONE);

    @Override
    public @NotNull AttributeKey<ItemDisplayType> getKey() {
        return KEY;
    }

    @Override
    public AttributeValue<ItemDisplayType> getDefaultValue() {
        return DEFAULT_VALUE;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.ITEM};
    }

    @Override
    public void apply(CompiledAttributeValue<ItemDisplayType> value, FinalDisplayRenderState state) {
        state.addMetadata(BuiltInMetadataKeys.ITEM_DISPLAY_TYPE.createValue(value.evaluate()));
    }
}


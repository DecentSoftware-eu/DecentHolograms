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
import eu.decentsoftware.holograms.display.render.state.PresentedRenderState;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayContent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class ItemLeatherColorAttributeDefinition implements AttributeDefinition<DecentColor> {

    public static final AttributeKey<DecentColor> KEY = AttributeKey.of("leather-color", DecentColor.class);

    @Override
    public @NotNull AttributeKey<DecentColor> getKey() {
        return KEY;
    }

    @Override
    public @Nullable AttributeValue<DecentColor> getDefaultValue() {
        return null;
    }

    @Override
    public void apply(CompiledAttributeValue<DecentColor> value, PresentedRenderState state) {
        if (!(state.getContent() instanceof ItemDisplayContent)) {
            return;
        }
        ItemDisplayContent itemDisplayContent = (ItemDisplayContent) state.getContent();
        itemDisplayContent.getContent().setLeatherColor(value.evaluate());
        state.setContent(itemDisplayContent); // set to mark dirty
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.ITEM};
    }
}

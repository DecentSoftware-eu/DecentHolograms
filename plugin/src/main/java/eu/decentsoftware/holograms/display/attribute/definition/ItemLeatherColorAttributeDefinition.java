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
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayContent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ItemLeatherColorAttributeDefinition implements AttributeDefinition<DecentColor> {

    public static final AttributeKey<DecentColor> KEY = AttributeKey.of("leather-color", DecentColor.class);
    private final DecentColorAttributeCommandHandler commandHandler = new DecentColorAttributeCommandHandler();

    @Override
    public @NotNull AttributeKey<DecentColor> getKey() {
        return KEY;
    }

    @Override
    public @Nullable DecentColor getDefaultValue() {
        return null;
    }

    @Override
    public void apply(DisplayAttribute<DecentColor> attribute, DisplayRenderState state, DisplayRenderContext context) {
        if (!(state.getContent() instanceof ItemDisplayContent)) {
            return;
        }
        ItemDisplayContent itemDisplayContent = (ItemDisplayContent) state.getContent();
        itemDisplayContent.getContent().setLeatherColor(attribute.getValue());
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.ITEM};
    }

    @Override
    public String format(DecentColor value) {
        return commandHandler.format(value);
    }

    @Override
    public @NotNull DecentColor parse(String[] args) {
        return commandHandler.parseColor(args);
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        return commandHandler.getHints(args);
    }
}

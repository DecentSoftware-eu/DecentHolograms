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
import eu.decentsoftware.holograms.display.render.placeholder.DisplayPlaceholderService;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderState;
import eu.decentsoftware.holograms.integration.Integration;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayContent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemSkullTextureAttributeDefinition implements AttributeDefinition<String> {

    public static final AttributeKey<String> KEY = AttributeKey.of("skull-texture", String.class);
    private final DisplayPlaceholderService placeholderService;

    public ItemSkullTextureAttributeDefinition(DisplayPlaceholderService placeholderService) {
        this.placeholderService = placeholderService;
    }

    @Override
    public @NotNull AttributeKey<String> getKey() {
        return KEY;
    }

    @Override
    public @Nullable String getDefaultValue() {
        return null;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.ITEM};
    }

    @Override
    public void apply(DisplayAttribute<String> attribute, DisplayRenderState state, DisplayRenderContext context) {
        if (!(state.getContent() instanceof ItemDisplayContent)) {
            return;
        }
        String value = attribute.getValue();
        if (value != null) {
            value = placeholderService.replacePlaceholders(value, context);
        }
        ItemDisplayContent itemDisplayContent = (ItemDisplayContent) state.getContent();
        itemDisplayContent.getContent().setSkullTexture(value);
    }

    @Override
    public @NotNull String parse(String[] args) {
        return args[0];
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> hints = new ArrayList<>();
            hints.add("{player}");
            if (Integration.PLACEHOLDER_API.isAvailable()) {
                hints.add("%player_name%");
            }
            if (Integration.HEAD_DATABASE.isAvailable()) {
                hints.add("HEADDATABASE_<id>");
            }
            Bukkit.getOnlinePlayers().forEach(player -> hints.add(player.getName()));
            return hints;
        }
        return Collections.emptyList();
    }
}

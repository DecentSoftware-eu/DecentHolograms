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

package eu.decentsoftware.holograms.display.attribute.command.handler;

import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.primitives.StringValueFactory;
import eu.decentsoftware.holograms.integration.Integration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultSkullTextureHandler implements AttributeCommandHandler<String> {

    private final StringValueFactory attributeValueFactory;

    public DefaultSkullTextureHandler(StringValueFactory attributeValueFactory) {
        this.attributeValueFactory = attributeValueFactory;
    }

    @Override
    public String getKeyword() {
        return null;
    }

    @Override
    public AttributeValue<String> parse(String[] args) {
        return attributeValueFactory.create(args[0]);
    }

    @Override
    public List<String> getHints(CommandSender sender, String[] args) {
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

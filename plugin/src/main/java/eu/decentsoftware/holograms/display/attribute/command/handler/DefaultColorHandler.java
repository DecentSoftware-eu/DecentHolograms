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
import eu.decentsoftware.holograms.display.attribute.value.color.RgbaValue;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DefaultColorHandler implements AttributeCommandHandler<DecentColor> {

    private final DecentColorCommandHelper commandHelper = new DecentColorCommandHelper();

    @Override
    public String getKeyword() {
        return null;
    }

    @Override
    public AttributeValue<DecentColor> parse(String[] args) {
        DecentColor parsedColor = commandHelper.parseColor(args);
        return new RgbaValue(
                parsedColor.getRed(),
                parsedColor.getGreen(),
                parsedColor.getBlue(),
                parsedColor.getAlpha()
        );
    }

    @Override
    public List<String> getHints(CommandSender sender, String[] args) {
        return commandHelper.getHints(args);
    }
}

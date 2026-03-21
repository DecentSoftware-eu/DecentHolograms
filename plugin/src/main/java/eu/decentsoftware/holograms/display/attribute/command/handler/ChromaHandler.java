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

import eu.decentsoftware.holograms.display.attribute.AttributeParseException;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.color.ChromaValue;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChromaHandler implements AttributeCommandHandler<DecentColor> {

    @Override
    public String getKeyword() {
        return "CHROMA";
    }

    @Override
    public AttributeValue<DecentColor> parse(String[] args) {
        if (args.length == 0) {
            return new ChromaValue();
        }
        int period = parseInt(args[0], "Period", 1, 200);
        if (args.length == 1) {
            return new ChromaValue(period);
        }
        int alpha = parseInt(args[1], "Alpha", 0, 255);
        if (args.length == 2) {
            return new ChromaValue(period, alpha);
        }
        int saturation = parseInt(args[2], "Saturation", 0, 100);
        if (args.length == 3) {
            return new ChromaValue(period, alpha, saturation);
        }
        int value = parseInt(args[3], "Value", 0, 100);
        return new ChromaValue(period, alpha, saturation, value);
    }

    private int parseInt(String value, String name, int min, int max) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < min || parsed > max) {
                throw new AttributeParseException(name + " must be between " + min + " and " + max + ".");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new AttributeParseException(name + " must be a number.");
        }
    }

    @Override
    public List<String> getHints(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("10", "20", "30");
        } else if (args.length == 2) {
            return Arrays.asList("0", "64", "128", "192", "255");
        } else if (args.length == 3) {
            return Arrays.asList("0", "25", "50", "75", "100");
        } else if (args.length == 4) {
            return Arrays.asList("0", "25", "50", "75", "100");
        }
        return Collections.emptyList();
    }
}

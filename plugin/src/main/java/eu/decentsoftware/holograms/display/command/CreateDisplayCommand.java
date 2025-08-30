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

package eu.decentsoftware.holograms.display.command;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.items.DecentMaterial;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DecentLocation;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.ItemDisplay;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.plugin.Validator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommandInfo(
        usage = "/dh d create <name> <type> [content]",
        description = "Create a new display",
        permissions = {"dh.command.displays.create"},
        aliases = {"new", "c"},
        playerOnly = true
)
class CreateDisplayCommand extends DecentCommand {

    private final DisplayService displayService;

    CreateDisplayCommand(DisplayService displayService) {
        super("create");
        this.displayService = displayService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(2, args);

            String name = args[0];
            if (!name.matches(Common.NAME_REGEX)) {
                Lang.DISPLAY_INVALID_NAME.send(sender, name);
                return true;
            }
            if (displayService.getDisplay(name) != null) {
                Lang.DISPLAY_ALREADY_EXISTS.send(sender, name);
                return true;
            }
            DisplayType type = DisplayType.fromString(args[1]);
            if (type == null) {
                Lang.DISPLAY_INVALID_TYPE.send(sender, args[1], String.join(", ", Stream.of(DisplayType.values()).map(Enum::name).toArray(String[]::new)));
                return true;
            }

            Location location = ((Player) sender).getLocation();
            DisplayBase<?> display = createDisplay(type, name, args, DecentLocation.fromBukkitLocation(location));
            displayService.saveDisplay(display);

            Lang.DISPLAY_CREATED.send(sender, name);
            return true;
        };
    }

    private DisplayBase<?> createDisplay(DisplayType type, String name, String[] args, DecentLocation location) {
        switch (type) {
            case TEXT:
                String text = Validator.getLineContent(args, 2);
                TextDisplay textDisplay = new TextDisplay(name, location);
                textDisplay.setLines(Arrays.asList(text.split("\\\\n")));
                return textDisplay;
            case ITEM:
                String itemContent = Validator.getLineContent(args, 2);
                HologramItem item = new HologramItem(itemContent);
                ItemDisplay itemDisplay = new ItemDisplay(name, location);
                itemDisplay.setDisplayedItem(item);
                return itemDisplay;
            case BLOCK:
                Material material = DecentMaterial.parseMaterial(args.length > 2 ? args[2] : "STONE");
                BlockDisplay blockDisplay = new BlockDisplay(name, location);
                blockDisplay.setMaterial(material);
                return blockDisplay;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 2) {
                return Stream.of(DisplayType.values())
                        .map(Enum::name)
                        .filter(s -> s.toUpperCase().startsWith(args[1].toUpperCase()))
                        .collect(Collectors.toList());
            }
            return null;
        };
    }
}

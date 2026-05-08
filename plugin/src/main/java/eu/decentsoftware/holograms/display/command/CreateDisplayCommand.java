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
import eu.decentsoftware.holograms.api.commands.DecentCommandException;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.display.DisplaySettings;
import eu.decentsoftware.holograms.display.ItemDisplay;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.display.attribute.defaults.AttributeDefaultService;
import eu.decentsoftware.holograms.platform.api.capability.PlatformMaterialService;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.plugin.Validator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommandInfo(
        usage = "/dh d create <type> <name> [content]",
        description = "Create a new display",
        permissions = {"dh.command.displays.create"},
        aliases = {"new", "c"},
        playerOnly = true,
        minArgs = 2
)
class CreateDisplayCommand extends DecentCommand {

    private final DisplayService displayService;
    private final AttributeDefaultService attributeDefaultService;
    private final PlatformMaterialService materialService;

    CreateDisplayCommand(DisplayService displayService,
                         AttributeDefaultService attributeDefaultService,
                         PlatformMaterialService materialService) {
        super("create");
        this.displayService = displayService;
        this.attributeDefaultService = attributeDefaultService;
        this.materialService = materialService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(2, args);

            DisplayType type = DisplayType.fromString(args[0]);
            if (type == null) {
                Lang.DISPLAY_INVALID_TYPE.send(sender, args[0], String.join(", ", getDisplayTypeNames()));
                return true;
            }
            String name = args[1];
            if (!name.matches(Common.NAME_REGEX)) {
                Lang.DISPLAY_INVALID_NAME.send(sender, name);
                return true;
            }
            if (displayService.getDisplay(name) != null) {
                Lang.DISPLAY_ALREADY_EXISTS.send(sender, name);
                return true;
            }

            Location location = ((Player) sender).getLocation();
            DisplayBase display = createDisplay(type, name, args, fromBukkitLocation(location));
            attributeDefaultService.applyDefaultValues(display);
            displayService.saveDisplay(display);

            Lang.DISPLAY_CREATED.send(sender, name);
            return true;
        };
    }

    private DecentLocation fromBukkitLocation(Location location) {
        return new DecentLocation(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    private DisplayBase createDisplay(DisplayType type, String name, String[] args, DecentLocation location) {
        String materialNamespacedKey;
        switch (type) {
            case TEXT:
                String text = Validator.getLineContent(args, 2);
                TextDisplay textDisplay = new TextDisplay(name, location, new DisplaySettings());
                textDisplay.setLines(Arrays.asList(text.split("\\\\n")));
                return textDisplay;
            case ITEM:
                materialNamespacedKey = materialService.toMojangNamespacedKey(args[2]);
                if (materialNamespacedKey == null || !materialService.isItem(materialNamespacedKey)) {
                    throw new DecentCommandException(Lang.DISPLAY_INVALID_ITEM_TYPE.getValue(), materialNamespacedKey);
                }

                ItemDisplay itemDisplay = new ItemDisplay(name, location, new DisplaySettings());
                itemDisplay.setMaterial(materialNamespacedKey);
                return itemDisplay;
            case BLOCK:
                materialNamespacedKey = materialService.toMojangNamespacedKey(args[2]);
                if (materialNamespacedKey == null || !materialService.isBlock(materialNamespacedKey)) {
                    throw new DecentCommandException(Lang.DISPLAY_INVALID_BLOCK_TYPE.getValue(), materialNamespacedKey);
                }
                BlockDisplay blockDisplay = new BlockDisplay(name, location, new DisplaySettings());
                blockDisplay.setMaterial(materialNamespacedKey);
                return blockDisplay;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 1) {
                List<String> displayTypeNames = getDisplayTypeNames();
                return TabCompleteHandler.getPartialMatches(args[0], displayTypeNames);
            } else if (args.length == 2) {
                return TabCompleteHandler.getPartialMatches(args[1], "<name>");
            } else if (args.length == 3) {
                DisplayType type = DisplayType.fromString(args[0]);
                if (type == DisplayType.ITEM) {
                    return TabCompleteHandler.getPartialMatches(args[2], materialService.getItemMaterialNames());
                } else if (type == DisplayType.BLOCK) {
                    return TabCompleteHandler.getPartialMatches(args[2], materialService.getBlockMaterialNames());
                } else if (type == DisplayType.TEXT) {
                    return TabCompleteHandler.getPartialMatches(args[2], "Hello World!");
                }
            }
            return null;
        };
    }

    private List<String> getDisplayTypeNames() {
        return Stream.of(DisplayType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}

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

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.utils.message.Message;
import eu.decentsoftware.holograms.location.DecentLocation;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayService;
import eu.decentsoftware.holograms.plugin.Validator;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@CommandInfo(
        usage = "/dh d nearby <range> [page]",
        description = "List nearby displays.",
        permissions = "dh.command.displays.nearby",
        playerOnly = true,
        aliases = {"near"}
)
class NearbyDisplaysCommand extends DecentCommand {

    private final DisplayService displayService;

    NearbyDisplaysCommand(DisplayService displayService) {
        super("nearby");
        this.displayService = displayService;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Validator.validateArgsCount(1, args);
            Player player = (Player) sender;
            DecentLocation location = DecentLocation.fromBukkitLocation(player.getLocation());
            double range = Validator.getDouble(args[0], "Range must be a valid number.");
            List<DisplayBase> displays = new ArrayList<>(displayService.getRegisteredDisplaysInRadius(location, range));
            if (displays.isEmpty()) {
                Lang.DISPLAY_NEARBY_NO_DISPLAYS.send(sender);
                return true;
            }
            int totalPages = (int) Math.ceil((double) displays.size() / 15);
            int currentPage = args.length >= 2 ? Validator.getInteger(args[1], 1, totalPages, "Page must be a valid integer.") - 1 : 0;
            List<String> header = Lists.newArrayList("", " &3&lNEARBY DISPLAYS - #" + (currentPage + 1), " &fList of nearby displays.", "");
            Function<DisplayBase, String> parseItem = display -> {
                DecentLocation l = display.getLocation();
                return String.format(" &8• &b%s &8| &7%s, %.2f, %.2f, %.2f", display.getName(), l.getWorldName(), l.getX(), l.getY(), l.getZ());
            };
            Message.sendPaginatedMessage((Player) sender, currentPage, "/dh d nearby " + range + " %d", 15, header, null, displays, parseItem);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length != 2) {
                return null;
            }

            int displayCount = displayService.getRegisteredDisplays().size();
            if (displayCount == 0) {
                return null;
            }

            List<String> pages = new ArrayList<>();
            int page = 0;
            while (displayCount > 0) {
                page++;
                pages.add(String.valueOf(page));
                displayCount -= 15;
            }

            return TabCompleteHandler.getPartialMatches(args[0], pages);
        };
    }

}

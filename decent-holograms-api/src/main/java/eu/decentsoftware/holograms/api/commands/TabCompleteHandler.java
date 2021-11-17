package eu.decentsoftware.holograms.api.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.ActionType;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FunctionalInterface
public interface TabCompleteHandler {

	DecentHolograms PLUGIN = DecentHologramsAPI.get();

	TabCompleteHandler HOLOGRAM_NAMES = (sender, args) -> {
		if (args.length == 1) {
			List<String> matches = Lists.newArrayList();
			StringUtil.copyPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames(), matches);
			return matches;
		}
		return null;
	};

	TabCompleteHandler HOLOGRAM_PAGES = (sender, args) -> {
		List<String> matches = Lists.newArrayList();
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames(), matches);
		} else if (args.length == 2) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				StringUtil.copyPartialMatches(args[1], IntStream
						.rangeClosed(1, hologram.size())
						.boxed().map(String::valueOf)
						.collect(Collectors.toList()), matches);
			}
		}
		return matches;
	};

	TabCompleteHandler HOLOGRAM_PAGES_CLICK_TYPES = (sender, args) -> {
		List<String> matches = Lists.newArrayList();
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames(), matches);
		} else if (args.length == 2) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				StringUtil.copyPartialMatches(args[1], IntStream
						.rangeClosed(1, hologram.size())
						.boxed().map(String::valueOf)
						.collect(Collectors.toList()), matches);
			}
		} else if (args.length == 3) {
			StringUtil.copyPartialMatches(args[2], Arrays.stream(ClickType.values()).map(Enum::name).collect(Collectors.toList()), matches);
		}
		return matches;
	};

	TabCompleteHandler HOLOGRAM_PAGES_ACTIONS = (sender, args) -> {
		List<String> matches = Lists.newArrayList();
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames(), matches);
		} else if (args.length == 2) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				StringUtil.copyPartialMatches(args[1], IntStream
						.rangeClosed(1, hologram.size())
						.boxed().map(String::valueOf)
						.collect(Collectors.toList()), matches);
			}
		} else if (args.length == 3) {
			StringUtil.copyPartialMatches(args[2], Arrays.stream(ClickType.values()).map(ClickType::name).collect(Collectors.toList()), matches);
		} else if (args.length == 4) {
			StringUtil.copyPartialMatches(args[3], ActionType.getActionTypes().stream().map(ActionType::getName).collect(Collectors.toList()), matches);
		}
		return matches;
	};

	TabCompleteHandler HOLOGRAM_PAGES_ACTION_INDEXES = (sender, args) -> {
		List<String> matches = Lists.newArrayList();
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames(), matches);
		} else if (args.length == 2) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				StringUtil.copyPartialMatches(args[1], IntStream
						.rangeClosed(1, hologram.size())
						.boxed().map(String::valueOf)
						.collect(Collectors.toList()), matches);
			}
		} else if (args.length == 3) {
			StringUtil.copyPartialMatches(args[2], Arrays.stream(ClickType.values()).map(ClickType::name).collect(Collectors.toList()), matches);
		} else if (args.length == 4) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				HologramPage page = hologram.getPage(CommandValidator.getInteger(args[1]) - 1);
				if (page != null) {
					ClickType clickType = ClickType.fromString(args[2]);
					if (clickType != null) {
						StringUtil.copyPartialMatches(args[3], IntStream
								.rangeClosed(1, page.getActions(clickType).size())
								.boxed().map(String::valueOf)
								.collect(Collectors.toList()), matches);
					}
				}
			}
		}
		return matches;
	};

	/**
	 * Handle Tab Complete.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return List of Tab Completed strings.
	 */
	List<String> handleTabComplete(CommandSender sender, String[] args);

}

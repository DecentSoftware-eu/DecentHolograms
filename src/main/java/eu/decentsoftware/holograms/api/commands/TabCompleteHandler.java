package eu.decentsoftware.holograms.api.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.ActionType;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FunctionalInterface
public interface TabCompleteHandler {

	DecentHolograms PLUGIN = DecentHologramsAPI.get();

	TabCompleteHandler HOLOGRAM_NAMES = (sender, args) -> {
		if (args.length == 1) {
			return getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
		}
		return null;
	};

	TabCompleteHandler HOLOGRAM_PAGES = (sender, args) -> {
		if (args.length == 1) {
			return getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
		} else if (args.length == 2) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				return getPartialMatches(args[1], IntStream
					.rangeClosed(1, hologram.size())
					.boxed().map(String::valueOf)
					.collect(Collectors.toList()));
			}
		}
		return null;
	};

	TabCompleteHandler HOLOGRAM_PAGES_CLICK_TYPES = (sender, args) -> {
		if (args.length == 1) {
			return getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
		} else if (args.length == 2) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				return getPartialMatches(args[1], IntStream
					.rangeClosed(1, hologram.size())
					.boxed().map(String::valueOf)
					.collect(Collectors.toList()));
			}
		} else if (args.length == 3) {
			return getPartialMatches(args[2], Arrays.stream(ClickType.values())
				.map(ClickType::name)
				.collect(Collectors.toList()));
		}
		return null;
	};

	TabCompleteHandler HOLOGRAM_PAGES_ACTIONS = (sender, args) -> {
		if (args.length == 1) {
			return getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
		} else if (args.length == 2) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				return getPartialMatches(args[1], IntStream
					.rangeClosed(1, hologram.size())
					.boxed().map(String::valueOf)
					.collect(Collectors.toList()));
			}
		} else if (args.length == 3) {
			return getPartialMatches(args[2], Arrays.stream(ClickType.values())
				.map(ClickType::name)
				.collect(Collectors.toList()));
		} else if (args.length == 4) {
			return getPartialMatches(args[2], ActionType.getActionTypes().stream()
				.map(ActionType::getName)
				.collect(Collectors.toList()));
		}
		return null;
	};

	TabCompleteHandler HOLOGRAM_PAGES_ACTION_INDEXES = (sender, args) -> {
		List<String> matches = Lists.newArrayList();
		if (args.length == 1) {
			return getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
		} else if (args.length == 2) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				return getPartialMatches(args[1], IntStream
					.rangeClosed(1, hologram.size())
					.boxed().map(String::valueOf)
					.collect(Collectors.toList()));
			}
		} else if (args.length == 3) {
			return getPartialMatches(args[2], Arrays.stream(ClickType.values())
				.map(ClickType::name)
				.collect(Collectors.toList()));
		} else if (args.length == 4) {
			Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
			if (hologram != null) {
				HologramPage page = hologram.getPage(CommandValidator.getInteger(args[1]) - 1);
				if (page != null) {
					ClickType clickType = ClickType.fromString(args[2]);
					if (clickType != null) {
						return getPartialMatches(args[3], IntStream
							.rangeClosed(1, page.getActions(clickType).size())
							.boxed().map(String::valueOf)
							.collect(Collectors.toList()));
					}
				}
			}
		}
		return matches;
	};
	
	static List<String> getPartialMatches(String token, String... originals) {
		return getPartialMatches(token, Arrays.asList(originals));
	}
	
	static List<String> getPartialMatches(String token, Collection<String> originals) {
		if (originals == null) {
			return Collections.emptyList();
		}
		
		if (token == null || token.isEmpty()) {
			return new ArrayList<>(originals);
		}
		
		List<String> matches = new ArrayList<>();
		for (String str : originals) {
			if (str.length() >= token.length() && str.regionMatches(true, 0, token, 0, token.length())) {
				matches.add(str);
			}
		}
		
		return matches;
	}

	/**
	 * Handle Tab Complete.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return List of Tab Completed strings.
	 */
	List<String> handleTabComplete(CommandSender sender, String[] args);

}

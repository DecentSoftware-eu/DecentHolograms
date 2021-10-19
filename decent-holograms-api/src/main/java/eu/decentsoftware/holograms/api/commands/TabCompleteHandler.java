package eu.decentsoftware.holograms.api.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.utils.items.DecentMaterial;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FunctionalInterface
public interface TabCompleteHandler {

	DecentHolograms PLUGIN = DecentHologramsProvider.getDecentHolograms();

	TabCompleteHandler HOLOGRAM_NAMES = (sender, args) -> {
		if (args.length == 1) {
			List<String> matches = Lists.newArrayList();
			StringUtil.copyPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames(), matches);
			return matches;
		}
		return null;
	};

	TabCompleteHandler HOLOGRAM_LINES = (sender, args) -> {
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

	TabCompleteHandler HOLOGRAM_LINES_CONTENT = (sender, args) -> {
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
		} else if (args.length == 4 && (args[2].startsWith("#ICON:") || args[2].startsWith("#HEAD:") || args[2].startsWith("#SMALLHEAD:"))) {
			StringUtil.copyPartialMatches(
					args[3],
					Arrays.stream(Material.values())
							.filter(DecentMaterial::isItem)
							.map(Material::name)
							.collect(Collectors.toList()),
					matches
			);
		} else if (args.length == 4 && args[2].startsWith("#ENTITY:")) {
			StringUtil.copyPartialMatches(args[3], DecentEntityType.getAllowedEntityTypeNames(), matches);
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

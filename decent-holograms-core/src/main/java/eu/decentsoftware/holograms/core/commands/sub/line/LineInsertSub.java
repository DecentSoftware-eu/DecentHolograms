package eu.decentsoftware.holograms.core.commands.sub.line;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import eu.decentsoftware.holograms.core.holograms.DefaultHologramLine;
import eu.decentsoftware.holograms.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.utils.items.DecentMaterial;
import org.bukkit.Material;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LineInsertSub extends DecentCommand {

	public LineInsertSub() {
		super("insert", "dh.line.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh line insert <hologram> <line> [content]";
	}

	@Override
	public String getDescription() {
		return "Insert a line into Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int index = EditValidator.getInteger(args[1], "Line index must be an integer.");
			final String content = EditValidator.getLineContent(args, 2);
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			HologramLine prevLine = EditValidator.getHologramLine(hologram, index);
			HologramLine line = new DefaultHologramLine(prevLine.getLocation(), content);

			if (hologram.insertLine(index - 1, line)) {
				hologram.save();
				Lang.LINE_INSERTED.send(sender);
			} else {
				Lang.LINE_INSERT_FAILED.send(sender);
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return (sender, args) -> {
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
	}

}

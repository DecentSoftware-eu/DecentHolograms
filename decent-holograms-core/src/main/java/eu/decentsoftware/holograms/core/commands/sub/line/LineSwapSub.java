package eu.decentsoftware.holograms.core.commands.sub.line;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.util.StringUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LineSwapSub extends DecentCommand {

	public LineSwapSub() {
		super("swap", "dh.line.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 3;
	}

	@Override
	public String getUsage() {
		return "/dh line swap <hologram> <line1> <line2>";
	}

	@Override
	public String getDescription() {
		return "Swap two lines in a Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int index1 = EditValidator.getInteger(args[1], "Line index must be a valid integer.");
			final int index2 = EditValidator.getInteger(args[2], "Line index must be a valid integer.");
			if (index1 == index2) {
				Lang.LINE_SWAP_SELF.send(sender);
				return true;
			}
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			if (hologram.swapLines(index1 - 1, index2 - 1)) {
				hologram.save();
				Lang.LINE_SWAPPED.send(sender);
			} else {
				Lang.LINE_SWAP_FAILED.send(sender);
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
			} else if (args.length == 3) {
				Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
				if (hologram != null) {
					StringUtil.copyPartialMatches(args[2], IntStream
							.rangeClosed(1, hologram.size())
							.boxed().map(String::valueOf)
							.collect(Collectors.toList()), matches);
				}
			}
			return matches;
		};
	}

}

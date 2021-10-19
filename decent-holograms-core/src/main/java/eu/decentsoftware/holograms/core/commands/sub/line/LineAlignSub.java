package eu.decentsoftware.holograms.core.commands.sub.line;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LineAlignSub extends DecentCommand {

	public LineAlignSub() {
		super("align", "dh.line.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 4;
	}

	@Override
	public String getUsage() {
		return "/dh line align <hologram> <X|Z|XZ> <line1> <line2>";
	}

	@Override
	public String getDescription() {
		return "Align two lines in hologram on a specified axis.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			final String axis = args[1];
			final int index1 = EditValidator.getInteger(args[2], "Line index must be an integer!");
			final int index2 = EditValidator.getInteger(args[3], "Line index must be an integer!");
			if (index1 == index2) {
				Lang.LINE_ALIGN_SELF.send(sender);
				return true;
			}
			HologramLine line1 = EditValidator.getHologramLine(hologram, index1);
			HologramLine line2 = EditValidator.getHologramLine(hologram, index2);
			switch (axis) {
				case "X":
					line1.setOffsetX(line2.getOffsetX());
					break;
				case "Z":
					line1.setOffsetZ(line2.getOffsetZ());
					break;
				case "XZ":
				case "ZX":
					line1.setOffsetX(line2.getOffsetX());
					line1.setOffsetZ(line2.getOffsetZ());
					break;
				default:
					Lang.LINE_ALIGN_AXIS.send(sender);
					return true;
			}
			hologram.realignLines();
			hologram.save();
			Lang.LINE_ALIGNED.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return (sender, args) -> {
			if (args.length == 1) {
				return TabCompleteHandler.HOLOGRAM_NAMES.handleTabComplete(sender, args);
			} else if (args.length == 2) {
				return StringUtil.copyPartialMatches(args[1], Arrays.asList("X", "Z", "XZ"), new ArrayList<>());
			} else if (args.length == 3) {
				Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
				if (hologram != null) {
					return StringUtil.copyPartialMatches(args[2], IntStream
							.rangeClosed(1, hologram.size())
							.boxed().map(String::valueOf)
							.collect(Collectors.toList()), new ArrayList<>());
				}
			} else if (args.length == 4) {
				Hologram hologram = PLUGIN.getHologramManager().getHologram(args[0]);
				if (hologram != null) {
					return StringUtil.copyPartialMatches(args[3], IntStream
							.rangeClosed(1, hologram.size())
							.boxed().map(String::valueOf)
							.collect(Collectors.toList()), new ArrayList<>());
				}
			}
			return new ArrayList<>();
		};
	}

}

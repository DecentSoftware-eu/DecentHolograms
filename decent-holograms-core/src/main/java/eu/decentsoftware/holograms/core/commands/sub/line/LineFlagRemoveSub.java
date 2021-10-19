package eu.decentsoftware.holograms.core.commands.sub.line;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.objects.enums.EnumFlag;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LineFlagRemoveSub extends DecentCommand {

	public LineFlagRemoveSub() {
		super("removeflag", "dh.line.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 3;
	}

	@Override
	public String getUsage() {
		return "/dh line removeflag <hologram> <line> <flag>";
	}

	@Override
	public String getDescription() {
		return "Remove a flag from line.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int index = EditValidator.getInteger(args[1], "Line index must be a valid number.");
			final EnumFlag flag = EditValidator.getFlag(args[2], String.format("Flag \"%s\" wasn't found.", args[2]));
			final Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			final HologramLine line = EditValidator.getHologramLine(hologram, index);
			if (line != null) {
				line.removeFlags(flag);
				hologram.save();
				Lang.LINE_FLAG_REMOVED.send(sender, flag.name());
			} else {
				Lang.LINE_DOES_NOT_EXIST.send(sender);
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
				StringUtil.copyPartialMatches(args[2], Arrays.stream(EnumFlag.values()).map(Enum::name).collect(Collectors.toList()), matches);
			}
			return matches;
		};
	}

}

package eu.decentsoftware.holograms.core.commands.sub.hologram;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.objects.enums.EnumFlag;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HologramFlagAddSub extends DecentCommand {

	public HologramFlagAddSub() {
		super("addflag", "dh.hologram.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh hologram addflag <hologram> <flag>";
	}

	@Override
	public String getDescription() {
		return "Add a flag to Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final EnumFlag flag = EditValidator.getFlag(args[1], String.format("Flag \"%s\" wasn't found.", args[1]));
			final Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			hologram.addFlags(flag);
			hologram.save();

			Lang.HOLOGRAM_FLAG_ADDED.send(sender, flag.name());
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
				StringUtil.copyPartialMatches(args[1],
						Arrays.stream(EnumFlag.values())
								.map(EnumFlag::name)
								.collect(Collectors.toList()), matches
				);
			}
			return matches;
		};
	}

}

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

public class HologramFlagRemoveSub extends DecentCommand {

	public HologramFlagRemoveSub() {
		super("removeflag", "dh.hologram.admin", false, "remflag");
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh hologram removeflag <hologram> <flag>";
	}

	@Override
	public String getDescription() {
		return "Remove a flag from Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final EnumFlag flag = EditValidator.getFlag(args[1], String.format("Flag \"%s\" wasn't found.", args[1]));
			final Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			hologram.removeFlags(flag);
			hologram.save();

			Lang.HOLOGRAM_FLAG_REMOVED.send(sender, flag.name());
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

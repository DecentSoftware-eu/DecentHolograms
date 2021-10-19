package eu.decentsoftware.holograms.core.commands.sub.hologram;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.objects.enums.EnumOrigin;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.util.StringUtil;

import java.util.List;

public class HologramOriginSub extends DecentCommand {

	public HologramOriginSub() {
		super("origin", "dh.hologram.admin", false, "setorigin");
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh hologram origin <hologram> <UP|DOWN>";
	}

	@Override
	public String getDescription() {
		return "Set origin of the hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			EnumOrigin origin;
			try {
				origin = EnumOrigin.valueOf(args[1]);
			} catch (Throwable throwable) {
				Lang.HOLOGRAM_ORIGIN_DOES_NOT_EXIST.send(sender);
				return true;
			}
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			hologram.setOrigin(origin);
			hologram.realignLines();
			hologram.save();
			Lang.HOLOGRAM_ORIGIN_SET.send(sender, origin.name());
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return (sender, args) -> {
			List<String> matches = Lists.newArrayList();
			if (args.length == 1 || args.length == 3) {
				StringUtil.copyPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames(), matches);
			} else if (args.length == 2) {
				StringUtil.copyPartialMatches(args[1], Lists.newArrayList("UP", "DOWN"), matches);
			}
			return matches;
		};
	}

}

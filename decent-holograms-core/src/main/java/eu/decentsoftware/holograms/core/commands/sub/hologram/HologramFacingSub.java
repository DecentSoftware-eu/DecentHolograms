package eu.decentsoftware.holograms.core.commands.sub.hologram;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.util.StringUtil;

import java.util.List;

public class HologramFacingSub extends DecentCommand {

	public HologramFacingSub() {
		super("setfacing", "dh.hologram.admin", false, "facing", "setface", "face");
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh hologram setfacing <hologram> <facing>";
	}

	@Override
	public String getDescription() {
		return "Set facing direction of a hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			float facing;
			switch (args[1].toUpperCase()) {
				case "SOUTH": facing = 0.0f; break;
				case "WEST": facing = 90.0f; break;
				case "NORTH": facing = 180.0f; break;
				case "EAST": facing = -90.0f; break;
				default:
					facing = EditValidator.getFloat(args[1], -180.0f, 180.0f, "Facing must be a valid number between -180 and 180.");
					break;
			}
			hologram.setFacing(facing);
			hologram.save();
			hologram.realignLines();
			Lang.HOLOGRAM_FACING_SET.send(sender, facing);
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
				StringUtil.copyPartialMatches(args[1], Lists.newArrayList(
						"SOUTH", "WEST", "NORTH", "EAST", "0", "45", "90", "135", "180", "-45", "-90", "-135"
				), matches);
			}
			return matches;
		};
	}

}

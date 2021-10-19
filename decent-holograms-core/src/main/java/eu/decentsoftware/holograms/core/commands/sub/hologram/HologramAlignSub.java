package eu.decentsoftware.holograms.core.commands.sub.hologram;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.Location;
import org.bukkit.util.StringUtil;

import java.util.List;

public class HologramAlignSub extends DecentCommand {

	public HologramAlignSub() {
		super("align", "dh.hologram.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 3;
	}

	@Override
	public String getUsage() {
		return "/dh hologram align <hologram> <X|Y|Z|XZ> <otherHologram>";
	}

	@Override
	public String getDescription() {
		return "Align hologram with other hologram on a specified axis.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			if (args[0].equals(args[2])) {
				Lang.HOLOGRAM_ALIGN_SELF.send(sender);
				return true;
			}

			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			Hologram otherHologram = EditValidator.getHologram(args[2], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			Location location = hologram.getLocation();
			Location otherLocation = otherHologram.getLocation();
			switch (args[1].toUpperCase()) {
				case "X":
					location.setX(otherLocation.getX());
					break;
				case "Y":
					location.setY(otherLocation.getY());
					break;
				case "Z":
					location.setZ(otherLocation.getZ());
					break;
				case "XZ":
				case "ZX":
					location.setX(otherLocation.getX());
					location.setZ(otherLocation.getZ());
					break;
				case "FACE":
				case "FACING":
					hologram.setFacing(otherHologram.getFacing());
					break;
				default:
					Lang.HOLOGRAM_ALIGN_AXIS.send(sender);
					return true;
			}
			hologram.setLocation(location);
			hologram.realignLines();
			hologram.save();
			Lang.HOLOGRAM_ALIGNED.send(sender);
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
				StringUtil.copyPartialMatches(args[1], Lists.newArrayList("X", "Y", "Z", "XZ", "FACE", "FACING"), matches);
			}
			return matches;
		};
	}

}

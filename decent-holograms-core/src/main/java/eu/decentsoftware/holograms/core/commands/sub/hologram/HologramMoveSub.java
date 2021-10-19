package eu.decentsoftware.holograms.core.commands.sub.hologram;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class HologramMoveSub extends DecentCommand {

	public HologramMoveSub() {
		super("move", "dh.hologram.admin", false, "mv");
	}

	@Override
	public int getMinArgs() {
		return 4;
	}

	@Override
	public String getUsage() {
		return "/dh hologram move <hologram> <x> <y> <z>";
	}

	@Override
	public String getDescription() {
		return "Move Hologram to a Location.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			Location location = hologram.getLocation();
			double x = EditValidator.getDouble(args[1], "The 'x' coordinate must be a valid number.");
			double y = EditValidator.getDouble(args[2], "The 'y' coordinate must be a valid number.");
			double z = EditValidator.getDouble(args[3], "The 'z' coordinate must be a valid number.");
			location.setX(x);
			location.setY(y);
			location.setZ(z);
			hologram.setLocation(location);
			hologram.realignLines();
			hologram.save();

			Lang.HOLOGRAM_MOVED.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return (sender, args) -> {
			Hologram hologram;
			Location location;
			if (args.length == 1) {
				List<String> matches = Lists.newArrayList();
				StringUtil.copyPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames(), matches);
				return matches;
			} else if (args.length == 2 && EditValidator.isPlayer(sender)) {
				hologram = PLUGIN.getHologramManager().getHologram(args[0]);
				location = hologram.getLocation();
				if (hologram != null) {
					return Collections.singletonList(String.valueOf(location.getX()));
				}
				return Lists.newArrayList(String.valueOf(((Player) sender).getLocation().getX()));
			} else if (args.length == 3 && EditValidator.isPlayer(sender)) {
				hologram = PLUGIN.getHologramManager().getHologram(args[0]);
				location = hologram.getLocation();
				if (hologram != null) {
					return Collections.singletonList(String.valueOf(location.getY()));
				}
				return Lists.newArrayList(String.valueOf(((Player) sender).getLocation().getY()));
			} else if (args.length == 4 && EditValidator.isPlayer(sender)) {
				hologram = PLUGIN.getHologramManager().getHologram(args[0]);
				location = hologram.getLocation();
				if (hologram != null) {
					return Collections.singletonList(String.valueOf(location.getZ()));
				}
				return Lists.newArrayList(String.valueOf(((Player) sender).getLocation().getZ()));
			}
			return null;
		};
	}

}

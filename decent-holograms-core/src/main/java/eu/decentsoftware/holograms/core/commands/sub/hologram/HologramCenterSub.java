package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.Location;

public class HologramCenterSub extends DecentCommand {

	public HologramCenterSub() {
		super("center", "dh.hologram.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh hologram center <hologram>";
	}

	@Override
	public String getDescription() {
		return "Move a Hologram into the center of a block.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			Location location = hologram.getLocation();

			int x = (int) location.getX();
			int z = (int) location.getZ();
			location.setX(x > location.getX() ? x - 0.5d : x + 0.5d);
			location.setZ(z > location.getZ() ? z - 0.5d : z + 0.5d);

			hologram.setLocation(location);
			hologram.realignLines();
			hologram.save();

			Lang.HOLOGRAM_MOVED.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

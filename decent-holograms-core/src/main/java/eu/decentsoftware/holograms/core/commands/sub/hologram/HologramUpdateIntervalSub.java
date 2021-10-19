package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class HologramUpdateIntervalSub extends DecentCommand {

	public HologramUpdateIntervalSub() {
		super("setupdateinterval", "dh.hologram.admin", false, "updateinterval");
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh hologram setupdateinterval <hologram> <range>";
	}

	@Override
	public String getDescription() {
		return "Set update interval of a hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int interval = EditValidator.getInteger(args[1], 1, 1200, "Interval must be a valid number between 1 and 1200.");
			final Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			hologram.setUpdateInterval(interval);
			hologram.save();
			Lang.HOLOGRAM_UPDATE_INTERVAL_SET.send(sender, interval);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

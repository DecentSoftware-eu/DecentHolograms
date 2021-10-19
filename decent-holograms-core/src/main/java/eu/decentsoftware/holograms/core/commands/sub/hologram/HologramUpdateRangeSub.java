package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class HologramUpdateRangeSub extends DecentCommand {

	public HologramUpdateRangeSub() {
		super("setupdaterange", "dh.hologram.admin", false, "updaterange");
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh hologram setupdaterange <hologram> <range>";
	}

	@Override
	public String getDescription() {
		return "Set update range of a hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int range = EditValidator.getInteger(args[1], 1, 64, "Range must be a valid number between 1 and 64.");
			final Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			hologram.setUpdateRange(range);
			hologram.save();
			Lang.HOLOGRAM_UPDATE_RANGE_SET.send(sender, range);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

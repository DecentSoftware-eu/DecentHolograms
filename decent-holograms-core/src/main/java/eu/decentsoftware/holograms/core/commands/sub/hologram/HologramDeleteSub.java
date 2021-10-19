package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class HologramDeleteSub extends DecentCommand {

	public HologramDeleteSub() {
		super("delete", "dh.hologram.admin", false, "del", "remove", "rem");
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh hologram delete <hologram>";
	}

	@Override
	public String getDescription() {
		return "Delete a Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			hologram.delete();

			PLUGIN.getHologramManager().removeHologram(args[0]);

			Lang.HOLOGRAM_DELETED.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

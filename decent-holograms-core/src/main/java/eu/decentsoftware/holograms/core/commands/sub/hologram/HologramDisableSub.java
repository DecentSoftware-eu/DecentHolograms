package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class HologramDisableSub extends DecentCommand {

	public HologramDisableSub() {
		super("disable", "dh.hologram.admin", false, "off");
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh h disable <hologram>";
	}

	@Override
	public String getDescription() {
		return "Disable a hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			if (!hologram.isEnabled()) {
				Lang.HOLOGRAM_ALREADY_DISABLED.send(sender);
				return true;
			}
			hologram.disable();

			Lang.HOLOGRAM_DISABLED.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.entity.Player;

public class HologramCloneSub extends DecentCommand {

	public HologramCloneSub() {
		super("clone", "dh.hologram.admin", true, "copy");
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh hologram clone <hologram> <name> [temp]";
	}

	@Override
	public String getDescription() {
		return "Clone an existing Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			boolean temp = false;
			if (args.length >= 3) {
				temp = EditValidator.getBoolean(args[2], "Value of temp must be true or false.");
			}
			Player player = (Player) sender;
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			Hologram clone = hologram.clone(args[1], player.getLocation(), temp);
			clone.show();
			clone.realignLines();
			clone.save();
			PLUGIN.getHologramManager().registerHologram(clone);
			Lang.HOLOGRAM_CLONED.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

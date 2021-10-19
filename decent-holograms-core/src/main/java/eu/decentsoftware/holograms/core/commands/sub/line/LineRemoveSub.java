package eu.decentsoftware.holograms.core.commands.sub.line;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class LineRemoveSub extends DecentCommand {

	public LineRemoveSub() {
		super("remove", "dh.line.admin", false, "rem", "del", "delete");
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh line remove <hologram> <line>";
	}

	@Override
	public String getDescription() {
		return "Remove a line from Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int index = EditValidator.getInteger(args[1], "Line index must be an integer.");
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			hologram.removeLine(index - 1);
			if (hologram.size() == 0) {
				hologram.delete();
				PLUGIN.getHologramManager().removeHologram(args[0]);
				Lang.LINE_REMOVED.send(sender);
				Lang.HOLOGRAM_DELETED.send(sender);
			} else {
				hologram.save();
				Lang.LINE_REMOVED.send(sender);
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_LINES;
	}

}

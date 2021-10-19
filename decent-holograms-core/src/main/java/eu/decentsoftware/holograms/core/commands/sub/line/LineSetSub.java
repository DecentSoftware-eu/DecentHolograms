package eu.decentsoftware.holograms.core.commands.sub.line;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class LineSetSub extends DecentCommand {

	public LineSetSub() {
		super("set", "dh.line.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh line set <hologram> <line> [content]";
	}

	@Override
	public String getDescription() {
		return "Set a line in Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int index = EditValidator.getInteger(args[1], "Line index must be an integer.");
			final String content = EditValidator.getLineContent(args, 2);
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			if (hologram.setLine(index - 1, content)) {
				hologram.save();
				Lang.LINE_SET.send(sender);
			} else {
				Lang.LINE_DOES_NOT_EXIST.send(sender, index);
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_LINES_CONTENT;
	}

}

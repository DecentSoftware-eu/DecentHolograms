package eu.decentsoftware.holograms.core.commands.sub.line;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class LineOffsetXSub extends DecentCommand {

	public LineOffsetXSub() {
		super("offsetx", "dh.line.admin", false, "xoffset", "offx", "xoff");
	}

	@Override
	public int getMinArgs() {
		return 3;
	}

	@Override
	public String getUsage() {
		return "/dh line offsetX <hologram> <line> <offset>";
	}

	@Override
	public String getDescription() {
		return "Set an X offset of a line.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int index = EditValidator.getInteger(args[1], "Line index must be an integer!");
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			HologramLine line = EditValidator.getHologramLine(hologram, index);
			if (line != null) {
				line.setOffsetX(EditValidator.getDouble(args[2], -2.5D, 2.5D,
						String.format("OffsetX must be a valid number in range. (Min: %.2f, Max: %.2f)", -2.5D, 2.5D)
				));
				hologram.realignLines();
				hologram.save();
				Lang.LINE_OFFSETX_SET.send(sender);
			} else {
				Lang.LINE_DOES_NOT_EXIST.send(sender, index);
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_LINES;
	}

}

package eu.decentsoftware.holograms.core.commands.sub.line;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class LineHeightSub extends DecentCommand {

	public LineHeightSub() {
		super("height", "dh.line.admin", false, "setheight");
	}

	@Override
	public int getMinArgs() {
		return 3;
	}

	@Override
	public String getUsage() {
		return "/dh line height <hologram> <line> <heigth>";
	}

	@Override
	public String getDescription() {
		return "Set height of a line.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int index = EditValidator.getInteger(args[1], "Line index must be an integer.");
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			HologramLine line = EditValidator.getHologramLine(hologram, index);
			if (line != null) {
				line.setHeight(EditValidator.getDouble(args[2], 0.0D, 2.5D,
						String.format("Height must be a valid number in range. (Min: %.2f, Max: %.2f)", 0.0D, 2.5D)
				));
				hologram.realignLines();
				hologram.save();

				Lang.LINE_HEIGHT_SET.send(sender);
			} else {
				Lang.LINE_DOES_NOT_EXIST.send(sender);
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_LINES;
	}

}

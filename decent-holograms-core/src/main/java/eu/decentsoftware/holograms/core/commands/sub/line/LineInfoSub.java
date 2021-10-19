package eu.decentsoftware.holograms.core.commands.sub.line;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import eu.decentsoftware.holograms.utils.Common;
import org.bukkit.Location;

public class LineInfoSub extends DecentCommand {

	public LineInfoSub() {
		super("info", "dh.line.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh line info <hologram> <line>";
	}

	@Override
	public String getDescription() {
		return "Show info about line.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			final int index = EditValidator.getInteger(args[1], "Line index must be an integer.");
			final HologramLine line = EditValidator.getHologramLine(hologram, index);
			Location loc = line.getLocation();

			sender.sendMessage("");
			Common.tell(sender, " &3&lHOLOGRAM LINE INFO");
			Common.tell(sender, " General information about a line");
			sender.sendMessage("");
			Common.tell(sender, " &8• &7Hologram: &b%s", hologram.getName());
			Common.tell(sender, " &8• &7Index: &b%d", index);
			Common.tell(sender, " &8• &7Location: &b%s, %.2f, %.2f, %.2f",
					loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ()
			);
			sender.sendMessage(Common.colorize(" &8• &7Content: &b") + line.getContent());
			Common.tell(sender, " &8• &7Height: &b%f", line.getHeight());
			Common.tell(sender, " &8• &7OffsetX: &b%f", line.getOffsetX());
			Common.tell(sender, " &8• &7OffsetY: &b%f", line.getOffsetY());
			Common.tell(sender, " &8• &7OffsetZ: &b%f", line.getOffsetZ());
			sender.sendMessage("");
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_LINES;
	}

}

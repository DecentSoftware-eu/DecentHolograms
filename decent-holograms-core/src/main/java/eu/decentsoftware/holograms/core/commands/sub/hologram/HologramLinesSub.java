package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.message.Message;
import org.bukkit.entity.Player;

public class HologramLinesSub extends DecentCommand {

	public HologramLinesSub() {
		super("lines", "dh.hologram.admin", false, "line", "l");
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh hologram lines <hologram> [page]";
	}

	@Override
	public String getDescription() {
		return "Lists all lines in a hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());

			sender.sendMessage("");
			Common.tell(sender, " &3&lHOLOGRAM LINES");
			Common.tell(sender, " &fLines in a hologram.");
			sender.sendMessage("");

			final int itemsPerPage = 15;
			final int itemsTotal = hologram.size();
			final int maxPage = itemsTotal % itemsPerPage == 0 ? itemsTotal / itemsPerPage - 1 : itemsTotal / itemsPerPage;
			int currentPage = args.length >= 2 ? EditValidator.getInteger(args[1], "Page must be a valid integer.") - 1 : 0;
			if (currentPage > maxPage) currentPage = maxPage;
			final int startIndex = currentPage * itemsPerPage;
			final int endIndex = Math.min(startIndex + itemsPerPage, itemsTotal);
			final String itemFormat = "   %d. %s";

			for (int i = startIndex; i < endIndex; i++) {
				HologramLine line = hologram.getLine(i);
				if (EditValidator.isPlayer(sender)) {
					String suggest = String.format("/dh l set %s %s %s", args[0], i + 1, line.getContent());
					String message = String.format(itemFormat, i + 1, line.getContent());
					String hoverFormat = Common.colorize(Lang.LINE_EDIT_HOVER.getValue().replace("{prefix}", Common.PREFIX));
					String hover = String.format(hoverFormat, suggest);
					Message.sendHoverSuggest((Player) sender, message, hover, suggest);
				} else {
					sender.sendMessage(String.format(itemFormat, i + 1, line.getContent()));
				}
			}

			sender.sendMessage("");
			if (maxPage > 0) {
				((Player) sender).spigot().sendMessage(Message.getPagesComponents(currentPage, maxPage == currentPage, "/dh h lines " + args[0] + " %d"));
				sender.sendMessage("");
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

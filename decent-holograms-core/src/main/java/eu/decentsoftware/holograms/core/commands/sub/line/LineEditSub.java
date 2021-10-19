package eu.decentsoftware.holograms.core.commands.sub.line;

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

public class LineEditSub extends DecentCommand {

	public LineEditSub() {
		super("edit", "dh.line.admin", true, "e");
	}

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public String getUsage() {
		return "/dh line edit <hologram> <line>";
	}

	@Override
	public String getDescription() {
		return "Edit a line.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int index = EditValidator.getInteger(args[1], "Line index must be an integer!");
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			HologramLine line = EditValidator.getHologramLine(hologram, index);
			String suggest = String.format("/dh l set %s %s %s", args[0], args[1], line.getContent());
			String message = Common.colorize(Lang.LINE_EDIT.getValue().replace("{prefix}", Common.PREFIX));
			String hoverFormat = Common.colorize(Lang.LINE_EDIT_HOVER.getValue().replace("{prefix}", Common.PREFIX));
			String hover = String.format(hoverFormat, suggest);
			sender.sendMessage("");
			Message.sendHoverSuggest((Player) sender, message, hover, suggest);
			sender.sendMessage("");
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_LINES;
	}

}

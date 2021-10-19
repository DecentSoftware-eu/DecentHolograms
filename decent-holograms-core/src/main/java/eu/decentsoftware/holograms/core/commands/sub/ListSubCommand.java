package eu.decentsoftware.holograms.core.commands.sub;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.message.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class ListSubCommand extends DecentCommand {

	public ListSubCommand() {
		super("list", "dh.hologram.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public String getUsage() {
		return "/dh list [page]";
	}

	@Override
	public String getDescription() {
		return "Show list of all Holograms.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final List<Hologram> holograms = Lists.newArrayList(PLUGIN.getHologramManager().getHolograms());
			if (holograms.isEmpty()) {
				Common.tell(sender, "%sThere are currenty no holograms.", Common.PREFIX);
				return true;
			}
			final int itemsPerPage = 15;
			final int itemsTotal = holograms.size();
			final int maxPage = itemsTotal % itemsPerPage == 0 ? itemsTotal / itemsPerPage - 1 : itemsTotal / itemsPerPage;
			int currentPage = args.length >= 1 ? EditValidator.getInteger(args[0], "Page must be a valid integer.") - 1 : 0;
			if (currentPage > maxPage) currentPage = maxPage;
			final int startIndex = currentPage * itemsPerPage;
			final int endIndex = Math.min(startIndex + itemsPerPage, itemsTotal);
			final String itemFormat = " &8• &b%s &8| &7%s, %.2f, %.2f, %.2f";

			sender.sendMessage("");
			Common.tell(sender, " &3&lHOLOGRAM LIST - #%d", currentPage + 1);
			Common.tell(sender, " &fList of all existing holograms.");
			sender.sendMessage("");
			for (int i = startIndex; i < endIndex; i++) {
				Hologram hologram = holograms.get(i);
				Location loc = hologram.getLocation();
				Common.tell(sender, itemFormat, hologram.isEnabled() ? hologram.getName() : "&c" + hologram.getName(), loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
			}
			sender.sendMessage("");
			if (maxPage > 0) {
				((Player) sender).spigot().sendMessage(Message.getPagesComponents(currentPage, maxPage == currentPage, "/dh list %d"));
				sender.sendMessage("");
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return null;
	}

}

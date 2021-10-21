package eu.decentsoftware.holograms.core.commands.sub.hologram;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import eu.decentsoftware.holograms.utils.Common;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class HologramNearSub extends DecentCommand {

	public HologramNearSub() {
		super("near", "dh.hologram.admin", true);
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh hologram near <range>";
	}

	@Override
	public String getDescription() {
		return "List of holograms near you.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			Player player = (Player) sender;
			int range = EditValidator.getIntegerInRange(
					EditValidator.getInteger(args[0], "Range must be a valid integer."),
					1, 1000,
					"Range must be a valid integer between 1 and 1000."
			);

			List<Hologram> nearHolograms = Lists.newArrayList();
			for (Hologram hologram : PLUGIN.getHologramManager().getHolograms()) {
				if (hologram.getLocation().distanceSquared(player.getLocation()) <= (range * range)) {
					nearHolograms.add(hologram);
				}
			}

			if (nearHolograms.isEmpty()) {
				Common.tell(sender, "%sThere are no holograms near you.", Common.PREFIX);
			} else {
				player.sendMessage("");
				Common.tell(player, " &3&lNEAR HOLOGRAMS");
				Common.tell(player, " &fList of holograms near you.");
				player.sendMessage("");
				for (Hologram hologram : nearHolograms) {
					Location loc = hologram.getLocation();
					Common.tell(sender,
							" &8• &7%s &8| &b%s, %.2f, %.2f, %.2f",
							hologram.getName(),
							loc.getWorld().getName(),
							loc.getX(),
							loc.getY(),
							loc.getZ()
					);
				}
				player.sendMessage("");
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return null;
	}

}

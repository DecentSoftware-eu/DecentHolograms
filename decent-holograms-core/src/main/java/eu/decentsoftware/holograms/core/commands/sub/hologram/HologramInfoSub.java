package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.objects.enums.EnumFlag;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import eu.decentsoftware.holograms.utils.Common;
import org.bukkit.Location;

import java.util.stream.Collectors;

public class HologramInfoSub extends DecentCommand {

	public HologramInfoSub() {
		super("info", "dh.hologram.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh hologram info <hologram>";
	}

	@Override
	public String getDescription() {
		return "Show info about a Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			Location location = hologram.getLocation();

			sender.sendMessage("");
			Common.tell(sender, " &3&lHOLOGRAM INFO");
			Common.tell(sender, " &fInformation about hologram.");
			sender.sendMessage("");
			Common.tell(sender, " &8• &7Name: &b%s", hologram.getName());
			Common.tell(sender, " &8• &7Location: &b%s, %.2f, %.2f, %.2f",
					location.getWorld().getName(), location.getX(), location.getY(), location.getZ()
			);
			Common.tell(sender, " &8• &7Enabled: &b%s", hologram.isEnabled());
			if (hologram.getPermission() != null && !hologram.getPermission().isEmpty()) {
				Common.tell(sender, " &8• &7Permission: &b%s", hologram.getPermission());
			}
			Common.tell(sender, " &8• &7Lines: &b%d", hologram.size());
			Common.tell(sender, " &8• &7Total Height: &b%.3f", hologram.getHeight());
			Common.tell(sender, " &8• &7Facing: &b%.1f deg", hologram.getFacing());
			Common.tell(sender, " &8• &7Origin: &b%s", hologram.getOrigin().name());
			Common.tell(sender, " &8• &7Update Interval: &b%d ticks", hologram.getUpdateInterval());
			Common.tell(sender, " &8• &7Update Range: &b%d", hologram.getUpdateRange());
			Common.tell(sender, " &8• &7Display Range: &b%d", hologram.getDisplayRange());
			if (!hologram.getFlags().isEmpty()) {
				Common.tell(sender, " &8• &7Flags: &b%s", hologram.getFlags().stream().map(EnumFlag::name).collect(Collectors.joining(", ")));
			}
			sender.sendMessage("");
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

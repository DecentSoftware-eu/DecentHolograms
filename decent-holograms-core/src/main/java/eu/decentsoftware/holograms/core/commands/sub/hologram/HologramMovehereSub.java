package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HologramMovehereSub extends DecentCommand {

	public HologramMovehereSub() {
		super("movehere", "dh.hologram.admin", true, "mvhr");
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh hologram movehere <hologram>";
	}

	@Override
	public String getDescription() {
		return "Move a Hologram to yourself.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			Player player = EditValidator.getPlayer(sender);
			Location playerLocation = player.getLocation();
			Location location = hologram.getLocation();
			location.setX(playerLocation.getX());
			location.setY(playerLocation.getY());
			location.setZ(playerLocation.getZ());
			hologram.setLocation(location);
			hologram.realignLines();
			hologram.save();

			Lang.HOLOGRAM_MOVED.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

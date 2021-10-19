package eu.decentsoftware.holograms.core.commands.sub.hologram;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class HologramPermissionSub extends DecentCommand {

	public HologramPermissionSub() {
		super("setpermission", "dh.hologram.admin", false, "permission", "setperm", "perm");
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh hologram setpermission <hologram> [permission]";
	}

	@Override
	public String getDescription() {
		return "Set hologram permission.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			if (args.length >= 2) {
				hologram.setPermission(args[1]);
				Lang.HOLOGRAM_PERMISSION_SET.send(sender, args[1]);
			} else {
				hologram.setPermission(null);
				Lang.HOLOGRAM_PERMISSION_REMOVED.send(sender);
			}
			hologram.save();
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return TabCompleteHandler.HOLOGRAM_NAMES;
	}

}

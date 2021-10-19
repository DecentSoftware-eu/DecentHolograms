package eu.decentsoftware.holograms.core.commands.sub.line;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.core.edit.EditValidator;

public class LinePermissionSub extends DecentCommand {

	public LinePermissionSub() {
		super("setpermission", "dh.line.admin", false, "permission", "setperm", "perm");
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh line setpermission <hologram> <line> [permission]";
	}

	@Override
	public String getDescription() {
		return "Set line permission.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final int index = EditValidator.getInteger(args[1], "Line index must be a valid number.");
			final Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			final HologramLine line = EditValidator.getHologramLine(hologram, index);
			if (line != null) {
				if (args.length >= 3) {
					line.setPermission(args[2]);
					Lang.LINE_PERMISSION_SET.send(sender, args[2]);
				} else {
					line.setPermission(null);
					Lang.LINE_PERMISSION_REMOVED.send(sender);
				}
				hologram.save();
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

package eu.decentsoftware.holograms.core.commands.sub;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.core.commands.sub.hologram.*;

public class HologramSubCommand extends DecentCommand {

	public HologramSubCommand() {
		super("holograms", "dh.hologram.admin", false, "hologram", "holo", "h");

		addSubCommand(new HologramHelpSub());
		addSubCommand(new HologramEnableSub());
		addSubCommand(new HologramDisableSub());
		addSubCommand(new HologramCreateSub());
		addSubCommand(new HologramUpdateSub());
		addSubCommand(new HologramCloneSub());
		addSubCommand(new HologramDeleteSub());
		addSubCommand(new HologramInfoSub());
		addSubCommand(new HologramLinesSub());
		addSubCommand(new HologramTeleportSub());
		addSubCommand(new HologramMovehereSub());
		addSubCommand(new HologramMoveSub());
		addSubCommand(new HologramCenterSub());
		addSubCommand(new HologramAlignSub());
		addSubCommand(new HologramNearSub());
		addSubCommand(new HologramOriginSub());
		addSubCommand(new HologramFacingSub());
		addSubCommand(new HologramFlagAddSub());
		addSubCommand(new HologramFlagRemoveSub());
		addSubCommand(new HologramPermissionSub());
		addSubCommand(new HologramDisplayRangeSub());
		addSubCommand(new HologramUpdateRangeSub());
		addSubCommand(new HologramUpdateIntervalSub());
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public String getUsage() {
		return "/dh holograms help";
	}

	@Override
	public String getDescription() {
		return "All commands for editting holograms.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			if (args.length == 0) {
				Lang.USE_HELP.send(sender);
				return true;
			}
			Lang.UNKNOWN_SUB_COMMAND.send(sender);
			Lang.USE_HELP.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return null;
	}

}

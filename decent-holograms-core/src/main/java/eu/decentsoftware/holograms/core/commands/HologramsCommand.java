package eu.decentsoftware.holograms.core.commands;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentPluginCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.core.commands.sub.*;

public class HologramsCommand extends DecentPluginCommand {

	public HologramsCommand() {
		super("decentholograms", null, false, "holograms", "hologram", "dh");

		addSubCommand(new HelpSubCommand());
		addSubCommand(new ReloadSubCommand());
		addSubCommand(new ListSubCommand());
		addSubCommand(new HologramSubCommand());
		addSubCommand(new LineSubCommand());
		addSubCommand(new FeatureSubCommand());
		addSubCommand(new ConvertSubCommand());
		addSubCommand(new VersionSubCommand());
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public String getUsage() {
		return "/dh <args>";
	}

	@Override
	public String getDescription() {
		return "The main DecentHolograms Command.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			if (sender.hasPermission("dh.help")) {
				if (args.length == 0) {
					Lang.USE_HELP.send(sender);
					return true;
				}
				Lang.UNKNOWN_SUB_COMMAND.send(sender);
				Lang.USE_HELP.send(sender);
			} else {
				Lang.sendVersionMessage(sender);
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return null;
	}

}

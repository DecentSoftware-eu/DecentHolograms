package eu.decentsoftware.holograms.core.commands;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentPluginCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.core.commands.sub.*;
import eu.decentsoftware.holograms.utils.Common;

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
				Common.tell(sender,
						Common.PREFIX + "&7This server is running &bDecentHolograms v%s&7 by &bd0by&7.\n&7(https://www.spigotmc.org/resources/96927/)",
						PLUGIN.getPlugin().getDescription().getVersion()
				);
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return null;
	}

}

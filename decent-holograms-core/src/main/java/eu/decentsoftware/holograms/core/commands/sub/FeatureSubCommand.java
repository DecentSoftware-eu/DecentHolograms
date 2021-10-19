package eu.decentsoftware.holograms.core.commands.sub;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.core.commands.sub.features.*;

public class FeatureSubCommand extends DecentCommand {

	public FeatureSubCommand() {
		super("features", "dh.features.admin", false, "feature", "f");

		addSubCommand(new FeatureHelpSub());
		addSubCommand(new FeatureListSub());
		addSubCommand(new FeatureInfoSub());
		addSubCommand(new FeatureEnableSub());
		addSubCommand(new FeatureDisableSub());
		addSubCommand(new FeatureReloadSub());
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public String getUsage() {
		return "/dh features help";
	}

	@Override
	public String getDescription() {
		return "All commands for managing features.";
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

package eu.decentsoftware.holograms.core.commands.sub;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;

public class ReloadSubCommand extends DecentCommand {

	public ReloadSubCommand() {
		super("reload", "dh.hologram.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public String getUsage() {
		return "/dh reload";
	}

	@Override
	public String getDescription() {
		return "Reload the plugin.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			PLUGIN.reload();
			Lang.RELOADED.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return null;
	}

}

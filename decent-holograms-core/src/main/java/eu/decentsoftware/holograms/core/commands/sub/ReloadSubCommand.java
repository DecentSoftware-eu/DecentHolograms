package eu.decentsoftware.holograms.core.commands.sub;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.core.DecentHologramsAPI;
import eu.decentsoftware.holograms.utils.Common;

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

			((DecentHologramsAPI) PLUGIN).getUpdateChecker().getVersion(version -> {
				String currentVersion = PLUGIN.getPlugin().getDescription().getVersion();
				if (currentVersion.equals(version)) {
					Common.tell(sender, Common.PREFIX + "There are currently no new updates.");
				} else {
					Common.tell(sender, Common.PREFIX + "There is a new update available &b%s -> %s&7.", currentVersion, version);
				}
			});
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return null;
	}

}

package eu.decentsoftware.holograms.core.commands.sub;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.commands.CommandBase;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.utils.Common;

import java.util.List;

public class HelpSubCommand extends DecentCommand {

	public HelpSubCommand() {
		super("help", "dh.help", false, "?");
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public String getUsage() {
		return "/dh help";
	}

	@Override
	public String getDescription() {
		return "Show general help.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			sender.sendMessage("");
			Common.tell(sender, " &3&lDECENT HOLOGRAMS HELP");
			Common.tell(sender, " All general commands.");
			sender.sendMessage("");
			CommandBase command = PLUGIN.getCommand();
			List<CommandBase> subCommands = Lists.newArrayList(command.getSubCommands());
			for (CommandBase subCommand : subCommands) {
				Common.tell(sender, " &8• &b%s &8- &7%s", subCommand.getUsage(), subCommand.getDescription());
			}
			sender.sendMessage("");
			Common.tell(sender, " &7Aliases: &b%s%s",
					command.getName(),
					command.getAliases().length > 1
							? ", " + String.join(", ", command.getAliases())
							: ""
			);
			sender.sendMessage("");
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return null;
	}

}

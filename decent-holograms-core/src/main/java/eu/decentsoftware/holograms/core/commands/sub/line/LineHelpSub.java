package eu.decentsoftware.holograms.core.commands.sub.line;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.commands.CommandBase;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.utils.Common;

import java.util.List;

public class LineHelpSub extends DecentCommand {

	public LineHelpSub() {
		super("help", "dh.help", false, "?");
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public String getUsage() {
		return "/dh line help";
	}

	@Override
	public String getDescription() {
		return "Show help for lines";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			sender.sendMessage("");
			Common.tell(sender, " &3&lDECENT HOLOGRAMS HELP (LINES)");
			Common.tell(sender, " All commands for editting hologram lines.");
			sender.sendMessage("");
			CommandBase command = PLUGIN.getCommand().getSubCommand("lines");
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

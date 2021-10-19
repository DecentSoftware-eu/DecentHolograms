package eu.decentsoftware.holograms.core.commands.sub;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.core.commands.sub.line.*;

public class LineSubCommand extends DecentCommand {

	public LineSubCommand() {
		super("lines", "dh.line.admin", false, "line", "l");

		addSubCommand(new LineHelpSub());
		addSubCommand(new LineAddSub());
		addSubCommand(new LineInsertSub());
		addSubCommand(new LineSetSub());
		addSubCommand(new LineEditSub());
		addSubCommand(new LineRemoveSub());
		addSubCommand(new LineInfoSub());
		addSubCommand(new LineSwapSub());
		addSubCommand(new LineAlignSub());
		addSubCommand(new LineHeightSub());
		addSubCommand(new LineOffsetXSub());
		addSubCommand(new LineOffsetZSub());
		addSubCommand(new LineFlagAddSub());
		addSubCommand(new LineFlagRemoveSub());
		addSubCommand(new LinePermissionSub());
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public String getUsage() {
		return "/dh lines help";
	}

	@Override
	public String getDescription() {
		return "All commands for editting hologram lines.";
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
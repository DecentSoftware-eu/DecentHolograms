package eu.decentsoftware.holograms.core.commands.sub;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;

public class VersionSubCommand extends DecentCommand {

    public VersionSubCommand() {
        super("version", "dh.admin", false, "ver", "about");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public String getUsage() {
        return "/dh version";
    }

    @Override
    public String getDescription() {
        return "Shows some info about your current DecentHolograms version.";
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            Lang.sendVersionMessage(sender);
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return null;
    }
}

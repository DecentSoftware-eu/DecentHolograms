package eu.decentsoftware.holograms.plugin.commands;

import java.util.List;

import com.google.common.collect.Lists;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandBase;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.features.AbstractFeature;
import eu.decentsoftware.holograms.api.utils.Common;

@CommandInfo(
		permissions = "dh.command.features",
		usage = "/dh features help",
		description = "所有用于管理功能的命令。",
		aliases = {"feature", "f"}
)
public class FeatureSubCommand extends DecentCommand {

	public FeatureSubCommand() {
		super("features");

		addSubCommand(new FeatureHelpSub());
		addSubCommand(new FeatureListSub());
		addSubCommand(new FeatureInfoSub());
		addSubCommand(new FeatureEnableSub());
		addSubCommand(new FeatureDisableSub());
		addSubCommand(new FeatureReloadSub());
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

    /*
     *  SubCommands
     */

    @CommandInfo(
            permissions = "dh.command.features.disable",
            usage = "/dh feature disable <feature>",
            description = "禁用功能。",
            aliases = {"off"},
            minArgs = 1
    )
    public static class FeatureDisableSub extends DecentCommand {

        public FeatureDisableSub() {
            super("disable");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                AbstractFeature feature = PLUGIN.getFeatureManager().getFeature(args[0]);
                if (feature == null) {
                    Lang.FEATURE_DOES_NOT_EXIST.send(sender, args[0]);
                } else {
                    if (!feature.isEnabled()) {
                        Lang.FEATURE_ALREADY_DISABLED.send(sender, args[0]);
                        return true;
                    }
                    feature.disable();
                    Lang.FEATURE_DISABLED.send(sender, args[0]);
                }
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return (sender, args) -> {
                if (args.length == 1) {
                    return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getFeatureManager().getFeatureNames());
                }
                return null;
            };
        }

    }

    @CommandInfo(
            permissions = "dh.command.features.enable",
            usage = "/dh feature enable <feature>",
            description = "启用功能。",
            aliases = {"on"},
            minArgs = 1
    )
    public static class FeatureEnableSub extends DecentCommand {

        public FeatureEnableSub() {
            super("enable");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                AbstractFeature feature = PLUGIN.getFeatureManager().getFeature(args[0]);
                if (feature == null) {
                    Lang.FEATURE_DOES_NOT_EXIST.send(sender, args[0]);
                } else {
                    if (feature.isEnabled()) {
                        Lang.FEATURE_ALREADY_ENABLED.send(sender, args[0]);
                        return true;
                    }
                    feature.enable();
                    Lang.FEATURE_ENABLED.send(sender, args[0]);
                }
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return (sender, args) -> {
                if (args.length == 1) {
                    return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getFeatureManager().getFeatureNames());
                }
                return null;
            };
        }

    }

    @CommandInfo(
            permissions = "dh.command.features.help",
            usage = "/dh feature help",
            description = "显示功能相关帮助。",
            aliases = {"?"}
    )
    public static class FeatureHelpSub extends DecentCommand {

        public FeatureHelpSub() {
            super("help");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                sender.sendMessage("");
                Common.tell(sender, " &3&lDECENT HOLOGRAMS 帮助 (功能)");
                Common.tell(sender, " 所有用于管理功能的命令。");
                sender.sendMessage("");
                CommandBase command = PLUGIN.getCommandManager().getMainCommand().getSubCommand("features");
                printHelpSubCommandsAndAliases(sender, command);
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return null;
        }

    }

    @CommandInfo(
            permissions = "dh.command.features.info",
            usage = "/dh feature info <feature>",
            description = "显示功能相关信息。",
            minArgs = 1
    )
    public static class FeatureInfoSub extends DecentCommand {

        public FeatureInfoSub() {
            super("info");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                AbstractFeature feature = PLUGIN.getFeatureManager().getFeature(args[0]);
                if (feature == null) {
                    Common.tell(sender, "%sFeature with that name does not exist.", Common.PREFIX);
                } else {
                    sender.sendMessage("");
                    Common.tell(sender, " &3&lFEATURE INFO");
                    Common.tell(sender, " Info about feature.");
                    sender.sendMessage("");
                    Common.tell(sender, " &8• &7Name: &b%s", feature.getName());
                    Common.tell(sender, " &8• &7Description: &b%s", feature.getDescription());
                    Common.tell(sender, " &8• &7Status: &b%s", feature.isEnabled() ? "&aON" : "&cOFF");
                    sender.sendMessage("");
                }
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return (sender, args) -> {
                if (args.length == 1) {
                    return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getFeatureManager().getFeatureNames());
                }
                return null;
            };
        }

    }

    @CommandInfo(
            permissions = "dh.command.features.list",
            usage = "/dh feature list",
            description = "所有功能列表"
    )
    public static class FeatureListSub extends DecentCommand {

        public FeatureListSub() {
            super("list");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                List<AbstractFeature> features = Lists.newArrayList(PLUGIN.getFeatureManager().getFeatures());
                if (features.isEmpty()) {
                    Common.tell(sender, "%sThere are no features are currently registered.");
                } else {
                    sender.sendMessage("");
                    Common.tell(sender, " &3&lFEATURES LIST");
                    Common.tell(sender, " List of all features.");
                    sender.sendMessage("");
                    for (AbstractFeature feature : features) {
                        Common.tell(sender, " &8• &b%s &8- %s", feature.getName(), feature.isEnabled() ? "&aON" : "&cOFF");
                    }
                    sender.sendMessage("");
                }
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return null;
        }

    }

    @CommandInfo(
            permissions = "dh.command.features.reload",
            usage = "/dh feature reload <feature>",
            description = "重新加载功能。",
            minArgs = 1
    )
    public static class FeatureReloadSub extends DecentCommand {

        public FeatureReloadSub() {
            super("reload");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                AbstractFeature feature = PLUGIN.getFeatureManager().getFeature(args[0]);
                if (feature == null) {
                    Lang.FEATURE_DOES_NOT_EXIST.send(sender, args[0]);
                } else {
                    feature.reload();
                    Lang.FEATURE_RELOADED.send(sender, args[0]);
                }
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return (sender, args) -> {
                if (args.length == 1) {
                    return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getFeatureManager().getFeatureNames());
                }
                return null;
            };
        }

    }
}

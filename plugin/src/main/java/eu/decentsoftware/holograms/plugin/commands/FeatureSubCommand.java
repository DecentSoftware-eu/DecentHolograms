package eu.decentsoftware.holograms.plugin.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.Permissions;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandBase;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.features.AbstractFeature;
import eu.decentsoftware.holograms.api.features.FeatureManager;
import eu.decentsoftware.holograms.api.utils.Common;

import java.util.List;

@CommandInfo(
        permissions = {Permissions.COMMAND_FEATURES},
        usage = "/dh features help",
        description = "All commands for managing features.",
        aliases = {"feature", "f"}
)
public class FeatureSubCommand extends DecentCommand {

    public FeatureSubCommand(FeatureManager featureManager, CommandManager commandManager) {
        super("features");

        addSubCommand(new FeatureHelpSub(commandManager));
        addSubCommand(new FeatureListSub(featureManager));
        addSubCommand(new FeatureInfoSub(featureManager));
        addSubCommand(new FeatureEnableSub(featureManager));
        addSubCommand(new FeatureDisableSub(featureManager));
        addSubCommand(new FeatureReloadSub(featureManager));
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
            permissions = Permissions.COMMAND_FEATURES_DISABLE,
            usage = "/dh feature disable <feature>",
            description = "Disable a Feature.",
            aliases = {"off"},
            minArgs = 1
    )
    public static class FeatureDisableSub extends DecentCommand {

        private final FeatureManager featureManager;

        public FeatureDisableSub(FeatureManager featureManager) {
            super("disable");
            this.featureManager = featureManager;
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                AbstractFeature feature = featureManager.getFeature(args[0]);
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
                    return TabCompleteHandler.getPartialMatches(args[0], featureManager.getFeatureNames());
                }
                return null;
            };
        }

    }

    @CommandInfo(
            permissions = Permissions.COMMAND_FEATURES_ENABLE,
            usage = "/dh feature enable <feature>",
            description = "Enable a Feature.",
            aliases = {"on"},
            minArgs = 1
    )
    public static class FeatureEnableSub extends DecentCommand {

        private final FeatureManager featureManager;

        public FeatureEnableSub(FeatureManager featureManager) {
            super("enable");
            this.featureManager = featureManager;
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                AbstractFeature feature = featureManager.getFeature(args[0]);
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
                    return TabCompleteHandler.getPartialMatches(args[0], featureManager.getFeatureNames());
                }
                return null;
            };
        }

    }

    @CommandInfo(
            permissions = Permissions.COMMAND_FEATURES_HELP,
            usage = "/dh feature help",
            description = "Show help for features.",
            aliases = {"?"}
    )
    public static class FeatureHelpSub extends DecentCommand {

        private final CommandManager commandManager;

        public FeatureHelpSub(CommandManager commandManager) {
            super("help");
            this.commandManager = commandManager;
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                sender.sendMessage("");
                Common.tell(sender, " &3&lDECENT HOLOGRAMS HELP (FEATURES)");
                Common.tell(sender, " All commands for managing features.");
                sender.sendMessage("");
                CommandBase command = commandManager.getMainCommand().getSubCommand("features");
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
            permissions = {Permissions.COMMAND_FEATURES_INFO},
            usage = "/dh feature info <feature>",
            description = "Info about feature.",
            minArgs = 1
    )
    public static class FeatureInfoSub extends DecentCommand {

        private final FeatureManager featureManager;

        public FeatureInfoSub(FeatureManager featureManager) {
            super("info");
            this.featureManager = featureManager;
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                AbstractFeature feature = featureManager.getFeature(args[0]);
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
                    return TabCompleteHandler.getPartialMatches(args[0], featureManager.getFeatureNames());
                }
                return null;
            };
        }

    }

    @CommandInfo(
            permissions = {Permissions.COMMAND_FEATURES_LIST},
            usage = "/dh feature list",
            description = "List of all features"
    )
    public static class FeatureListSub extends DecentCommand {

        private final FeatureManager featureManager;

        public FeatureListSub(FeatureManager featureManager) {
            super("list");
            this.featureManager = featureManager;
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                List<AbstractFeature> features = Lists.newArrayList(featureManager.getFeatures());
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
            permissions = {Permissions.COMMAND_FEATURES_RELOAD},
            usage = "/dh feature reload <feature>",
            description = "Reload a Feature.",
            minArgs = 1
    )
    public static class FeatureReloadSub extends DecentCommand {

        private final FeatureManager featureManager;

        public FeatureReloadSub(FeatureManager featureManager) {
            super("reload");
            this.featureManager = featureManager;
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                AbstractFeature feature = featureManager.getFeature(args[0]);
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
                    return TabCompleteHandler.getPartialMatches(args[0], featureManager.getFeatureNames());
                }
                return null;
            };
        }

    }
}

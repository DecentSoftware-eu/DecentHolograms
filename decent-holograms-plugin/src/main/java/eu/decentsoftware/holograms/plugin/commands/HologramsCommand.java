package eu.decentsoftware.holograms.plugin.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.*;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.player.DecentPlayer;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.message.Message;
import eu.decentsoftware.holograms.plugin.Validator;
import eu.decentsoftware.holograms.plugin.convertors.ConvertorType;
import eu.decentsoftware.holograms.plugin.convertors.HolographicDisplaysConvertor;
import eu.decentsoftware.holograms.plugin.menu.MainMenu;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@CommandInfo(
		aliases = {"holograms", "hologram", "dh"},
		permission = "",
		usage = "/dh <args>",
		description = "The main DecentHolograms Command."
)
public class HologramsCommand extends DecentCommand {

	public HologramsCommand() {
		super("decentholograms");

		addSubCommand(new HelpSubCommand());
		addSubCommand(new ReloadSubCommand());
		addSubCommand(new ListSubCommand());
		addSubCommand(new HologramSubCommand());
		addSubCommand(new LineSubCommand());
		addSubCommand(new FeatureSubCommand());
		addSubCommand(new PageSubCommand());
		addSubCommand(new ConvertSubCommand());
		addSubCommand(new VersionSubCommand());
//		addSubCommand(new MenuSubCommand());

        // Shortcuts
        addSubCommand(new HologramSubCommand.HologramCreateSub());
        addSubCommand(new HologramSubCommand.HologramDeleteSub());
        addSubCommand(new HologramSubCommand.HologramCloneSub());
        addSubCommand(new HologramSubCommand.HologramEnableSub());
        addSubCommand(new HologramSubCommand.HologramDisableSub());
        addSubCommand(new HologramSubCommand.HologramAlignSub());
        addSubCommand(new HologramSubCommand.HologramCenterSub());
        addSubCommand(new HologramSubCommand.HologramInfoSub());
        addSubCommand(new HologramSubCommand.HologramNearSub());
        addSubCommand(new HologramSubCommand.HologramTeleportSub());
        addSubCommand(new HologramSubCommand.HologramMoveSub());
        addSubCommand(new HologramSubCommand.HologramMovehereSub());
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
				Lang.sendVersionMessage(sender);
			}
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
            permission = "dh.admin",
            aliases = {"ver", "about"},
            usage = "/dh version",
            description = "Shows some info about your current DecentHolograms version."
    )
    public static class VersionSubCommand extends DecentCommand {

        public VersionSubCommand() {
            super("version");
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

    @CommandInfo(
            permission = "dh.admin",
            usage = "/dh reload",
            description = "Reload the plugin."
    )
    public static class ReloadSubCommand extends DecentCommand {

        public ReloadSubCommand() {
            super("reload");
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

    @CommandInfo(
            playerOnly = true,
            aliases = {"gui"},
            permission = "dh.admin",
            usage = "/dh menu",
            description = "Open the main DecentHolograms menu."
    )
    public static class MenuSubCommand extends DecentCommand {

        public MenuSubCommand() {
            super("menu");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                Player player = Validator.getPlayer(sender);
                DecentPlayer decentPlayer = DecentPlayer.getByUUID(player.getUniqueId());
                new MainMenu(decentPlayer, null).open();
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return null;
        }
    }

    @CommandInfo(
            permission = "dh.admin",
            usage = "/dh list [page]",
            description = "Show list of all Holograms.",
            playerOnly = true
    )
    public static class ListSubCommand extends DecentCommand {

        public ListSubCommand() {
            super("list");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                final List<Hologram> holograms = Lists.newArrayList(PLUGIN.getHologramManager().getHolograms());
                if (holograms.isEmpty()) {
                    Common.tell(sender, "%sThere are currenty no holograms.", Common.PREFIX);
                    return true;
                }
                int currentPage = args.length >= 1 ? Validator.getInteger(args[0], "Page must be a valid integer.") - 1 : 0;
                List<String> header = Lists.newArrayList("", " &3&lHOLOGRAM LIST - #" + (currentPage + 1), " &fList of all existing holograms.", "");
                Function<Hologram, String> parseItem = hologram -> {
                    Location l = hologram.getLocation();
                    String name = hologram.isEnabled() ? hologram.getName() : "&c" + hologram.getName();
                    return String.format(" &8• &b%s &8| &7%s, %.2f, %.2f, %.2f", name, l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
                };
                Message.sendPaginatedMessage((Player) sender, currentPage, "/dh list %d", 15, header, null, holograms, parseItem);
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return null;
        }

    }

    @CommandInfo(
            permission = "dh.admin",
            usage = "/dh help",
            description = "Show general help.",
            aliases = {"?"}
    )
    public static class HelpSubCommand extends DecentCommand {

        public HelpSubCommand() {
            super("help");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                sender.sendMessage("");
                Common.tell(sender, " &3&lDECENT HOLOGRAMS HELP");
                Common.tell(sender, " All general commands.");
                sender.sendMessage("");
                CommandBase command = PLUGIN.getCommandManager().getMainCommand();
                List<CommandBase> subCommands = Lists.newArrayList(command.getSubCommands());
                for (CommandBase subCommand : subCommands) {
                    if (subCommand.getClass().toString().contains("HologramSubCommand")) continue;
                    Common.tell(sender, " &8• &b%s &8- &7%s", subCommand.getUsage(), subCommand.getDescription());
                }
                sender.sendMessage("");
                Common.tell(sender, " &7Aliases: &b%s%s",
                        command.getName(),
                        command.getAliases().size() > 1
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

    @CommandInfo(
            permission = "dh.admin",
            usage = "/dh convert <plugin> [file]",
            description = "Convert holograms from given plugin.",
            minArgs = 1
    )
    public static class ConvertSubCommand extends DecentCommand {

        public ConvertSubCommand() {
            super("convert");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                final ConvertorType convertorType = ConvertorType.getByName(args[0]);
                final String path = args.length >= 2 ? args[0] : null;

                switch (convertorType) {
                    case HOLOGRAPHIC_DISPLAYS:
                        Common.tell(sender, Common.PREFIX + "Converting from " + convertorType.getName());
                        if (path != null) {
                            File file = new File(path);
                            new HolographicDisplaysConvertor().convert(file);
                        } else {
                            new HolographicDisplaysConvertor().convert();
                        }
                        return true;
                    default:
                        break;
                }
                Common.tell(sender, Common.PREFIX + "Plugin '" + args[0] + "' couldn't be found.");
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return (sender, args) -> {
                if (args.length == 1) {
                    return StringUtil.copyPartialMatches(
                            args[0],
                            Arrays.stream(ConvertorType.values()).map(ConvertorType::getName).collect(Collectors.toList()),
                            Lists.newArrayList()
                    );
                }
                return new ArrayList<>();
            };
        }

    }
}

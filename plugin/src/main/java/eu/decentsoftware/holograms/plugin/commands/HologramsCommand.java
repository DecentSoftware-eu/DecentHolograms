package eu.decentsoftware.holograms.plugin.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandBase;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.message.Message;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.plugin.Validator;
import eu.decentsoftware.holograms.plugin.convertors.ConvertorResult;
import eu.decentsoftware.holograms.plugin.convertors.ConvertorType;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@CommandInfo(
		aliases = {"holograms", "hologram", "dh", "holo"},
		permissions = {"dh.default", "dh.command.decentholograms"},
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
//        addSubCommand(new TestSubCommand());

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
			if (sender.hasPermission("dh.admin")) {
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

//    @CommandInfo(
//            permissions = "dh.command.test",
//            usage = "/dh test",
//            playerOnly = true,
//            minArgs = 1,
//            description = "Test command."
//    )
//    public static class TestSubCommand extends DecentCommand {
//
//        public TestSubCommand() {
//            super("test");
//        }
//
//        @Override
//        public CommandHandler getCommandHandler() {
//            return (sender, args) -> {
//                if (!(sender instanceof Player)) {
//                    Lang.ONLY_PLAYER.send(sender);
//                    return true;
//                }
//                if (args.length < 2) {
//                    Lang.USE_HELP.send(sender);
//                    return true;
//                }
//                Player player = (Player) sender;
//                DecentPosition position = DecentPosition.fromBukkitLocation(player.getLocation());
//                NmsHologramRendererFactory componentFactory = PLUGIN.getNmsAdapter().getHologramComponentFactory();
//                switch (args[0].toUpperCase()) {
//                    case "TEXT":
//                        NmsTextHologramRenderer textComponent = componentFactory.createTextRenderer();
//                        textComponent.display(player, position, args[1]);
//                        break;
//                    case "ENTITY":
//                        NmsEntityHologramRenderer entityComponent = componentFactory.createEntityRenderer();
//                        EntityType entityType = XEntityType.of(args[1]).orElse(XEntityType.PIG).get();
//                        entityComponent.display(player, position, entityType);
//                        break;
//                    case "ICON":
//                        NmsIconHologramRenderer iconComponent = componentFactory.createIconRenderer();
//                        iconComponent.display(player, position, getItemStack(args));
//                        break;
//                    case "HEAD":
//                        NmsHeadHologramRenderer headComponent = componentFactory.createHeadRenderer();
//                        headComponent.display(player, position, getItemStack(args));
//                        break;
//                    case "SMALLHEAD":
//                    case "SMALL_HEAD":
//                        NmsHeadHologramRenderer smallHeadComponent = componentFactory.createSmallHeadRenderer();
//                        smallHeadComponent.display(player, position, getItemStack(args));
//                        break;
//                    default:
//                        Lang.USE_HELP.send(sender);
//                        return true;
//                }
//
//                return true;
//            };
//        }
//
//        private ItemStack getItemStack(String[] args) {
//            Material material = XMaterial.valueOf(args[1]).or(XMaterial.STONE).get();
//            return new ItemStack(material);
//        }
//
//        @Override
//        public TabCompleteHandler getTabCompleteHandler() {
//            return null;
//        }
//    }

    @CommandInfo(
            permissions = {"dh.default", "dh.command.version"},
            usage = "/dh version",
            aliases = {"ver", "about"},
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
            permissions = "dh.command.reload",
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
                S.async(() -> {
                    long start = System.currentTimeMillis();
                    PLUGIN.reload();
                    long end = System.currentTimeMillis();
                    Lang.RELOADED.send(sender, end - start);
                });
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return null;
        }

    }

    @CommandInfo(
            permissions = "dh.command.list",
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
                    Common.tell(sender, "%sThere are currently no holograms.", Common.PREFIX);
                    return true;
                }
                int currentPage = args.length >= 1 ? Validator.getInteger(args[0], "Page must be a valid integer.") - 1 : 0;
                List<String> header = Lists.newArrayList("", " &3&lHOLOGRAM LIST - #" + (currentPage + 1), " &fList of all existing holograms.", "");
                Function<Hologram, String> parseItem = hologram -> {
                    Location l = hologram.getLocation();
                    String name = (hologram.isEnabled() ? "" : "&c") + hologram.getName();
                    return String.format(" &8• &b%s &8| &7%s, %.2f, %.2f, %.2f", name, l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
                };
                Message.sendPaginatedMessage((Player) sender, currentPage, "/dh list %d", 15, header, null, holograms, parseItem);
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return (sender, args) -> {
                if (args.length != 1) {
                    return null;
                }

                int holograms = PLUGIN.getHologramManager().getHolograms().size();
                if (holograms == 0) {
                    return null;
                }

                List<String> pages = new ArrayList<>();
                int page = 0;
                while(holograms > 0) {
                    page++;
                    pages.add(String.valueOf(page));
                    holograms -= 15;
                }

                return TabCompleteHandler.getPartialMatches(args[0], pages);
            };
        }

    }

    @CommandInfo(
            permissions = "dh.command.help",
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
                printHelpSubCommandsAndAliases(sender, command,
                        subCommand -> !subCommand.getClass().toString().contains("HologramSubCommand"));
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return null;
        }

    }

    @CommandInfo(
            permissions = "dh.command.convert",
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
                final ConvertorType convertorType = ConvertorType.fromString(args[0]);
                final String path = args.length >= 2 ? args[0] : null;
                if (convertorType == null) {
                    Common.tell(sender, "%s&cCannot convert Holograms! Unknown plugin '%s' provided", Common.PREFIX, args[0]);
                    return true;
                }

                long startTime = System.currentTimeMillis();
                IConvertor convertor = convertorType.getConvertor();
                if (convertor == null) {
                    Common.tell(sender, "%s&cCannot convert Holograms! Unknown plugin '%s' provided", Common.PREFIX, args[0]);
                    return true;
                }

                Common.tell(sender, "%sConverting holograms from %s...", Common.PREFIX, convertorType.getName());
                if (convertorType.isLimited()) {
                    Common.tell(sender, "%s&6NOTE: %s support is limited!", Common.PREFIX, convertorType.getName());
                }

                ConvertorResult result;
                if (path != null) {
                    File file = new File(path);
                    result = convertor.convert(file);
                } else {
                    result = convertor.convert();
                }
                sendResult(sender, startTime, result);
                return true;
            };
        }

        public void sendResult(CommandSender sender, long startTime, ConvertorResult result) {
            Common.tell(sender, "%sConverted %d hologram(s) in %s ms!", Common.PREFIX, result.getTotalCount(), System.currentTimeMillis() - startTime);
            Common.tell(sender, "%s- &a%d successful", Common.PREFIX, result.getSuccessCount());
            Common.tell(sender, "%s- &e%d skipped", Common.PREFIX, result.getSkippedCount());
            Common.tell(sender, "%s- &c%d failed", Common.PREFIX, result.getFailedCount());
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return (sender, args) -> {
                if (args.length == 1) {
                    return TabCompleteHandler.getPartialMatches(args[0], Arrays.stream(ConvertorType.values()).map(ConvertorType::getName).collect(Collectors.toList()));
                }
                return null;
            };
        }

    }
}

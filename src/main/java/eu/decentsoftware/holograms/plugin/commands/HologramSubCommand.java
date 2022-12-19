package eu.decentsoftware.holograms.plugin.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.*;
import eu.decentsoftware.holograms.api.holograms.DisableCause;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.api.utils.items.DecentMaterial;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import eu.decentsoftware.holograms.api.utils.message.Message;
import eu.decentsoftware.holograms.plugin.Validator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(
		permission = "dh.admin",
		usage = "/dh holograms help",
		description = "All commands for editing holograms.",
		aliases = {"hologram", "holo", "h"}
)
public class HologramSubCommand extends DecentCommand {

	public HologramSubCommand() {
		super("holograms");

		addSubCommand(new HologramHelpSub());
		addSubCommand(new HologramEnableSub());
		addSubCommand(new HologramDisableSub());
		addSubCommand(new HologramCreateSub());
		addSubCommand(new HologramUpdateSub());
		addSubCommand(new HologramCloneSub());
		addSubCommand(new HologramDeleteSub());
		addSubCommand(new HologramInfoSub());
		addSubCommand(new HologramLinesSub());
		addSubCommand(new HologramTeleportSub());
		addSubCommand(new HologramMovehereSub());
		addSubCommand(new HologramMoveSub());
		addSubCommand(new HologramCenterSub());
		addSubCommand(new HologramAlignSub());
		addSubCommand(new HologramNearSub());
		addSubCommand(new HologramDownOriginSub());
//		addSubCommand(new HologramAlwaysFacePlayerSub());
		addSubCommand(new HologramFacingSub());
		addSubCommand(new HologramFlagAddSub());
		addSubCommand(new HologramFlagRemoveSub());
		addSubCommand(new HologramPermissionSub());
		addSubCommand(new HologramDisplayRangeSub());
		addSubCommand(new HologramUpdateRangeSub());
		addSubCommand(new HologramUpdateIntervalSub());
		addSubCommand(new HologramRenameSub());
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
            permission = "dh.admin",
            usage = "/dh hologram update <hologram>",
            description = "Update a Hologram.",
            minArgs = 1
    )
    public static class HologramUpdateSub extends DecentCommand {

        public HologramUpdateSub() {
            super("update");
        }

        @Override
        public CommandHandler getCommandHandler() {
            return (sender, args) -> {
                Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
                if (hologram.isEnabled()) {
                    hologram.hideAll();
                    hologram.showAll();
                }
                Lang.HOLOGRAM_UPDATED.send(sender);
                return true;
            };
        }

        @Override
        public TabCompleteHandler getTabCompleteHandler() {
            return TabCompleteHandler.HOLOGRAM_NAMES;
        }

    }

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram align <hologram> <X|Y|Z|XZ> <otherHologram>",
			description = "Align hologram with other hologram on a specified axis.",
			minArgs = 3
	)
	public static class HologramAlignSub extends DecentCommand {

		public HologramAlignSub() {
			super("align");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				if (args[0].equals(args[2])) {
					Lang.HOLOGRAM_ALIGN_SELF.send(sender);
					return true;
				}

				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				Hologram otherHologram = Validator.getHologram(args[2], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				Location location = hologram.getLocation();
				Location otherLocation = otherHologram.getLocation();
				switch (args[1].toUpperCase()) {
					case "X":
						location.setX(otherLocation.getX());
						break;
					case "Y":
						location.setY(otherLocation.getY());
						break;
					case "Z":
						location.setZ(otherLocation.getZ());
						break;
					case "XZ":
					case "ZX":
						location.setX(otherLocation.getX());
						location.setZ(otherLocation.getZ());
						break;
					case "FACE":
					case "FACING":
						hologram.setFacing(otherHologram.getFacing());
						break;
					default:
						Lang.HOLOGRAM_ALIGN_AXIS.send(sender);
						return true;
				}
				hologram.setLocation(location);
				hologram.realignLines();
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}
				
				Lang.HOLOGRAM_ALIGNED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				if (args.length == 1 || args.length == 3) {
					return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
				} else if (args.length == 2) {
					return TabCompleteHandler.getPartialMatches(args[1], "X", "Y", "Z", "XZ", "FACE", "FACING");
				}
				return null;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram center <hologram>",
			description = "Move a Hologram into the center of a block.",
			minArgs = 1
	)
	public static class HologramCenterSub extends DecentCommand {

		public HologramCenterSub() {
			super("center");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				Location location = hologram.getLocation();

				int x = (int) location.getX();
				int z = (int) location.getZ();
				location.setX(x > location.getX() ? x - 0.5d : x + 0.5d);
				location.setZ(z > location.getZ() ? z - 0.5d : z + 0.5d);

				hologram.setLocation(location);
				hologram.realignLines();
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}

				Lang.HOLOGRAM_MOVED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram clone <hologram> <name> [temp] [-l:<world:x:y:z>]",
			description = "Clone an existing Hologram.",
			aliases = {"copy"},
			minArgs = 2
	)
	public static class HologramCloneSub extends DecentCommand {

		public HologramCloneSub() {
			super("clone");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				if (Hologram.getCachedHologramNames().contains(args[1])) {
					Lang.HOLOGRAM_ALREADY_EXISTS.send(sender, args[1]);
					return true;
				}

				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				boolean containsLocation = false;
				boolean temp = false;
				Location loc = null;
				if (args.length >= 3) {
					for (int i = 2; i < args.length; i++) {
						String s = args[i];
						if (s.toLowerCase().startsWith("-l:")) {
							String locationString = s.substring(3);
							loc = LocationUtils.asLocation(locationString);
							if (loc != null) {
								containsLocation = true;
								break;
							}
						} else {
							temp = Validator.getBoolean(args[2], "Value of temp must be true or false.");
						}
					}
				}

				if (!(sender instanceof Player) && !containsLocation) {
					Lang.ONLY_PLAYER.send(sender);
					return true;
				} else {
					if (loc == null) {
						Player player = (Player) sender;
						loc = player.getLocation();
					}
				}

				Hologram clone = hologram.clone(args[1], loc, temp);
				if (!clone.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return true;
				}
				clone.showAll();
				clone.realignLines();
				PLUGIN.getHologramManager().registerHologram(clone);
				Lang.HOLOGRAM_CLONED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram create <name> [content]",
			description = "Create new Hologram.",
			aliases = {"new", "c"},
			playerOnly = true,
			minArgs = 1
	)
	public static class HologramCreateSub extends DecentCommand {

		public HologramCreateSub() {
			super("create");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final String hologramName = args[0];
				// Check if the name is valid.
				if (!hologramName.matches("[a-zA-Z0-9_-]+")) {
					Lang.HOLOGRAM_INVALID_NAME.send(sender, hologramName);
					return true;
				}
				if (PLUGIN.getHologramManager().containsHologram(hologramName)) {
					Lang.HOLOGRAM_ALREADY_EXISTS.send(sender, hologramName);
					return true;
				}
				Player player = Validator.getPlayer(sender);
				String content = Validator.getLineContent(player, args, 1);
				Hologram hologram = new Hologram(hologramName, player.getLocation());
				HologramPage page = hologram.getPage(0);
				HologramLine line = new HologramLine(page, page.getNextLineLocation(), content);
				page.addLine(line);
				hologram.showAll();
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return true;
				}

				PLUGIN.getHologramManager().registerHologram(hologram);

				Lang.HOLOGRAM_CREATED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				if (args.length == 3 && (args[1].startsWith("#ICON:") || args[1].startsWith("#HEAD:") || args[1].startsWith("#SMALLHEAD:"))) {
					return TabCompleteHandler.getPartialMatches(args[2], Arrays.stream(Material.values())
						.filter(DecentMaterial::isItem)
						.map(Material::name)
						.collect(Collectors.toList()));
				} else if (args.length == 3 && args[1].startsWith("#ENTITY:")) {
					return TabCompleteHandler.getPartialMatches(args[2], DecentEntityType.getAllowedEntityTypeNames());
				}
				return null;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram delete <hologram>",
			description = "Delete a Hologram.",
			aliases = {"del", "remove", "rem"},
			minArgs = 1
	)
	public static class HologramDeleteSub extends DecentCommand {

		public HologramDeleteSub() {
			super("delete");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.delete();

				PLUGIN.getHologramManager().removeHologram(args[0]);

				Lang.HOLOGRAM_DELETED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram disable <hologram>",
			description = "Disable a hologram.",
			aliases = {"off"},
			minArgs = 1
	)
	public static class HologramDisableSub extends DecentCommand {

		public HologramDisableSub() {
			super("disable");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				if (!hologram.isEnabled()) {
					Lang.HOLOGRAM_ALREADY_DISABLED.send(sender);
					return true;
				}
				hologram.disable(DisableCause.COMMAND);
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}

				Lang.HOLOGRAM_DISABLED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram setdisplayrange <hologram> <range>",
			description = "Set display range of a hologram.",
			aliases = {"displayrange"},
			minArgs = 2
	)
	public static class HologramDisplayRangeSub extends DecentCommand {

		public HologramDisplayRangeSub() {
			super("setdisplayrange");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final int range = Validator.getInteger(args[1], 1, 64, "Range must be a valid number between 1 and 64.");
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.setDisplayRange(range);
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}
				
				Lang.HOLOGRAM_DISPLAY_RANGE_SET.send(sender, range);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram downorigin <hologram> <true|false>",
			description = "Set down origin state of the hologram.",
			aliases = {"setdownorigin"},
			minArgs = 2
	)
	public static class HologramDownOriginSub extends DecentCommand {

		public HologramDownOriginSub() {
			super("downorigin");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				boolean value = Validator.getBoolean(args[1], Lang.HOLOGRAM_DOWN_ORIGIN_DOES_NOT_EXIST.getValue());
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.setDownOrigin(value);
				hologram.realignLines();
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}
				
				Lang.HOLOGRAM_DOWN_ORIGIN_SET.send(sender, value);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				if (args.length == 1 || args.length == 3) {
					return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
				} else if (args.length == 2) {
					return TabCompleteHandler.getPartialMatches(args[1], "true", "false");
				}
				return null;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram alwaysfaceplayer <hologram> <true|false>",
			description = "Set always face player state of the hologram.",
			aliases = {"setalwaysfaceplayer"},
			minArgs = 2
	)
	public static class HologramAlwaysFacePlayerSub extends DecentCommand {

		public HologramAlwaysFacePlayerSub() {
			super("alwaysfaceplayer");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				boolean value = Validator.getBoolean(args[1], Lang.HOLOGRAM_ALWAYS_FACE_PLAYER_DOES_NOT_EXIST.getValue());
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.setAlwaysFacePlayer(value);
				hologram.realignLines();
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}
				
				Lang.HOLOGRAM_ALWAYS_FACE_PLAYER_SET.send(sender, value);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				if (args.length == 1) {
					return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
				} else if (args.length == 2) {
					return TabCompleteHandler.getPartialMatches(args[1], "true", "false");
				}
				return null;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram enable <hologram>",
			description = "Enable a hologram.",
			aliases = {"on"},
			minArgs = 1
	)
	public static class HologramEnableSub extends DecentCommand {

		public HologramEnableSub() {
			super("enable");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				if (hologram.isEnabled()) {
					Lang.HOLOGRAM_ALREADY_ENABLED.send(sender);
					return true;
				}
				hologram.enable();
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}

				Lang.HOLOGRAM_ENABLED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram setfacing <hologram> <facing>",
			description = "Set facing direction of a hologram.",
			aliases = {"facing", "setface", "face"},
			minArgs = 2
	)
	public static class HologramFacingSub extends DecentCommand {

		public HologramFacingSub() {
			super("setfacing");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				float facing;
				switch (args[1].toUpperCase()) {
					case "SOUTH": facing = 0.0f; break;
					case "WEST": facing = 90.0f; break;
					case "NORTH": facing = 180.0f; break;
					case "EAST": facing = -90.0f; break;
					default:
						facing = Validator.getFloat(args[1], -180.0f, 180.0f, "Facing must be a valid number between -180 and 180.");
						break;
				}
				hologram.setFacing(facing);
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}
				
				hologram.realignLines();
				Lang.HOLOGRAM_FACING_SET.send(sender, facing);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				if (args.length == 1 || args.length == 3) {
					return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
				} else if (args.length == 2) {
					return TabCompleteHandler.getPartialMatches(args[1], "NORTH", "EAST", "SOUTH", "WEST", "0", "45", "90", "135", "180", "-45", "-90", "-135");
				}
				return null;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram addflag <hologram> <flag>",
			description = "Add a flag to Hologram.",
			minArgs = 2
	)
	public static class HologramFlagAddSub extends DecentCommand {

		public HologramFlagAddSub() {
			super("addflag");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final EnumFlag flag = Validator.getFlag(args[1], String.format("Flag \"%s\" wasn't found.", args[1]));
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.addFlags(flag);
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}

				Lang.HOLOGRAM_FLAG_ADDED.send(sender, flag.name());
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				if (args.length == 1) {
					return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
				} else if (args.length == 2) {
					return TabCompleteHandler.getPartialMatches(args[1], Arrays.stream(EnumFlag.values())
						.map(EnumFlag::name)
						.collect(Collectors.toList()));
				}
				return null;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram removeflag <hologram> <flag>",
			description = "Remove a flag from Hologram.",
			aliases = {"remflag"},
			minArgs = 2
	)
	public static class HologramFlagRemoveSub extends DecentCommand {

		public HologramFlagRemoveSub() {
			super("removeflag");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final EnumFlag flag = Validator.getFlag(args[1], String.format("Flag \"%s\" wasn't found.", args[1]));
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.removeFlags(flag);
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}

				Lang.HOLOGRAM_FLAG_REMOVED.send(sender, flag.name());
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				if (args.length == 1) {
					return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
				} else if (args.length == 2) {
					return TabCompleteHandler.getPartialMatches(args[1], Arrays.stream(EnumFlag.values())
						.map(EnumFlag::name)
						.collect(Collectors.toList()));
				}
				return null;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram help",
			description = "Show help for holograms",
			aliases = {"?"}
	)
	public static class HologramHelpSub extends DecentCommand {

		public HologramHelpSub() {
			super("help");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				sender.sendMessage("");
				Common.tell(sender, " &3&lDECENT HOLOGRAMS HELP (HOLOGRAMS)");
				Common.tell(sender, " All commands for editing holograms.");
				sender.sendMessage("");
				CommandBase command = PLUGIN.getCommandManager().getMainCommand().getSubCommand("holograms");
				List<CommandBase> subCommands = Lists.newArrayList(command.getSubCommands());
				for (CommandBase subCommand : subCommands) {
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
			usage = "/dh hologram info <hologram>",
			description = "Show info about a Hologram.",
			minArgs = 1
	)
	public static class HologramInfoSub extends DecentCommand {

		public HologramInfoSub() {
			super("info");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				sender.sendMessage("");
				Common.tell(sender, " &3&lHOLOGRAM INFO");
				Common.tell(sender, " &fInformation about hologram.");
				sender.sendMessage("");
				Common.tell(sender, " &8• &7Name: &b%s", hologram.getName());
				for (String s : Lang.getHologramInfo(hologram)) {
					Common.tell(sender, s);
				}
				sender.sendMessage("");
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram lines <hologram> <page> [listPage]",
			description = "Lists all lines in a hologram.",
			aliases = {"line", "l"},
			minArgs = 2
	)
	public static class HologramLinesSub extends DecentCommand {

		public HologramLinesSub() {
			super("lines");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0]);
				if (hologram == null) {
					Lang.HOLOGRAM_DOES_NOT_EXIST.send(sender);
					return true;
				}
				int pageIndex = Validator.getInteger(args[1]);
				HologramPage page = Validator.getHologramPage(hologram, pageIndex);
				if (page == null) {
					Lang.HOLOGRAM_DOES_NOT_EXIST.send(sender);
					return true;
				}

				sender.sendMessage("");
				Common.tell(sender, " &3&lHOLOGRAM LINES");
				Common.tell(sender, " &fLines in a page.");
				sender.sendMessage("");

				final int itemsPerPage = 15;
				final int itemsTotal = page.size();
				final int maxPage = itemsTotal % itemsPerPage == 0 ? itemsTotal / itemsPerPage - 1 : itemsTotal / itemsPerPage;
				int currentPage = args.length >= 3 ? Validator.getInteger(args[2], "Page must be a valid integer.") - 1 : 0;
				if (currentPage > maxPage) currentPage = maxPage;
				final int startIndex = currentPage * itemsPerPage;
				final int endIndex = Math.min(startIndex + itemsPerPage, itemsTotal);
				final String itemFormat = "   %d. %s";

				for (int i = startIndex; i < endIndex; i++) {
					HologramLine line = page.getLine(i);
					if (Validator.isPlayer(sender)) {
						String suggest = String.format("/dh l set %s %s %s", args[0], i + 1, line.getContent());
						String message = String.format(itemFormat, i + 1, line.getContent());
						String hoverFormat = Common.colorize(Lang.LINE_EDIT_HOVER.getValue().replace("{prefix}", Common.PREFIX));
						String hover = String.format(hoverFormat, suggest);
						Message.sendHoverSuggest((Player) sender, message, hover, suggest);
					} else {
						sender.sendMessage(String.format(itemFormat, i + 1, line.getContent()));
					}
				}

				sender.sendMessage("");
				if (maxPage > 0) {
					((Player) sender).spigot().sendMessage(Message.getPagesComponents(currentPage,
							maxPage == currentPage, "/dh h lines " + args[0] + " " + pageIndex + " %d")
					);
					sender.sendMessage("");
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram movehere <hologram>",
			description = "Move a Hologram to yourself.",
			aliases = {"mvhr"},
			playerOnly = true,
			minArgs = 1
	)
	public static class HologramMovehereSub extends DecentCommand {

		public HologramMovehereSub() {
			super("movehere");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				Player player = Validator.getPlayer(sender);
				Location playerLocation = player.getLocation();
				Location location = hologram.getLocation();
				location.setWorld(playerLocation.getWorld());
				location.setX(playerLocation.getX());
				location.setY(playerLocation.getY());
				location.setZ(playerLocation.getZ());
				hologram.setLocation(location);
				hologram.realignLines();
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}
				
				Lang.HOLOGRAM_MOVED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram move <hologram> <x> <y> <z>",
			description = "Move Hologram to a Location.",
			aliases = {"mv"},
			minArgs = 4
	)
	public static class HologramMoveSub extends DecentCommand {

		public HologramMoveSub() {
			super("move");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				Location location = hologram.getLocation();
				double x = Validator.getLocationValue(args[1], location.getX());
				double y = Validator.getLocationValue(args[2], location.getY());
				double z = Validator.getLocationValue(args[3], location.getZ());
				location.setX(x);
				location.setY(y);
				location.setZ(z);
				hologram.setLocation(location);
				hologram.realignLines();
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}

				Lang.HOLOGRAM_MOVED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				Hologram hologram;
				Location location;
				if (args.length == 1) {
					return TabCompleteHandler.getPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames());
				} else if (args.length == 2 && Validator.isPlayer(sender)) {
					hologram = PLUGIN.getHologramManager().getHologram(args[0]);
					location = hologram == null ? null : hologram.getLocation();
					if (location != null) {
						return Lists.newArrayList(String.valueOf(location.getX()), "~");
					}
					return Lists.newArrayList(String.valueOf(((Player) sender).getLocation().getX()));
				} else if (args.length == 3 && Validator.isPlayer(sender)) {
					hologram = PLUGIN.getHologramManager().getHologram(args[0]);
					location = hologram == null ? null : hologram.getLocation();
					if (location != null) {
						return Lists.newArrayList(String.valueOf(location.getY()), "~");
					}
					return Lists.newArrayList(String.valueOf(((Player) sender).getLocation().getY()));
				} else if (args.length == 4 && Validator.isPlayer(sender)) {
					hologram = PLUGIN.getHologramManager().getHologram(args[0]);
					location = hologram == null ? null : hologram.getLocation();
					if (location != null) {
						return Lists.newArrayList(String.valueOf(location.getZ()), "~");
					}
					return Lists.newArrayList(String.valueOf(((Player) sender).getLocation().getZ()));
				}
				return null;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram near <range>",
			description = "List of holograms near you.",
			playerOnly = true,
			minArgs = 1
	)
	public static class HologramNearSub extends DecentCommand {

		public HologramNearSub() {
			super("near");
		}

		@Override
		public int getMinArgs() {
			return 1;
		}

		@Override
		public String getUsage() {
			return "/dh hologram near <range>";
		}

		@Override
		public String getDescription() {
			return "List of holograms near you.";
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Player player = (Player) sender;
				int range = Validator.getIntegerInRange(
						Validator.getInteger(args[0], "Range must be a valid integer."),
						1, 1000,
						"Range must be a valid integer between 1 and 1000."
				);
				Location playerLocation = player.getLocation();

				List<Hologram> nearHolograms = Lists.newArrayList();
				for (Hologram hologram : PLUGIN.getHologramManager().getHolograms()) {
					Location hologramLocation = hologram.getLocation();
					if (hologramLocation.getWorld().equals(playerLocation.getWorld()) && hologramLocation.distanceSquared(playerLocation) <= (range * range)) {
						nearHolograms.add(hologram);
					}
				}

				if (nearHolograms.isEmpty()) {
					Common.tell(sender, "%sThere are no holograms near you.", Common.PREFIX);
				} else {
					player.sendMessage("");
					Common.tell(player, " &3&lNEAR HOLOGRAMS");
					Common.tell(player, " &fList of holograms near you.");
					player.sendMessage("");
					for (Hologram hologram : nearHolograms) {
						Location loc = hologram.getLocation();
						Common.tell(sender,
								" &8• &7%s &8| &b%s, %.2f, %.2f, %.2f",
								hologram.getName(),
								loc.getWorld().getName(),
								loc.getX(),
								loc.getY(),
								loc.getZ()
						);
					}
					player.sendMessage("");
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
			permission = "dh.admin",
			usage = "/dh hologram setpermission <hologram> [permission]",
			description = "Set hologram permission.",
			aliases = {"permission", "setperm", "perm"},
			minArgs = 1
	)
	public static class HologramPermissionSub extends DecentCommand {

		public HologramPermissionSub() {
			super("setpermission");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				if (args.length >= 2) {
					hologram.setPermission(args[1]);
					Lang.HOLOGRAM_PERMISSION_SET.send(sender, args[1]);
				} else {
					hologram.setPermission(null);
					Lang.HOLOGRAM_PERMISSION_REMOVED.send(sender);
				}
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}
				
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram teleport <hologram>",
			description = "Teleport to a Hologram.",
			playerOnly = true,
			aliases = {"tp", "tele"},
			minArgs = 1
	)
	public static class HologramTeleportSub extends DecentCommand {

		public HologramTeleportSub() {
			super("teleport");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				Player player = Validator.getPlayer(sender);
				player.teleport(hologram.getLocation());

				Lang.HOLOGRAM_TELEPORTED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram setupdateinterval <hologram> <range>",
			description = "Set update interval of a hologram.",
			aliases = {"updateinterval"},
			minArgs = 2
	)
	public static class HologramUpdateIntervalSub extends DecentCommand {

		public HologramUpdateIntervalSub() {
			super("setupdateinterval");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final int interval = Validator.getInteger(args[1], 1, 1200, "Interval must be a valid number between 1 and 1200.");
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.setUpdateInterval(interval);
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}
				
				Lang.HOLOGRAM_UPDATE_INTERVAL_SET.send(sender, interval);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram setupdaterange <hologram> <range>",
			description = "Set update range of a hologram.",
			aliases = {"updaterange"},
			minArgs = 2
	)
	public static class HologramUpdateRangeSub extends DecentCommand {

		public HologramUpdateRangeSub() {
			super("setupdaterange");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final int range = Validator.getInteger(args[1], 1, 64, "Range must be a valid number between 1 and 64.");
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.setUpdateRange(range);
				
				if (!hologram.save()) {
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}
				
				Lang.HOLOGRAM_UPDATE_RANGE_SET.send(sender, range);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh hologram rename <hologram> <new_name>",
			description = "Rename a hologram.",
			minArgs = 2
	)
	public static class HologramRenameSub extends DecentCommand {

		public HologramRenameSub() {
			super("rename");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram oldHologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());

				String oldName = oldHologram.getName();
				String newName = args[1];

				if (Hologram.getCachedHologramNames().contains(newName)) {
					Lang.HOLOGRAM_ALREADY_EXISTS.send(sender, newName);
					return false;
				}

				// Create a new hologram, with the new name
				Hologram newHologram = oldHologram.clone(newName, oldHologram.getLocation(), false);

				if (!newHologram.save()) {
					newHologram.delete();
					Lang.HOLOGRAM_SAVE_FAILED.send(sender);
					return false;
				}

				PLUGIN.getHologramManager().registerHologram(newHologram);
				newHologram.showAll();

				// Delete the old hologram
				oldHologram.delete();

				Lang.HOLOGRAM_RENAMED.send(sender, oldName, newName);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

}

package eu.decentsoftware.holograms.plugin.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.commands.CommandBase;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@CommandInfo(
		permissions = "dh.command.holograms",
		usage = "/dh holograms help",
		description = "所有用于编辑悬浮字的命令。",
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
            permissions = "dh.command.holograms.update",
            usage = "/dh hologram update <hologram>",
            description = "更新悬浮字。",
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
			permissions = "dh.command.holograms.align",
			usage = "/dh hologram align <hologram> <X|Y|Z|XZ|FACE> <otherHologram>",
			description = "将悬浮字与其他悬浮字在指定轴上对齐或调整其朝向角度。",
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
				hologram.save();
				
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
			permissions = "dh.command.holograms.center",
			usage = "/dh hologram center <hologram>",
			description = "将悬浮字移动到方块中央。",
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
				hologram.save();

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
			permissions = "dh.command.holograms.clone",
			usage = "/dh hologram clone <hologram> <name> [temp] [-l:<world:x:y:z>]",
			description = "克隆现有的悬浮字。",
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
				String cloneHologramName = args[1];
				if (!cloneHologramName.matches(Common.NAME_REGEX)) {
					Lang.HOLOGRAM_INVALID_NAME.send(sender, cloneHologramName);
					return true;
				}
				if (Hologram.getCachedHologramNames().contains(cloneHologramName)) {
					Lang.HOLOGRAM_ALREADY_EXISTS.send(sender, cloneHologramName);
					return true;
				}

				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				boolean containsLocation = false;
				boolean temp = false;
				Location location = null;
				if (args.length >= 3) {
					for (int i = 2; i < args.length; i++) {
						String s = args[i];
						if (s.toLowerCase().startsWith("-l:")) {
							String locationString = s.substring(3);
							location = LocationUtils.asLocation(locationString);
							if (location != null) {
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
					if (location == null) {
						final Player player = (Player) sender;
						location = Settings.HOLOGRAMS_EYE_LEVEL_POSITIONING ? player.getEyeLocation() : player.getLocation();
					}
				}

				final Hologram clone = hologram.clone(cloneHologramName, location, temp);
				clone.save();
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
			permissions = "dh.command.holograms.create",
			usage = "/dh hologram create <name> [-l:world:x:y:z] [--center] [content]",
			description = "创建新的悬浮字。",
			aliases = {"new", "c"},
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
				if (!hologramName.matches(Common.NAME_REGEX)) {
					Lang.HOLOGRAM_INVALID_NAME.send(sender, hologramName);
					return true;
				}
				if (PLUGIN.getHologramManager().containsHologram(hologramName)) {
					Lang.HOLOGRAM_ALREADY_EXISTS.send(sender, hologramName);
					return true;
				}
				
				Location location = null;
				boolean centerHologram = false;
				
				List<String> contentArgs = new ArrayList<>();
				
				// Iterate through the args to find -l:<world>:<x>:<y>:<z> and/or --center.
				// If found, set values and skip arg, else add arg to content List.
				for (int i = 1; i < args.length; i++) {
					if (args[i].toLowerCase(Locale.ROOT).startsWith("-l:")) {
						String locationString = args[i].substring(3);
						location = LocationUtils.asLocation(locationString);
						
						// Valid Location, skip this arg.
						if (location != null)
							continue;
					} else if (args[i].equalsIgnoreCase("--center")) {
						centerHologram = true;
						continue;
					}
					
					contentArgs.add(args[i]);
				}
				
				if (!(sender instanceof Player) && location == null) {
					Lang.ONLY_PLAYER.send(sender);
					return true;
				} else {
					if (location == null) {
						final Player player = (Player) sender;
						location = Settings.HOLOGRAMS_EYE_LEVEL_POSITIONING ? player.getEyeLocation() : player.getLocation();
					}
					
					if (centerHologram) {
						int x = (int) location.getX();
						int z = (int) location.getZ();
						location.setX(x > location.getX() ? x - 0.5d : x + 0.5d);
						location.setZ(z > location.getZ() ? z - 0.5d : z + 0.5d);
					}
				}

				// Get the content of the line.
				final String content = Validator.getLineContent(contentArgs.toArray(new String[0]), 0);
				// Create the hologram.
				final Hologram hologram = new Hologram(hologramName, location);
				// Add the first line to the hologram.
				final HologramPage page = hologram.getPage(0);
				final HologramLine line = new HologramLine(page, page.getNextLineLocation(), content);
				page.addLine(line);

				hologram.showAll();
				hologram.save();

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
			permissions = "dh.command.holograms.delete",
			usage = "/dh hologram delete <hologram>",
			description = "删除悬浮字。",
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
			permissions = "dh.command.holograms.disable",
			usage = "/dh hologram disable <hologram>",
			description = "禁用悬浮字。",
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
				hologram.save();

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
			permissions = "dh.command.holograms.setdisplayrange",
			usage = "/dh hologram setdisplayrange <hologram> <range>",
			description = "设置悬浮字的显示范围。",
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
				final int range = Validator.getInteger(args[1], 1, 64, "范围必须是1到64之间的有效数字。");
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.setDisplayRange(range);
				hologram.save();
				
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
			permissions = "dh.command.holograms.downorigin",
			usage = "/dh hologram downorigin <hologram> <true|false>",
			description = "设置悬浮字的下坐标原点状态。",
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
				hologram.save();
				
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
			permissions = "dh.command.holograms.enable",
			usage = "/dh hologram enable <hologram>",
			description = "启用悬浮字。",
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
				hologram.save();

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
			permissions = "dh.command.holograms.setfacing",
			usage = "/dh hologram setfacing <hologram> <facing>",
			description = "设置悬浮字的朝向。",
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
						facing = Validator.getFloat(args[1], -180.0f, 180.0f, "朝向必须是-180到180之间的有效数字。");
						break;
				}
				hologram.setFacing(facing);
				hologram.save();
				
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
			permissions = "dh.command.holograms.addflag",
			usage = "/dh hologram addflag <hologram> <flag>",
			description = "为悬浮字添加标志。",
			minArgs = 2
	)
	public static class HologramFlagAddSub extends DecentCommand {

		public HologramFlagAddSub() {
			super("addflag");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final EnumFlag flag = Validator.getFlag(args[1], String.format("标志 \"%s\" 未找到。", args[1]));
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.addFlags(flag);
				hologram.save();

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
			permissions = "dh.command.holograms.removeflag",
			usage = "/dh hologram removeflag <hologram> <flag>",
			description = "从悬浮字移除标志。",
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
				final EnumFlag flag = Validator.getFlag(args[1], String.format("标志 \"%s\" 未找到。", args[1]));
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.removeFlags(flag);
				hologram.save();

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
			permissions = "dh.command.holograms.help",
			usage = "/dh hologram help",
			description = "显示悬浮字相关帮助",
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
				Common.tell(sender, " &3&l悬浮字帮助");
				Common.tell(sender, " 所有用于编辑悬浮字的命令。");
				sender.sendMessage("");
				CommandBase command = PLUGIN.getCommandManager().getMainCommand().getSubCommand("holograms");
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
			permissions = "dh.command.holograms.info",
			usage = "/dh hologram info <hologram>",
			description = "显示悬浮字的相关信息。",
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
				Common.tell(sender, " &3&l悬浮字信息");
				Common.tell(sender, " &f悬浮字的相关信息。");
				sender.sendMessage("");
				Common.tell(sender, " &8• &7名称: &b%s", hologram.getName());
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
			permissions = "dh.command.holograms.lines",
			usage = "/dh hologram lines <hologram> <page> [listPage]",
			description = "列出悬浮字中的所有行。",
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
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				int pageIndex = Validator.getInteger(args[1]);
				HologramPage page = Validator.getHologramPage(hologram, pageIndex);
				if (page == null) {
					Lang.HOLOGRAM_DOES_NOT_EXIST.send(sender);
					return true;
				}

				sender.sendMessage("");
				Common.tell(sender, " &3&l悬浮字行列表");
				Common.tell(sender, " &f页面中的所有行。");
				sender.sendMessage("");

				final int itemsPerPage = 15;
				final int itemsTotal = page.size();
				final int maxPage = itemsTotal % itemsPerPage == 0 ? itemsTotal / itemsPerPage - 1 : itemsTotal / itemsPerPage;
				int currentPage = args.length >= 3 ? Validator.getInteger(args[2], "页码必须是有效的整数。") - 1 : 0;
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
			permissions = "dh.command.holograms.movehere",
			usage = "/dh hologram movehere <hologram>",
			description = "将悬浮字移动到自己位置。",
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
				Location playerLocation = Settings.HOLOGRAMS_EYE_LEVEL_POSITIONING ? player.getEyeLocation() : player.getLocation();
				Location location = hologram.getLocation();
				location.setWorld(playerLocation.getWorld());
				location.setX(playerLocation.getX());
				location.setY(playerLocation.getY());
				location.setZ(playerLocation.getZ());
				hologram.setLocation(location);
				hologram.realignLines();
				hologram.save();
				
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
			permissions = "dh.command.holograms.move",
			usage = "/dh hologram move <hologram> <x> <y> <z>",
			description = "将悬浮字移动到指定位置。",
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
				hologram.save();

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
			permissions = "dh.command.holograms.near",
			usage = "/dh hologram near <range>",
			description = "列出您附近的悬浮字。",
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
			return "列出你附近的悬浮字。";
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Player player = (Player) sender;
				int range = Validator.getIntegerInRange(
						Validator.getInteger(args[0], "范围必须是有效的整数。"),
						1, 1000,
						"范围必须是1到1000之间的有效整数。"
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
					Common.tell(player, " &3&l附近的悬浮字");
					Common.tell(player, " &f你附近的悬浮字列表。");
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
			permissions = "dh.command.holograms.setpermission",
			usage = "/dh hologram setpermission <hologram> [permission]",
			description = "设置悬浮字权限。",
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
				hologram.save();
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return TabCompleteHandler.HOLOGRAM_NAMES;
		}

	}

	@CommandInfo(
			permissions = "dh.command.holograms.teleport",
			usage = "/dh hologram teleport <hologram>",
			description = "传送到悬浮字位置。",
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
			permissions = "dh.command.holograms.setupdateinterval",
			usage = "/dh hologram setupdateinterval <hologram> <interval>",
			description = "设置悬浮字的更新间隔。",
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
				final int interval = Validator.getInteger(args[1], 1, 1200, "间隔必须是1到1200之间的有效数字。");
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.setUpdateInterval(interval);
				hologram.save();
				
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
			permissions = "dh.command.holograms.setupdaterange",
			usage = "/dh hologram setupdaterange <hologram> <range>",
			description = "设置悬浮字的更新范围。",
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
				final int range = Validator.getInteger(args[1], 1, 64, "范围必须是1到64之间的有效数字。");
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				hologram.setUpdateRange(range);
				hologram.save();
				
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
			permissions = "dh.command.holograms.rename",
			usage = "/dh hologram rename <hologram> <new_name>",
			description = "重命名悬浮字。",
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
				newHologram.save();

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

package eu.decentsoftware.holograms.plugin.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.commands.*;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.api.utils.items.DecentMaterial;
import eu.decentsoftware.holograms.api.utils.message.Message;
import eu.decentsoftware.holograms.plugin.Validator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandInfo(
		permission = "dh.admin",
		usage = "/dh lines help",
		description = "All commands for editing hologram lines.",
		aliases = {"line", "l"}
)
public class LineSubCommand extends DecentCommand {

	public LineSubCommand() {
		super("lines");

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
		addSubCommand(new LineFacingSub());
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
			usage = "/dh line add <hologram> <page> [content]",
			description = "Add a line to Hologram.",
			aliases = {"append"},
			playerOnly = true,
			minArgs = 2
	)
	static class LineAddSub extends DecentCommand {

		public LineAddSub() {
			super("add");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				String content = args.length > 2 ? sender instanceof Player ? Validator.getLineContent((Player) sender, args, 2) : Validator.getLineContent(args, 2) : Settings.DEFAULT_TEXT.getValue();
				HologramLine line = new HologramLine(page, page.getNextLineLocation(), content);
				if (page.addLine(line)) {
					hologram.save();
					Lang.LINE_ADDED.send(sender);
				} else {
					Lang.LINE_ADD_FAILED.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length >= 3) {
					putContent(Arrays.copyOfRange(args, 2, args.length), matches);
				}
				return matches;
			};
		}
	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line align <hologram> <page> <line1> <line2> <X|Z|XZ>",
			description = "Align two lines in hologram on a specified axis.",
			minArgs = 5
	)
	static class LineAlignSub extends DecentCommand {

		public LineAlignSub() {
			super("align");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				final int index1 = Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue());
				final int index2 = Validator.getInteger(args[3], Lang.LINE_DOES_NOT_EXIST.getValue());
				final String axis = args[4];
				if (index1 == index2) {
					Lang.LINE_ALIGN_SELF.send(sender);
					return true;
				}
				HologramLine line1 = Validator.getHologramLine(page, index1);
				HologramLine line2 = Validator.getHologramLine(page, index2);
				switch (axis) {
					case "X":
						line1.setOffsetX(line2.getOffsetX());
						break;
					case "Z":
						line1.setOffsetZ(line2.getOffsetZ());
						break;
					case "XZ":
					case "ZX":
						line1.setOffsetX(line2.getOffsetX());
						line1.setOffsetZ(line2.getOffsetZ());
						break;
					default:
						Lang.LINE_ALIGN_AXIS.send(sender);
						return true;
				}
				hologram.realignLines();
				hologram.save();
				Lang.LINE_ALIGNED.send(sender);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				} else if (args.length == 4) {
					putLines(args[0], Validator.getInteger(args[1]), args[3], matches);
				} else if (args.length == 5) {
					StringUtil.copyPartialMatches(args[2], Arrays.asList("X", "Z", "XZ"), matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line edit <hologram> <page> <line>",
			description = "Edit a line.",
			aliases = {"e"},
			minArgs = 3
	)
	static class LineEditSub extends DecentCommand {

		public LineEditSub() {
			super("edit");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				HologramLine line = Validator.getHologramLine(page, Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue()));

				String suggest = String.format("/dh l set %s %s %s %s", args[0], args[1], args[2], line.getContent());
				String message = Common.colorize(Lang.LINE_EDIT.getValue().replace("{prefix}", Common.PREFIX));
				String hoverFormat = Common.colorize(Lang.LINE_EDIT_HOVER.getValue().replace("{prefix}", Common.PREFIX));
				String hover = String.format(hoverFormat, suggest);
				sender.sendMessage("");
				Message.sendHoverSuggest((Player) sender, message, hover, suggest);
				sender.sendMessage("");
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line addflag <hologram> <page> <line> <flag>",
			description = "Add a flag to line.",
			minArgs = 4
	)
	static class LineFlagAddSub extends DecentCommand {

		public LineFlagAddSub() {
			super("addflag");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				HologramLine line = Validator.getHologramLine(page, Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue()));

				EnumFlag flag = Validator.getFlag(args[3], String.format("%s&cFlag \"%s\" wasn't found.", Common.PREFIX, args[2]));
				if (line != null) {
					line.addFlags(flag);
					hologram.save();
					Lang.LINE_FLAG_ADDED.send(sender, flag.name());
				} else {
					Lang.LINE_DOES_NOT_EXIST.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				} else if (args.length == 4) {
					putFlags(args[3], matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line removeflag <hologram> <page> <line> <flag>",
			description = "Remove a flag from line.",
			minArgs = 4
	)
	static class LineFlagRemoveSub extends DecentCommand {

		public LineFlagRemoveSub() {
			super("removeflag");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				HologramLine line = Validator.getHologramLine(page, Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue()));

				EnumFlag flag = Validator.getFlag(args[3], String.format("%s&cFlag \"%s\" wasn't found.", Common.PREFIX, args[2]));
				if (line != null) {
					line.removeFlags(flag);
					hologram.save();
					Lang.LINE_FLAG_REMOVED.send(sender, flag.name());
				} else {
					Lang.LINE_DOES_NOT_EXIST.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				} else if (args.length == 4) {
					putFlags(args[3], matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line height <hologram> <page> <line> <height>",
			description = "Set height of a line.",
			aliases = {"setheight"},
			minArgs = 4
	)
	static class LineHeightSub extends DecentCommand {

		public LineHeightSub() {
			super("height");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				HologramLine line = Validator.getHologramLine(page, Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue()));
				if (line != null) {
					line.setHeight(Validator.getDouble(args[3], 0.0D, 2.5D,
							String.format("Height must be a valid number in range. (Min: %.2f, Max: %.2f)", 0.0D, 2.5D)
					));
					hologram.realignLines();
					hologram.save();

					Lang.LINE_HEIGHT_SET.send(sender);
				} else {
					Lang.LINE_DOES_NOT_EXIST.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line help",
			description = "Show help for lines.",
			aliases = {"?"}
	)
	static class LineHelpSub extends DecentCommand {

		public LineHelpSub() {
			super("help");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				sender.sendMessage("");
				Common.tell(sender, " &3&lDECENT HOLOGRAMS HELP (LINES)");
				Common.tell(sender, " All commands for editing hologram lines.");
				sender.sendMessage("");
				CommandBase command = PLUGIN.getCommandManager().getMainCommand().getSubCommand("lines");
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
			usage = "/dh line info <hologram> <page> <line>",
			description = "Show info about line.",
			minArgs = 3
	)
	static class LineInfoSub extends DecentCommand {

		public LineInfoSub() {
			super("info");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				int index = Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue());
				HologramLine line = Validator.getHologramLine(page, index);
				Location loc = line.getLocation();

				sender.sendMessage("");
				Common.tell(sender, " &3&lHOLOGRAM LINE INFO");
				Common.tell(sender, " General information about a line");
				sender.sendMessage("");
				Common.tell(sender, " &8• &7Hologram: &b%s", hologram.getName());
				Common.tell(sender, " &8• &7Index: &b%d", index);
				Common.tell(sender, " &8• &7Location: &b%s, %.2f, %.2f, %.2f",
						loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ()
				);
				sender.sendMessage(Common.colorize(" &8• &7Content: &b") + line.getContent());
				Common.tell(sender, " &8• &7Height: &b%f", line.getHeight());
				Common.tell(sender, " &8• &7OffsetX: &b%f", line.getOffsetX());
				Common.tell(sender, " &8• &7OffsetY: &b%f", line.getOffsetY());
				Common.tell(sender, " &8• &7OffsetZ: &b%f", line.getOffsetZ());
				sender.sendMessage("");
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line insert <hologram> <page> <line> [content]",
			description = "Insert a line into Hologram.",
			minArgs = 3
	)
	static class LineInsertSub extends DecentCommand {

		public LineInsertSub() {
			super("insert");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				int index = Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue());
				HologramLine prevLine = Validator.getHologramLine(page, index);
				String content = args.length > 3 ? sender instanceof Player ? Validator.getLineContent((Player) sender, args, 3) : Validator.getLineContent(args, 3) : Settings.DEFAULT_TEXT.getValue();
				HologramLine line = new HologramLine(page, prevLine.getLocation(), content);

				if (page.insertLine(index - 1, line)) {
					hologram.save();
					Lang.LINE_INSERTED.send(sender);
				} else {
					Lang.LINE_INSERT_FAILED.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = Lists.newArrayList();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				} else if (args.length >= 4) {
					putContent(Arrays.copyOfRange(args, 3, args.length), matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line offsetX <hologram> <page> <line> <offset>",
			description = "Set an X offset of a line.",
			aliases = {"xoffset", "offx", "xoff"},
			minArgs = 4
	)
	static class LineOffsetXSub extends DecentCommand {

		public LineOffsetXSub() {
			super("offsetx");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				HologramLine line = Validator.getHologramLine(page, Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue()));
				if (line != null) {
					line.setOffsetX(Validator.getDouble(args[3], -2.5D, 2.5D,
							String.format("OffsetX must be a valid number in range. (Min: %.2f, Max: %.2f)", -2.5D, 2.5D)
					));
					page.realignLines();
					hologram.save();
					Lang.LINE_OFFSETX_SET.send(sender);
				} else {
					Lang.LINE_DOES_NOT_EXIST.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line offsetZ <hologram> <page> <line> <offset>",
			description = "Set an Z offset of a line.",
			aliases = {"zoffset", "offz", "zoff"},
			minArgs = 4
	)
	static class LineOffsetZSub extends DecentCommand {

		public LineOffsetZSub() {
			super("offsetz");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				HologramLine line = Validator.getHologramLine(page, Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue()));
				if (line != null) {
					line.setOffsetZ(Validator.getDouble(args[3], -2.5D, 2.5D,
							String.format("OffsetZ must be a valid number in range. (Min: %.2f, Max: %.2f)", -2.5D, 2.5D)
					));
					page.realignLines();
					hologram.save();
					Lang.LINE_OFFSETZ_SET.send(sender);
				} else {
					Lang.LINE_DOES_NOT_EXIST.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line setpermission <hologram> <page> <line> [permission]",
			description = "Set line permission.",
			aliases = {"permission", "setperm", "perm"},
			minArgs = 3
	)
	static class LinePermissionSub extends DecentCommand {

		public LinePermissionSub() {
			super("setpermission");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				HologramLine line = Validator.getHologramLine(page, Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue()));
				if (line != null) {
					if (args.length >= 4) {
						line.setPermission(args[3]);
						Lang.LINE_PERMISSION_SET.send(sender, args[3]);
					} else {
						line.setPermission(null);
						Lang.LINE_PERMISSION_REMOVED.send(sender);
					}
					hologram.save();
				} else {
					Lang.LINE_DOES_NOT_EXIST.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line remove <hologram> <page> <line>",
			description = "Remove a line from Hologram.",
			aliases = {"rem", "del", "delete"},
			minArgs = 3
	)
	static class LineRemoveSub extends DecentCommand {

		public LineRemoveSub() {
			super("remove");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				int pageIndex = Validator.getInteger(args[1], 1, hologram.size(), Lang.PAGE_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, pageIndex, Lang.PAGE_DOES_NOT_EXIST.getValue());
				final int index = Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue());
				page.removeLine(index - 1);
				if (page.size() == 0) {
					hologram.removePage(pageIndex);
					Lang.LINE_REMOVED.send(sender);
					Lang.PAGE_DELETED.send(sender);
				} else {
					hologram.save();
					Lang.LINE_REMOVED.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line set <hologram> <page> <line> <content>",
			description = "Set a line in Hologram.",
			minArgs = 4
	)
	static class LineSetSub extends DecentCommand {

		public LineSetSub() {
			super("set");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				int index = Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue());
				String content = sender instanceof Player ? Validator.getLineContent((Player) sender, args, 3) : Validator.getLineContent(args, 3);
				if (page.setLine(index - 1, content)) {
					hologram.save();
					Lang.LINE_SET.send(sender);
				} else {
					Lang.LINE_DOES_NOT_EXIST.send(sender, index);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				} else if (args.length >= 4) {
					putContent(Arrays.copyOfRange(args, 3, args.length), matches);
				}
				return matches;
			};
		}

	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line swap <hologram> <page> <line1> <line2>",
			description = "Swap two lines in a Hologram.",
			minArgs = 4
	)
	static class LineSwapSub extends DecentCommand {

		public LineSwapSub() {
			super("swap");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				int index1 = Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue());
				int index2 = Validator.getInteger(args[3], Lang.LINE_DOES_NOT_EXIST.getValue());
				if (index1 == index2) {
					Lang.LINE_SWAP_SELF.send(sender);
					return true;
				}
				Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				if (page.swapLines(index1 - 1, index2 - 1)) {
					hologram.save();
					Lang.LINE_SWAPPED.send(sender);
				} else {
					Lang.LINE_SWAP_FAILED.send(sender);
				}
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> {
				List<String> matches = new ArrayList<>();
				if (args.length == 1) {
					putHologramNames(args[0], matches);
				} else if (args.length == 2) {
					putPages(args[0], args[1], matches);
				} else if (args.length == 3) {
					putLines(args[0], Validator.getInteger(args[1]), args[2], matches);
				} else if (args.length == 4) {
					putLines(args[0], Validator.getInteger(args[1]), args[3], matches);
				}
				return matches;
			};
		}
	}

	@CommandInfo(
			permission = "dh.admin",
			usage = "/dh line setfacing <hologram> <page> <line> <facing>",
			description = "Set facing direction of a line.",
			aliases = {"facing", "setface", "face"},
			minArgs = 4
	)
	public static class LineFacingSub extends DecentCommand {

		public LineFacingSub() {
			super("setfacing");
		}

		@Override
		public CommandHandler getCommandHandler() {
			return (sender, args) -> {
				final Hologram hologram = Validator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
				final HologramPage page = Validator.getHologramPage(hologram, args[1], Lang.PAGE_DOES_NOT_EXIST.getValue());
				final HologramLine line = Validator.getHologramLine(page, Validator.getInteger(args[2], Lang.LINE_DOES_NOT_EXIST.getValue()));
				float facing;
				switch (args[3].toUpperCase()) {
					case "SOUTH": facing = 0.0f; break;
					case "WEST": facing = 90.0f; break;
					case "NORTH": facing = 180.0f; break;
					case "EAST": facing = -90.0f; break;
					default:
						facing = Validator.getFloat(args[3], -180.0f, 180.0f, "Facing must be a valid number between -180 and 180.");
						break;
				}
				line.setFacing(facing);
				page.realignLines();
				hologram.save();
				Lang.LINE_FACING_SET.send(sender, facing);
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return null;
		}
	}

	/*
	 *	Utility Methods
	 */

	protected static void putHologramNames(String token, Collection<String> collection) {
		StringUtil.copyPartialMatches(token, PLUGIN.getHologramManager().getHologramNames(), collection);
	}

	protected static void putContent(String[] args, Collection<String> collection) {
		if (args.length == 1 && args[0].toLowerCase(Locale.ROOT).startsWith("#")) {
			StringUtil.copyPartialMatches(
				args[0],
				Arrays.asList("#ICON: ", "#HEAD: ", "#SMALLHEAD: ", "#ENTITY: "), 
				collection
			);
		} else if (args.length == 2) {
			switch(args[0].toUpperCase(Locale.ROOT)) {
				case "#ICON:":
				case "#HEAD:":
				case "#SMALLHEAD:":
					StringUtil.copyPartialMatches(
						args[1],
						Arrays.stream(Material.values())
							.filter(DecentMaterial::isItem)
							.map(Material::name)
							.collect(Collectors.toList()),
						collection
					);
					break;
				
				case "#ENTITY:":
					StringUtil.copyPartialMatches(args[1], DecentEntityType.getAllowedEntityTypeNames(), collection);
			}
		} else if (args.length >= 3) {
			String item = args[1].toUpperCase(Locale.ROOT);
			if (args[2].startsWith("(") && args.length == 3 && (item.contains("HEAD") || item.contains("SKULL"))) {
				List<String> names = Bukkit.getOnlinePlayers().stream()
					.map(player -> "(" + player.getName() + ")")
					.collect(Collectors.toList());
				
				if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					names.add("(%player_name%)");
				}
				
				if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
					names.add("(HEADDATABASE_<id>)");
				}
				
				StringUtil.copyPartialMatches(args[2], names, collection);
				return;
			}
			
			String lastArg = args[args.length - 1];
			if (lastArg.startsWith("!")) {
				collection.add("!ENCHANTED");
			}
		}
	}

	protected static void putPages(String hologramName, String token, Collection<String> collection) {
		Hologram hologram = PLUGIN.getHologramManager().getHologram(hologramName);
		if (hologram != null) {
			StringUtil.copyPartialMatches(token, IntStream
					.rangeClosed(1, hologram.size())
					.boxed().map(String::valueOf)
					.collect(Collectors.toList()), collection);
		}
	}

	protected static void putLines(String hologramName, int pageIndex, String token, Collection<String> collection) {
		Hologram hologram = PLUGIN.getHologramManager().getHologram(hologramName);
		if (hologram == null) return;
		HologramPage page = hologram.getPage(pageIndex - 1);
		if (page != null) {
			StringUtil.copyPartialMatches(token, IntStream
					.rangeClosed(1, page.size())
					.boxed().map(String::valueOf)
					.collect(Collectors.toList()), collection);
		}
	}

	protected static void putFlags(String token, Collection<String> collection) {
		StringUtil.copyPartialMatches(token, Arrays.stream(EnumFlag.values()).map(Enum::name).collect(Collectors.toList()), collection);
	}

}
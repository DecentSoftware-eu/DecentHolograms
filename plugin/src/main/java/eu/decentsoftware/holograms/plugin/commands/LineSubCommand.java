package eu.decentsoftware.holograms.plugin.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.commands.CommandBase;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandInfo(
		permissions = "dh.command.lines",
		usage = "/dh lines help",
		description = "所有用于编辑悬浮字行的命令。",
		aliases = {"line", "l"}
)
public class LineSubCommand extends DecentCommand {
	
	private static final List<String> items = Arrays.stream(Material.values())
		.filter(DecentMaterial::isItem)
		.map(Material::name)
		.collect(Collectors.toList());
	
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
			permissions = "dh.command.lines.add",
			usage = "/dh line add <hologram> <page> [content]",
			description = "向悬浮字添加一行。",
			aliases = {"append"},
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
				String content = args.length > 2 ? sender instanceof Player ? Validator.getLineContent((Player) sender, args, 2) : Validator.getLineContent(args, 2) : Settings.DEFAULT_TEXT;
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
				if (args.length <= 2) {
					return handleCommonArgs(args);
				} else {
					return getContent(Arrays.copyOfRange(args, 2, args.length));
				}
			};
		}
	}

	@CommandInfo(
			permissions = "dh.command.lines.align",
			usage = "/dh line align <hologram> <page> <line1> <line2> <X|Z|XZ|FACE>",
			description = "在指定轴或朝向角度上对齐悬浮字中的两行。",
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
					case "FACE":
					case "FACING":
						line1.setFacing(line2.getFacing());
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
				if (args.length <= 3) {
					return handleCommonArgs(args);
				} else if (args.length == 4) {
					return getLines(args[0], Validator.getInteger(args[1]), args[3]);
				} else if (args.length == 5) {
					return TabCompleteHandler.getPartialMatches(args[4], "X", "Z", "XZ");
				}
				
				return Collections.emptyList();
			};
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.edit",
			usage = "/dh line edit <hologram> <page> <line>",
			description = "编辑行内容。",
			aliases = {"e"},
			playerOnly = true,
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
			return (sender, args) -> handleCommonArgs(args);
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.addflag",
			usage = "/dh line addflag <hologram> <page> <line> <flag>",
			description = "为行添加标志。",
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

				EnumFlag flag = Validator.getFlag(args[3], String.format("%s&c标志 \"%s\" 未找到。", Common.PREFIX, args[2]));
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
				if (args.length <= 3) {
					return handleCommonArgs(args);
				} else {
					return getFlags(args[3]);
				}
			};
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.removeflag",
			usage = "/dh line removeflag <hologram> <page> <line> <flag>",
			description = "从行中移除标志。",
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

				EnumFlag flag = Validator.getFlag(args[3], String.format("%s&c标志 \"%s\" 未找到。", Common.PREFIX, args[2]));
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
				if (args.length <= 3) {
					return handleCommonArgs(args);
				} else {
					return getFlags(args[3]);
				}
			};
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.height",
			usage = "/dh line height <hologram> <page> <line> <height>",
			description = "设置行的高度。",
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
							String.format("高度必须是有效范围内的数字。(最小值: %.2f, 最大值: %.2f)", 0.0D, 2.5D)
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
				if (args.length <= 3) {
					return handleCommonArgs(args);
				}
				
				return Collections.emptyList();
			};
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.help",
			usage = "/dh line help",
			description = "显示行命令的帮助。",
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
				Common.tell(sender, " &3&l悬浮字帮助 (行命令)");
				Common.tell(sender, " 所有用于编辑悬浮字行的命令。");
				sender.sendMessage("");
				CommandBase command = PLUGIN.getCommandManager().getMainCommand().getSubCommand("lines");
				List<CommandBase> subCommands = Lists.newArrayList(command.getSubCommands());
				for (CommandBase subCommand : subCommands) {
					Common.tell(sender, " &8• &b%s &8- &7%s", subCommand.getUsage(), subCommand.getDescription());
				}
				sender.sendMessage("");
				Common.tell(sender, " &7别名: &b%s%s",
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
			permissions = "dh.command.lines.info",
			usage = "/dh line info <hologram> <page> <line>",
			description = "显示行的信息。",
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
				Common.tell(sender, " &3&l悬浮字行信息");
				Common.tell(sender, " 关于行的一般信息");
				sender.sendMessage("");
				Common.tell(sender, " &8• &7悬浮字: &b%s", hologram.getName());
				Common.tell(sender, " &8• &7索引: &b%d", index);
				Common.tell(sender, " &8• &7位置: &b%s, %.2f, %.2f, %.2f",
						loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ()
				);
				Common.tell(sender, " &8• &7内容: &b%s", line.getContent());
				Common.tell(sender, " &8• &7高度: &b%f", line.getHeight());
				Common.tell(sender, " &8• &7X轴偏移: &b%f", line.getOffsetX());
				Common.tell(sender, " &8• &7Y轴偏移: &b%f", line.getOffsetY());
				Common.tell(sender, " &8• &7Z轴偏移: &b%f", line.getOffsetZ());
				sender.sendMessage("");
				return true;
			};
		}

		@Override
		public TabCompleteHandler getTabCompleteHandler() {
			return (sender, args) -> handleCommonArgs(args);
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.insert",
			usage = "/dh line insert <hologram> <page> <line> [content]",
			description = "在悬浮字中插入一行。",
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
				String content = args.length > 3 ? sender instanceof Player ? Validator.getLineContent((Player) sender, args, 3) : Validator.getLineContent(args, 3) : Settings.DEFAULT_TEXT;
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
				if (args.length <= 3) {
					return handleCommonArgs(args);
				} else {
					return getContent(Arrays.copyOfRange(args, 3, args.length));
				}
			};
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.offsetx",
			usage = "/dh line offsetX <hologram> <page> <line> <offset>",
			description = "设置行的X轴偏移。",
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
							String.format("X轴偏移必须是有效范围内的数字。(最小值: %.2f, 最大值: %.2f)", -2.5D, 2.5D)
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
			return (sender, args) -> handleCommonArgs(args);
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.offsetz",
			usage = "/dh line offsetZ <hologram> <page> <line> <offset>",
			description = "设置行的Z轴偏移。",
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
							String.format("Z轴偏移必须是有效范围内的数字。(最小值: %.2f, 最大值: %.2f)", -2.5D, 2.5D)
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
			return (sender, args) -> handleCommonArgs(args);
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.setpermission",
			usage = "/dh line setpermission <hologram> <page> <line> [permission]",
			description = "设置行的权限。",
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
			return (sender, args) -> handleCommonArgs(args);
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.remove",
			usage = "/dh line remove <hologram> <page> <line>",
			description = "从悬浮字中移除一行。",
			aliases = {"rm", "rem", "del", "delete"},
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
					hologram.removePage(pageIndex - 1);
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
			return (sender, args) -> handleCommonArgs(args);
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.set",
			usage = "/dh line set <hologram> <page> <line> <content>",
			description = "设置悬浮字中的行内容。",
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
				if (args.length <= 3) {
					return handleCommonArgs(args);
				} else {
					return getContent(Arrays.copyOfRange(args, 3, args.length));
				}
			};
		}

	}

	@CommandInfo(
			permissions = "dh.command.lines.swap",
			usage = "/dh line swap <hologram> <page> <line1> <line2>",
			description = "交换悬浮字中的两行。",
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
				if (args.length <= 3) {
					return handleCommonArgs(args);
				} else {
					return getLines(args[0], Validator.getInteger(args[1]), args[3]);
				}
			};
		}
	}

	@CommandInfo(
			permissions = "dh.command.lines.setfacing",
			usage = "/dh line setfacing <hologram> <page> <line> <facing>",
			description = "设置行的朝向。",
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
						facing = Validator.getFloat(args[3], -180.0f, 180.0f, "朝向必须是-180到180之间的有效数字。");
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
			return (sender, args) -> {
				if (args.length <= 3) {
					return handleCommonArgs(args);
				} else {
					return TabCompleteHandler.getPartialMatches(args[3], "NORTH", "EAST", "SOUTH", "WEST");
				}
			};
		}
	}

	/*
	 *	Utility Methods
	 */
	
	// Utility method to handle all "/dh <subcommand> <hologram> <page> <line>" stuff
	protected static List<String> handleCommonArgs(String[] args) {
		if (args.length == 1) {
			return getHologramNames(args[0]);
		} else if (args.length == 2) {
			return getPages(args[0], args[1]);
		} else if (args.length == 3) {
			return getLines(args[0], Validator.getInteger(args[1]), args[2]);
		}
		
		return Collections.emptyList();
	}
	
	protected static List<String> getHologramNames(String token) {
		return TabCompleteHandler.getPartialMatches(token, PLUGIN.getHologramManager().getHologramNames());
	}
	
	protected static List<String> getContent(String[] args) {
		if (args.length == 1 && args[0].startsWith("#")) {
			return TabCompleteHandler.getPartialMatches(args[0], "#ICON: ", "#HEAD: ", "#SMALLHEAD: ", "#ENTITY: ");
		} else if (args.length == 2) {
			switch (args[0].toUpperCase(Locale.ROOT)) {
				case "#ICON:":
				case "#HEAD:":
				case "#SMALLHEAD:":
					return TabCompleteHandler.getPartialMatches(args[1], items);
				
				case "#ENTITY:":
					return TabCompleteHandler.getPartialMatches(args[1], DecentEntityType.getAllowedEntityTypeNames());
			}
		} else if (args.length >= 3) {
			String item = args[1].toUpperCase(Locale.ROOT);
			if (args[2].startsWith("(") && (item.contains("HEAD") || item.contains("SKULL"))) {
				List<String> names = Bukkit.getOnlinePlayers().stream()
					.map(player -> "(" + player.getName() + ")")
					.collect(Collectors.toList());
				
				if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					names.add("(%player_name%)");
				}
				
				if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
					names.add("(HEADDATABASE_<id>)");
				}
				
				return TabCompleteHandler.getPartialMatches(args[args.length - 1], names);
			}
			
			String lastArg = args[args.length - 1];
			if ("!ENCHANTED".regionMatches(true, 0, lastArg, 0, lastArg.length()) && args[0].toUpperCase(Locale.ROOT).startsWith("#ICON:")) {
				return Collections.singletonList("!ENCHANTED");
			}
		}
		
		return Collections.emptyList();
	}
	
	protected static List<String> getPages(String hologramName, String token) {
		Hologram hologram = PLUGIN.getHologramManager().getHologram(hologramName);
		if (hologram != null) {
			return TabCompleteHandler.getPartialMatches(token, IntStream
				.rangeClosed(1, hologram.size())
				.boxed().map(String::valueOf)
				.collect(Collectors.toList()));
		}
		
		return Collections.emptyList();
	}
	
	protected static List<String> getLines(String hologramName, int pageIndex, String token) {
		Hologram hologram = PLUGIN.getHologramManager().getHologram(hologramName);
		if (hologram == null) return Collections.emptyList();
		HologramPage page = hologram.getPage(pageIndex - 1);
		if (page != null) {
			return TabCompleteHandler.getPartialMatches(token, IntStream
				.rangeClosed(1, page.size())
				.boxed().map(String::valueOf)
				.collect(Collectors.toList()));
		}
		
		return Collections.emptyList();
	}
	
	protected static List<String> getFlags(String token) {
		return TabCompleteHandler.getPartialMatches(token, Arrays.stream(EnumFlag.values()).map(Enum::name).collect(Collectors.toList()));
	}

}

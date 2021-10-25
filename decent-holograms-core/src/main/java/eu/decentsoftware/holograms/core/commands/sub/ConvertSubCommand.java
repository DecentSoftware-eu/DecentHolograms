package eu.decentsoftware.holograms.core.commands.sub;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.core.convertor.ConvertorType;
import eu.decentsoftware.holograms.core.convertor.HolographicDisplaysConvertor;
import eu.decentsoftware.holograms.utils.Common;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConvertSubCommand extends DecentCommand {

	public ConvertSubCommand() {
		super("convert", "dh.hologram.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh convert <plugin> [file]";
	}

	@Override
	public String getDescription() {
		return "Convert holograms from given plugin.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final ConvertorType convertorType = ConvertorType.getByName(args[0]);
			final String path = args.length >= 2 ? args[0] : null;

			if (convertorType != null) {
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
			}
			Common.tell(sender, Common.PREFIX + "Plugin &b'" + args[0] + "'&7 couldn't be found.");
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

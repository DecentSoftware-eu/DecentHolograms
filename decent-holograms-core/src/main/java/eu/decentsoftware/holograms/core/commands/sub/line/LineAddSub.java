package eu.decentsoftware.holograms.core.commands.sub.line;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import eu.decentsoftware.holograms.core.holograms.DefaultHologramLine;
import eu.decentsoftware.holograms.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.utils.items.DecentMaterial;
import org.bukkit.Material;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LineAddSub extends DecentCommand {

	public LineAddSub() {
		super("add", "dh.line.admin", true, "append");
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh line add <hologram> [content]";
	}

	@Override
	public String getDescription() {
		return "Add a line to Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final String content = EditValidator.getLineContent(args, 1);
			Hologram hologram = EditValidator.getHologram(args[0], Lang.HOLOGRAM_DOES_NOT_EXIST.getValue());
			HologramLine line = new DefaultHologramLine(hologram.getNextLineLocation(), content);

			if (hologram.addLine(line)) {
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
			List<String> matches = Lists.newArrayList();
			if (args.length == 1) {
				StringUtil.copyPartialMatches(args[0], PLUGIN.getHologramManager().getHologramNames(), matches);
			} else if (args.length == 3 && (args[1].startsWith("#ICON:") || args[1].startsWith("#HEAD:") || args[1].startsWith("#SMALLHEAD:"))) {
				StringUtil.copyPartialMatches(
						args[2],
						Arrays.stream(Material.values())
								.filter(DecentMaterial::isItem)
								.map(Material::name)
								.collect(Collectors.toList()),
						matches
				);
			} else if (args.length == 3 && args[1].startsWith("#ENTITY:")) {
				StringUtil.copyPartialMatches(args[2], DecentEntityType.getAllowedEntityTypeNames(), matches);
			}
			return matches;
		};
	}

}

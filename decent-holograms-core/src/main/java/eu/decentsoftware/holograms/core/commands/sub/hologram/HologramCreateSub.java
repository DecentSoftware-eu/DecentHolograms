package eu.decentsoftware.holograms.core.commands.sub.hologram;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.core.edit.EditValidator;
import eu.decentsoftware.holograms.core.holograms.DefaultHologram;
import eu.decentsoftware.holograms.core.holograms.DefaultHologramLine;
import eu.decentsoftware.holograms.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.utils.items.DecentMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HologramCreateSub extends DecentCommand {

	public HologramCreateSub() {
		super("create", "dh.hologram.admin", true, "new", "c");
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh hologram create <name> [content]";
	}

	@Override
	public String getDescription() {
		return "Create new Hologram.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			final String content = EditValidator.getLineContent(args, 1);
			final Player player = EditValidator.getPlayer(sender);
			EditValidator.isTrue(!PLUGIN.getHologramManager().containsHologram(name), Lang.HOLOGRAM_ALREADY_EXISTS.getValue());

			Hologram hologram = new DefaultHologram(args[0], player.getLocation());
			HologramLine line = new DefaultHologramLine(hologram.getNextLineLocation(), content);
			hologram.addLine(line);
			hologram.show();
			hologram.save();

			PLUGIN.getHologramManager().registerHologram(hologram);

			Lang.HOLOGRAM_CREATED.send(sender);
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return (sender, args) -> {
			List<String> matches = Lists.newArrayList();
			if (args.length == 3 && (args[1].startsWith("#ICON:") || args[1].startsWith("#HEAD:") || args[1].startsWith("#SMALLHEAD:"))) {
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

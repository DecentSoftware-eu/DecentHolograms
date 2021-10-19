package eu.decentsoftware.holograms.core.commands.sub.features;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.features.IFeature;
import eu.decentsoftware.holograms.utils.Common;
import org.bukkit.util.StringUtil;

import java.util.List;

public class FeatureInfoSub extends DecentCommand {

	public FeatureInfoSub() {
		super("info", "dh.features.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh feature info <feature>";
	}

	@Override
	public String getDescription() {
		return "Info about feature.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			IFeature feature = PLUGIN.getFeatureManager().getFeature(args[0]);
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
				List<String> matches = Lists.newArrayList();
				StringUtil.copyPartialMatches(args[0], PLUGIN.getFeatureManager().getFeatureNames(), matches);
				return matches;
			}
			return null;
		};
	}

}

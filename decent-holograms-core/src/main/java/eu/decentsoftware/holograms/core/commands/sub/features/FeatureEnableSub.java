package eu.decentsoftware.holograms.core.commands.sub.features;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.features.IFeature;
import org.bukkit.util.StringUtil;

import java.util.List;

public class FeatureEnableSub extends DecentCommand {

	public FeatureEnableSub() {
		super("enable", "dh.features.admin", false, "on");
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public String getUsage() {
		return "/dh feature enable <feature>";
	}

	@Override
	public String getDescription() {
		return "Enable a Feature.";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			IFeature feature = PLUGIN.getFeatureManager().getFeature(args[0]);
			if (feature == null) {
				Lang.FEATURE_DOES_NOT_EXIST.send(sender, args[0]);
			} else {
				if (feature.isEnabled()) {
					Lang.FEATURE_ALREADY_ENABLED.send(sender, args[0]);
					return true;
				}
				feature.enable();
				Lang.FEATURE_ENABLED.send(sender, args[0]);
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

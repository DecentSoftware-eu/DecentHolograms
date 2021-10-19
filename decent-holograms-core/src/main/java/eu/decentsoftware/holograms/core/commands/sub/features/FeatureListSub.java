package eu.decentsoftware.holograms.core.commands.sub.features;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.api.features.IFeature;
import eu.decentsoftware.holograms.utils.Common;

import java.util.List;

public class FeatureListSub extends DecentCommand {

	public FeatureListSub() {
		super("list", "dh.features.admin", false);
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public String getUsage() {
		return "/dh feature list";
	}

	@Override
	public String getDescription() {
		return "List of all features";
	}

	@Override
	public CommandHandler getCommandHandler() {
		return (sender, args) -> {
			List<IFeature> features = Lists.newArrayList(PLUGIN.getFeatureManager().getFeatures());
			if (features.isEmpty()) {
				Common.tell(sender, "%sThere are no features are currenty registered.");
			} else {
				sender.sendMessage("");
				Common.tell(sender, " &3&lFEATURES LIST");
				Common.tell(sender, " List of all features.");
				sender.sendMessage("");
				for (IFeature feature : features) {
					Common.tell(sender, " &8• &b%s &8- %s", feature.getName(), feature.isEnabled() ? "&aON" : "&cOFF");
				}
				sender.sendMessage("");
			}
			return true;
		};
	}

	@Override
	public TabCompleteHandler getTabCompleteHandler() {
		return null;
	}

}

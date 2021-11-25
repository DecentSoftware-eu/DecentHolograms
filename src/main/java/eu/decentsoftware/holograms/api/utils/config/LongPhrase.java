package eu.decentsoftware.holograms.api.utils.config;

import eu.decentsoftware.holograms.api.utils.Common;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class LongPhrase extends ConfigValue<List<String>>{

	public LongPhrase(Configuration config, String path, String... defaultValue) {
		super(config, path, Arrays.asList(defaultValue));
	}

	public LongPhrase(Configuration config, String path, List<String> defaultValue) {
		super(config, path, defaultValue);
	}

	public void send(CommandSender sender) {
		List<String> value = getValue();
		for (String line : value) {
			Common.tell(sender, line.replace("{prefix}", Common.PREFIX));
		}
	}

}
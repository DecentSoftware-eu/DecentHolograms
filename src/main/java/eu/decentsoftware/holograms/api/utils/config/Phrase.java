package eu.decentsoftware.holograms.api.utils.config;

import eu.decentsoftware.holograms.api.utils.Common;
import org.bukkit.command.CommandSender;

public class Phrase extends ConfigValue<String> {

	public Phrase(FileConfig config, String path, String defaultValue) {
		super(config, path, defaultValue);
	}

	public void send(CommandSender sender) {
		Common.tell(sender, getValue().replace("{prefix}", Common.PREFIX));
	}

	public void send(CommandSender sender, Object... args) {
		Common.tell(sender, getValue().replace("{prefix}", Common.PREFIX), args);
	}

	@Override
	public String getValue() {
		return super.getValue().replace("{prefix}", Common.PREFIX);
	}
}

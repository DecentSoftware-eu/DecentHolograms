package eu.decentsoftware.holograms.core.hooks;

import eu.decentsoftware.holograms.utils.Common;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderHook_PAPI extends PlaceholderHook {

	PlaceholderHook_PAPI() {
		Common.log("Using PlaceholderAPI for placeholder support!");
	}

	public static String setPlaceholders(Player player, String string) {
		return PlaceholderAPI.setPlaceholders(player, string);
	}

	public static String setPlaceholders(OfflinePlayer player, String string) {
		return PlaceholderAPI.setPlaceholders(player, string);
	}

}

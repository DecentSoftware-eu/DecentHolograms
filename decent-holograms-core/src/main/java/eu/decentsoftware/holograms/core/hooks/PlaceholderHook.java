package eu.decentsoftware.holograms.core.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PlaceholderHook {

	public static void reload() {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new PlaceholderHook_PAPI();
		}
	}

	/**
	 * Set placeholders to given String for given Player.
	 *
	 * @param player The player
	 * @param string  The string
	 * @return The string with replaced placeholders.
	 */
	public static String setPlaceholders(Player player, String string) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			return PlaceholderHook_PAPI.setPlaceholders(player, string);
		}
		return string;
	}

	public static List<String> setPlaceholders(Player player, List<String> stringList) {
		return stringList.stream().map(string -> setPlaceholders(player, string)).collect(Collectors.toList());
	}

}

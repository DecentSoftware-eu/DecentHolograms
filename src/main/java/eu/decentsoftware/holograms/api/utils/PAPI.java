package eu.decentsoftware.holograms.api.utils;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@UtilityClass
public class PAPI {

	/**
	 * Set placeholders to given String for given Player.
	 *
	 * @param player The player.
	 * @param string The string.
	 * @return The string with replaced placeholders.
	 */
	public static String setPlaceholders(Player player, String string) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			try {
				return PlaceholderAPI.setPlaceholders(player, string.replace("&", "§")).replace("§", "&");
			} catch (Exception ignored) {}
		}
		return string;
	}

	/**
	 * Set placeholders to given List of Strings for given Player.
	 *
	 * @param player The player.
	 * @param stringList The string list.
	 * @return The string with replaced placeholders.
	 */
	public static List<String> setPlaceholders(Player player, List<String> stringList) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			return stringList.stream().map(s -> setPlaceholders(player, s)).collect(Collectors.toList());
		}
		return stringList;
	}

	public static boolean containsPlaceholders(String string) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			return PlaceholderAPI.containsPlaceholders(string);
		}
		return false;
	}

}

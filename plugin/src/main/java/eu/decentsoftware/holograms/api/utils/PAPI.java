package eu.decentsoftware.holograms.api.utils;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PAPI {

	/**
	 * Check if PlaceholderAPI is available.
	 *
	 * @return True if PlaceholderAPI is available.
	 */
	public static boolean isAvailable() {
		return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}

	/**
	 * Set placeholders to given String for given Player.
	 *
	 * @param player The player.
	 * @param string The string.
	 * @return The string with replaced placeholders.
	 */
	public static String setPlaceholders(Player player, String string) {
		if (isAvailable()) {
			return PlaceholderAPI.setPlaceholders(player, string);
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
		if (isAvailable()) {
			return stringList.stream().map(s -> setPlaceholders(player, s)).collect(Collectors.toList());
		}
		return stringList;
	}

	/**
	 * Check if the given string contains any placeholders.
	 *
	 * @param string The string.
	 * @return True if the string contains any placeholders, false otherwise.
	 */
	public static boolean containsPlaceholders(String string) {
		if (isAvailable()) {
			return PlaceholderAPI.containsPlaceholders(string);
		}
		return false;
	}

}

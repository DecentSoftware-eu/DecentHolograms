package eu.decentsoftware.holograms.api.utils;

import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.utils.color.IridiumColorAPI;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

@UtilityClass
public class Common {

	private static final Pattern SPACING_CHARS_REGEX;
	public static String PREFIX;

	static {
		SPACING_CHARS_REGEX = Pattern.compile("[_ \\-]+");
		PREFIX = "&8[&3DecentHolograms&8] &7";
	}

	/*
	 * 	Colorize
	 */

	public static String colorize(String string) {
		return IridiumColorAPI.process(string);
	}

	public static List<String> colorize(List<String> list) {
		list.replaceAll(Common::colorize);
		return list;
	}

	public static String stripColors(String string) {
		return ChatColor.stripColor(IridiumColorAPI.stripColorFormatting(string));
	}

	/*
	 * 	Log
	 */

	/**
	 * Log a message into console.
	 *
	 * @param message The message.
	 */
	public static void log(String message) {
		log(Level.INFO, message);
	}

	/**
	 * Log a message into console.
	 * <p>
	 *     This method formats given arguments in the message.
	 * </p>
	 *
	 * @param message The message.
	 * @param args The arguments
	 */
	public static void log(String message, Object... args) {
		log(String.format(message, args));
	}

	/**
	 * Log a message into console.
	 *
	 * @param level Level of this message.
	 * @param message The message.
	 */
	public static void log(Level level, String message) {
		Bukkit.getServer().getLogger().log(level, "[DecentHolograms] " + message);
	}

	/**
	 * Log a message into console.
	 * <p>
	 *     This method formats given arguments in the message.
	 * </p>
	 *
	 * @param level Level of this message.
	 * @param message The message.
	 * @param args The arguments.
	 */
	public static void log(Level level, String message, Object... args) {
		log(level, String.format(message, args));
	}

	/*
	 * 	Debug
	 */

	/**
	 * Print an object into console.
	 *
	 * @param o Object to print.
	 */
	public static void debug(Object o) {
		System.out.println(o);
	}

	/*
	 * 	Tell
	 */

	/**
	 * Send a message to given CommandSender.
	 * <p>
	 *     This method will colorize the message.
	 * </p>
	 *
	 * @param player The CommandSender receiving the message.
	 * @param message The message.
	 */
	public static void tell(CommandSender player, String message) {
		player.sendMessage(colorize(message));
	}

	/**
	 * Send a message to given CommandSender.
	 * <p>
	 *     This method will colorize the message and formats given arguments to the message.
	 * </p>
	 *
	 * @param player The CommandSender receiving the message.
	 * @param message The message.
	 * @param args The arguments.
	 */
	public static void tell(CommandSender player, String message, Object... args) {
		tell(player, String.format(message, args));
	}

	public static String removeSpacingChars(String string) {
		return SPACING_CHARS_REGEX.matcher(string).replaceAll("");
	}

	/**
	 * Check whether the given version is higher than the current version.
	 *
	 * @param version The version.
	 * @return Boolean.
	 */
	public static boolean isVersionHigher(String version) {
		if (!version.matches("(\\d+)\\.(\\d+)\\.(\\d+)(\\.(\\d+))?")) {
			return false;
		}
		String current = Settings.getAPIVersion();
		int[] i1 = splitVersion(version);
		int[] i2 = splitVersion(current);
		if (i1 == null || i2 == null) {
			return false;
		}
		return i1[0] > i2[0] // Major version is higher.
				|| (i1[0] == i2[0] && i1[1] > i2[1]) // Minor version is higher and major is the same.
				|| (i1[0] == i2[0] && i1[1] == i2[1] && i1[2] > i2[2]); // Major and minor versions are the same and patch is higher.
	}

	private static int[] splitVersion(String version) {
		String[] spl = version == null ? null : version.split("\\.");
		if (spl == null || spl.length < 3) {
			return new int[0];
		}
		int[] arr = new int[spl.length];
		for (int i = 0; i < spl.length; i++) {
			arr[i] = parseInt(spl[i]);
		}
		return arr;
	}

	private static int parseInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Check whether the given plugin is enabled.
	 *
	 * @param name The plugins name.
	 * @return The boolean.
	 */
	public static boolean isPluginEnabled(String name) {
		return Bukkit.getPluginManager().isPluginEnabled(name);
	}

}

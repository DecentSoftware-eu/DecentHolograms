package eu.decentsoftware.holograms.api.utils;

import eu.decentsoftware.holograms.api.utils.color.IridiumColorAPI;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

@UtilityClass
public class Common {

    public static final String NAME_REGEX = "[a-zA-Z0-9_-]+";

    private static final Pattern SPACING_CHARS_REGEX;
    public static String PREFIX;

    static {
        SPACING_CHARS_REGEX = Pattern.compile("[_ \\-]+");
        PREFIX = "&8[&3DecentHolograms&8] &7";
    }

    /**
     * This method generates random Integer between min and max
     *
     * @param min Minimal random number (inclusive)
     * @param max Maximum random number (inclusive)
     * @return Randomly generated Integer
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * @return Random float between zero and one.
     */
    public static float randomFloat() {
        return ThreadLocalRandom.current().nextFloat();
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
     * 	Tell
     */

    /**
     * Send a message to given CommandSender.
     * <p>
     * This method will colorize the message.
     * </p>
     *
     * @param player  The CommandSender receiving the message.
     * @param message The message.
     */
    public static void tell(CommandSender player, String message) {
        player.sendMessage(colorize(message));
    }

    /**
     * Send a message to given CommandSender.
     * <p>
     * This method will colorize the message and formats given arguments to the message.
     * </p>
     *
     * @param player  The CommandSender receiving the message.
     * @param message The message.
     * @param args    The arguments.
     */
    public static void tell(CommandSender player, String message, Object... args) {
        tell(player, String.format(message, args));
    }

    /**
     * Remove spacing characters from the given string.
     *
     * <p>Spacing characters: ' ', '-', '_'</p>
     *
     * @param string The string.
     * @return The string without spacing characters.
     */
    public static String removeSpacingChars(String string) {
        if (string == null) {
            return null;
        }
        return SPACING_CHARS_REGEX.matcher(string).replaceAll("");
    }

    /**
     * Check whether the given version is higher than the current version.
     *
     * @param currentVersion The current version.
     * @param newVersion     The new version.
     * @return Boolean.
     */
    public static boolean isVersionHigher(String currentVersion, String newVersion) {
        if (isVersionInvalid(currentVersion) || isVersionInvalid(newVersion)) {
            return false;
        }

        String[] currentVersionParts = currentVersion.split("\\.");
        String[] newVersionParts = newVersion.split("\\.");

        for (int i = 0; i < 3; i++) {
            int currentVersionPart = Integer.parseInt(currentVersionParts[i]);
            int newVersionPart = Integer.parseInt(newVersionParts[i]);

            if (newVersionPart > currentVersionPart) {
                return true;
            } else if (newVersionPart < currentVersionPart) {
                return false;
            }
        }

        return false;
    }

    private static boolean isVersionInvalid(String version2) {
        return version2 == null || !version2.matches("\\d+\\.\\d+\\.\\d+(\\.\\d+)?");
    }

}

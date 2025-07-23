package eu.decentsoftware.holograms.api.utils;

import eu.decentsoftware.holograms.api.utils.color.IridiumColorAPI;
import lombok.experimental.UtilityClass;

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

    /*
     * 	Colorize
     */

    public static String colorize(String string) {
        return IridiumColorAPI.process(string);
    }

    /*
     * 	Tell
     */

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

}

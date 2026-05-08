package eu.decentsoftware.holograms.api.utils.color.patterns;

import eu.decentsoftware.holograms.api.utils.color.IridiumColorAPI;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;

public class SolidPattern implements Pattern {

    public static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("[<{]#([A-Fa-f0-9]{6})[}>]|[&§]?#([A-Fa-f0-9]{6})");

    /**
     * Applies a solid RGB color to the provided String.
     * Output might be the same as the input if this pattern is not present.
     *
     * @param string The String to which this pattern should be applied to
     * @return The new String with an applied pattern
     */
    public String process(String string) {
        if (string.indexOf('#') == -1) {
            return string;
        }

        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            String color = matcher.group(1);
            if (color == null) {
                color = matcher.group(2);
            }

            if (Version.supportsHex()) {
                string = string.replace(matcher.group(), "§#" + color);
            } else {
                ChatColor closestColor = IridiumColorAPI.getClosestColor(new Color(Integer.parseInt(string, 16)));
                string = string.replace(matcher.group(), closestColor.toString());
            }
        }
        return string;
    }

}
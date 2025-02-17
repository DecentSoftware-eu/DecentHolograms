package eu.decentsoftware.holograms.plugin.features;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public final class FeatureCommons {

    private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

    public static String formatNumber(double number) {
        return FORMAT.format(number);
    }

    public static String formatNumberShort(double damage) {
        if (damage >= 1000000) {
            return formatNumber(damage / 1000000) + "M";
        } else if (damage >= 1000) {
            return formatNumber(damage / 1000) + "k";
        } else {
            return formatNumber(damage);
        }
    }

}

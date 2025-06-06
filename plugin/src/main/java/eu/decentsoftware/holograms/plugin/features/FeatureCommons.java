package eu.decentsoftware.holograms.plugin.features;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public final class FeatureCommons {

    private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

    public static String formatNumber(double number) {
        return FORMAT.format(number);
    }

}

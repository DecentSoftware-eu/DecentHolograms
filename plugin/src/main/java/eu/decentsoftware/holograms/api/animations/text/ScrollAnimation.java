package eu.decentsoftware.holograms.api.animations.text;

import eu.decentsoftware.holograms.api.animations.TextAnimation;
import eu.decentsoftware.holograms.api.utils.color.SpecialColorFormattingExtractionResult;
import eu.decentsoftware.holograms.api.utils.color.StripColorUtil;
import lombok.NonNull;

public class ScrollAnimation extends TextAnimation {

    public ScrollAnimation() {
        super("scroll", 3, 0);
    }

    @Override
    public String animate(@NonNull String string, long step, String... args) {
        SpecialColorFormattingExtractionResult strippingResult = StripColorUtil.extractSpecialColorsFormatting(string);
        string = strippingResult.getCleanedString();

        String stripped = StripColorUtil.stripLegacyColorCodes(string);
        int length = stripped.length();
        int size = length / 3 * 2;
        int currentStep = getCurrentStep(step, length);
        int index2 = currentStep + size;
        String specialColors = strippingResult.getSpecialFormatting();
        if (index2 > length) {
            return specialColors + stripped.substring(currentStep) + " " + specialColors + stripped.substring(0, index2 - length);
        }
        return specialColors + stripped.substring(currentStep, index2);
    }
}

package eu.decentsoftware.holograms.api.animations.text;

import eu.decentsoftware.holograms.api.animations.TextAnimation;
import eu.decentsoftware.holograms.api.utils.color.StripColorUtil;
import lombok.NonNull;

import java.util.Arrays;

public class TypewriterAnimation extends TextAnimation {

    public TypewriterAnimation() {
        super("typewriter", 3, 20);
    }

    @Override
    public String animate(@NonNull String string, long step, String... args) {
        String specialColors = StripColorUtil.extractSpecialColorsFormatting(string);
        String stripped = StripColorUtil.stripLegacyColorCodes(string);

        int currentStep = getCurrentStep(step, stripped.length());
        return specialColors + String.valueOf(Arrays.copyOfRange(stripped.toCharArray(), 0, currentStep));
    }
}

package eu.decentsoftware.holograms.api.animations.text;

import eu.decentsoftware.holograms.api.animations.TextAnimation;
import eu.decentsoftware.holograms.api.utils.color.StripColorUtil;
import lombok.NonNull;

public class BurnAnimation extends TextAnimation {

    public BurnAnimation() {
        super("burn", 2, 40);
    }

    @Override
    public String animate(@NonNull String string, long step, String... args) {
        String specialColors = StripColorUtil.extractSpecialColorsFormatting(string);
        String stripped = StripColorUtil.stripLegacyColorCodes(string);

        int currentStep = getCurrentStep(step, stripped.length());
        String start = stripped.substring(0, currentStep);
        String end = stripped.substring(currentStep);
        return args[1] + specialColors + start + args[0] + specialColors + end;
    }
}

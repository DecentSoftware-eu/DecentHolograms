package eu.decentsoftware.holograms.api.animations.text;

import eu.decentsoftware.holograms.api.animations.TextAnimation;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.color.IridiumColorAPI;
import lombok.NonNull;

public class BurnAnimation extends TextAnimation {

    public BurnAnimation() {
        super("burn", 2, 40);
    }

    @Override
    public String animate(@NonNull String string, long step, String... args) {
        StringBuilder specialColors = new StringBuilder();
        for (String color : IridiumColorAPI.SPECIAL_COLORS) {
            if (string.contains(color)) {
                specialColors.append(color);
                string = string.replace(color, "");
            }
        }
        String stripped = Common.stripColors(string);
        int currentStep = getCurrentStep(step, stripped.length());
        String start = stripped.substring(0, currentStep);
        String end = stripped.substring(currentStep);
        return args[1] + specialColors + start + args[0] + specialColors + end;
    }
}

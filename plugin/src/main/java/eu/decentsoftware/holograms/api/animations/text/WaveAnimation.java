package eu.decentsoftware.holograms.api.animations.text;

import eu.decentsoftware.holograms.api.animations.TextAnimation;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.color.IridiumColorAPI;
import lombok.NonNull;

public class WaveAnimation extends TextAnimation {

    public WaveAnimation() {
        super("wave", 2, 40);
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
        int length = stripped.length();
        int size = length / 4;
        int currentStep = getCurrentStep(step, length + size);
        int index1 = currentStep > size ? currentStep - size : 0;
        int index2 = currentStep < size ? size - (size - currentStep) : currentStep;
        String start = index1 != 0 ? stripped.substring(0, index1) : "";
        String mid = length > index2 ? stripped.substring(index1, index2) : stripped.substring(index1);
        String end = length > index2 ? stripped.substring(index2) : "";
        return args[0] + specialColors + start + args[1] + specialColors + mid + args[0] + specialColors + end;
    }

}

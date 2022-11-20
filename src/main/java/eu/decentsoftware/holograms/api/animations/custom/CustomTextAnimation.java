package eu.decentsoftware.holograms.api.animations.custom;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.animations.TextAnimation;
import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CustomTextAnimation extends TextAnimation {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    private final @NonNull List<String> steps;

    public CustomTextAnimation(@NonNull String name, int speed, int pause, @NonNull List<String> steps) {
        super(name, speed, pause);
        this.steps = steps;
    }

    public CustomTextAnimation(@NonNull String name, int speed, int pause, @NonNull List<String> steps, String... aliases) {
        super(name, speed, pause, aliases);
        this.steps = steps;
    }

    @Override
    public String animate(@NonNull String string, long step, String... args) {
        int currentStep = getCurrentStep(step, steps.size() - 1);
        return steps.get(currentStep);
    }

    public static CustomTextAnimation fromFile(@NonNull String fileName) {
        FileConfig config = new FileConfig(DECENT_HOLOGRAMS.getPlugin(), "animations/" + fileName);

        // Parse animation name
        String name;
        if (fileName.toLowerCase().startsWith("animation_") && fileName.length() > "animation_".length()) {
            name = fileName.substring("animation_".length(), fileName.length() - 4);
        } else {
            name = fileName.substring(0, fileName.length() - 4);
        }

        int speed = config.isInt("speed") ? config.getInt("speed") : 1;
        int pause = config.isInt("pause") ? config.getInt("pause") : 1;
        List<String> steps = config.isList("steps") ? config.getStringList("steps") : new ArrayList<>();
        return new CustomTextAnimation(name, speed, pause, steps);
    }

}

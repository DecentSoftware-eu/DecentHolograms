package eu.decentsoftware.holograms.api.animations;

import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

@Getter
public abstract class Animation {

    private final @NonNull String name;
    private final List<String> aliases;
    private final int speed;
    private final int pause;

    public Animation(@NonNull String name, int speed, int pause) {
        this(name, speed, pause, new String[0]);
    }

    public Animation(@NonNull String name, int speed, int pause, String... aliases) {
        this.name = name;
        this.speed = speed;
        this.pause = pause;
        this.aliases = Arrays.asList(aliases);
    }

    protected int getCurrentStep(long step, int maxSteps) {
        if (maxSteps <= 0) return 0;
        long actualStep = step / speed;
        // Adapt the pause to speed.
        int actualPause = pause <= 0 ? 0 : pause / speed;
        int currentStep = (int) (actualStep % (maxSteps + actualPause));
        return Math.min(currentStep, maxSteps);
    }

    public boolean isIdentifier(@NonNull String string) {
        return name.equalsIgnoreCase(string) || aliases.contains(string.toLowerCase());
    }

}

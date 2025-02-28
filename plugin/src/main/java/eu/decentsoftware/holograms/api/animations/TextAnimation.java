package eu.decentsoftware.holograms.api.animations;

import lombok.NonNull;

public abstract class TextAnimation extends Animation {

    protected TextAnimation(@NonNull String name, int speed, int pause) {
        super(name, speed, pause);
    }

    protected TextAnimation(@NonNull String name, int speed, int pause, String... aliases) {
        super(name, speed, pause, aliases);
    }

    public abstract String animate(@NonNull String string, long step, String... args);

}

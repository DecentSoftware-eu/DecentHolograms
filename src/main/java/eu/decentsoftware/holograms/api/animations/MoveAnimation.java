package eu.decentsoftware.holograms.api.animations;

import lombok.NonNull;
import org.bukkit.Location;

public abstract class MoveAnimation extends Animation {

    public MoveAnimation(@NonNull String name, int speed, int pause) {
        super(name, speed, pause);
    }

    public MoveAnimation(@NonNull String name, int speed, int pause, String... aliases) {
        super(name, speed, pause, aliases);
    }

    public abstract Location animate(Location location, long step, String... args);

}

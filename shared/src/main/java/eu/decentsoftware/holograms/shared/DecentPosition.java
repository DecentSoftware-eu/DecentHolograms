package eu.decentsoftware.holograms.shared;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;

/**
 * Represents a 3D position with yaw and pitch.
 * This position is not specific to any world.
 *
 * <p>This implementation is immutable.</p>
 *
 * @author d0by
 * @since 2.9.0
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DecentPosition {

    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    /**
     * Subtract from the Y coordinate.
     *
     * @param y The amount to subtract.
     * @return A new {@link DecentPosition} with the subtracted Y position.
     */
    public DecentPosition subtractY(double y) {
        if (y == 0) {
            return this;
        }
        return new DecentPosition(x, this.y - y, z, yaw, pitch);
    }

    /**
     * Create a {@link DecentPosition} from a Bukkit Location.
     *
     * @param location The Bukkit Location.
     * @return The new {@link DecentPosition}.
     */
    public static DecentPosition fromBukkitLocation(Location location) {
        return new DecentPosition(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

}

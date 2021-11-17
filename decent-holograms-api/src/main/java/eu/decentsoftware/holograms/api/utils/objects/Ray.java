package eu.decentsoftware.holograms.api.utils.objects;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@Getter
public class Ray {

    private final Location start;
    private final Vector direction;

    public Ray(@NotNull Location start, @NotNull Vector direction) {
        this.start = start;
        this.direction = direction;
    }

    public Iterator<Location> locations(final double maxDistance, final double step) {
        return new Iterator<Location>() {
            double t = 0.0d;

            @Override
            public boolean hasNext() {
                return t < maxDistance;
            }

            @Override
            public Location next() {
                double x = start.getX() + t * direction.getX();
                double y = start.getY() + t * direction.getY();
                double z = start.getZ() + t * direction.getZ();
                t += step;
                return new Location(start.getWorld(), x, y, z);
            }
        };
    }

    public boolean intersects(@NotNull BoundingBox bb, double maxDistance, double step) {
        for (double t = 0.0d; t < maxDistance; t += step) {
            double x = start.getX() + t * direction.getX();
            double y = start.getY() + t * direction.getY();
            double z = start.getZ() + t * direction.getZ();
            if (bb.contains(x, y, z)) return true;
        }
        return false;
    }

}

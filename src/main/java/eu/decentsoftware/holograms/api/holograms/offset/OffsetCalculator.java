package eu.decentsoftware.holograms.api.holograms.offset;

public class OffsetCalculator {

    private static double angleInternal(double firstX, double firstY, double secondX, double secondY) {
        double angle = Math.atan2((secondX - firstX), (secondY - firstY)) * 180 / Math.PI;
        if (angle < 0) {
            return (360 + angle);
        } else {
            return (angle);
        }
    }

    public static double angleOn(double firstX, double firstY, double secondX, double secondY) {
        double angle = angleInternal(firstX, firstY, secondX, secondY);
        return Math.toRadians(angle);
    }

    public static class Loc2D {
        private final double x;
        private final double z;

        public Loc2D(double x, double z) {
            this.x = x;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public double getZ() {
            return z;
        }

        public boolean isDifferent(Loc2D other) {
            return (int) other.x != (int) x || (int) other.z != (int) z;
        }
    }

    public static Loc2D calculateOffSet(Loc2D player, Loc2D baseOffSet, Loc2D hologram) {
        return calculateOffSet(angleOn(player.x, player.z, hologram.x, hologram.z), baseOffSet, hologram);
    }

    public static Loc2D calculateOffSet(double playerLookAngle, Loc2D baseOffSet, Loc2D hologram) {
        double offSetBaseAngle = angleOn(baseOffSet.x + hologram.x, baseOffSet.z + hologram.z, hologram.x, hologram.z);
        double offSetBaseDistance = Math.sqrt((0 - baseOffSet.z) * (0 - baseOffSet.z) + (0 - baseOffSet.x) * (0 - baseOffSet.x));
        double radians = playerLookAngle + offSetBaseAngle;
        return new Loc2D(
                (Math.sin(radians) * offSetBaseDistance) + hologram.x,
                (Math.cos(radians) * offSetBaseDistance) + hologram.z
        );
    }

}
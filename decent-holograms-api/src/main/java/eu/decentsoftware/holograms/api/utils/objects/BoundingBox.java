package eu.decentsoftware.holograms.api.utils.objects;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BoundingBox {

    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;

    @NotNull
    public static BoundingBox of(@NotNull Vector corner1, @NotNull Vector corner2) {
        Validate.notNull(corner1, "Corner1 is null!");
        Validate.notNull(corner2, "Corner2 is null!");
        return new BoundingBox(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ());
    }

    @NotNull
    public static BoundingBox of(@NotNull Location corner1, @NotNull Location corner2) {
        Validate.notNull(corner1, "Corner1 is null!");
        Validate.notNull(corner2, "Corner2 is null!");
        Validate.isTrue(Objects.equals(corner1.getWorld(), corner2.getWorld()), "Locations from different worlds!");
        return new BoundingBox(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ());
    }

    @NotNull
    public static BoundingBox of(@NotNull Block corner1, @NotNull Block corner2) {
        Validate.notNull(corner1, "Corner1 is null!");
        Validate.notNull(corner2, "Corner2 is null!");
        Validate.isTrue(Objects.equals(corner1.getWorld(), corner2.getWorld()), "Blocks from different worlds!");
        int x1 = corner1.getX();
        int y1 = corner1.getY();
        int z1 = corner1.getZ();
        int x2 = corner2.getX();
        int y2 = corner2.getY();
        int z2 = corner2.getZ();
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2) + 1;
        int maxY = Math.max(y1, y2) + 1;
        int maxZ = Math.max(z1, z2) + 1;
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @NotNull
    public static BoundingBox of(@NotNull Block block) {
        Validate.notNull(block, "Block is null!");
        return new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 1, block.getZ() + 1);
    }

    @NotNull
    public static BoundingBox of(@NotNull Vector center, double x, double y, double z) {
        Validate.notNull(center, "Center is null!");
        return new BoundingBox(center.getX() - x, center.getY() - y, center.getZ() - z, center.getX() + x, center.getY() + y, center.getZ() + z);
    }

    @NotNull
    public static BoundingBox of(@NotNull Location center, double x, double y, double z) {
        Validate.notNull(center, "Center is null!");
        return new BoundingBox(center.getX() - x, center.getY() - y, center.getZ() - z, center.getX() + x, center.getY() + y, center.getZ() + z);
    }

    public BoundingBox() {
        this.resize(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    public BoundingBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.resize(x1, y1, z1, x2, y2, z2);
    }

    @NotNull
    public BoundingBox resize(double x1, double y1, double z1, double x2, double y2, double z2) {
        NumberConversions.checkFinite(x1, "x1 not finite");
        NumberConversions.checkFinite(y1, "y1 not finite");
        NumberConversions.checkFinite(z1, "z1 not finite");
        NumberConversions.checkFinite(x2, "x2 not finite");
        NumberConversions.checkFinite(y2, "y2 not finite");
        NumberConversions.checkFinite(z2, "z2 not finite");
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
        return this;
    }

    public double getMinX() {
        return this.minX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getMinZ() {
        return this.minZ;
    }

    @NotNull
    public Vector getMin() {
        return new Vector(this.minX, this.minY, this.minZ);
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public double getMaxZ() {
        return this.maxZ;
    }

    @NotNull
    public Vector getMax() {
        return new Vector(this.maxX, this.maxY, this.maxZ);
    }

    public double getWidthX() {
        return this.maxX - this.minX;
    }

    public double getWidthZ() {
        return this.maxZ - this.minZ;
    }

    public double getHeight() {
        return this.maxY - this.minY;
    }

    public double getVolume() {
        return this.getHeight() * this.getWidthX() * this.getWidthZ();
    }

    public double getCenterX() {
        return this.minX + this.getWidthX() * 0.5D;
    }

    public double getCenterY() {
        return this.minY + this.getHeight() * 0.5D;
    }

    public double getCenterZ() {
        return this.minZ + this.getWidthZ() * 0.5D;
    }

    @NotNull
    public Vector getCenter() {
        return new Vector(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }

    public boolean contains(double x, double y, double z) {
        return x >= this.minX && x < this.maxX && y >= this.minY && y < this.maxY && z >= this.minZ && z < this.maxZ;
    }

    public boolean contains(@NotNull Vector position) {
        Validate.notNull(position);
        return this.contains(position.getX(), position.getY(), position.getZ());
    }

}
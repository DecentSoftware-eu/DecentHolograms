package eu.decentsoftware.holograms.api.utils.location;

import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.exception.LocationParseException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@UtilityClass
public class LocationUtils {

    public static String asString(@NonNull Location l, boolean includeYawPitch) {
        String location = String.format("%s:%.3f:%.3f:%.3f", l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
        if (includeYawPitch) {
            location += String.format(":%.3f:%.3f", l.getYaw(), l.getPitch());
        }
        return location;
    }

    public static @Nullable Location asLocation(String string) {
        return asLocation(string, ":");
    }

    public static @Nullable Location asLocation(String string, String separator) {
        try {
            return asLocationE(string, separator);
        } catch (LocationParseException e) {
            Log.warn( "解析位置 %s 时出错", e, string);
            return null;
        }
    }

    public static Location asLocationE(String string) throws LocationParseException {
        return asLocationE(string, ":");
    }

    public static Location asLocationE(String string, String separator) throws LocationParseException {
        if (string == null || string.trim().isEmpty()) return null;
        String[] spl = string.replace(",", ".").split(separator);
        Location location;
        if (spl.length >= 4) {
            World world = getWorld(spl[0]);
            if (world != null) {
                try {
                    location = new Location(world, Double.parseDouble(spl[1]), Double.parseDouble(spl[2]), Double.parseDouble(spl[3]));
                    if (spl.length >= 6) {
                        location.setYaw(Float.parseFloat(spl[4]));
                        location.setPitch(Float.parseFloat(spl[5]));
                    }
                    return location;
                } catch (NumberFormatException e) {
                    Log.warn("解析位置 %s 时出错", e, string);
                }
            }
            throw new LocationParseException(String.format("未找到世界 '%s'。", spl[0]), LocationParseException.Reason.WORLD, spl[0]);
        }
        throw new LocationParseException(String.format("位置格式错误: %s", string));
    }

    public static Location randomizeLocation(@NonNull Location location) {
        return location.add(
                Common.randomFloat() - 0.5D,
                Common.randomFloat() - 0.5D,
                Common.randomFloat() - 0.5D
        );
    }

    public static double distance2D(@NonNull Location location1, @NonNull Location location2) {
        return Math.sqrt(NumberConversions.square(location1.getX() - location2.getX()) + NumberConversions.square(location1.getZ() - location2.getZ()));
    }

    // Plugins like GHolo use the world's UUID instead of the name for location.
    private static World getWorld(@NonNull String value) {
        World world = Bukkit.getWorld(value);
        if (world != null) {
            return world;
        }

        try {
            UUID uuid = UUID.fromString(value);
            world = Bukkit.getWorld(uuid);
        } catch (IllegalArgumentException ignored) {}

        // World was neither retrieved from name nor UUID. How is this possible?
        if (world == null) {
            Log.warn("无法从值 %s 获取世界！它既不是有效的名称也不是UUID。", value);
        }

        return world;
    }

}

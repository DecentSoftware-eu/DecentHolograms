package eu.decentsoftware.holograms.api.utils.location;

import eu.decentsoftware.holograms.api.utils.RandomUtils;
import eu.decentsoftware.holograms.api.utils.exception.LocationParseException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.Nullable;

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
		try {
			return asLocationE(string);
		} catch (LocationParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Location asLocationE(String string) throws LocationParseException {
		if (string == null || string.trim().isEmpty()) return null;
		String[] spl = string.replace(",", ".").split(":");
		Location location;
		if (spl.length >= 4) {
			World world = Bukkit.getWorld(spl[0]);
			if (world != null) {
				try {
					location = new Location(world, Double.parseDouble(spl[1]), Double.parseDouble(spl[2]), Double.parseDouble(spl[3]));
					if (spl.length >= 6) {
						location.setYaw(Float.parseFloat(spl[4]));
						location.setPitch(Float.parseFloat(spl[5]));
					}
					return location;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			throw new LocationParseException(String.format("World '%s' not found.", spl[0]));
		}
		throw new LocationParseException(String.format("Wrong location format: %s", string));
	}

	public static Location randomizeLocation(@NonNull Location location) {
		return location.add(
				RandomUtils.randomFloat() - 0.5D,
				RandomUtils.randomFloat() - 0.5D,
				RandomUtils.randomFloat() - 0.5D
		);
	}

	public static double distance2D(@NonNull Location location1, @NonNull Location location2) {
		return Math.sqrt(NumberConversions.square(location1.getX() - location2.getX()) + NumberConversions.square(location1.getZ() - location2.getZ()));
	}

}

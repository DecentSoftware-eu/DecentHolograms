package eu.decentsoftware.holograms.utils.location;

import eu.decentsoftware.holograms.utils.MathUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
public class LocationUtils {

	public static String asString(Location l, boolean includeYawPitch) {
		String location = String.format("%s:%.3f:%.3f:%.3f", l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
		if (includeYawPitch) {
			location += String.format(":%.3f:%.3f", l.getYaw(), l.getPitch());
		}
		return location;
	}

	public static Location asLocation(String string) {
		String[] spl = string.split(":");
		Location location = null;

		if (spl.length >= 4) {
			World world = Bukkit.getWorld(spl[0]);
			if (world != null) {
				try {
					location = new Location(world, Double.parseDouble(spl[1]), Double.parseDouble(spl[2]), Double.parseDouble(spl[3]));
					if (spl.length >= 6) {
						location.setYaw(Float.parseFloat(spl[4]));
						location.setPitch(Float.parseFloat(spl[5]));
					}
				} catch (NumberFormatException ignored) {}
			}
		}
		return location;
	}

	public static Location randomizeLocation(Location location) {
		return location.add(
				MathUtils.randomFloat() - 0.5D,
				MathUtils.randomFloat() - 0.5D,
				MathUtils.randomFloat() - 0.5D
		);
	}

}

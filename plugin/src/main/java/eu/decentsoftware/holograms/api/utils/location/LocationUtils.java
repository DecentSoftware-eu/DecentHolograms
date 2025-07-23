package eu.decentsoftware.holograms.api.utils.location;

import eu.decentsoftware.holograms.api.utils.Common;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;

@UtilityClass
public class LocationUtils {

    public static Location randomizeLocation(@NonNull Location location) {
        return location.add(
                Common.randomFloat() - 0.5D,
                Common.randomFloat() - 0.5D,
                Common.randomFloat() - 0.5D
        );
    }

}

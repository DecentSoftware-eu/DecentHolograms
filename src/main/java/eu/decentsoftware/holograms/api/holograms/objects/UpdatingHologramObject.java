package eu.decentsoftware.holograms.api.holograms.objects;

import eu.decentsoftware.holograms.api.Settings;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public abstract class UpdatingHologramObject extends HologramObject {

    /*
     *	Fields
     */

    protected int displayRange = Settings.DEFAULT_DISPLAY_RANGE;
    protected int updateRange = Settings.DEFAULT_UPDATE_RANGE;
    protected volatile int updateInterval = Settings.DEFAULT_UPDATE_INTERVAL;

    /*
     *	Constructors
     */

    public UpdatingHologramObject(@NonNull Location location) {
        super(location);
    }

}

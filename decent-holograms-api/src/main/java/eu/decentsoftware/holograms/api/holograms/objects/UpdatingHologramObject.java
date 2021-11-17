package eu.decentsoftware.holograms.api.holograms.objects;

import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.utils.scheduler.ConsumerTask;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.bukkit.Location;

@Getter
@Setter
public abstract class UpdatingHologramObject extends HologramObject {

    /*
     *	Fields
     */

    @FieldNameConstants.Exclude
    protected ConsumerTask<Hologram> updateTask;
    protected int displayRange = Settings.DEFAULT_DISPLAY_RANGE.getValue();
    protected int updateRange = Settings.DEFAULT_UPDATE_RANGE.getValue();
    protected int updateInterval = Settings.DEFAULT_UPDATE_INTERVAL.getValue();

    /*
     *	Constructors
     */

    public UpdatingHologramObject(Location location) {
        super(location);
    }

    /*
     *	General Methods
     */

    @Override
    public void enable() {
        super.enable();
        this.startUpdate();
    }

    @Override
    public void disable() {
        this.stopUpdate();
        super.disable();
    }

    /*
     *	Updating Methods
     */

    /**
     * Start the update task of this hologram.
     */
    public void startUpdate() {
        if (updateTask == null) return;
        if (!isEnabled() || hasFlag(EnumFlag.DISABLE_UPDATING)) return;
        updateTask.restart();
    }

    /**
     * Stop the update task of this hologram.
     */
    public void stopUpdate() {
        if (updateTask == null) return;
        updateTask.stop();
    }

}

package eu.decentsoftware.holograms.api.holograms.objects;

import com.google.common.collect.ImmutableSet;
import eu.decentsoftware.holograms.api.holograms.DisableCause;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class HologramObject extends FlagHolder {

    /*
     *	Fields
     */

    protected boolean enabled = true;
    protected @NonNull DisableCause cause = DisableCause.NONE;
    protected final @NonNull Set<UUID> viewers = Collections.synchronizedSet(new HashSet<>());
    protected @NonNull Location location;
    protected String permission = null;
    protected float facing = 0.0f;

    /*
     *	Constructors
     */

    public HologramObject(@NonNull Location location) {
        this.location = location;
        this.location.setPitch(0.0f);
    }

    /*
     *	General Methods
     */

    /**
     * Hide this hologram object to all viewers and stop it from updating.
     */
    public void destroy() {
        disable();
    }

    /**
     * Hide this hologram object from all players, stop it from updating and delete it completely.
     */
    public void delete() {
        destroy();
    }

    /**
     * Enable updating and showing to players automatically.
     */
    public void enable() {
        this.cause = DisableCause.NONE;
        this.enabled = true;
    }

    /**
     * Disable updating and showing to players automatically.
     */
    public void disable() {
        disable(DisableCause.API);
    }

    /**
     * Disable updating and showing to players automatically.
     * <br>Allows you to set a {@link DisableCause cause} for why the Hologram was disabled.
     *
     * @param cause The cause for why the Hologram was disabled.
     * @throws IllegalArgumentException When {@link DisableCause#NONE} is used as disable cause.
     */
    public void disable(@NonNull DisableCause cause) {
        if (cause == DisableCause.NONE)
            throw new IllegalArgumentException("Cannot use DisableCause NONE while disabling Hologram!");

        this.cause = cause;
        this.enabled = false;
    }

    /**
     * The cause for disabling the hologram.
     * <br>May return {@link DisableCause#NONE} if the Hologram is still enabled.
     *
     * @return The cause of why the Hologram is disabled, or {@link DisableCause#NONE} if it is still enabled.
     */
    @NonNull
    public DisableCause getDisableCause() {
        return cause;
    }

    /**
     * Check whether the given player is allowed to see this hologram object.
     *
     * @param player Given player.
     * @return Boolean whether the given player is allowed to see this hologram object.
     */
    public boolean canShow(Player player) {
        if (permission == null || permission.trim().isEmpty()) {
            return true;
        }
        return player != null && player.hasPermission(permission);
    }

    /*
     *	Location Methods
     */

    /**
     * Set facing direction of this hologram.
     *
     * @param facing New facing direction of this hologram.
     */
    public void setFacing(float facing) {
        this.facing = facing;
        this.location.setYaw(facing);
    }

    public void setLocation(@NonNull Location location) {
        this.location = location;
        this.location.setYaw(facing);
        this.location.setPitch(0.0f);
    }

    /*
     *	Viewer Methods
     */

    /**
     * Get List of all players that currently see this hologram.
     *
     * @return List of all players that currently see this hologram.
     */
    @NonNull
    public Set<UUID> getViewers() {
        return ImmutableSet.copyOf(viewers);
    }

    /**
     * Get List of all players that currently see this hologram.
     *
     * @return List of all players that currently see this hologram.
     */
    @NonNull
    public List<Player> getViewerPlayers() {
        return getViewers().stream()
                .map(Bukkit::getPlayer)
                .filter(player -> player != null && player.isOnline())
                .collect(Collectors.toList());
    }

    /**
     * Check whether this hologram object is visible to the given player.
     *
     * @param player Given player.
     * @return Boolean whether this hologram object is visible to the given player.
     */
    public boolean isVisible(@NonNull Player player) {
        return viewers.contains(player.getUniqueId());
    }

}

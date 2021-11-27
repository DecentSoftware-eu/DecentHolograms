package eu.decentsoftware.holograms.api.holograms.objects;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class HologramObject extends FlagHolder {

    /*
     *	Fields
     */

    protected boolean enabled = true;
    protected final Set<UUID> viewers = Collections.synchronizedSet(new HashSet<>());
    protected Location location;
    protected String permission = null;
    protected float facing = 0.0f;

    /*
     *	Constructors
     */

    public HologramObject(@Nonnull Location location) {
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
        this.enabled = true;
    }

    /**
     * Disable updating and showing to players automatically.
     */
    public void disable() {
        this.enabled = false;
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

    public void setLocation(Location location) {
        this.location = location;
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
    public Set<UUID> getViewers() {
        return ImmutableSet.copyOf(viewers);
    }

    /**
     * Get List of all players that currently see this hologram.
     *
     * @return List of all players that currently see this hologram.
     */
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
    public boolean isVisible(Player player) {
        return viewers.contains(player.getUniqueId());
    }

}

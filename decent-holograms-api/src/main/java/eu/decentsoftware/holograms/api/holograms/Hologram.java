package eu.decentsoftware.holograms.api.holograms;

import eu.decentsoftware.holograms.api.objects.IFlagsHolder;
import eu.decentsoftware.holograms.api.objects.ILocationHolder;
import eu.decentsoftware.holograms.api.objects.IPermissionHolder;
import eu.decentsoftware.holograms.api.objects.enums.EnumOrigin;
import eu.decentsoftware.holograms.utils.scheduler.ConsumerTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This interface represents a Hologram.
 */
public interface Hologram extends ILocationHolder, IPermissionHolder, IFlagsHolder {

	/*
	 *	General Methods
	 */

	/**
	 * Hide this hologram from all viewers and stop it from updating.
	 */
	void destroy();

	/**
	 * Hide this hologram from all players, stop it from updating and delete it completely.
	 */
	void delete();

	/**
	 * Save this hologram to a file.
	 */
	void save();

	/**
	 * Enable updating and showing to players automatically.
	 */
	void enable();

	/**
	 * Disable updating and showing to players automatically.
	 */
	void disable();

	/**
	 * Check whether this hologram is currently enabled.
	 * @return Boolean whether this hologram is currently enabled.
	 */
	boolean isEnabled();

	/**
	 * Set hologram origin.
	 * @param origin New value of origin.
	 */
	void setOrigin(EnumOrigin origin);

	/**
	 * Get hologram origin.
	 * @return Current value of origin.
	 */
	EnumOrigin getOrigin();

	/**
	 * Get hologram size. (Number of lines)
	 * @return Number of lines in this hologram.
	 */
	int size();

	/**
	 * Get height of this hologram in blocks.
	 * @return height of this hologram in blocks.
	 */
	double getHeight();

	/**
	 * Set facing direction of this hologram.
	 * @param facing New facing direction of this hologram.
	 */
	void setFacing(float facing);

	/**
	 * Get current facing direction of this hologram.
	 * @return current facing direction of this hologram.
	 */
	float getFacing();

	/**
	 * Get List of all players that currently see this hologram.
	 * @return List of all players that currently see this hologram.
	 */
	List<Player> getViewers();

	/**
	 * Get the name of this hologram.
	 * @return the name of this hologram.
	 */
	String getName();

	/**
	 * Create a new instance of hologram that is identical to this one.
	 *
	 * @param name Name of the clone.
	 * @param location Location of the clone.
	 * @param temp Boolean whether the clone is only temporary. (Not saved)
	 * @return Cloned instance of this hologram.
	 */
	Hologram clone(String name, Location location, boolean temp);

	/*
	 *	Visiblity Methods
	 */

	/**
	 * Show this hologram for given players.
	 * @param players Given players.
	 */
	void show(Player... players);

	/**
	 * Update this hologram for given players.
	 * @param players Given players.
	 */
	void update(Player... players);

	/**
	 * Hide this hologram for given players.
	 * @param players Given players.
	 */
	void hide(Player... players);

	/**
	 * Check whether this hologram is visible to the given player.
	 * @param player Given player.
	 * @return Boolean whether this hologram is visible to the given player.
	 */
	boolean isVisible(Player player);

	/**
	 * Check whether the given player is allowed to see this hologram.
	 * @param player Given player.
	 * @return Boolean whether the given player is allowed to see this hologram.
	 */
	boolean canShow(Player player);

	/**
	 * Check whether the given player is in display range of this hologram.
	 * @param player Given player.
	 * @return Boolean whether the given player is in display range of this hologram.
	 */
	boolean isInDisplayRange(Player player);

	/**
	 * Check whether the given player is in update range of this hologram.
	 * @param player Given player.
	 * @return Boolean whether the given player is in update range of this hologram.
	 */
	boolean isInUpdateRange(Player player);

	/*
	 *	Lines Methods
	 */

	/**
	 * Re-Aling the lines in this hologram putting them to the right place.
	 * <p>
	 *     This method is good to use after teleporting the hologram.
	 * </p>
	 */
	void realignLines();

	/**
	 * Swap two lines in this hologram.
	 * @param index1 First line.
	 * @param index2 Second line.
	 * @return Boolean whether the operation was successful.
	 */
	boolean swapLines(int index1, int index2);

	/**
	 * Insert a new line into this hologram.
	 * @param index Index of the new line.
	 * @param line New line.
	 * @return Boolean whether the operation was successful.
	 */
	boolean insertLine(int index, HologramLine line);

	/**
	 * Set new content of a line in this hologram.
	 * @param index Index of the line.
	 * @param content Line's new content.
	 * @return Boolean whether the operation was successful.
	 */
	boolean setLine(int index, String content);

	/**
	 * Add a new line on the bottom of this hologram.
	 * @param line New line.
	 * @return Boolean whether the operation was successful.
	 */
	boolean addLine(HologramLine line);

	/**
	 * Get line on a specific index in this hologram.
	 * @param index Index of the line.
	 * @return The HologramLine or null if it wasn't found.
	 */
	HologramLine getLine(int index);

	/**
	 * Remove a line from this hologram.
	 * @param index Index of the line.
	 * @return The removed line or null if it wasn't found.
	 */
	HologramLine removeLine(int index);

	/**
	 * Get the List of all lines in this hologram.
	 * @return List of all lines in this hologram.
	 */
	List<HologramLine> getLines();

	/**
	 * Get the Location at the bottom of this hologram that's available for a new line.
	 * @return the Location at the bottom of this hologram that's available for a new line.
	 */
	Location getNextLineLocation();

	/*
	 *	Updating Methods
	 */

	/**
	 * Set new value of Display Range.
	 * @param displayRange new value of Display Range.
	 */
	void setDisplayRange(int displayRange);

	/**
	 * Get the current value of Display Range.
	 * @return the current value of Display Range.
	 */
	int getDisplayRange();

	/**
	 * Set new value of Update Range.
	 * @param updateRange new value of Update Range.
	 */
	void setUpdateRange(int updateRange);

	/**
	 * Get the current value of Update Range.
	 * @return the current value of Update Range.
	 */
	int getUpdateRange();

	/**
	 * Set new value of Update Interval.
	 * @param updateInterval new value of Update Interval.
	 */
	void setUpdateInterval(int updateInterval);

	/**
	 * Get the current value of Update Interval.
	 * @return the current value of Update Interval.
	 */
	int getUpdateInterval();

	/**
	 * Start the update task of this hologram.
	 */
	void startUpdate();

	/**
	 * Stop the update task of this hologram.
	 */
	void stopUpdate();

	/**
	 * Get the update task of this hologram.
	 * @return the update task of this hologram.
	 */
	ConsumerTask<Hologram> getUpdateTask();

}

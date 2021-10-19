package eu.decentsoftware.holograms.api.holograms;

import eu.decentsoftware.holograms.api.objects.IFlagsHolder;
import eu.decentsoftware.holograms.api.objects.ILocationHolder;
import eu.decentsoftware.holograms.api.objects.IPermissionHolder;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * This interface represents a Hologram Line.
 */
public interface HologramLine extends ILocationHolder, IPermissionHolder, IFlagsHolder {

	/*
	 *	General Methods
	 */

	/**
	 * Hide this line to all viewers and stop it from updating.
	 */
	void destroy();

	/**
	 * Parse the current content String.
	 */
	void parseContent();

	/**
	 * Save this hologram line to a ConfigurationSection.
	 * @param config The ConfigurationSection.
	 */
	void save(ConfigurationSection config);

	/**
	 * Create a new instance of hologram line that's identical to this one.
	 * @param location Location of the clone.
	 * @return Cloned instance of this line.
	 */
	HologramLine clone(Location location);

	/**
	 * Get the type of this line.
	 * @return the type of this line.
	 */
	HologramLineType getType();

	/**
	 * Set the parent hologram of this line.
	 * @param parent New parent hologram of this line.
	 */
	void setParent(Hologram parent);

	/**
	 * Get the current parent hologram of this line.
	 * @return the current parent hologram of this line.
	 */
	Hologram getParent();

	/*
	 *	Visibility Methods
	 */

	/**
	 * Show this line for given players.
	 * @param players Given players.
	 */
	void show(Player... players);

	/**
	 * Hide this line for given players.
	 * @param players Given players.
	 */
	void hide(Player... players);

	/**
	 * Update this line for given players.
	 * @param players Given players.
	 */
	void update(Player... players);

	/**
	 * Update the location of this line for given players.
	 * @param players Given players.
	 */
	void updateLocation(Player... players);

	/**
	 * Check whether this line is visible to the given player.
	 * @param player Given player.
	 * @return Boolean whether this line is visible to the given player.
	 */
	boolean isVisible(Player player);

	/**
	 * Check whether the given player is allowed to see this line.
	 * @param player Given player.
	 * @return Boolean whether the given player is allowed to see this line.
	 */
	boolean canShow(Player player);

	/*
	 *	Content Methods
	 */

	/**
	 * Give this line a new content.
	 * @param content The new content.
	 */
	void setContent(String content);

	/**
	 * Get the current content of this line.
	 * @return the current content of this line.
	 */
	String getContent();

	/*
	 *	Heigth & Offset Methods
	 */

	/**
	 * Set new value of heigth.
	 * @param height new value of heigth.
	 */
	void setHeight(double height);

	/**
	 * Get the current value of heigth.
	 * @return the current value of heigth.
	 */
	double getHeight();

	/**
	 * Set new value of offsetX.
	 * @param offsetX new value of offsetX.
	 */
	void setOffsetX(double offsetX);

	/**
	 * Get the current value of offsetX.
	 * @return the current value of offsetX.
	 */
	double getOffsetX();

	/**
	 * Set new value of offsetY.
	 * @param offsetY new value of offsetY.
	 */
	void setOffsetY(double offsetY);

	/**
	 * Get the current value of offsetY.
	 * @return the current value of offsetY.
	 */
	double getOffsetY();

	/**
	 * Set new value of offsetZ.
	 * @param offsetZ new value of offsetZ.
	 */
	void setOffsetZ(double offsetZ);

	/**
	 * Get the current value of offsetZ.
	 * @return the current value of offsetZ.
	 */
	double getOffsetZ();

}

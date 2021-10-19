package eu.decentsoftware.holograms.api.features;

/**
 * This interface represents a feature. You can create your
 * own features doing anything you want.
 */
public interface IFeature {

	/**
	 * Reload this feature.
	 */
	void reload();

	/**
	 * Enable this feature.
	 */
	void enable();

	/**
	 * Disable this feature.
	 */
	void disable();

	/**
	 * Destroy this feature.
	 */
	void destroy();

	/**
	 * Check whether this feature is enabled.
	 *
	 * @return Boolean whether this feature is enabled.
	 */
	boolean isEnabled();

	/**
	 * Get the name of this feature.
	 *
	 * @return The name of this feature.
	 */
	String getName();

	/**
	 * Get description.
	 *
	 * @return description.
	 */
	String getDescription();

}

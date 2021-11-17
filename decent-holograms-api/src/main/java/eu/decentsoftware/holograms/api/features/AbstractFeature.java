package eu.decentsoftware.holograms.api.features;

/**
 * This interface represents a feature. You can create your
 * own features doing anything you want.
 */
public abstract class AbstractFeature {

	protected final String name;
	protected boolean enabled = false;

	public AbstractFeature(String name) {
		this.name = name;
	}

	/**
	 * Check whether this feature is enabled.
	 *
	 * @return Boolean whether this feature is enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	public boolean toggle() {
		if (isEnabled()) {
			disable();
		} else {
			enable();
		}
		return isEnabled();
	}

	/**
	 * Get the name of this feature.
	 *
	 * @return The name of this feature.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Reload this feature.
	 */
	public abstract void reload();

	/**
	 * Enable this feature.
	 */
	public abstract void enable();

	/**
	 * Disable this feature.
	 */
	public abstract void disable();

	/**
	 * Destroy this feature.
	 */
	public abstract void destroy();

	/**
	 * Get description.
	 *
	 * @return description.
	 */
	public abstract String getDescription();

}

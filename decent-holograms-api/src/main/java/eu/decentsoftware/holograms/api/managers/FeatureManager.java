package eu.decentsoftware.holograms.api.managers;

import eu.decentsoftware.holograms.api.features.IFeature;

import java.util.Collection;
import java.util.Set;

/**
 * This interface represents a Manager for handling features.
 */
public interface FeatureManager {

	/**
	 * Reload this manager and all the features.
	 */
	void reload();

	/**
	 * Destroy this manager and all the features.
	 */
	void destroy();

	/**
	 * Get a feature by name.
	 * @param name Name of the feature.
	 * @return The feature or null is it wasn't found.
	 */
	IFeature getFeature(String name);

	/**
	 * Register a new feature.
	 * @param feature The feature.
	 * @return The feature or null if it wasn't registered successfully.
	 */
	IFeature registerFeature(IFeature feature);

	/**
	 * Get the names of all registered features.
	 * @return Set of the names of all registered features.
	 */
	Set<String> getFeatureNames();

	/**
	 * Get all registered features.
	 * @return Collection of all registered features.
	 */
	Collection<IFeature> getFeatures();

}

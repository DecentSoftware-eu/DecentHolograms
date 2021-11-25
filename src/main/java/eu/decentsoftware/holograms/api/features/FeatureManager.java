package eu.decentsoftware.holograms.api.features;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a Manager for handling features.
 */
public class FeatureManager {

	private final Map<String, AbstractFeature> featureMap = Maps.newLinkedHashMap();

	/**
	 * Reload this manager and all the features.
	 */
	public void reload() {
		for (AbstractFeature value : featureMap.values()) {
			value.reload();
		}
	}

	/**
	 * Destroy this manager and all the features.
	 */
	public void destroy() {
		for (AbstractFeature value : featureMap.values()) {
			value.destroy();
		}
		featureMap.clear();
	}

	/**
	 * Get a feature by name.
	 * @param name Name of the feature.
	 * @return The feature or null is it wasn't found.
	 */
	public AbstractFeature getFeature(String name) {
		return featureMap.get(name);
	}

	/**
	 * Register a new feature.
	 * @param feature The feature.
	 * @return The feature or null if it wasn't registered successfully.
	 */
	public AbstractFeature registerFeature(AbstractFeature feature) {
		return featureMap.put(feature.getName(), feature);
	}

	/**
	 * Get the names of all registered features.
	 * @return Set of the names of all registered features.
	 */
	public Set<String> getFeatureNames() {
		return featureMap.keySet();
	}

	/**
	 * Get all registered features.
	 * @return Collection of all registered features.
	 */
	public Collection<AbstractFeature> getFeatures() {
		return featureMap.values();
	}

}

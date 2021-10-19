package eu.decentsoftware.holograms.core.managers;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.features.IFeature;
import eu.decentsoftware.holograms.api.managers.FeatureManager;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DefaultFeatureManager implements FeatureManager {

	private final Map<String, IFeature> featureMap = Maps.newLinkedHashMap();

	@Override
	public IFeature getFeature(String name) {
		return featureMap.get(name);
	}

	@Override
	public IFeature registerFeature(IFeature feature) {
		return featureMap.put(feature.getName(), feature);
	}

	@Override
	public Set<String> getFeatureNames() {
		return featureMap.keySet();
	}

	@Override
	public Collection<IFeature> getFeatures() {
		return featureMap.values();
	}

	@Override
	public void destroy() {
		for (IFeature value : featureMap.values()) {
			value.destroy();
		}
		featureMap.clear();
	}

	@Override
	public void reload() {
		for (IFeature value : featureMap.values()) {
			value.reload();
		}
	}

}

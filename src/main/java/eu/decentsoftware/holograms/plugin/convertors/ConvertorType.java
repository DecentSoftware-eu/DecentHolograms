package eu.decentsoftware.holograms.plugin.convertors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConvertorType {
	HOLOGRAPHIC_DISPLAYS("HolographicDisplays");

	public static ConvertorType getByName(String name) {
		for (ConvertorType convertorType : ConvertorType.values()) {
			if (convertorType.getName().equals(name)) return convertorType;
		}
		return null;
	}

	private final String name;

}

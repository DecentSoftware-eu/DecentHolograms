package eu.decentsoftware.holograms.plugin.convertors;

import java.util.Arrays;
import java.util.List;

public enum ConvertorType {
	HOLOGRAPHIC_DISPLAYS("HolographicDisplays", "DH", "hd");

	public static ConvertorType fromString(String alias) {
		for (ConvertorType convertorType : ConvertorType.values()) {
			if (convertorType.getName().equals(alias) || convertorType.getAliases().contains(alias)) {
				return convertorType;
			}
		}
		return null;
	}

	private final String name;
	private final List<String> aliases;

	ConvertorType(String name, String... aliases) {
		this.name = name;
		this.aliases = Arrays.asList(aliases);
	}

	public String getName() {
		return name;
	}

	public List<String> getAliases() {
		return aliases;
	}

}

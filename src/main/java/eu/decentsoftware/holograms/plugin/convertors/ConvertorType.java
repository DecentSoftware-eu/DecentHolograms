package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.convertor.IConvertor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ConvertorType {
	CMI("cmi"),
	FUTURE_HOLOGRAMS("FutureHolograms", "fh", "fholograms"),
	GHOLO("GHolo", "gholo", "gh"),
	HOLOGRAPHIC_DISPLAYS("HolographicDisplays", "DH", "hd");

	public static ConvertorType fromString(String alias) {
		for (ConvertorType convertorType : ConvertorType.values()) {
			if (convertorType.getName().equalsIgnoreCase(alias) || convertorType.getAliases().contains(alias)) {
				return convertorType;
			}
		}
		return null;
	}

	private final String name;
	private final List<String> aliases;

	ConvertorType(String name, String... aliases) {
		this.name = name;
		this.aliases = aliases == null ? Collections.emptyList() : Arrays.asList(aliases);
	}

	public String getName() {
		return name;
	}

	public List<String> getAliases() {
		return aliases;
	}
	
	public IConvertor getConvertor() {
		switch(this) {
			case CMI:
				return new CMIConverter();
			
			case FUTURE_HOLOGRAMS:
				return new FutureHologramsConverter();
			
			case GHOLO:
				return new GHoloConverter();
			
			case HOLOGRAPHIC_DISPLAYS:
				return new HolographicDisplaysConvertor();
			
			default:
				return null;
		}
	}

}

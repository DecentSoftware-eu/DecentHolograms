package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.plugin.convertors.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ConvertorType {
	CMI(true, "cmi"),
	FUTURE_HOLOGRAMS(true, "FutureHolograms", "fh", "fholograms"),
	GHOLO(false, "GHolo", "gholo", "gh"),
	HOLOGRAPHIC_DISPLAYS(false, "HolographicDisplays", "HD", "hd"),
	HOLOGRAMS(true, "Holograms"),
	;

	@Nullable
	public static ConvertorType fromString(String alias) {
		for (ConvertorType convertorType : ConvertorType.values()) {
			if (convertorType.getName().equalsIgnoreCase(alias) || convertorType.getAliases().contains(alias)) {
				return convertorType;
			}
		}
		return null;
	}

	private final @NotNull String name;
	private final @NotNull List<String> aliases;
	private final boolean limited;

	ConvertorType(boolean limited, @NotNull String name, String... aliases) {
		this.limited = limited;
		this.name = name;
		this.aliases = aliases == null ? Collections.emptyList() : Arrays.asList(aliases);
	}

	@NotNull
	public String getName() {
		return name;
	}

	@NotNull
	public List<String> getAliases() {
		return aliases;
	}

	public boolean isLimited() {
		return limited;
	}

	@Nullable
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
			case HOLOGRAMS:
				return new HologramsConvertor();
			default:
				return null;
		}
	}

}

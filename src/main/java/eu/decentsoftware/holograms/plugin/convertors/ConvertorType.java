package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.plugin.convertors.impl.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Getter
public enum ConvertorType {
	CMI(true, "CMI"),
	FUTURE_HOLOGRAMS(true, "FutureHolograms", "fh", "fholograms"),
	GHOLO(false, "GHolo", "gh"),
	HOLOGRAPHIC_DISPLAYS(false, "HolographicDisplays", "hd"),
	HOLOGRAMS(true, "Holograms"),
	;

	@Nullable
	public static ConvertorType fromString(String alias) {
		for (ConvertorType convertorType : ConvertorType.values()) {
			if (convertorType.getName().equalsIgnoreCase(alias) || convertorType.getAliases().contains(alias.toLowerCase(Locale.ROOT))) {
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

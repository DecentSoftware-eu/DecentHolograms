package eu.decentsoftware.holograms.plugin.convertors;

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

    private final String name;
    private final List<String> aliases;
    private final boolean limited;

    ConvertorType(boolean limited, @NotNull String name, String... aliases) {
        this.limited = limited;
        this.name = name;
        this.aliases = aliases == null ? Collections.emptyList() : Arrays.asList(aliases);
    }
}

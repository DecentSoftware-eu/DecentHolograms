package eu.decentsoftware.holograms.api.utils.reflect;

import eu.decentsoftware.holograms.platform.api.server.MinecraftVersion;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatform;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformType;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

/**
 * Enum of supported NMS versions.
 *
 * <p>Each constant maps a set of Minecraft versions and a server platform onto the name of an NMS
 * module. A constant declaring {@link ServerPlatformType#BUKKIT} works anywhere in the Bukkit
 * family; a constant declaring something more specific exists because that platform needs its own
 * mappings, and takes precedence there.</p>
 */
@SuppressWarnings("java:S115") // SonarLint: Enum values naming convention
public enum Version {
    v1_8_R1(8, ServerPlatformType.BUKKIT, "1.8"),
    v1_8_R2(8, ServerPlatformType.BUKKIT, "1.8.3"),
    v1_8_R3(8, ServerPlatformType.BUKKIT, "1.8.4", "1.8.5", "1.8.6", "1.8.7", "1.8.8"),
    v1_9_R1(9, ServerPlatformType.BUKKIT, "1.9", "1.9.2"),
    v1_9_R2(9, ServerPlatformType.BUKKIT, "1.9.4"),
    v1_10_R1(10, ServerPlatformType.BUKKIT, "1.10", "1.10.2"),
    v1_11_R1(11, ServerPlatformType.BUKKIT, "1.11", "1.11.1", "1.11.2"),
    v1_12_R1(12, ServerPlatformType.BUKKIT, "1.12", "1.12.1", "1.12.2"),
    v1_13_R1(13, ServerPlatformType.BUKKIT, "1.13"),
    v1_13_R2(13, ServerPlatformType.BUKKIT, "1.13.1", "1.13.2"),
    v1_14_R1(14, ServerPlatformType.BUKKIT, "1.14", "1.14.1", "1.14.2", "1.14.3", "1.14.4"),
    v1_15_R1(15, ServerPlatformType.BUKKIT, "1.15", "1.15.1", "1.15.2"),
    v1_16_R1(16, ServerPlatformType.BUKKIT, "1.16", "1.16.1"),
    v1_16_R2(16, ServerPlatformType.BUKKIT, "1.16.2", "1.16.3"),
    v1_16_R3(16, ServerPlatformType.BUKKIT, "1.16.4", "1.16.5"),
    v1_17_R1(17, ServerPlatformType.BUKKIT, "1.17", "1.17.1"),
    v1_18_R1(18, ServerPlatformType.BUKKIT, "1.18", "1.18.1"),
    v1_18_R2(18, ServerPlatformType.BUKKIT, "1.18.2"),
    v1_19_R1(19, ServerPlatformType.BUKKIT, "1.19", "1.19.1", "1.19.2"),
    v1_19_R2(19, ServerPlatformType.BUKKIT, "1.19.3"),
    v1_19_R3(19, ServerPlatformType.BUKKIT, "1.19.4"),
    v1_20_R1(20, ServerPlatformType.BUKKIT, "1.20", "1.20.1"),
    v1_20_R2(20, ServerPlatformType.BUKKIT, "1.20.2"),
    v1_20_R3(20, ServerPlatformType.BUKKIT, "1.20.3", "1.20.4"),
    v1_20_R4(20, ServerPlatformType.BUKKIT, "1.20.5", "1.20.6"),
    v1_21_R1(21, ServerPlatformType.BUKKIT, "1.21", "1.21.1"),
    v1_21_R2(21, ServerPlatformType.BUKKIT, "1.21.2", "1.21.3"),
    v1_21_R3(21, ServerPlatformType.BUKKIT, "1.21.4"),
    v1_21_R4(21, ServerPlatformType.BUKKIT, "1.21.5"),
    v1_21_R5(21, ServerPlatformType.BUKKIT, "1.21.6", "1.21.7", "1.21.8"),
    v1_21_R6(21, ServerPlatformType.BUKKIT, "1.21.9", "1.21.10"),
    paper_v1_21_R6(21, ServerPlatformType.PAPER, "1.21.9", "1.21.10"),
    v1_21_R7(21, ServerPlatformType.BUKKIT, "1.21.11"),
    paper_v1_21_R7(21, ServerPlatformType.PAPER, "1.21.11"),
    v26_1(26, ServerPlatformType.BUKKIT, "26.1", "26.1.1", "26.1.2"),
    v26_2(26, ServerPlatformType.BUKKIT, "26.2"),
    ;

    /*
     *  Resolution
     */

    /**
     * Finds the NMS module to use on the given server.
     *
     * <p>More than one constant can match, because a platform also matches entries declared for
     * anything it derives from. The most specific one wins, so a Paper server loads the
     * Paper-mapped module rather than the family-wide one.</p>
     *
     * @param serverPlatform The detected server platform.
     * @return The matching module, or empty if this server version is unsupported.
     */
    @NotNull
    public static Optional<Version> resolve(@NotNull ServerPlatform serverPlatform) {
        Objects.requireNonNull(serverPlatform, "serverPlatform cannot be null");
        return resolve(serverPlatform.getMinecraftVersion(), serverPlatform.getType());
    }

    /**
     * @param minecraftVersion The Minecraft version the server is running.
     * @param platformType     The server platform.
     * @return The matching module, or empty if this server version is unsupported.
     */
    @NotNull
    public static Optional<Version> resolve(@NotNull MinecraftVersion minecraftVersion,
                                            @NotNull ServerPlatformType platformType) {
        Objects.requireNonNull(minecraftVersion, "minecraftVersion cannot be null");
        Objects.requireNonNull(platformType, "platformType cannot be null");

        Version best = null;
        for (Version candidate : values()) {
            if (!platformType.isA(candidate.platform) || !candidate.supports(minecraftVersion)) {
                continue;
            }
            if (best == null || candidate.platform.getSpecificity() > best.platform.getSpecificity()) {
                best = candidate;
            }
        }
        return Optional.ofNullable(best);
    }

    /**
     * Parse a Version from string.
     *
     * @param version The string.
     * @return The parsed Version or null.
     */
    @Nullable
    public static Version fromString(String version) {
        if (version == null) {
            return null;
        }

        for (Version value : Version.values()) {
            if (value.name().equalsIgnoreCase(version)) {
                return value;
            }
        }
        return null;
    }

    private static volatile Version current;

    /**
     * Records the module resolved for this server, so the deprecated version checks below keep
     * working while they are migrated away.
     *
     * @param version The resolved module.
     */
    public static void setCurrent(@NotNull Version version) {
        current = Objects.requireNonNull(version, "version cannot be null");
    }

    private static Version current() {
        Version resolved = current;
        if (resolved == null) {
            throw new IllegalStateException("Version has not been resolved yet. "
                    + "The platform must be detected before any version check runs.");
        }
        return resolved;
    }

    public static boolean is(int minor) {
        return current().getMinor() == minor;
    }

    public static boolean is(@NonNull Version version) {
        return current() == version;
    }

    public static boolean after(int minor) {
        return current().getMinor() > minor;
    }

    public static boolean after(@NonNull Version version) {
        return current().ordinal() > version.ordinal();
    }

    public static boolean afterOrEqual(int minor) {
        return current().getMinor() >= minor;
    }

    public static boolean afterOrEqual(@NonNull Version version) {
        return current().ordinal() >= version.ordinal();
    }

    public static boolean before(int minor) {
        return current().getMinor() < minor;
    }

    public static boolean before(@NonNull Version version) {
        return current().ordinal() < version.ordinal();
    }

    public static boolean beforeOrEqual(int minor) {
        return current().getMinor() <= minor;
    }

    public static boolean beforeOrEqual(@NonNull Version version) {
        return current().ordinal() <= version.ordinal();
    }

    public static boolean supportsHex() {
        return afterOrEqual(16);
    }

    /*
     *  Version
     */

    private final int minor;
    private final ServerPlatformType platform;
    private final List<MinecraftVersion> minecraftVersions;

    Version(int minor, ServerPlatformType platform, String... minecraftVersions) {
        this.minor = minor;
        this.platform = platform;
        List<MinecraftVersion> parsed = new ArrayList<>(minecraftVersions.length);
        for (String minecraftVersion : minecraftVersions) {
            parsed.add(MinecraftVersion.parse(minecraftVersion));
        }
        this.minecraftVersions = Collections.unmodifiableList(parsed);
    }

    public int getMinor() {
        return minor;
    }

    /**
     * @return The platform this module is built for. {@link ServerPlatformType#BUKKIT} means it
     * works anywhere in the Bukkit family.
     */
    @NotNull
    public ServerPlatformType getPlatform() {
        return platform;
    }

    @NotNull
    public List<MinecraftVersion> getMinecraftVersions() {
        return minecraftVersions;
    }

    /**
     * @param minecraftVersion The version to test.
     * @return True if this module covers the given Minecraft version.
     */
    public boolean supports(@NotNull MinecraftVersion minecraftVersion) {
        return minecraftVersions.contains(minecraftVersion);
    }
}

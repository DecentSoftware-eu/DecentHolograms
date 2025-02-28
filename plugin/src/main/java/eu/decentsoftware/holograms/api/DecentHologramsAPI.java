package eu.decentsoftware.holograms.api;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * This class is used to access DecentHologramsAPI. You can use this class
 * to get the instance of running DecentHolograms.
 *
 * @author d0by
 * @see DecentHolograms
 */
@UtilityClass
public final class DecentHologramsAPI {

    private static DecentHolograms implementation;
    private static boolean enabled = false;

    /**
     * <b>This is an internal method. Do not use it.</b>
     * <p>
     * Load DecentHologramsAPI. This method will be called by DecentHolograms
     * plugin when it is being loaded.
     *
     * @param plugin The instance of DecentHolograms plugin.
     */
    @ApiStatus.Internal
    public static void onLoad(@NonNull JavaPlugin plugin) {
        if (implementation != null) {
            return;
        }
        implementation = new DecentHolograms(plugin);
    }

    /**
     * <b>This is an internal method. Do not use it.</b>
     * <p>
     * Enable DecentHologramsAPI. This method will be called by DecentHolograms
     * plugin when it is being  enabled.
     */
    @ApiStatus.Internal
    public static void onEnable() {
        if (implementation == null) {
            return;
        }
        enabled = true;
        implementation.enable();
    }

    /**
     * <b>This is an internal method. Do not use it.</b>
     * <p>
     * Disable DecentHologramsAPI. This method will be called by DecentHolograms
     * plugin when it is being disabled.
     */
    @ApiStatus.Internal
    public static void onDisable() {
        if (implementation == null) {
            return;
        }
        implementation.disable();
        implementation = null;
        enabled = false;
    }

    /**
     * Check whether DecentHologramsAPI is currently running and ready for use.
     *
     * @return True if DecentHologramsAPI is running, false otherwise.
     */
    public static boolean isRunning() {
        return implementation != null && enabled;
    }

    /**
     * Get the instance of running DecentHolograms. This method will throw
     * an exception if DecentHologramsAPI is not running. You can check whether
     * DecentHologramsAPI is running by using {@link #isRunning()}.
     * <p>
     * You might need to wait until DecentHologramsAPI is fully enabled before
     * using this method. You can check whether DecentHologramsAPI is enabled
     * by using {@link #isRunning()}. Alternatively, you can use the
     * {@link PluginEnableEvent} event to detect when DecentHologramsAPI
     * is enabled.
     *
     * @return The instance of running DecentHolograms (if running).
     * @throws IllegalStateException If DecentHologramsAPI is not running.
     * @see #isRunning()
     * @see PluginEnableEvent
     */
    public static DecentHolograms get() {
        if (implementation == null || !enabled) {
            throw new IllegalStateException("DecentHolograms is not running (yet). Do you have DecentHolograms plugin installed?");
        }
        return implementation;
    }

}

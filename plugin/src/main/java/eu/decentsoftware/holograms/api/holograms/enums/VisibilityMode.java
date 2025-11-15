package eu.decentsoftware.holograms.api.holograms.enums;

/**
 * Represents the visibility mode for a hologram.
 * This allows plugins to control whether a hologram should be automatically shown/hidden
 * based on display range or if it should be forced to a specific state.
 *
 * @author d0by
 * @since 3.0.0
 */
public enum VisibilityMode {

    /**
     * Default behavior - hologram visibility is controlled by display range
     * and other standard DecentHolograms logic.
     */
    DEFAULT,

    /**
     * Force the hologram to be hidden for the player.
     * The hologram will NOT be automatically shown when the player enters display range.
     * This is useful for optimization plugins that want to hide holograms without
     * constantly checking and re-hiding them.
     */
    FORCE_HIDDEN,

    /**
     * Force the hologram to be visible for the player.
     * The hologram will be shown regardless of display range.
     * This is useful for important holograms that should always be visible.
     */
    FORCE_VISIBLE

}

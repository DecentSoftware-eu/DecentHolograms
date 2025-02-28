package eu.decentsoftware.holograms.api.holograms;

/**
 * Used in {@link Hologram#disable(DisableCause)}to set a cause
 * for disabling the Hologram in question.
 * 
 * <p>This is mainly used for when a world is loaded to determine,
 * if the Hologram has been unloaded by command, the
 * API or through the world being unloaded.
 */
public enum DisableCause{
    API,
    COMMAND,
    WORLD_UNLOAD,
    
    NONE
}

package eu.decentsoftware.holograms.api.expansion.context;

/**
 * Interface for handling events related to the lifecycle of an ExpansionContext.
 *
 * @author ZorTik
 */
public interface ExpansionContextEventHandler {

    /**
     * Called when the context is closed.
     *
     * @param context the context that was closed
     */
    default void onContextClosed(ExpansionContext context) {
    }
}

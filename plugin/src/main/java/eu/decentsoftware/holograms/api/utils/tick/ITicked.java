package eu.decentsoftware.holograms.api.utils.tick;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;

/**
 * This interface is used to create tickable objects. Any class, that implements this interface,
 * can be registered to DecentHologramsAPI ticker. This ticker will call the tick() method every
 * interval ticks. The interval is defined by the {@link #getInterval()} method.
 *
 * @author d0by
 * @see Ticker
 */
public interface ITicked {

    /**
     * Get the ID of this tickable object. This ID is used to identify
     * the object in the ticker.
     *
     * @return The ID of this tickable object.
     */
    String getId();

    /**
     * This method is called every interval ticks. The interval is defined
     * by the {@link #getInterval()} method.
     */
    void tick();

    /**
     * Get the interval of this tickable object. This interval is used to
     * determine how often the {@link #tick()} method should be called.
     *
     * @return The interval of this tickable object.
     */
    long getInterval();

    /**
     * Check whether the tickable object should tick on the given tick. This
     * method is used by the ticker to determine whether the {@link #tick()} method
     * should be called on the given tick.
     *
     * @param tick The tick to check.
     * @return True if the tickable object should tick on the given tick, false otherwise.
     */
    default boolean shouldTick(long tick) {
        return tick % getInterval() == 0;
    }

    /**
     * Register this tickable object to the ticker. By registering the object,
     * the ticker will call the {@link #tick()} method every interval ticks.
     */
    default void register() {
        DecentHologramsAPI.get().getTicker().register(this);
    }

    /**
     * Unregister this tickable object from the ticker. By unregistering the object,
     * the ticker will no longer call the {@link #tick()} method.
     */
    default void unregister() {
        DecentHologramsAPI.get().getTicker().unregister(getId());
    }

}

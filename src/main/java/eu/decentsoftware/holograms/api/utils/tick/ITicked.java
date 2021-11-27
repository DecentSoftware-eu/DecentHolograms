package eu.decentsoftware.holograms.api.utils.tick;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;

public interface ITicked {

    String getId();

    void tick();

    long getInterval();

    default boolean shouldTick(long tick) {
        return tick % getInterval() == 0;
    }

    default void register() {
        DecentHologramsAPI.get().getTicker().register(this);
    }

    default void unregister() {
        DecentHologramsAPI.get().getTicker().unregister(getId());
    }

}

package eu.decentsoftware.holograms.api.utils.tick;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Ticked implements ITicked {

    private final String id;
    private final AtomicLong interval;

    protected Ticked(long interval) {
        this(UUID.randomUUID().toString(), interval);
    }

    protected Ticked(String id, long interval) {
        this.id = id;
        this.interval = new AtomicLong(interval);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getInterval() {
        return interval.get();
    }

}

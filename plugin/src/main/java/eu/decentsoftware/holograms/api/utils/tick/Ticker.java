package eu.decentsoftware.holograms.api.utils.tick;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.scheduler.S;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Ticker {

    private final int taskId;
    private final AtomicLong ticks;
    private final Map<String, ITicked> tickedObjects;
    private volatile boolean performingTick;

    /**
     * Default constructor. Ticker is initialized and started.
     */
    public Ticker() {
        this.ticks = new AtomicLong(0);
        this.tickedObjects = new ConcurrentHashMap<>();
        this.performingTick = false;
        this.taskId = S.asyncTask(() -> {
            if (!performingTick) tick();
        }, 1L, 5L).getTaskId();
    }

    /**
     * Stop the ticker and unregister all ticked objects.
     */
    public void destroy() {
        S.stopTask(taskId);
        tickedObjects.clear();
    }

    /**
     * Register a new ticked object.
     *
     * @param ticked The ticked object.
     */
    public void register(ITicked ticked) {
        tickedObjects.put(ticked.getId(), ticked);
    }

    /**
     * Unregister a ticked object.
     *
     * @param id The id of the ticked object.
     */
    public void unregister(String id) {
        tickedObjects.remove(id);
    }

    private void tick() {
        performingTick = true;

        // Tick all ticked objects
        tickedObjects.forEach((id, ticked) -> {
            if (ticked.shouldTick(ticks.get())) {
                try {
                    ticked.tick();
                } catch (Exception ex) {
                    Log.warn("无法执行对象的tick操作: %s", ex, ticked.getId(), ticked.getClass().getSimpleName());
                }
            }
        });

        performingTick = false;
        ticks.incrementAndGet();
    }

}

package eu.decentsoftware.holograms.api.utils.scheduler;

import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayerService;
import eu.decentsoftware.holograms.platform.api.scheduler.PlatformScheduler;
import eu.decentsoftware.holograms.platform.api.scheduler.TaskHandle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Static entry point to the platform scheduler.
 *
 * <p>This exists so that the older parts of the plugin, which have no constructor injection to
 * hook into, can still schedule work without knowing about the platform. New code should take a
 * {@link PlatformScheduler} directly.</p>
 */
public final class S {

    private static volatile PlatformScheduler scheduler;
    private static volatile PlatformPlayerService playerService;

    private S() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Binds this facade to the platform. Called once while the plugin is enabling.
     *
     * @param scheduler     The platform scheduler.
     * @param playerService The platform player service, used to resolve players for {@link #forPlayer}.
     * @throws NullPointerException If either argument is null.
     */
    public static void initialize(@NotNull PlatformScheduler scheduler, @NotNull PlatformPlayerService playerService) {
        Objects.requireNonNull(scheduler, "scheduler cannot be null");
        Objects.requireNonNull(playerService, "playerService cannot be null");
        S.scheduler = scheduler;
        S.playerService = playerService;
    }

    /**
     * Runs a task that acts on the given player, or on the world around them.
     *
     * @param player   The player the task acts on.
     * @param runnable The task.
     * @return A handle to the scheduled task.
     */
    public static TaskHandle forPlayer(Player player, Runnable runnable) {
        return scheduler().runForPlayer(platformPlayer(player), runnable);
    }

    /**
     * Runs a task that acts on the given player, handing the task the platform-agnostic view of
     * them so it does not have to resolve the player a second time.
     *
     * @param player   The player the task acts on.
     * @param consumer The task.
     * @return A handle to the scheduled task.
     */
    public static TaskHandle forPlayer(Player player, Consumer<PlatformPlayer> consumer) {
        PlatformPlayer platformPlayer = platformPlayer(player);
        return scheduler().runForPlayer(platformPlayer, () -> consumer.accept(platformPlayer));
    }

    /**
     * Runs a task that touches only server-wide state.
     *
     * @param runnable The task.
     * @return A handle to the scheduled task.
     */
    public static TaskHandle sync(Runnable runnable) {
        return scheduler().runGlobal(runnable);
    }

    public static TaskHandle async(Runnable runnable) {
        return scheduler().runAsync(runnable);
    }

    public static TaskHandle async(Runnable runnable, long delay) {
        return scheduler().runAsyncLater(runnable, delay);
    }

    /**
     * Repeatedly runs a task that touches no platform state.
     *
     * @param runnable The task.
     * @param interval Ticks between executions.
     * @param delay    Ticks to wait before the first execution.
     * @return A handle to the scheduled task.
     */
    public static TaskHandle asyncTask(Runnable runnable, long delay, long interval) {
        return scheduler().runAsyncTimer(runnable, delay, interval);
    }

    private static PlatformScheduler scheduler() {
        PlatformScheduler current = scheduler;
        if (current == null) {
            throw new IllegalStateException("Scheduler is not set. S#initialize must be called before scheduling tasks.");
        }
        return current;
    }

    private static PlatformPlayer platformPlayer(Player player) {
        Objects.requireNonNull(player, "player cannot be null");
        PlatformPlayerService current = playerService;
        if (current == null) {
            throw new IllegalStateException("Player service is not set. S#initialize must be called before scheduling tasks.");
        }
        return current.getPlayer(player);
    }
}

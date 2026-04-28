package eu.decentsoftware.holograms.api.utils.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class S {

    private static JavaPlugin plugin;
    private static boolean folia;
    private static final Set<TaskHandle> TASKS = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public interface TaskHandle {
        void cancel();
    }

    private enum NoopTaskHandle implements TaskHandle {
        INSTANCE;

        @Override
        public void cancel() {
        }
    }

    private static final class BukkitTaskHandle implements TaskHandle {
        private final int id;

        BukkitTaskHandle(int id) {
            this.id = id;
        }

        @Override
        public void cancel() {
            TASKS.remove(this);
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    private static final class ReflectiveTaskHandle implements TaskHandle {
        private final Object scheduledTask;
        private final Method cancelMethod;

        ReflectiveTaskHandle(Object scheduledTask, Method cancelMethod) {
            this.scheduledTask = scheduledTask;
            this.cancelMethod = cancelMethod;
        }

        @Override
        public void cancel() {
            TASKS.remove(this);
            try {
                cancelMethod.invoke(scheduledTask);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to cancel reflective task", e);
            }
        }
    }

    public static synchronized void init(JavaPlugin owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        plugin = owner;
        folia = classExists("io.papermc.paper.threadedregions.RegionizedServer");
    }

    public static synchronized void shutdown() {
        TaskHandle[] handles = TASKS.toArray(new TaskHandle[0]);
        TASKS.clear();
        for (TaskHandle handle : handles) {
            handle.cancel();
        }
        if (plugin != null && !folia) {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
        plugin = null;
        folia = false;
    }

    public static boolean isFolia() {
        return folia;
    }

    public static void stopTask(int id) {
        ensureInitialized();
        Bukkit.getScheduler().cancelTask(id);
    }

    public static void stopTask(TaskHandle task) {
        if (task != null) {
            task.cancel();
        }
    }

    public static TaskHandle sync(Runnable runnable) {
        return global(runnable);
    }

    public static TaskHandle sync(Runnable runnable, long delay) {
        return global(runnable, delay);
    }

    public static TaskHandle global(Runnable runnable) {
        ensureInitialized();
        requireNonNull(runnable, "runnable");
        if (folia) {
            Object scheduler = invoke(plugin, "getGlobalRegionScheduler", null);
            Object task = invoke(scheduler, "run", new Class<?>[]{Runnable.class}, runnable);
            return reflectiveHandle(task);
        } else {
            BukkitTask task = Bukkit.getScheduler().runTask(plugin, runnable);
            return new BukkitTaskHandle(task.getTaskId());
        }
    }

    public static TaskHandle global(Runnable runnable, long delay) {
        ensureInitialized();
        requireNonNull(runnable, "runnable");
        if (delay <= 0) {
            return global(runnable);
        }
        if (folia) {
            Object scheduler = invoke(plugin, "getGlobalRegionScheduler", null);
            Object task = invoke(scheduler, "runDelayed", new Class<?>[]{Runnable.class, long.class}, runnable, delay * 50L);
            return reflectiveHandle(task);
        } else {
            BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
            return new BukkitTaskHandle(task.getTaskId());
        }
    }

    public static TaskHandle entity(Entity entity, Runnable runnable) {
        ensureInitialized();
        requireNonNull(entity, "entity");
        requireNonNull(runnable, "runnable");
        if (folia) {
            Object scheduler = invoke(entity, "getScheduler", null);
            Runnable callback = () -> {
            };
            Object task = invoke(scheduler, "run", new Class<?>[]{Runnable.class, Consumer.class}, runnable, callback);
            return reflectiveHandle(task);
        } else {
            BukkitTask task = Bukkit.getScheduler().runTask(plugin, runnable);
            return new BukkitTaskHandle(task.getTaskId());
        }
    }

    public static TaskHandle entity(Entity entity, Runnable runnable, long delay) {
        ensureInitialized();
        requireNonNull(entity, "entity");
        requireNonNull(runnable, "runnable");
        if (delay <= 0) {
            return entity(entity, runnable);
        }
        if (folia) {
            Object scheduler = invoke(entity, "getScheduler", null);
            Runnable callback = () -> {
            };
            Object task = invoke(scheduler, "runDelayed", new Class<?>[]{Runnable.class, Consumer.class, long.class}, runnable, callback, delay * 50L);
            return reflectiveHandle(task);
        } else {
            BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
            return new BukkitTaskHandle(task.getTaskId());
        }
    }

    public static TaskHandle async(Runnable runnable) {
        ensureInitialized();
        requireNonNull(runnable, "runnable");
        if (folia) {
            Object scheduler = invoke(plugin, "getAsyncScheduler", null);
            Object task = invoke(scheduler, "runNow", new Class<?>[]{Runnable.class}, runnable);
            return reflectiveHandle(task);
        } else {
            BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
            return new BukkitTaskHandle(task.getTaskId());
        }
    }

    public static TaskHandle async(Runnable runnable, long delay) {
        ensureInitialized();
        requireNonNull(runnable, "runnable");
        if (delay <= 0) {
            return async(runnable);
        }
        if (folia) {
            Object scheduler = invoke(plugin, "getAsyncScheduler", null);
            Object task = invoke(scheduler, "runDelayed", new Class<?>[]{Runnable.class, long.class, TimeUnit.class}, runnable, delay * 50L, TimeUnit.MILLISECONDS);
            return reflectiveHandle(task);
        } else {
            BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
            return new BukkitTaskHandle(task.getTaskId());
        }
    }

    public static TaskHandle asyncTask(Runnable runnable, long interval) {
        return asyncTask(runnable, interval, 0);
    }

    public static TaskHandle asyncTask(Runnable runnable, long interval, long delay) {
        ensureInitialized();
        requireNonNull(runnable, "runnable");
        if (interval <= 0) {
            throw new IllegalArgumentException("interval must be positive");
        }
        if (folia) {
            Object scheduler = invoke(plugin, "getAsyncScheduler", null);
            Object task = invoke(scheduler, "runAtFixedRate", new Class<?>[]{Runnable.class, long.class, long.class, TimeUnit.class}, runnable, delay * 50L, interval * 50L, TimeUnit.MILLISECONDS);
            return reflectiveHandle(task);
        } else {
            BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
            return new BukkitTaskHandle(task.getTaskId());
        }
    }

    public static CompletableFuture<Boolean> teleport(Player player, Location location) {
        requireNonNull(player, "player");
        requireNonNull(location, "location");
        ensureInitialized();
        if (folia) {
            Method teleportAsync = method(player.getClass(), "teleportAsync", Location.class);
            if (teleportAsync != null) {
                try {
                    Object result = invoke(player, "teleportAsync", new Class<?>[]{Location.class}, location);
                    if (result instanceof CompletableFuture) {
                        return (CompletableFuture<Boolean>) result;
                    }
                    CompletableFuture<Boolean> future = new CompletableFuture<>();
                    if (Boolean.TRUE.equals(result)) {
                        future.complete(true);
                    } else {
                        entity(player, () -> future.complete(player.teleport(location)));
                    }
                    return future;
                } catch (Exception e) {
                    CompletableFuture<Boolean> future = new CompletableFuture<>();
                    future.completeExceptionally(e);
                    return future;
                }
            }
        }
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        entity(player, () -> {
            try {
                result.complete(player.teleport(location));
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
        });
        return result;
    }

    private static void ensureInitialized() {
        if (plugin == null) {
            throw new IllegalStateException("S has not been initialized. Call S.init() first.");
        }
    }

    private static void requireNonNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
    }

    private static boolean classExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Method method(Class<?> type, String name, Class<?>... parameterTypes) {
        try {
            return type.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Object invoke(Object target, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to invoke " + methodName + " on " + target.getClass().getName(), e);
        }
    }

    private static TaskHandle reflectiveHandle(Object scheduledTask) {
        if (scheduledTask == null) {
            return NoopTaskHandle.INSTANCE;
        }
        try {
            Method cancelMethod = scheduledTask.getClass().getMethod("cancel");
            TaskHandle handle = new ReflectiveTaskHandle(scheduledTask, cancelMethod);
            TASKS.add(handle);
            return handle;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Scheduled task does not have a cancel method", e);
        }
    }

}

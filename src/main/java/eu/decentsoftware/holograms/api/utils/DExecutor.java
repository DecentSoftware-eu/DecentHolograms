package eu.decentsoftware.holograms.api.utils;

import eu.decentsoftware.holograms.api.utils.collection.DList;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DExecutor {

    private static ExecutorService service;
    private static int threadId;

    public static void init(int threads) {
        threadId = 0;
        service = Executors.newFixedThreadPool(threads, (runnable) -> {
            Thread thread = new Thread(runnable);
            thread.setName("DecentHolograms Thread #" + ++threadId);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.setUncaughtExceptionHandler((t, ex) -> {
                Common.log("Exception encountered in " + t.getName());
                ex.printStackTrace();
            });
            return thread;
        });
    }

    public static void schedule(Runnable... runnables) {
        if (runnables == null || runnables.length == 0) {
            return;
        }
        create(runnables.length).queue(runnables).complete();
    }

    public static DExecutor create(int estimate) {
        return new DExecutor(service, estimate);
    }

    private final ExecutorService executor;
    private final DList<CompletableFuture<Void>> running;

    public DExecutor(ExecutorService executor, int estimate) {
        this.executor = executor;
        this.running = new DList<>(estimate);
    }

    public CompletableFuture<Void> queue(Runnable r) {
        synchronized(running) {
            CompletableFuture<Void> c = CompletableFuture.runAsync(r, executor);
            running.add(c);
            return c;
        }
    }

    public DExecutor queue(Runnable... runnables) {
        if (runnables == null || runnables.length == 0) {
            return this;
        }

        synchronized (running) {
            for (Runnable runnable : runnables) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(runnable, executor);
                running.add(future);
            }
        }
        return this;
    }

    public void complete() {
        synchronized(running) {
            try {
                CompletableFuture.allOf(running.toArray(new CompletableFuture[0])).get();
                running.clear();
            } catch(InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}

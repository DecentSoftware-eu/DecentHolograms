package eu.decentsoftware.holograms.api.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.bungee.BungeeServerInfoRetriever;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class BungeeUtils {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    @Getter
    private static final Map<String, Integer> serverOnlineCache = new HashMap<>();
    private static final Cache<String, Long> lastServerCheck = CacheBuilder.newBuilder()
            .expireAfterWrite(3L, TimeUnit.SECONDS) // 3 seconds delay for checking for servers should be okay since we don't want to flood the plugin message channel
            .build();
    @Getter
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(DECENT_HOLOGRAMS.getPlugin(), "BungeeCord");
        messenger.registerIncomingPluginChannel(DECENT_HOLOGRAMS.getPlugin(), "BungeeCord", new BungeeServerInfoRetriever()); // register player count checker
        initialized = true;
    }

    public static void destroy() {
        if (!initialized) return;
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.unregisterOutgoingPluginChannel(DECENT_HOLOGRAMS.getPlugin(), "BungeeCord");
        messenger.unregisterIncomingPluginChannel(DECENT_HOLOGRAMS.getPlugin(), "BungeeCord"); // unregister player count checker
        initialized = false;
    }

    public static void connect(Player player, String server) {
        if (!initialized) init();
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(DECENT_HOLOGRAMS.getPlugin(), "BungeeCord", out.toByteArray());
        } catch (Exception ignored) {}
    }

    public static void retrieveOnlinePlayers(Player player, String server) {
        if (lastServerCheck.asMap().containsKey(server)) {
            return; // do not check for players
        }
        S.async(() -> {
            if (!initialized) init();
            try {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("PlayerCount");
                out.writeUTF(server);
                player.sendPluginMessage(DECENT_HOLOGRAMS.getPlugin(), "BungeeCord", out.toByteArray());
            } catch (Exception ignored) {}
        });
    }
}

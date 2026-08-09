package eu.decentsoftware.holograms.api.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.decentsoftware.holograms.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.util.Objects;

public final class BungeeUtils {

    private static final String BUNGEE_CORD_CHANNEL = "BungeeCord";
    private static volatile JavaPlugin plugin;

    private BungeeUtils() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    public static void init(JavaPlugin plugin) {
        Objects.requireNonNull(plugin, "plugin cannot be null");
        if (BungeeUtils.plugin != null) {
            return;
        }
        BungeeUtils.plugin = plugin;
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(plugin, BUNGEE_CORD_CHANNEL);
    }

    public static void destroy() {
        if (BungeeUtils.plugin == null) {
            return;
        }
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.unregisterOutgoingPluginChannel(plugin, BUNGEE_CORD_CHANNEL);
        BungeeUtils.plugin = null;
    }

    public static void connect(Player player, String server) {
        if (BungeeUtils.plugin == null) {
            throw new IllegalStateException("BungeeUtils is not initialized");
        }
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(plugin, BUNGEE_CORD_CHANNEL, out.toByteArray());
        } catch (Exception e) {
            Log.warn("Failed to connect player %s to server %s.", e, player.getName(), server);
        }
    }

}

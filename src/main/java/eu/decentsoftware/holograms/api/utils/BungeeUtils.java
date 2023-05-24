package eu.decentsoftware.holograms.api.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

@UtilityClass
public class BungeeUtils {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    private static final String BUNGEE_CORD_CHANNEL = "BungeeCord";
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(DECENT_HOLOGRAMS.getPlugin(), BUNGEE_CORD_CHANNEL);
        initialized = true;
    }

    public static void destroy() {
        if (!initialized) return;
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.unregisterOutgoingPluginChannel(DECENT_HOLOGRAMS.getPlugin(), BUNGEE_CORD_CHANNEL);
        initialized = false;
    }

    public static void connect(Player player, String server) {
        if (!initialized) init();
        S.async(() -> {
            try {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(server);
                player.sendPluginMessage(DECENT_HOLOGRAMS.getPlugin(), BUNGEE_CORD_CHANNEL, out.toByteArray());
            } catch (Exception ignored) {
                // Ignore
            }
        });
    }

}

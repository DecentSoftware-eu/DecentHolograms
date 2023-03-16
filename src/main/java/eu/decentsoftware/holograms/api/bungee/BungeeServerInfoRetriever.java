package eu.decentsoftware.holograms.api.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeServerInfoRetriever implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        try {
            final ByteArrayDataInput in = ByteStreams.newDataInput(message);

            final String sub = in.readUTF();

            if (sub.equals("PlayerCount")) {
                final String server = in.readUTF();
                final int playerCount = in.readInt();

                if (BungeeUtils.getServerOnlineCache().containsKey(server)) {
                    BungeeUtils.getServerOnlineCache().replace(server, playerCount);
                    return;
                }

                BungeeUtils.getServerOnlineCache().put(server, playerCount);
            }
        } catch (Exception exception) {
            DecentHologramsAPI.get().getPlugin().getLogger().severe("Could not retrieve player count. (Invalid server?)");
        }
    }
}

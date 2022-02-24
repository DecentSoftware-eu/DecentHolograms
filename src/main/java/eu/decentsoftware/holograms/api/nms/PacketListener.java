package eu.decentsoftware.holograms.api.nms;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.Common;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketListener {

    private static final NMS nms = NMS.getInstance();
    private static final String IDENTIFIER = "DecentHolograms";
    private boolean usingProtocolLib = false;

    public PacketListener() {
        if (Common.isPluginEnabled("ProtocolLib")) {
            // If ProtocolLib is present, use it for packet listening.
            new PacketHandler__ProtocolLib();
            usingProtocolLib = true;
            Common.log("Using ProtocolLib for packet listening.");
        } else {
            hookAll();
        }
    }

    public void destroy() {
        if (usingProtocolLib) {
            if (Common.isPluginEnabled("ProtocolLib")) {
                ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
                protocolManager.removePacketListeners(DecentHologramsAPI.get().getPlugin());
                usingProtocolLib = false;
            }
        } else {
            unhookAll();
        }
    }

    public boolean hook(Player player) {
        if (usingProtocolLib) {
            return true;
        }

        try {
            ChannelPipeline pipeline = nms.getPipeline(player);
            if (pipeline.get(IDENTIFIER) == null) {
                PacketHandler__Custom packetHandler = new PacketHandler__Custom(player);
                pipeline.addBefore("packet_handler", IDENTIFIER, packetHandler);
            }
            return true;
        } catch (Exception ignored) {}
        return true;
    }

    public void hookAll() {
        if (!usingProtocolLib) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                hook(player);
            }
        }
    }

    public boolean unhook(Player player) {
        if (usingProtocolLib) {
            return true;
        }

        try {
            ChannelPipeline pipeline = NMS.getInstance().getPipeline(player);
            if (pipeline.get(IDENTIFIER) != null) {
                pipeline.remove(IDENTIFIER);
            }
            return true;
        } catch (Exception ignored) {}
        return false;
    }

    public void unhookAll() {
        if (!usingProtocolLib) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                unhook(player);
            }
        }
    }

}

package eu.decentsoftware.holograms.api.nms;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.Common;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSPacketListener {

    private static final NMS nms = NMS.getInstance();
    private boolean usingProtocolLib;

    public NMSPacketListener() {
        registerAll();
    }

    public void destroy() {
        if (usingProtocolLib && Common.isPluginEnabled("ProtocolLib")) {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            protocolManager.removePacketListeners(DecentHologramsAPI.get().getPlugin());
            usingProtocolLib = false;
            return;
        }
        unregisterAll();
    }

    public boolean register(Player player) {
        if (usingProtocolLib) {
            return true;
        }

        unregister(player);
        ChannelPipeline pipeline = nms.getPipeline(player);
        PacketHandler__Custom packetHandler = new PacketHandler__Custom(player);
        try {
            pipeline.addBefore("packet_handler", "decent_holograms", packetHandler);
            return true;
        } catch (Exception ignored) {
            // fail
        }
        return true;
    }

    public void registerAll() {
        // If ProtocolLib is present, use it for packet listening.
        if (Common.isPluginEnabled("ProtocolLib")) {
            new PacketHandler__ProtocolLib();
            usingProtocolLib = true;
            Common.log("Using ProtocolLib for packet listening.");
        } else {
            usingProtocolLib = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                register(player);
            }
        }
    }

    public boolean unregister(Player player) {
        if (usingProtocolLib) {
            return true;
        }

        ChannelPipeline pipeline = NMS.getInstance().getPipeline(player);
        try {
            pipeline.remove("decent_holograms");
            return true;
        } catch (Exception ignored) {
            // not registered
        }
        return false;
    }

    public void unregisterAll() {
        if (usingProtocolLib) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            unregister(player);
        }
    }

}

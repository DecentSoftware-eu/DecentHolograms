package eu.decentsoftware.holograms.api.nms;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.Common;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class PacketListener {

    private static final NMS nms = NMS.getInstance();
    private static final String IDENTIFIER = "DecentHolograms";
    private final DecentHolograms decentHolograms;
    private boolean usingProtocolLib = false;

    public PacketListener(DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
        if (Common.isPluginEnabled("ProtocolLib")) {
            // If ProtocolLib is present, use it for packet listening.
            new PacketHandlerProtocolLib();
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

    public void hook(Player player) {
        if (usingProtocolLib) {
            return;
        }

        try {
            ChannelPipeline pipeline = nms.getPipeline(player);
            if (pipeline.get(IDENTIFIER) == null) {
                PacketHandlerCustom packetHandler = new PacketHandlerCustom(player);
                pipeline.addBefore("packet_handler", IDENTIFIER, packetHandler);
            }
        } catch (Exception e) {
            decentHolograms.getLogger().log(Level.WARNING, "Failed to hook into player's pipeline. (" + player.getName() + ")", e);
        }
    }

    public void hookAll() {
        if (!usingProtocolLib) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                hook(player);
            }
        }
    }

    public void unhook(Player player) {
        if (usingProtocolLib) {
            return;
        }

        try {
            ChannelPipeline pipeline = NMS.getInstance().getPipeline(player);
            if (pipeline.get(IDENTIFIER) != null) {
                pipeline.remove(IDENTIFIER);
            }
        } catch (Exception e) {
            decentHolograms.getLogger().log(Level.WARNING, "Failed to unhook from player's pipeline. (" + player.getName() + ")", e);
        }
    }

    public void unhookAll() {
        if (!usingProtocolLib) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                unhook(player);
            }
        }
    }

}

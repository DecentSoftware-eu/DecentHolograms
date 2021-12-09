package eu.decentsoftware.holograms.api.nms;

import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketListener {

    private static final NMS nms = NMS.getInstance();

    public PacketListener() {
        this.registerAll();
    }

    public void destroy() {
        this.unregisterAll();
    }

    public boolean register(Player player) {
        this.unregister(player);
        ChannelPipeline pipeline = nms.getPipeline(player);
        PacketHandler packetHandler = new PacketHandler(player);
        try {
            pipeline.addBefore("packet_handler", "decent_holograms", packetHandler);
            return true;
        } catch (Exception ignored) {
            // fail
        }
        return true;
    }

    public void registerAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.register(player);
        }
    }

    public boolean unregister(Player player) {
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.unregister(player);
        }
    }

}

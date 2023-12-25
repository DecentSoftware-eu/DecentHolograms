package eu.decentsoftware.holograms.api.nms;

import eu.decentsoftware.holograms.api.DecentHolograms;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.logging.Level;

public class PacketListener {

    private static final String IDENTIFIER = "DecentHolograms";
    private final DecentHolograms decentHolograms;
    private final NMS nms;

    public PacketListener(DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
        this.nms = NMS.getInstance();
        hookAll();
    }

    public void destroy() {
        unhookAll();
    }

    public void hook(Player player) {
        executeOnPipeline(player, pipeline -> {
            if (pipeline.get(IDENTIFIER) != null) {
                pipeline.remove(IDENTIFIER);
            }
            pipeline.addBefore("packet_handler", IDENTIFIER, new PacketHandlerCustom(player));
        });
    }

    public void unhook(Player player) {
        executeOnPipeline(player, pipeline -> {
            if (pipeline.get(IDENTIFIER) != null) {
                pipeline.remove(IDENTIFIER);
            }
        });
    }

    private void executeOnPipeline(Player player, Consumer<ChannelPipeline> consumer) {
        try {
            if (!player.isOnline()) {
                return;
            }

            ChannelPipeline pipeline = nms.getPipeline(player);
            EventLoop eventLoop = pipeline.channel().eventLoop();

            if (eventLoop.inEventLoop()) {
                consumer.accept(pipeline);
            } else {
                eventLoop.execute(() -> executeOnPipeline(player, consumer));
            }
        } catch (Exception e) {
            decentHolograms.getLogger().log(Level.WARNING, "Failed to modify player's pipeline. (" + player.getName() + ")", e);
        }
    }

    public void hookAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            hook(player);
        }
    }

    public void unhookAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            unhook(player);
        }
    }

}

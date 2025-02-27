package eu.decentsoftware.holograms.api.nms;

import eu.decentsoftware.holograms.api.utils.Log;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class PacketListener {

    private static final String IDENTIFIER = "DecentHolograms";
    private static final String DEFAULT_PIPELINE_TAIL = "DefaultChannelPipeline$TailContext#0";
    private final NMS nms;

    public PacketListener() {
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
            try {
                pipeline.addBefore("packet_handler", IDENTIFIER, new PacketHandlerCustom(player));
            } catch (NoSuchElementException e) {
                List<String> handlers = pipeline.names();
                if (handlers.size() == 1 && handlers.iterator().next().equals(DEFAULT_PIPELINE_TAIL)) {
                    // player disconnecting
                    return;
                }
                throw e;
            }
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
            List<String> availableElements = getAvailableElements(player);
            Log.warn("Failed to modify player's pipeline. player: %s, availableElements: %s",
                    e, player.getName(), availableElements);
        }
    }

    /**
     * Get the available elements in the player's pipeline.
     *
     * <p>This is mainly used in case the modification of the pipeline fails.
     * We can then log the available elements in the pipeline to help debug the issue.</p>
     *
     * @param player the player
     * @return the available elements or an empty list if the elements could not be retrieved
     */
    private List<String> getAvailableElements(Player player) {
        try {
            ChannelPipeline pipeline = nms.getPipeline(player);
            return pipeline.names();
        } catch (Exception e) {
            Log.warn("Failed to get available elements in player's pipeline.", e);
            return Collections.emptyList();
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

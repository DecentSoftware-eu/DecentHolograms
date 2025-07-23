package eu.decentsoftware.holograms.nms.v1_19_R1;


import eu.decentsoftware.holograms.nms.api.NmsAdapter;

import eu.decentsoftware.holograms.nms.api.renderer.NmsHologramRendererFactory;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;


import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
public class NmsAdapterImpl implements NmsAdapter {

    private static final String PACKET_HANDLER_NAME = "decent_holograms_packet_handler";
    private static final String DEFAULT_PIPELINE_TAIL = "DefaultChannelPipeline$TailContext#0";
    private final HologramRendererFactory hologramComponentFactory;

    public NmsAdapterImpl() {
        this.hologramComponentFactory = new HologramRendererFactory(new EntityIdGenerator());
    }

    @Override
    public NmsHologramRendererFactory getHologramComponentFactory() {
        return hologramComponentFactory;
    }

    @Override
    public void registerPacketListener(Player player) {
        Objects.requireNonNull(player, "player cannot be null");


        executeOnPipelineInEventLoop(player, pipeline -> {
            if (pipeline.get(PACKET_HANDLER_NAME) != null) {
                pipeline.remove(PACKET_HANDLER_NAME);
            }
            pipeline.addBefore("packet_handler", PACKET_HANDLER_NAME, new InboundPacketHandler(player));
        });
    }

    @Override
    public void unregisterPacketListener(Player player) {
        Objects.requireNonNull(player, "player cannot be null");

        executeOnPipelineInEventLoop(player, pipeline -> {
            if (pipeline.get(PACKET_HANDLER_NAME) != null) {
                pipeline.remove(PACKET_HANDLER_NAME);
            }
        });
    }

    private void executeOnPipelineInEventLoop(Player player, Consumer<ChannelPipeline> task) {
        ChannelPipeline pipeline = getPipeline(player);
        EventLoop eventLoop = pipeline.channel().eventLoop();

        if (eventLoop.inEventLoop()) {
            executeOnPipeline(player, task, pipeline);
        } else {
            eventLoop.execute(() -> executeOnPipeline(player, task, pipeline));
        }
    }

    private ChannelPipeline getPipeline(Player player) {
        return ((CraftPlayer) player).getHandle().b.a().m.pipeline();
    }

    private void executeOnPipeline(Player player, Consumer<ChannelPipeline> task, ChannelPipeline pipeline) {
        if (!player.isOnline()) {
            return;
        }

        try {
            task.accept(pipeline);
        } catch (NoSuchElementException e) {
            List<String> handlers = pipeline.names();
            if (handlers.size() == 1 && handlers.get(0).equals(DEFAULT_PIPELINE_TAIL)) {
                // player disconnected immediately after joining
                return;
            }
            throwFailedToModifyPipelineException(player, e);
        } catch (Exception e) {
            throwFailedToModifyPipelineException(player, e);
        }
    }

    private void throwFailedToModifyPipelineException(Player player, Exception e) {
        throw new DecentHologramsNmsException("Failed to modify player's pipeline. player: " + player.getName(), e);
    }

}

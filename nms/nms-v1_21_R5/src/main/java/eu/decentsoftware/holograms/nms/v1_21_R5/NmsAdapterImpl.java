package eu.decentsoftware.holograms.nms.v1_21_R5;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
import eu.decentsoftware.holograms.nms.api.renderer.NmsHologramRendererFactory;
import eu.decentsoftware.holograms.shared.reflect.ReflectField;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unused") // Instantiated via reflection
public class NmsAdapterImpl implements NmsAdapter {

    private static final String PACKET_HANDLER_NAME = "decent_holograms_packet_handler";
    private static final String DEFAULT_PIPELINE_TAIL = "DefaultChannelPipeline$TailContext#0";
    private static final ReflectField<NetworkManager> NETWORK_MANAGER_FIELD = new ReflectField<>(
            ServerCommonPacketListenerImpl.class, "e");
    private final HologramRendererFactory hologramComponentFactory;

    public NmsAdapterImpl() {
        this.hologramComponentFactory = new HologramRendererFactory(new EntityIdGenerator());
    }

    @Override
    public NmsHologramRendererFactory getHologramComponentFactory() {
        return hologramComponentFactory;
    }

    @Override
    public void registerPacketListener(Player player, NmsPacketListener listener) {
        Objects.requireNonNull(player, "player cannot be null");
        Objects.requireNonNull(listener, "listener cannot be null");

        executeOnPipelineInEventLoop(player, pipeline -> {
            if (pipeline.get(PACKET_HANDLER_NAME) != null) {
                pipeline.remove(PACKET_HANDLER_NAME);
            }
            pipeline.addBefore("packet_handler", PACKET_HANDLER_NAME, new InboundPacketHandler(player, listener));
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
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().g;
        NetworkManager networkManager = NETWORK_MANAGER_FIELD.get(playerConnection);
        return networkManager.n.pipeline();
    }

    private void executeOnPipeline(Player player, Consumer<ChannelPipeline> task, ChannelPipeline pipeline) {
        if (!player.isOnline()) {
            return;
        }

        try {
            task.accept(pipeline);
        } catch (NoSuchElementException e) {
            List<String> handlers = pipeline.names();
            if (handlers.size() == 1 && handlers.getFirst().equals(DEFAULT_PIPELINE_TAIL)) {
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

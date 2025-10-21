package eu.decentsoftware.holograms.nms.paper_v1_21_R6;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
import eu.decentsoftware.holograms.nms.api.event.NmsEntityInteractAction;
import eu.decentsoftware.holograms.nms.api.event.NmsEntityInteractEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.bukkit.entity.Player;

class InboundPacketHandler extends ChannelInboundHandlerAdapter {

    private final Player player;
    private final NmsPacketListener listener;

    InboundPacketHandler(Player player, NmsPacketListener listener) {
        this.player = player;
        this.listener = listener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        if (packet instanceof ServerboundInteractPacket serverboundInteractPacket) {
            FriendlyByteBufWrapper serializer = FriendlyByteBufWrapper.getInstance();
            ServerboundInteractPacket.STREAM_CODEC.encode(serializer.getSerializer(), serverboundInteractPacket);

            int entityId = serializer.readVarInt();
            int actionEnumValueOrdinal = serializer.readVarInt();

            NmsEntityInteractAction action = mapActionEnumValueOrdinalToNmsEntityInteractionAction(actionEnumValueOrdinal);
            NmsEntityInteractEvent event = new NmsEntityInteractEvent(player, entityId, action);
            listener.onEntityInteract(event);
            if (event.isHandled()) {
                return;
            }
        }
        super.channelRead(ctx, packet);
    }

    private NmsEntityInteractAction mapActionEnumValueOrdinalToNmsEntityInteractionAction(int ordinal) {
        // 0 = INTERACT
        // 1 = ATTACK
        // 2 = INTERACT_AT
        //
        // https://minecraft.wiki/w/Java_Edition_protocol#Interact
        return switch (ordinal) {
            case 1:
                yield player.isSneaking() ? NmsEntityInteractAction.SHIFT_LEFT_CLICK : NmsEntityInteractAction.LEFT_CLICK;
            case 0, 2:
                yield player.isSneaking() ? NmsEntityInteractAction.SHIFT_RIGHT_CLICK : NmsEntityInteractAction.RIGHT_CLICK;
            default:
                throw new DecentHologramsNmsException("Unknown entity use action: " + ordinal);
        };
    }

}

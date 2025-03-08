package eu.decentsoftware.holograms.nms.v1_13_R2;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
import eu.decentsoftware.holograms.nms.api.event.NmsEntityInteractAction;
import eu.decentsoftware.holograms.nms.api.event.NmsEntityInteractEvent;
import eu.decentsoftware.holograms.shared.reflect.ReflectField;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.server.v1_13_R2.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

class InboundPacketHandler extends ChannelInboundHandlerAdapter {

    private static final ReflectField<Integer> ENTITY_ID_FIELD = new ReflectField<>(PacketPlayInUseEntity.class, "a");
    private final Player player;
    private final NmsPacketListener listener;

    InboundPacketHandler(Player player, NmsPacketListener listener) {
        this.player = player;
        this.listener = listener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        if (packet instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity useEntityPacket = (PacketPlayInUseEntity) packet;
            int entityId = ENTITY_ID_FIELD.get(useEntityPacket);
            NmsEntityInteractAction action = extractEntityInteractAction(useEntityPacket);

            NmsEntityInteractEvent event = new NmsEntityInteractEvent(player, entityId, action);
            listener.onEntityInteract(event);
            if (event.isHandled()) {
                return;
            }
        }
        super.channelRead(ctx, packet);
    }

    private NmsEntityInteractAction extractEntityInteractAction(PacketPlayInUseEntity useEntityPacket) {
        NmsEntityInteractAction action;
        PacketPlayInUseEntity.EnumEntityUseAction enumEntityUseAction = useEntityPacket.b();
        switch (enumEntityUseAction) {
            case ATTACK:
                action = player.isSneaking() ? NmsEntityInteractAction.SHIFT_LEFT_CLICK : NmsEntityInteractAction.LEFT_CLICK;
                break;
            case INTERACT:
            case INTERACT_AT:
                action = player.isSneaking() ? NmsEntityInteractAction.SHIFT_RIGHT_CLICK : NmsEntityInteractAction.RIGHT_CLICK;
                break;
            default:
                throw new DecentHologramsNmsException("Unknown entity use action: " + enumEntityUseAction.name());
        }
        return action;
    }

}

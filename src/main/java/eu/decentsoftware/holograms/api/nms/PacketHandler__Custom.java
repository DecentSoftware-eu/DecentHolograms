package eu.decentsoftware.holograms.api.nms;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.entity.Player;

public class PacketHandler__Custom extends ChannelDuplexHandler {

    private final Player player;

    public PacketHandler__Custom(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
        if (!PacketHandlerCommon.handlePacket(packet, player)) {
            super.channelRead(channelHandlerContext, packet);
        }
    }

}

package eu.decentsoftware.holograms.api.nms;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.entity.Player;

class PacketHandlerCustom extends ChannelDuplexHandler {

    private final Player player;

    PacketHandlerCustom(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
        if (!PacketHandlerCommon.handlePacket(packet, player)) {
            super.channelRead(channelHandlerContext, packet);
        }
    }

}

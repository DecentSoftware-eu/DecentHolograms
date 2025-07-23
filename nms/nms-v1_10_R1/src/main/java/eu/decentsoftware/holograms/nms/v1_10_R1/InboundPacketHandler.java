package eu.decentsoftware.holograms.nms.v1_10_R1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

class InboundPacketHandler extends ChannelInboundHandlerAdapter {



    InboundPacketHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        super.channelRead(ctx, packet);
    }



}

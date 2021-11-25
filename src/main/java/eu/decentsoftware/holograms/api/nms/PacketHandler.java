package eu.decentsoftware.holograms.api.nms;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.reflect.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.entity.Player;

public class PacketHandler extends ChannelDuplexHandler {

    protected static final Class<?> ENTITY_USE_PACKET_CLASS;
    protected static final ReflectField<Integer> ENTITY_USE_PACKET_ID_FIELD;
    private static final Class<?> PACKET_DATA_SERIALIZER_CLASS;
    private static final ReflectConstructor PACKET_DATA_SERIALIZER_CONSTRUCTOR;
    private static final ReflectMethod ENTITY_USE_PACKET_A_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_READ_INT_METHOD;

    static {
        if (Common.SERVER_VERSION.isAfterOrEqual(Version.v1_17_R1)) {
            ENTITY_USE_PACKET_CLASS = ReflectionUtil.getNMClass("network.protocol.game.PacketPlayInUseEntity");
            PACKET_DATA_SERIALIZER_CLASS = ReflectionUtil.getNMClass("network.PacketDataSerializer");
        } else {
            ENTITY_USE_PACKET_CLASS = ReflectionUtil.getNMSClass("PacketPlayInUseEntity");
            PACKET_DATA_SERIALIZER_CLASS = ReflectionUtil.getNMSClass("PacketDataSerializer");
        }
        ENTITY_USE_PACKET_ID_FIELD = new ReflectField<>(ENTITY_USE_PACKET_CLASS, "a");
        PACKET_DATA_SERIALIZER_CONSTRUCTOR = new ReflectConstructor(PACKET_DATA_SERIALIZER_CLASS, ByteBuf.class);
        if (Common.SERVER_VERSION.isAfterOrEqual(Version.v1_17_R1)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "j");
            ENTITY_USE_PACKET_A_METHOD = new ReflectMethod(ENTITY_USE_PACKET_CLASS, "a", PACKET_DATA_SERIALIZER_CLASS);
        } else if (Common.SERVER_VERSION.isAfterOrEqual(Version.v1_14_R1)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "i");
            ENTITY_USE_PACKET_A_METHOD = new ReflectMethod(ENTITY_USE_PACKET_CLASS, "b", PACKET_DATA_SERIALIZER_CLASS);
        } else if (Common.SERVER_VERSION.isAfterOrEqual(Version.v1_9_R1)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "g");
            ENTITY_USE_PACKET_A_METHOD = new ReflectMethod(ENTITY_USE_PACKET_CLASS, "b", PACKET_DATA_SERIALIZER_CLASS);
        } else {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "e");
            ENTITY_USE_PACKET_A_METHOD = new ReflectMethod(ENTITY_USE_PACKET_CLASS, "b", PACKET_DATA_SERIALIZER_CLASS);
        }
    }

    private final Player player;

    public PacketHandler(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
        if (packet != null && packet.getClass().isAssignableFrom(ENTITY_USE_PACKET_CLASS)) {
            int entityId = ENTITY_USE_PACKET_ID_FIELD.getValue(packet);
            ClickType clickType = this.getClickType(packet);
            DecentHologramsAPI.get().getHologramManager().onClick(player, entityId, clickType);
        }
        super.channelRead(channelHandlerContext, packet);
    }

    protected int getEntityUseActionOrdinal(Object packet) {
        if (packet != null && packet.getClass().isAssignableFrom(ENTITY_USE_PACKET_CLASS)) {
            Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
            ENTITY_USE_PACKET_A_METHOD.invoke(packet, packetDataSerializer);
            PACKET_DATA_SERIALIZER_READ_INT_METHOD.invoke(packetDataSerializer);
            return PACKET_DATA_SERIALIZER_READ_INT_METHOD.invoke(packetDataSerializer);
        }
        return -1;
    }

    private ClickType getClickType(Object packet) {
        int ordinal = this.getEntityUseActionOrdinal(packet);
        if (ordinal == 1) {
            return player.isSneaking() ? ClickType.SHIFT_LEFT : ClickType.LEFT;
        } else {
            return player.isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT;
        }
    }

}

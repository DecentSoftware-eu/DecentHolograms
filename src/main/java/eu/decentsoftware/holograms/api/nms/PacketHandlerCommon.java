package eu.decentsoftware.holograms.api.nms;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.utils.reflect.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public final class PacketHandlerCommon {

    private static final Class<?> ENTITY_USE_PACKET_CLASS;
    private static final ReflectField<Integer> ENTITY_USE_PACKET_ID_FIELD;
    private static final Class<?> PACKET_DATA_SERIALIZER_CLASS;
    private static final ReflectConstructor PACKET_DATA_SERIALIZER_CONSTRUCTOR;
    private static final ReflectMethod ENTITY_USE_PACKET_A_METHOD;
    private static final ReflectMethod PACKET_DATA_SERIALIZER_READ_INT_METHOD;

    static {
        if (Version.afterOrEqual(17)) {
            ENTITY_USE_PACKET_CLASS = ReflectionUtil.getNMClass("network.protocol.game.PacketPlayInUseEntity");
            PACKET_DATA_SERIALIZER_CLASS = ReflectionUtil.getNMClass("network.PacketDataSerializer");
        } else {
            ENTITY_USE_PACKET_CLASS = ReflectionUtil.getNMSClass("PacketPlayInUseEntity");
            PACKET_DATA_SERIALIZER_CLASS = ReflectionUtil.getNMSClass("PacketDataSerializer");
        }
        ENTITY_USE_PACKET_ID_FIELD = new ReflectField<>(ENTITY_USE_PACKET_CLASS, "a");
        PACKET_DATA_SERIALIZER_CONSTRUCTOR = new ReflectConstructor(PACKET_DATA_SERIALIZER_CLASS, ByteBuf.class);
        if (Version.afterOrEqual(Version.v1_19_R3)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "m");
        } else if (Version.afterOrEqual(19)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "k");
        } else if (Version.afterOrEqual(17)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "j");
        } else if (Version.afterOrEqual(14)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "i");
        } else if (Version.afterOrEqual(9)) {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "g");
        } else {
            PACKET_DATA_SERIALIZER_READ_INT_METHOD = new ReflectMethod(PACKET_DATA_SERIALIZER_CLASS, "e");
        }
        if (Version.afterOrEqual(17)) {
            ENTITY_USE_PACKET_A_METHOD = new ReflectMethod(ENTITY_USE_PACKET_CLASS, "a", PACKET_DATA_SERIALIZER_CLASS);
        } else {
            ENTITY_USE_PACKET_A_METHOD = new ReflectMethod(ENTITY_USE_PACKET_CLASS, "b", PACKET_DATA_SERIALIZER_CLASS);
        }
    }

    /**
     * Handle the PacketPlayInEntityUse packet and detect possible hologram clicks.
     *
     * @param packet The packet.
     * @param player The player that clicked.
     */
    public static boolean handlePacket(Object packet, Player player) {
        if (packet == null || !packet.getClass().isAssignableFrom(ENTITY_USE_PACKET_CLASS)) {
            return false;
        }
        int entityId = ENTITY_USE_PACKET_ID_FIELD.getValue(packet);
        ClickType clickType = getClickType(packet, player);
        return DecentHologramsAPI.get().getHologramManager().onClick(player, entityId, clickType);
    }

    private static int getEntityUseActionOrdinal(Object packet) {
        Object packetDataSerializer = PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(Unpooled.buffer());
        ENTITY_USE_PACKET_A_METHOD.invoke(packet, packetDataSerializer);
        PACKET_DATA_SERIALIZER_READ_INT_METHOD.invoke(packetDataSerializer);
        return PACKET_DATA_SERIALIZER_READ_INT_METHOD.invoke(packetDataSerializer);
    }

    private static ClickType getClickType(Object packet, Player player) {
        int ordinal = getEntityUseActionOrdinal(packet);
        if (ordinal == 1) {
            return player.isSneaking() ? ClickType.SHIFT_LEFT : ClickType.LEFT;
        } else {
            return player.isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT;
        }
    }

}

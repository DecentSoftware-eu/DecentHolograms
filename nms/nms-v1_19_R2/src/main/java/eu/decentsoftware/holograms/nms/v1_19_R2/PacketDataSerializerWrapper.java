package eu.decentsoftware.holograms.nms.v1_19_R2;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;

class PacketDataSerializerWrapper {

    private static final ThreadLocal<PacketDataSerializerWrapper> LOCAL_INSTANCE = ThreadLocal.withInitial(
            PacketDataSerializerWrapper::new);
    private final PacketDataSerializer serializer;

    private PacketDataSerializerWrapper() {
        this.serializer = new PacketDataSerializer(Unpooled.buffer());
    }

    PacketDataSerializer getSerializer() {
        return serializer;
    }

    void clear() {
        serializer.clear();
    }

    void writeIntArray(int[] value) {
        serializer.a(value);
    }

    void writeVarInt(int value) {
        serializer.d(value);
    }

    void writeByte(int value) {
        serializer.writeByte(value);
    }

    void writeDouble(double value) {
        serializer.writeDouble(value);
    }

    void writeBoolean(boolean value) {
        serializer.writeBoolean(value);
    }

    void writePacket(Packet<?> packet) {
        packet.a(serializer);
    }

    int readVarInt() {
        return serializer.k();
    }

    static PacketDataSerializerWrapper getInstance() {
        PacketDataSerializerWrapper instance = LOCAL_INSTANCE.get();
        instance.clear();
        return instance;
    }

}

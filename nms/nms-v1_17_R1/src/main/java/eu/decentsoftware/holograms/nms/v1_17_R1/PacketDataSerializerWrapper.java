package eu.decentsoftware.holograms.nms.v1_17_R1;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.DataWatcher;

import java.util.List;
import java.util.UUID;

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

    void writeShort(int value) {
        serializer.writeShort(value);
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

    void writeUuid(UUID value) {
        serializer.a(value);
    }

    void writeWatchableObjects(List<DataWatcher.Item<?>> watchableObjects) {
        DataWatcher.a(watchableObjects, serializer);
    }

    void writePacket(Packet<?> packet) {
        packet.a(serializer);
    }

    int readVarInt() {
        return serializer.j();
    }

    static PacketDataSerializerWrapper getInstance() {
        PacketDataSerializerWrapper instance = LOCAL_INSTANCE.get();
        instance.clear();
        return instance;
    }

}

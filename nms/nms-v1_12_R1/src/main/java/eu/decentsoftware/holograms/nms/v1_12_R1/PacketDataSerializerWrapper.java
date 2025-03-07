package eu.decentsoftware.holograms.nms.v1_12_R1;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

class PacketDataSerializerWrapper {

    private static final ThreadLocal<PacketDataSerializerWrapper> LOCAL_INSTANCE = ThreadLocal.withInitial(
            PacketDataSerializerWrapper::new);
    private final PacketDataSerializer serializer;

    private PacketDataSerializerWrapper() {
        this.serializer = new PacketDataSerializer(Unpooled.buffer());
    }

    void writeToPacket(Packet<?> packet) {
        try {
            packet.a(serializer);
        } catch (IOException e) {
            throw new DecentHologramsNmsException("Failed to write data to packet", e);
        }
    }

    void clear() {
        serializer.clear();
    }

    void writeInt(int value) {
        serializer.writeInt(value);
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
        try {
            DataWatcher.a(watchableObjects, serializer);
        } catch (IOException e) {
            throw new DecentHologramsNmsException("Failed to write watchable objects", e);
        }
    }

    static PacketDataSerializerWrapper getInstance() {
        PacketDataSerializerWrapper instance = LOCAL_INSTANCE.get();
        instance.clear();
        return instance;
    }

}

package eu.decentsoftware.holograms.nms.v1_8_R2;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R2.DataWatcher;
import net.minecraft.server.v1_8_R2.Packet;
import net.minecraft.server.v1_8_R2.PacketDataSerializer;

import java.io.IOException;
import java.util.List;

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

    void writeVarInt(int value) {
        serializer.b(value);
    }

    void writeShort(int value) {
        serializer.writeShort(value);
    }

    void writeByte(int value) {
        serializer.writeByte(value);
    }

    void writeWatchableObjects(List<DataWatcher.WatchableObject> watchableObjects) {
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

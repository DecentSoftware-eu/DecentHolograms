package eu.decentsoftware.holograms.nms.v1_21_R5;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;

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

    void writeIntArray(int[] array) {
        serializer.a(array);
    }

    void writeVarInt(int value) {
        serializer.c(value);
    }

    int readVarInt() {
        return serializer.l();
    }

    static PacketDataSerializerWrapper getInstance() {
        PacketDataSerializerWrapper instance = LOCAL_INSTANCE.get();
        instance.clear();
        return instance;
    }

}

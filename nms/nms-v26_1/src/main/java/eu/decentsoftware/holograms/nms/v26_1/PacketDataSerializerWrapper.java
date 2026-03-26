package eu.decentsoftware.holograms.nms.v26_1;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

class PacketDataSerializerWrapper {

    private static final ThreadLocal<PacketDataSerializerWrapper> LOCAL_INSTANCE = ThreadLocal.withInitial(
            PacketDataSerializerWrapper::new);
    private final FriendlyByteBuf serializer;

    private PacketDataSerializerWrapper() {
        this.serializer = new FriendlyByteBuf(Unpooled.buffer());
    }

    FriendlyByteBuf getSerializer() {
        return serializer;
    }

    void clear() {
        serializer.clear();
    }

    void writeIntArray(int[] array) {
        serializer.writeVarIntArray(array);
    }

    void writeVarInt(int value) {
        serializer.writeVarInt(value);
    }

    int readVarInt() {
        return serializer.readVarInt();
    }

    static PacketDataSerializerWrapper getInstance() {
        PacketDataSerializerWrapper instance = LOCAL_INSTANCE.get();
        instance.clear();
        return instance;
    }
}

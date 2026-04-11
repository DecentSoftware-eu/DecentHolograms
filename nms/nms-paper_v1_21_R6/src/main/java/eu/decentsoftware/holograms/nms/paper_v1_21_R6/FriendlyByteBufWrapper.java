package eu.decentsoftware.holograms.nms.paper_v1_21_R6;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

class FriendlyByteBufWrapper {

    private static final ThreadLocal<FriendlyByteBufWrapper> LOCAL_INSTANCE = ThreadLocal.withInitial(
            FriendlyByteBufWrapper::new);
    private final FriendlyByteBuf serializer;

    private FriendlyByteBufWrapper() {
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

    static FriendlyByteBufWrapper getInstance() {
        FriendlyByteBufWrapper instance = LOCAL_INSTANCE.get();
        instance.clear();
        return instance;
    }

}

package eu.decentsoftware.holograms.api.nms;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.nms.versions.NMS_1_17;
import eu.decentsoftware.holograms.api.nms.versions.NMS_1_8;
import eu.decentsoftware.holograms.api.nms.versions.NMS_1_9;
import eu.decentsoftware.holograms.api.utils.objects.Pair;
import eu.decentsoftware.holograms.api.utils.reflect.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class NMS {

    protected static final Map<String, Pair<Integer, Float>> mapEntityTypes = Maps.newHashMap();

    // SEND PACKET
    protected static final Class<?> PACKET_CLASS;
    protected static final ReflectMethod CRAFT_PLAYER_GET_HANDLE_METHOD;
    protected static final ReflectMethod PLAYER_CONNECTION_SEND_PACKET_METHOD;
    protected static ReflectField<?> ENTITY_PLAYER_CONNECTION_FIELD;
    // PACKET LISTENER
    protected static ReflectField<?> PLAYER_CONNECTION_NETWORK_MANAGER_FIELD;
    protected static ReflectField<?> NETWORK_MANAGER_CHANNEL_FIELD;

    static {
        // SEND PACKET
        Class<?> entityPlayerClass;
        Class<?> playerConnectionClass;
        Class<?> craftPlayerClass;
        Class<?> networkManagerClass;
        if (Version.afterOrEqual(17)) {
            entityPlayerClass = ReflectionUtil.getNMClass("server.level.EntityPlayer");
            playerConnectionClass = ReflectionUtil.getNMClass("server.network.PlayerConnection");
            craftPlayerClass = ReflectionUtil.getObcClass("entity.CraftPlayer");
            networkManagerClass = ReflectionUtil.getNMClass("network.NetworkManager");
            PACKET_CLASS = ReflectionUtil.getNMClass("network.protocol.Packet");
            // Because NMS has different names for fields in almost every version.
            for (Field field : entityPlayerClass.getDeclaredFields()) {
                if (field.getType().isAssignableFrom(playerConnectionClass)) {
                    ENTITY_PLAYER_CONNECTION_FIELD  = new ReflectField<>(entityPlayerClass, field.getName());
                    break;
                }
            }
            if (Version.afterOrEqual(Version.v1_20_R2)) {
                for (Field field : playerConnectionClass.getFields()) {
                    if (field.getType().isAssignableFrom(networkManagerClass)) {
                        PLAYER_CONNECTION_NETWORK_MANAGER_FIELD = new ReflectField<>(playerConnectionClass, field.getName());
                        break;
                    }
                }
            } else {
                for (Field field : playerConnectionClass.getDeclaredFields()) {
                    if (field.getType().isAssignableFrom(networkManagerClass)) {
                        PLAYER_CONNECTION_NETWORK_MANAGER_FIELD = new ReflectField<>(playerConnectionClass, field.getName());
                        break;
                    }
                }
            }
            for (Field field : networkManagerClass.getDeclaredFields()) {
                if (field.getType().isAssignableFrom(Channel.class)) {
                    NETWORK_MANAGER_CHANNEL_FIELD = new ReflectField<>(networkManagerClass, field.getName());
                    break;
                }
            }
        } else {
            entityPlayerClass = ReflectionUtil.getNMSClass("EntityPlayer");
            playerConnectionClass = ReflectionUtil.getNMSClass("PlayerConnection");
            craftPlayerClass = ReflectionUtil.getObcClass("entity.CraftPlayer");
            networkManagerClass = ReflectionUtil.getNMSClass("NetworkManager");
            PACKET_CLASS = ReflectionUtil.getNMSClass("Packet");
            ENTITY_PLAYER_CONNECTION_FIELD = new ReflectField<>(entityPlayerClass, "playerConnection");
            PLAYER_CONNECTION_NETWORK_MANAGER_FIELD = new ReflectField<>(playerConnectionClass, "networkManager");
            NETWORK_MANAGER_CHANNEL_FIELD = new ReflectField<>(networkManagerClass, "channel");
        }
        CRAFT_PLAYER_GET_HANDLE_METHOD = new ReflectMethod(craftPlayerClass, "getHandle");
        if (Version.afterOrEqual(Version.v1_20_R2)) {
            PLAYER_CONNECTION_SEND_PACKET_METHOD = new ReflectMethod(playerConnectionClass, "b", PACKET_CLASS);
        } else if (Version.afterOrEqual(18)) {
            PLAYER_CONNECTION_SEND_PACKET_METHOD = new ReflectMethod(playerConnectionClass, "a", PACKET_CLASS);
        } else {
            PLAYER_CONNECTION_SEND_PACKET_METHOD = new ReflectMethod(playerConnectionClass, "sendPacket", PACKET_CLASS);
        }

        if (Version.beforeOrEqual(12)) {
            // Entities
            mapEntityTypes.put("BAT", new Pair<>(65, 0.9f));
            mapEntityTypes.put("BLAZE", new Pair<>(61, 1.8f));
            mapEntityTypes.put("CAVE_SPIDER", new Pair<>(59, 0.5f));
            mapEntityTypes.put("CHICKEN", new Pair<>(93, 0.7f));
            mapEntityTypes.put("COW", new Pair<>(92, 1.4f));
            mapEntityTypes.put("CREEPER", new Pair<>(50, 1.7f));
            mapEntityTypes.put("DONKEY", new Pair<>(31, 1.39648f));
            mapEntityTypes.put("ELDER_GUARDIAN", new Pair<>(4, 2.9f));
            mapEntityTypes.put("ENDER_DRAGON", new Pair<>(63, 8.0f));
            mapEntityTypes.put("ENDERMAN", new Pair<>(58, 2.9f));
            mapEntityTypes.put("ENDERMITE", new Pair<>(67, 0.3f));
            mapEntityTypes.put("EVOKER", new Pair<>(34, 1.95f));
            mapEntityTypes.put("HORSE", new Pair<>(100, 1.6f));
            mapEntityTypes.put("HUSK", new Pair<>(23, 1.95f));
            mapEntityTypes.put("ILLUSIONER", new Pair<>(37, 1.95f));
            mapEntityTypes.put("LLAMA", new Pair<>(103, 1.87f));
            mapEntityTypes.put("MAGMA_CUBE", new Pair<>(62, 0.51000005f));
            mapEntityTypes.put("MULE", new Pair<>(32, 1.6f));
            mapEntityTypes.put("MUSHROOM_COW", new Pair<>(96, 1.4f));
            mapEntityTypes.put("OCELOT", new Pair<>(98, 0.7f));
            mapEntityTypes.put("PARROT", new Pair<>(105, 0.9f));
            mapEntityTypes.put("PIG", new Pair<>(90, 0.9f));
            mapEntityTypes.put("PIG_ZOMBIE", new Pair<>(57, 1.8f));
            mapEntityTypes.put("POLAR_BEAR", new Pair<>(102, 1.4f));
            mapEntityTypes.put("RABBIT", new Pair<>(101, 0.5f));
            mapEntityTypes.put("SHEEP", new Pair<>(91, 1.3f));
            mapEntityTypes.put("SILVERFISH", new Pair<>(60, 0.3f));
            mapEntityTypes.put("SKELETON", new Pair<>(51, 1.99f));
            mapEntityTypes.put("SKELETON_HORSE", new Pair<>(28, 1.6f));
            mapEntityTypes.put("SLIME", new Pair<>(55, 0.51000005f));
            mapEntityTypes.put("SNOWMAN", new Pair<>(97, 1.9f));
            mapEntityTypes.put("GUARDIAN", new Pair<>(68, 0.85f));
            mapEntityTypes.put("SPIDER", new Pair<>(52, 0.9f));
            mapEntityTypes.put("SQUID", new Pair<>(94, 1.8f));
            mapEntityTypes.put("STRAY", new Pair<>(6, 1.99f));
            mapEntityTypes.put("VEX", new Pair<>(35, 0.95f));
            mapEntityTypes.put("VILLAGER", new Pair<>(120, 1.95f));
            mapEntityTypes.put("IRON_GOLEM", new Pair<>(99, 2.7f));
            mapEntityTypes.put("VINDICATOR", new Pair<>(36, 1.95f));
            mapEntityTypes.put("WITCH", new Pair<>(66, 1.95f));
            mapEntityTypes.put("WITHER", new Pair<>(64, 3.5f));
            mapEntityTypes.put("WITHER_SKELETON", new Pair<>(5, 2.4f));
            mapEntityTypes.put("WOLF", new Pair<>(95, 0.85f));
            mapEntityTypes.put("ZOMBIE", new Pair<>(54, 1.95f));
            mapEntityTypes.put("ZOMBIE_HORSE", new Pair<>(29, 1.6f));
            mapEntityTypes.put("ZOMBIE_VILLAGER", new Pair<>(27, 1.95f));

            // Objects
            mapEntityTypes.put("ENDER_CRYSTAL", new Pair<>(200, 2.0f));
            mapEntityTypes.put("ARROW", new Pair<>(10, 0.5f));
            mapEntityTypes.put("SNOWBALL", new Pair<>(11, 0.25f));
            mapEntityTypes.put("EGG", new Pair<>(7, 0.25f));
            mapEntityTypes.put("FIREBALL", new Pair<>(12, 1.0f));
            mapEntityTypes.put("SMALL_FIREBALL", new Pair<>(13, 0.3125f));
            mapEntityTypes.put("ENDER_PEARL", new Pair<>(14, 0.25f));
            mapEntityTypes.put("ENDER_SIGNAL", new Pair<>(15, 0.25f));
            mapEntityTypes.put("FIREWORK", new Pair<>(22, 0.25f));
        } else {
            // Entities
            mapEntityTypes.put("BAT", new Pair<>(3, 0.9f));
            mapEntityTypes.put("BLAZE", new Pair<>(4, 1.8f));
            mapEntityTypes.put("CAVE_SPIDER", new Pair<>(6, 0.5f));
            mapEntityTypes.put("CHICKEN", new Pair<>(7, 0.7f));
            mapEntityTypes.put("COD", new Pair<>(8, 0.3f));
            mapEntityTypes.put("COW", new Pair<>(9, 1.4f));
            mapEntityTypes.put("CREEPER", new Pair<>(10, 1.7f));
            mapEntityTypes.put("DONKEY", new Pair<>(11, 1.39648f));
            mapEntityTypes.put("DOLPHIN", new Pair<>(12, 0.6f));
            mapEntityTypes.put("DROWNED", new Pair<>(14, 1.95f));
            mapEntityTypes.put("ELDER_GUARDIAN", new Pair<>(15, 2.9f));
            mapEntityTypes.put("ENDER_DRAGON", new Pair<>(17, 8.0f));
            mapEntityTypes.put("ENDERMAN", new Pair<>(18, 2.9f));
            mapEntityTypes.put("ENDERMITE", new Pair<>(19, 0.3f));
            mapEntityTypes.put("EVOKER", new Pair<>(21, 1.95f));
            mapEntityTypes.put("HORSE", new Pair<>(28, 1.6f));
            mapEntityTypes.put("HUSK", new Pair<>(30, 1.95f));
            mapEntityTypes.put("ILLUSIONER", new Pair<>(31, 1.95f));
            mapEntityTypes.put("LLAMA", new Pair<>(36, 1.87f));
            mapEntityTypes.put("MAGMA_CUBE", new Pair<>(38, 0.51000005f));
            mapEntityTypes.put("MULE", new Pair<>(46, 1.6f));
            mapEntityTypes.put("MUSHROOM_COW", new Pair<>(47, 1.4f));
            mapEntityTypes.put("OCELOT", new Pair<>(48, 0.7f));
            mapEntityTypes.put("PARROT", new Pair<>(50, 0.9f));
            mapEntityTypes.put("PIG", new Pair<>(51, 0.9f));
            mapEntityTypes.put("PUFFERFISH", new Pair<>(52, 0.7f));
            mapEntityTypes.put("PIG_ZOMBIE", new Pair<>(53, 1.8f));
            mapEntityTypes.put("POLAR_BEAR", new Pair<>(54, 1.4f));
            mapEntityTypes.put("RABBIT", new Pair<>(56, 0.5f));
            mapEntityTypes.put("SALMON", new Pair<>(57, 0.4f));
            mapEntityTypes.put("SHEEP", new Pair<>(58, 1.3f));
            mapEntityTypes.put("SILVERFISH", new Pair<>(61, 0.3f));
            mapEntityTypes.put("SKELETON", new Pair<>(62, 1.99f));
            mapEntityTypes.put("SKELETON_HORSE", new Pair<>(63, 1.6f));
            mapEntityTypes.put("SLIME", new Pair<>(64, 0.51000005f));
            mapEntityTypes.put("SNOWMAN", new Pair<>(66, 1.9f));
            mapEntityTypes.put("GUARDIAN", new Pair<>(68, 0.85f));
            mapEntityTypes.put("SPIDER", new Pair<>(69, 0.9f));
            mapEntityTypes.put("SQUID", new Pair<>(70, 1.8f));
            mapEntityTypes.put("STRAY", new Pair<>(71, 1.99f));
            mapEntityTypes.put("TROPICAL_FISH", new Pair<>(72, 0.4f));
            mapEntityTypes.put("TURTLE", new Pair<>(73, 0.4f));
            mapEntityTypes.put("VEX", new Pair<>(78, 0.95f));
            mapEntityTypes.put("VILLAGER", new Pair<>(79, 1.95f));
            mapEntityTypes.put("IRON_GOLEM", new Pair<>(80, 2.7f));
            mapEntityTypes.put("VINDICATOR", new Pair<>(81, 1.95f));
            mapEntityTypes.put("WITCH", new Pair<>(82, 1.95f));
            mapEntityTypes.put("WITHER", new Pair<>(83, 3.5f));
            mapEntityTypes.put("WITHER_SKELETON", new Pair<>(84, 2.4f));
            mapEntityTypes.put("WOLF", new Pair<>(86, 0.85f));
            mapEntityTypes.put("ZOMBIE", new Pair<>(87, 1.95f));
            mapEntityTypes.put("ZOMBIE_HORSE", new Pair<>(88, 1.6f));
            mapEntityTypes.put("ZOMBIE_VILLAGER", new Pair<>(89, 1.95f));
            mapEntityTypes.put("PHANTOM", new Pair<>(90, 0.5f));

            // Objects
            mapEntityTypes.put("ENDER_CRYSTAL", new Pair<>(51, 2.0f));
            mapEntityTypes.put("ARROW", new Pair<>(60, 0.5f));
            mapEntityTypes.put("SNOWBALL", new Pair<>(61, 0.25f));
            mapEntityTypes.put("EGG", new Pair<>(62, 0.25f));
            mapEntityTypes.put("FIREBALL", new Pair<>(63, 1.0f));
            mapEntityTypes.put("SMALL_FIREBALL", new Pair<>(64, 0.3125f));
            mapEntityTypes.put("ENDER_PEARL", new Pair<>(65, 0.25f));
            mapEntityTypes.put("ENDER_SIGNAL", new Pair<>(72, 0.25f));
            mapEntityTypes.put("FIREWORK", new Pair<>(76, 0.25f));
        }
    }

    @Getter
    private static NMS instance;

    public static void init() {
        if (Version.before(9)) {
            instance = new NMS_1_8();
        } else if (Version.before(17)) {
            instance = new NMS_1_9();
        } else {
            instance = new NMS_1_17();
        }
    }

    protected Object getPlayerConnection(Player player) {
        Object entityPlayer = CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player);
        return ENTITY_PLAYER_CONNECTION_FIELD.getValue(entityPlayer);
    }

    public void sendPacket(Player player, Object packet) {
        if (packet == null || !PACKET_CLASS.isAssignableFrom(packet.getClass())) return;
        Object playerConnection = getPlayerConnection(player);
        PLAYER_CONNECTION_SEND_PACKET_METHOD.invoke(playerConnection, packet);
    }

    public ChannelPipeline getPipeline(Player player) {
        Object playerConnection = getPlayerConnection(player);
        Object networkManager = PLAYER_CONNECTION_NETWORK_MANAGER_FIELD.getValue(playerConnection);
        Channel channel = (Channel) NETWORK_MANAGER_CHANNEL_FIELD.getValue(networkManager);
        return channel.pipeline();
    }

    public int getEntityTypeId(EntityType type) {
        if (type == null) return -1;
        String name = type.name();
        if (mapEntityTypes.containsKey(name)) {
            return mapEntityTypes.get(name).getKey();
        }
        return -1;
    }

    public float getEntityHeight(EntityType type) {
        if (type == null) return 0.0f;
        String name = type.name();
        if (mapEntityTypes.containsKey(name)) {
            return mapEntityTypes.get(name).getValue();
        }
        return 0.0f;
    }

    public abstract int getFreeEntityId();

    public abstract void showFakeEntity(Player player, Location location, EntityType entityType, int entityId);

    public abstract void showFakeEntityLiving(Player player, Location location, EntityType entityType, int entityId);

    public abstract void showFakeEntityArmorStand(Player player, Location location, int entityId, boolean invisible, boolean small, boolean clickable);

    public abstract void showFakeEntityItem(Player player, Location location, ItemStack itemStack, int entityId);

    public abstract void updateFakeEntityCustomName(Player player, String name, int entityId);

    public abstract void teleportFakeEntity(Player player, Location location, int entityId);

    public abstract void helmetFakeEntity(Player player, ItemStack itemStack, int entityId);

    public abstract void attachFakeEntity(Player player, int vehicleId, int entityId);

    public abstract void hideFakeEntities(Player player, int... entityIds);

}

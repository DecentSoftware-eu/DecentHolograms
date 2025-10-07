package eu.decentsoftware.holograms.api.utils.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.skin.SkinService;
import eu.decentsoftware.holograms.skin.mojang.MojangSkinSource;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

/**
 * Utility class for modifying the textures or owners or skull ItemStacks.
 * All ItemStacks modified by this class have to be of type either SKULL_ITEM
 * or PLAYER_HEAD in versions 1.13 and up.
 *
 * @author d0by
 */
@UtilityClass
public final class SkullUtils {

    private static final SkinService skinService = new SkinService(new MojangSkinSource());

    private static final String RESOLVABLE_PROFILE_CLASS_PATH = "net.minecraft.world.item.component.ResolvableProfile";
    private static Field profileField;
    private static Method setProfileMethod;
    private static boolean initialized = false;

    private static Constructor<?> resolvableProfileConstructor;
    private static Field gameProfileFieldResolvableProfile;

    private static Function<Property, String> valueResolver;

    static {
        try {
            if (ReflectionUtil.checkClassExists(RESOLVABLE_PROFILE_CLASS_PATH)) {
                Class<?> resolvableProfileClass = ReflectionUtil.getClass(RESOLVABLE_PROFILE_CLASS_PATH);
                resolvableProfileConstructor = resolvableProfileClass == null ? null : resolvableProfileClass.getConstructor(GameProfile.class);

                // find the game profile field in the resolvable profile class
                gameProfileFieldResolvableProfile = ReflectionUtil.findField(resolvableProfileClass, GameProfile.class);
            }
        } catch (NoSuchMethodException ignored) {
            // old version, no resolvable profile class.
        }
    }

    /**
     * Get the Base64 texture of the given skull ItemStack.
     *
     * @param itemStack The ItemStack.
     * @return The skull texture. (Base64)
     */
    @Nullable
    public static String getSkullTexture(@NonNull ItemStack itemStack) {
        Method propertyValueMethod;
        try {
            ItemMeta meta = itemStack.getItemMeta();
            if (!(meta instanceof SkullMeta)) {
                return null;
            }

            if (profileField == null) {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
            }
            Object profileObject = profileField.get(meta);
            if (profileObject == null) return null;

            GameProfile profile = (GameProfile) (gameProfileFieldResolvableProfile == null ? profileObject : gameProfileFieldResolvableProfile.get(profileObject));
            if (profile == null) return null;

            if (valueResolver == null) {
                try {
                    // Pre 1.20(.4?) uses getValue
                    Property.class.getMethod("getValue");
                    valueResolver = Property::getValue;
                } catch (NoSuchMethodException ignored) {
                    // Since 1.20(.4?) the Property class is a record and utilizes record-style getter methods
                    //noinspection JavaReflectionMemberAccess - method does exist in newer versions
                    propertyValueMethod = Property.class.getMethod("value");
                    valueResolver = property -> {
                        try {
                            return (String) propertyValueMethod.invoke(property);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            Log.error("Failed to invoke Property#value", e);
                        }
                        return null;
                    };
                }
            }

            PropertyMap properties = profile.getProperties();
            Collection<Property> property = properties.get("textures");
            if (property != null && !property.isEmpty()) {
                return valueResolver.apply(property.iterator().next());
            }
        } catch (Exception e) {
            Log.error("An exception occurred while retrieving skull texture", e);
        }
        return null;
    }

    /**
     * Get the Base64 texture of the given skull ItemStack.
     *
     * @param itemStack The ItemStack.
     * @return The skull texture. (Base64)
     * @deprecated Use {@link #getSkullTexture(ItemStack)} instead.
     */
    @Deprecated
    @Nullable
    public static String getTexture(@NonNull ItemStack itemStack) {
        return getSkullTexture(itemStack);
    }

    /**
     * Set the Base64 texture of the given skull ItemStack.
     *
     * @param itemStack The ItemStack.
     * @param texture   The new skull texture (Base64).
     */
    public static void setSkullTexture(@NonNull ItemStack itemStack, @NonNull String texture) {
        try {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta instanceof SkullMeta) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), "");
                Property property = new Property("textures", texture);

                PropertyMap properties = profile.getProperties();
                properties.put("textures", property);
                if (setProfileMethod == null && !initialized) {
                    try {
                        // This method only exists in versions 1.16 and up. For older versions, we use reflection
                        // to set the profile field directly.
                        setProfileMethod = meta.getClass().getDeclaredMethod("setProfile", resolvableProfileConstructor == null ? GameProfile.class : resolvableProfileConstructor.getDeclaringClass());
                        setProfileMethod.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        // Server is running an older version.
                    }
                    initialized = true;
                }

                if (setProfileMethod != null) {
                    setProfileMethod.invoke(meta, resolvableProfileConstructor == null ? profile : resolvableProfileConstructor.newInstance(profile));
                } else {
                    if (profileField == null) {
                        profileField = meta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                    }
                    profileField.set(meta, profile);
                }
            }
            itemStack.setItemMeta(meta);

            if (Version.before(13)) {
                // noinspection deprecation
                itemStack.setDurability((short) SkullType.PLAYER.ordinal());
            }
        } catch (Exception e) {
            Log.error("An exception occurred while setting skull texture", e);
        }
    }

    /**
     * Get the owner of the given skull ItemStack.
     *
     * @param itemStack The ItemStack.
     * @return The skull owner.
     * @since 2.7.5
     */
    @SuppressWarnings("deprecation")
    @Nullable
    public static String getSkullOwner(@NonNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta instanceof SkullMeta) {
            return ((SkullMeta) meta).getOwner();
        }
        return null;
    }

    /**
     * Set the owner of the given skull ItemStack.
     *
     * @param itemStack The ItemStack.
     * @param owner     The new skull owner.
     * @since 2.7.5
     */
    @SuppressWarnings("deprecation")
    public static void setSkullOwner(@NonNull ItemStack itemStack, @NonNull String owner) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta instanceof SkullMeta) {
            ((SkullMeta) meta).setOwner(owner);

            itemStack.setItemMeta(meta);

            if (Version.before(13)) {
                // noinspection deprecation
                itemStack.setDurability((short) SkullType.PLAYER.ordinal());
            }
        }
    }

    /**
     * Returns the cached Base64 value from a Player name - if actually cached - or
     * attempts to retrieve it using the provided Player name.
     *
     * @param username The player username.
     * @return The Base64 or null if the texture couldn't be obtained.
     * @since 2.8.17
     */
    @Nullable
    public static String getCachedOrFetchFromUsername(String username) {
        return skinService.getSkinTextureByPlayerName(username);
    }
}

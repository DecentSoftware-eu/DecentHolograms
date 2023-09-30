package eu.decentsoftware.holograms.api.utils.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.UUID;

/**
 * Utility class for modifying the textures or owners or skull ItemStacks.
 * All ItemStacks modified by this class have to be of type either SKULL_ITEM
 * or PLAYER_HEAD in versions 1.13 and up.
 *
 * @author d0by
 */
@UtilityClass
public final class SkullUtils {

	private static Field PROFILE_FIELD;
	private static Method SET_PROFILE_METHOD;
	private static boolean INITIALIZED = false;

	/**
	 * Get the Base64 texture of the given skull ItemStack.
	 *
	 * @param itemStack The ItemStack.
	 * @return The skull texture. (Base64)
	 */
	@Nullable
	public static String getSkullTexture(@NonNull ItemStack itemStack) {
		try {
			ItemMeta meta = itemStack.getItemMeta();
			if (!(meta instanceof SkullMeta)) {
				return null;
			}

			if (PROFILE_FIELD == null) {
				PROFILE_FIELD = meta.getClass().getDeclaredField("profile");
				PROFILE_FIELD.setAccessible(true);
			}

			GameProfile profile = (GameProfile) PROFILE_FIELD.get(meta);
			if (profile == null) {
				return null;
			}

			PropertyMap properties = profile.getProperties();
			Collection<Property> property = properties.get("textures");
			if (property != null && !property.isEmpty()) {
				return property.stream().findFirst().get().getValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
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

				if (SET_PROFILE_METHOD == null && !INITIALIZED) {
					try {
						// This method only exists in versions 1.16 and up. For older versions we use reflection
						// to set the profile field directly.
						SET_PROFILE_METHOD = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
						SET_PROFILE_METHOD.setAccessible(true);
					} catch (NoSuchMethodException e) {
						// Server is running an older version.
					}
					INITIALIZED = true;
				}

				if (SET_PROFILE_METHOD != null) {
					SET_PROFILE_METHOD.invoke(meta, profile);
				} else {
					if (PROFILE_FIELD == null) {
						PROFILE_FIELD = meta.getClass().getDeclaredField("profile");
						PROFILE_FIELD.setAccessible(true);
					}
					PROFILE_FIELD.set(meta, profile);
				}
			}
			itemStack.setItemMeta(meta);

			if (Version.before(13)) {
				// noinspection deprecation
				itemStack.setDurability((short) SkullType.PLAYER.ordinal());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the owner of the given skull ItemStack.
	 *
	 * @param itemStack The ItemStack.
	 * @return The skull owner.
	 * @since 2.7.5
	 */
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
	 * Get a Base64 texture from URL by player username.
	 *
	 * @param username The player username.
	 * @return The Base64 or null if the URL is invalid.
	 * @since 2.7.10
	 */
	@Nullable
	public static String getTextureFromURLByPlayerName(String username) {
		final String uuid = getPlayerUUID(username);
		if (uuid == null) {
			return null;
		}
		final String url = "https://api.minetools.eu/profile/" + uuid;
		try {
			JSONParser jsonParser = new JSONParser();
			String userData = readUrl(url);
			Object parsedData = jsonParser.parse(userData);

			JSONObject jsonData = (JSONObject) parsedData;
			JSONObject decoded = (JSONObject) jsonData.get("raw");
			JSONArray textures = (JSONArray) decoded.get("properties");
			JSONObject data = (JSONObject) textures.get(0);

			return data.get("value").toString();
		} catch (Exception ignored) {
		}
		return null;
	}

	/**
	 * Fetch the UUID of a player from Minetools's API.
	 *
	 * @param playerName The player name.
	 * @return
	 */
	@Nullable
	public static String getPlayerUUID(String playerName) {
		try {
			String url = "https://api.minetools.eu/uuid/" + playerName;
			JSONParser jsonParser = new JSONParser();
			String userData = readUrl(url);
			Object parsedData = jsonParser.parse(userData);

			JSONObject jsonData = (JSONObject) parsedData;

			if (jsonData.get("id") != null) {
				return jsonData.get("id").toString();
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	/**
	 * Read the content of a URL.
	 *
	 * @param urlString The URL.
	 * @return The content.
	 * @throws Exception
	 */
	@NonNull
	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		URLConnection connection = null;
		try {
			connection = new URL(urlString).openConnection();
			connection.setConnectTimeout(50);
			connection.setReadTimeout(50);
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder builder = new StringBuilder();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1) {
				builder.append(chars, 0, read);
			}
			return builder.toString();
		} finally {
			if (connection instanceof HttpURLConnection) {
				((HttpURLConnection) connection).disconnect();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

}

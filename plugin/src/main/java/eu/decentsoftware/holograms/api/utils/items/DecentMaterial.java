package eu.decentsoftware.holograms.api.utils.items;

import com.cryptomorin.xseries.XMaterial;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public final class DecentMaterial {

    private static final Map<String, String> MATERIAL_ALIASES = new HashMap<>();

    static {
        for (Material material : Material.values()) {
            MATERIAL_ALIASES.put(Common.removeSpacingChars(material.name()).toLowerCase(), material.name());
        }
    }

    /**
     * Resolved on first use rather than in the static initializer, because it depends on the
     * server version, which is only known once the platform has been detected. Initializing it
     * eagerly would make merely touching this class before that point fail with an
     * {@link ExceptionInInitializerError}, which cannot be recovered from.
     */
    private static final class IsItemMethodHolder {

        private static final ReflectMethod METHOD = Version.before(13)
                ? new ReflectMethod(ReflectionUtil.getNMSClass("Item"), "getById", int.class)
                : new ReflectMethod(Material.class, "isItem");

        private IsItemMethodHolder() {
        }
    }

    public static Material parseMaterial(String materialName) {
        // Backward compatibility
        Material materialFromAliases = Material.getMaterial(MATERIAL_ALIASES.get(Common.removeSpacingChars(materialName).toLowerCase()));
        if (materialFromAliases != null) {
            return materialFromAliases;
        }
        Optional<XMaterial> xMaterialOptional = XMaterial.matchXMaterial(materialName);
        return xMaterialOptional.map(XMaterial::get).orElse(null);
    }

    @SuppressWarnings("deprecation")
    public static boolean isItem(Material material) {
        if (Version.afterOrEqual(13)) {
            return IsItemMethodHolder.METHOD.invoke(material);
        } else {
            return IsItemMethodHolder.METHOD.invokeStatic(material.getId()) != null;
        }
    }

    public static boolean isSkull(Material material) {
        XMaterial xMaterial = matchXMaterial(material);
        // XMaterial also handles legacy skull materials: SKULL, SKULL_ITEM
        return xMaterial == XMaterial.PLAYER_HEAD || xMaterial == XMaterial.PLAYER_WALL_HEAD;
    }

    public static boolean isLeatherArmor(Material material) {
        XMaterial xMaterial = matchXMaterial(material);
        return xMaterial == XMaterial.LEATHER_HELMET
                || xMaterial == XMaterial.LEATHER_CHESTPLATE
                || xMaterial == XMaterial.LEATHER_LEGGINGS
                || xMaterial == XMaterial.LEATHER_BOOTS
                || xMaterial == XMaterial.LEATHER_HORSE_ARMOR;
    }

    /**
     * Match the given material to an XMaterial.
     *
     * @param material The material.
     * @return The matching XMaterial, or null if XSeries does not know this material.
     * @since 2.10.2
     */
    private static XMaterial matchXMaterial(Material material) {
        try {
            return XMaterial.matchXMaterial(material);
        } catch (IllegalArgumentException e) {
            // XSeries throws for unknown materials. This can happen when the bundled version of XSeries
            // doesn't support the current server version. Since we only need to check for leather armor
            // and player head materials here, we don't need those new materials anyway, so it's safe to
            // return null in this case.
            return null;
        }
    }
}

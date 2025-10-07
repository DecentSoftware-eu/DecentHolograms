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
    private static final ReflectMethod MATERIAL_IS_ITEM_METHOD;

    static {
        for (Material material : Material.values()) {
            MATERIAL_ALIASES.put(Common.removeSpacingChars(material.name()).toLowerCase(), material.name());
        }

        if (Version.before(13)) {
            MATERIAL_IS_ITEM_METHOD = new ReflectMethod(ReflectionUtil.getNMSClass("Item"), "getById", int.class);
        } else {
            MATERIAL_IS_ITEM_METHOD = new ReflectMethod(Material.class, "isItem");
        }
    }

    public static Material parseMaterial(String materialName) {
        // Backwards compatibility
        Material materialFromAliases = Material.getMaterial(MATERIAL_ALIASES.get(Common.removeSpacingChars(materialName).toLowerCase()));
        if (materialFromAliases != null) {
            return materialFromAliases;
        }
        Optional<XMaterial> xMaterialOptional = XMaterial.matchXMaterial(materialName);
        return xMaterialOptional.map(XMaterial::parseMaterial).orElse(null);
    }

    @SuppressWarnings("deprecation")
    public static boolean isItem(Material material) {
        if (Version.afterOrEqual(13)) {
            return MATERIAL_IS_ITEM_METHOD.invoke(material);
        } else {
            return MATERIAL_IS_ITEM_METHOD.invokeStatic(material.getId()) != null;
        }
    }

}

package eu.decentsoftware.holograms.api.utils.items;

import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class DecentMaterial {

    private static final Map<String, String> MATERIAL_ALIASES = new HashMap<>();
    private static final ReflectMethod MATERIAL_IS_ITEM_METHOD;

    static {
        for (Material material : Material.values()) {
            MATERIAL_ALIASES.put(Common.removeSpacingChars(material.name()).toLowerCase(), material.name());
        }

        if (Common.SERVER_VERSION.isBefore(Version.v1_13_R1)) {
            MATERIAL_ALIASES.put("brewing stand", "BREWING_STAND_ITEM");
            MATERIAL_ALIASES.put("carrot", "CARROT_ITEM");
            MATERIAL_ALIASES.put("carrot on stick", "CARROT_STICK");
            MATERIAL_ALIASES.put("carrot on a stick", "CARROT_STICK");
            MATERIAL_ALIASES.put("cauldron", "CAULDRON_ITEM");
            MATERIAL_ALIASES.put("steak", "COOKED_BEEF");
            MATERIAL_ALIASES.put("cobblestone wall", "COBBLE_WALL");
            MATERIAL_ALIASES.put("command block", "COMMAND");
            MATERIAL_ALIASES.put("repeater", "DIODE");
            MATERIAL_ALIASES.put("double slab", "DOUBLE_STEP");
            MATERIAL_ALIASES.put("diamond shovel", "DIAMOND_SPADE");
            MATERIAL_ALIASES.put("flower pot", "FLOWER_POT_ITEM");
            MATERIAL_ALIASES.put("grilled porkchop", "GRILLED_PORK");
            MATERIAL_ALIASES.put("cooked porkchop", "GRILLED_PORK");
            MATERIAL_ALIASES.put("gold shovel", "GOLD_SPADE");
            MATERIAL_ALIASES.put("huge brown mushroom", "HUGE_MUSHROOM_1");
            MATERIAL_ALIASES.put("huge red mushroom", "HUGE_MUSHROOM_2");
            MATERIAL_ALIASES.put("hardened clay", "HARD_CLAY");
            MATERIAL_ALIASES.put("iron bars", "IRON_FENCE");
            MATERIAL_ALIASES.put("dye", "INK_SACK");
            MATERIAL_ALIASES.put("iron shovel", "IRON_SPADE");
            MATERIAL_ALIASES.put("mycelium", "MYCEL");
            MATERIAL_ALIASES.put("nether wart", "NETHER_STALK");
            MATERIAL_ALIASES.put("nether warts", "NETHER_STALK");
            MATERIAL_ALIASES.put("porkchop", "PORK");
            MATERIAL_ALIASES.put("raw porkchop", "PORK");
            MATERIAL_ALIASES.put("potato", "POTATO_ITEM");
            MATERIAL_ALIASES.put("piston", "PISTON_BASE");
            MATERIAL_ALIASES.put("sticky piston", "PISTON_STICKY_BASE");
            MATERIAL_ALIASES.put("comparator", "REDSTONE_COMPARATOR");
            MATERIAL_ALIASES.put("redstone torch", "REDSTONE_TORCH_ON");
            MATERIAL_ALIASES.put("redstone lamp", "REDSTONE_LAMP_OFF");
            MATERIAL_ALIASES.put("poppy", "RED_ROSE");
            MATERIAL_ALIASES.put("head", "SKULL_ITEM");
            MATERIAL_ALIASES.put("skull", "SKULL_ITEM");
            MATERIAL_ALIASES.put("slab", "STEP");
            MATERIAL_ALIASES.put("gunpowder", "SULPHUR");
            MATERIAL_ALIASES.put("glistering lamp", "SPECKLED_MELON");
            MATERIAL_ALIASES.put("stone shovel", "STONE_SPADE");
            MATERIAL_ALIASES.put("stone brick", "SMOOTH_BRICK");
            MATERIAL_ALIASES.put("stone bricks", "SMOOTH_BRICK");
            MATERIAL_ALIASES.put("stone stair", "SMOOTH_STAIRS");
            MATERIAL_ALIASES.put("stone stairs", "SMOOTH_STAIRS");
            MATERIAL_ALIASES.put("glass pane", "THIN_GLASS");
            MATERIAL_ALIASES.put("double wood slab", "WOOD_DOUBLE_STEP");
            MATERIAL_ALIASES.put("wood slab", "WOOD_STEP");
            MATERIAL_ALIASES.put("wood shovel", "WOOD_SPADE");
            MATERIAL_ALIASES.put("wooden shovel", "WOOD_SPADE");
            MATERIAL_ALIASES.put("lilypad", "WATER_LILY");

            MATERIAL_IS_ITEM_METHOD = new ReflectMethod(ReflectionUtil.getNMSClass("Item"), "getById", int.class);
        } else {
            MATERIAL_IS_ITEM_METHOD = new ReflectMethod(Material.class, "isItem");
        }
    }

    public static Material parseMaterial(String string) {
        return Material.getMaterial(MATERIAL_ALIASES.get(Common.removeSpacingChars(string).toLowerCase()));
    }

    @SuppressWarnings("deprecation")
    public static boolean isItem(Material material) {
        if (Common.SERVER_VERSION.isAfterOrEqual(Version.v1_13_R1)) {
            return MATERIAL_IS_ITEM_METHOD.invoke(material);
        } else {
            return MATERIAL_IS_ITEM_METHOD.invokeStatic(material.getId()) != null;
        }
    }

}

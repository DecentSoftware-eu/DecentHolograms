package eu.decentsoftware.holograms.utils.items;

import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.utils.reflect.ReflectionUtil;
import eu.decentsoftware.holograms.utils.reflect.ServerVersion;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DecentMaterial {

    private static final Map<String, Material> MATERIAL_ALIASES = new HashMap<>();

    static {
        for (Material material : Material.values()) {
            MATERIAL_ALIASES.put(Common.removeSpacingChars(material.name()).toLowerCase(), material);
        }

        if (Common.SERVER_VERSION.isBefore(ServerVersion.v1_13_R1)) {
            MATERIAL_ALIASES.put("brewing stand", Material.BREWING_STAND_ITEM);
            MATERIAL_ALIASES.put("carrot", Material.CARROT_ITEM);
            MATERIAL_ALIASES.put("carrot on stick", Material.CARROT_STICK);
            MATERIAL_ALIASES.put("carrot on a stick", Material.CARROT_STICK);
            MATERIAL_ALIASES.put("cauldron", Material.CAULDRON_ITEM);
            MATERIAL_ALIASES.put("steak", Material.COOKED_BEEF);
            MATERIAL_ALIASES.put("cobblestone wall", Material.COBBLE_WALL);
            MATERIAL_ALIASES.put("command block", Material.COMMAND);
            MATERIAL_ALIASES.put("repeater", Material.DIODE);
            MATERIAL_ALIASES.put("double slab", Material.DOUBLE_STEP);
            MATERIAL_ALIASES.put("diamond showel", Material.DIAMOND_SPADE);
            MATERIAL_ALIASES.put("flower pot", Material.FLOWER_POT_ITEM);
            MATERIAL_ALIASES.put("grilled porkchop", Material.GRILLED_PORK);
            MATERIAL_ALIASES.put("cooked porkchop", Material.GRILLED_PORK);
            MATERIAL_ALIASES.put("gold showel", Material.GOLD_SPADE);
            MATERIAL_ALIASES.put("huge brown mushroom", Material.HUGE_MUSHROOM_1);
            MATERIAL_ALIASES.put("huge red mushroom", Material.HUGE_MUSHROOM_2);
            MATERIAL_ALIASES.put("hardened clay", Material.HARD_CLAY);
            MATERIAL_ALIASES.put("iron bars", Material.IRON_FENCE);
            MATERIAL_ALIASES.put("dye", Material.INK_SACK);
            MATERIAL_ALIASES.put("iron showel", Material.IRON_SPADE);
            MATERIAL_ALIASES.put("mycelium", Material.MYCEL);
            MATERIAL_ALIASES.put("nether wart", Material.NETHER_STALK);
            MATERIAL_ALIASES.put("nether warts", Material.NETHER_STALK);
            MATERIAL_ALIASES.put("porkchop", Material.PORK);
            MATERIAL_ALIASES.put("raw porkchop", Material.PORK);
            MATERIAL_ALIASES.put("potato", Material.POTATO_ITEM);
            MATERIAL_ALIASES.put("piston", Material.PISTON_BASE);
            MATERIAL_ALIASES.put("sticky piston", Material.PISTON_STICKY_BASE);
            MATERIAL_ALIASES.put("comparator", Material.REDSTONE_COMPARATOR);
            MATERIAL_ALIASES.put("redstone torch", Material.REDSTONE_TORCH_ON);
            MATERIAL_ALIASES.put("redstone lamp", Material.REDSTONE_LAMP_OFF);
            MATERIAL_ALIASES.put("poppy", Material.RED_ROSE);
            MATERIAL_ALIASES.put("head", Material.SKULL_ITEM);
            MATERIAL_ALIASES.put("skull", Material.SKULL_ITEM);
            MATERIAL_ALIASES.put("slab", Material.STEP);
            MATERIAL_ALIASES.put("gunpowder", Material.SULPHUR);
            MATERIAL_ALIASES.put("glistering lamp", Material.SPECKLED_MELON);
            MATERIAL_ALIASES.put("stone showel", Material.STONE_SPADE);
            MATERIAL_ALIASES.put("stone brick", Material.SMOOTH_BRICK);
            MATERIAL_ALIASES.put("stone bricks", Material.SMOOTH_BRICK);
            MATERIAL_ALIASES.put("stone stair", Material.SMOOTH_STAIRS);
            MATERIAL_ALIASES.put("stone stairs", Material.SMOOTH_STAIRS);
            MATERIAL_ALIASES.put("glass pane", Material.THIN_GLASS);
            MATERIAL_ALIASES.put("double wood slab", Material.WOOD_DOUBLE_STEP);
            MATERIAL_ALIASES.put("wood slab", Material.WOOD_STEP);
            MATERIAL_ALIASES.put("wood showel", Material.WOOD_SPADE);
            MATERIAL_ALIASES.put("wooden showel", Material.WOOD_SPADE);
            MATERIAL_ALIASES.put("lilypad", Material.WATER_LILY);
        }
    }

    @SuppressWarnings("deprecation")
    @Nullable
    public static Material parseMaterial(String string) {
        if (Common.SERVER_VERSION.isBefore(ServerVersion.v1_13_R1)) {
            try {
                return Material.getMaterial(Integer.parseInt(string));
            } catch (NumberFormatException ignored) {}
        }
        return MATERIAL_ALIASES.get(Common.removeSpacingChars(string).toLowerCase());
    }

    @SuppressWarnings("deprecation")
    public static boolean isItem(Material material) {
        if (Common.SERVER_VERSION.isAfterOrEqual(ServerVersion.v1_13_R1)) {
            ReflectMethod method = new ReflectMethod(Material.class, "isItem");
            return method.invoke(material);
        } else {
            ReflectMethod method = new ReflectMethod(ReflectionUtil.getNMSClass("Item"), "getById", int.class);
            return method.invokeStatic(material.getId()) != null;
        }
    }

}

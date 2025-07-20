package eu.decentsoftware.holograms.plugin;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.commands.DecentCommandException;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@UtilityClass
public final class Validator {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    /*
     *	General Methods
     */

    public static Hologram getHologram(String name) throws DecentCommandException {
        Hologram hologram = DECENT_HOLOGRAMS.getHologramManager().getHologram(name);
        if (hologram == null) {
            throw new DecentCommandException("Hologram with that name couldn't be found.");
        }
        return hologram;
    }

    public static HologramPage getHologramPage(Hologram hologram, int index) throws DecentCommandException {
        index = getIntegerInRange(index, 1, hologram.size(), "Page index must be in bounds of given hologram.");
        return hologram.getPage(index - 1);
    }

    public static HologramLine getHologramLine(HologramPage page, int index, String message) throws DecentCommandException {
        HologramLine line = page.getLine(Validator.getHologramLineIndex(page, index) - 1);
        if (line == null) {
            throw new DecentCommandException(message);
        }
        return line;
    }

    public static HologramLine getHologramLine(HologramPage page, int index) throws DecentCommandException {
        return getHologramLine(page, index, "Hologram line couldn't be found.");
    }

    /*
     *	Hologram Methods
     */

    public static Hologram getHologram(String name, String message) throws DecentCommandException {
        Hologram hologram = DECENT_HOLOGRAMS.getHologramManager().getHologram(name);
        if (hologram == null) throw new DecentCommandException(message);
        return hologram;
    }

    public static HologramPage getHologramPage(Hologram hologram, int index, String message) throws DecentCommandException {
        index = getIntegerInRange(index, 1, hologram.size(), message);
        return hologram.getPage(index - 1);
    }

    public static HologramPage getHologramPage(Hologram hologram, String indexString, String message) throws DecentCommandException {
        int index = getInteger(indexString, 1, hologram.size(), message);
        return hologram.getPage(index - 1);
    }

    public static int getHologramLineIndex(HologramPage page, int index) throws DecentCommandException {
        return getIntegerInRange(index, 1, page.size(), "Line index must be in bounds of given hologram page.");
    }

    public static int getHologramLineIndex(HologramPage page, String index) throws DecentCommandException {
        return Validator.getInteger(index, 1, page.size(), "Line index must be in bounds of given hologram page.");
    }

    public static EnumFlag getFlag(String string, String message) throws DecentCommandException {
        try {
            return EnumFlag.valueOf(string);
        } catch (Exception e) {
            throw new DecentCommandException(message);
        }
    }

    /*
     *	String & Arrays Methods
     */

    @NonNull
    public static String getString(String[] arr, int beginIndex, int endIndex) {
        return String.join(" ", Arrays.copyOfRange(arr, beginIndex, endIndex));
    }

    @NonNull
    public static String getLineContent(String @NonNull [] args, int beginIndex) {
        String text = Settings.DEFAULT_TEXT;
        if (args.length > beginIndex) {
            String[] textArray = Arrays.copyOfRange(args, beginIndex, args.length);
            text = textArray.length == 1 ? textArray[0] : String.join(" ", textArray);
        }
        return text;
    }

    @NonNull
    public static String getLineContent(Player player, String @NonNull [] args, int beginIndex) {
        String text = Settings.DEFAULT_TEXT;
        if (args.length > beginIndex) {
            String[] textArray = Arrays.copyOfRange(args, beginIndex, args.length);
            text = textArray.length == 1 ? textArray[0] : String.join(" ", textArray);
            if (text.contains("<HAND>")) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                    text = text.replace("<HAND>", HologramItem.fromItemStack(itemStack).getContent());
                } else {
                    text = text.replace("<HAND>", "STONE");
                }
            }
        }
        return text;
    }

    /*
     *	CommandSender-Player Methods
     */

    public static Player getPlayer(CommandSender sender) throws DecentCommandException {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            throw new DecentCommandException(Lang.ONLY_PLAYER.getValue());
        }
    }

    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    /*
     *	String-Integer Methods
     */

    public static int getIntegerInRange(int i, int min, int max, String message) throws DecentCommandException {
        if (i < min || i > max) throw new DecentCommandException(Common.PREFIX + "&c" + message);
        return i;
    }

    public static int getInteger(String string, String message) throws DecentCommandException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            throw new DecentCommandException(message);
        }
    }

    public static int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public static int getInteger(String string, int min, int max, String message) throws DecentCommandException {
        try {
            return getIntegerInRange(Integer.parseInt(string), min, max, message);
        } catch (NumberFormatException ex) {
            throw new DecentCommandException(message);
        }
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /*
     *	String-Float Methods
     */

    public static float getFloat(String string, double min, double max, String message) throws DecentCommandException {
        float d = getFloat(string, message);
        if (d < min || d > max) {
            throw new DecentCommandException(message);
        }
        return d;
    }

    public static float getFloat(String string, String message) throws DecentCommandException {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException ex) {
            throw new DecentCommandException(message);
        }
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /*
     *	String-Double Methods
     */

    public static double getDouble(String string, double min, double max, String message) throws DecentCommandException {
        double d = getDouble(string, message);
        if (d < min || d > max) {
            throw new DecentCommandException(message);
        }
        return d;
    }

    public static double getDouble(String string, String message) throws DecentCommandException {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException ex) {
            throw new DecentCommandException(message);
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /*
     *	String-Byte Methods
     */

    public static double getByte(String string, byte min, byte max, String message) throws DecentCommandException {
        double d = getByte(string, message);
        if (d < min || d > max) {
            throw new DecentCommandException(message);
        }
        return d;
    }

    public static double getByte(String string, String message) throws DecentCommandException {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException ex) {
            throw new DecentCommandException(message);
        }
    }

    public static boolean isByte(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /*
     *	String-Boolean Methods
     */

    public static boolean getBoolean(String string, String message) throws DecentCommandException {
        try {
            return Boolean.parseBoolean(string);
        } catch (Exception e) {
            throw new DecentCommandException(message);
        }
    }

    public static boolean isBoolean(String string) {
        try {
            Boolean.parseBoolean(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static double getDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception ignored) {
            return 0;
        }
    }

    public static double getLocationValue(@NotNull String string, double initialValue) {
        boolean isDiff = false;
        if (string.startsWith("~")) {
            isDiff = true;
            string = string.substring(1);
        }

        double number = getDouble(string);
        if (isDiff) {
            return initialValue + number;
        }
        return number;
    }

}

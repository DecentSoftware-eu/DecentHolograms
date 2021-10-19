package eu.decentsoftware.holograms.core.edit;

import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.exception.DecentCommandException;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.objects.enums.EnumFlag;
import eu.decentsoftware.holograms.utils.Common;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class EditValidator {

	/*
	 *	General Methods
	 */

	public static void notNull(Object object, String message) throws DecentCommandException {
		if (object == null) throw new DecentCommandException(message);
	}

	public static void isTrue(boolean bool, String message) throws DecentCommandException {
		if (!bool) throw new DecentCommandException(message);
	}

	/*
	 *	Hologram Methods
	 */

	public static Hologram getHologram(String name, String message) throws DecentCommandException {
		Hologram hologram = DecentHologramsProvider.getDecentHolograms().getHologramManager().getHologram(name);
		if (hologram == null) throw new DecentCommandException(message);
		return hologram;
	}

	public static HologramLine getHologramLine(Hologram hologram, int index) throws DecentCommandException {
		return getHologramLine(hologram, index, "Hologram line couldn't be found.");
	}

	public static HologramLine getHologramLine(Hologram hologram, int index, String message) throws DecentCommandException {
		HologramLine line = hologram.getLine(EditValidator.getHologramLineIndex(hologram, index) - 1);
		if (line == null) throw new DecentCommandException(message);
		return line;
	}

	public static int getHologramLineIndex(Hologram hologram, int index) throws DecentCommandException {
		return EditValidator.getIntegerInRange(index, 1, hologram.size(), "Line index must be in bounds of given hologram.");
	}

	public static EnumFlag getFlag(String string, String message) throws DecentCommandException {
		try {
			return EnumFlag.valueOf(string);
		} catch (Throwable throwable) {
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
		}
	}

	/*
	 *	String & Arrays Methods
	 */

	public static String getString(String[] arr, int beginIndex, int endIndex) {
		return String.join(" ", Arrays.copyOfRange(arr, beginIndex, endIndex));
	}

	public static String getLineContent(String[] args, int beginIndex) {
		String text = Settings.DEFAULT_TEXT.getValue();
		if (args.length > beginIndex) {
			String[] textArray = Arrays.copyOfRange(args, beginIndex, args.length);
			text = textArray.length == 1 ? textArray[0] : String.join(" ", textArray);
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
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
		}
	}

	public static int getInteger(String string, int min, int max, String message) throws DecentCommandException {
		try {
			return getIntegerInRange(Integer.parseInt(string), min, max, message);
		} catch (NumberFormatException ex) {
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
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
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
		}
		return d;
	}

	public static float getFloat(String string, String message) throws DecentCommandException {
		try {
			return Float.parseFloat(string);
		} catch (NumberFormatException ex) {
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
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
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
		}
		return d;
	}

	public static double getDouble(String string, String message) throws DecentCommandException {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException ex) {
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
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
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
		}
		return d;
	}

	public static double getByte(String string, String message) throws DecentCommandException {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException ex) {
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
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
			throw new DecentCommandException(Common.PREFIX + "&c" + message);
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

}

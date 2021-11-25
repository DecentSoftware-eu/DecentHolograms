package eu.decentsoftware.holograms.api.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomUtils {

	public static final Random RANDOM = new Random();

	/**
	 * This method generates random Integer between min and max
	 *
	 * @param min Minimal random number
	 * @param max Maximum random number
	 * @return Randomly generated Integer
	 */
	public static int randomInt(int min, int max) {
		return RANDOM.nextInt((max - min) + 1) + min;
	}

	/**
	 * @return Random boolean value
	 */
	public static boolean randomBool() {
		return RANDOM.nextBoolean();
	}

	/**
	 * @return Random float between 0 and 1
	 */
	public static float randomFloat() {
		return RANDOM.nextFloat();
	}

}

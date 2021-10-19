package eu.decentsoftware.holograms.api.convertor;

import java.io.File;

/**
 * This interface represents a Convertor used to convert holograms from other plugins or sources.
 */
public interface IConvertor {

	/**
	 * Automatically find a file to convert the holograms from and convert them.
	 * @return Boolean whether the operation was successful.
	 */
	boolean convert();

	/**
	 * Convert holograms from the given file.
	 * @param file Given file.
	 * @return Boolean whether the operation was successful.
	 */
	boolean convert(File file);

	/**
	 * Convert holograms from all the given files.
	 * @param files Given files.
	 * @return Boolean whether the operation was successful.
	 */
	boolean convert(File... files);

}

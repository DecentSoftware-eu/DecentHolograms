package eu.decentsoftware.holograms.api.convertor;

import java.io.File;
import java.util.List;

/**
 * This interface represents a Convertor used to convert holograms from other plugins or sources.
 */
public interface IConvertor {

	/**
	 * Automatically find a file to convert the holograms from and convert them.
	 *
	 * @return Boolean whether the operation was successful.
	 */
	boolean convert();

	/**
	 * Convert holograms from the given file.
	 *
	 * @param file Given file.
	 * @return Boolean whether the operation was successful.
	 */
	boolean convert(File file);

	/**
	 * Convert holograms from all the given files.
	 *
	 * @param files Given files.
	 * @return Boolean whether the operation was successful.
	 */
	boolean convert(File... files);
	
	/**
	 * Check if a provided file is not null, does exist, is not a folder and has the same name (with extension)
	 * as provided.
	 * 
	 * @param file The file to check.
	 * @param fileName The file name to check.
	 * @return true if the aforementioned cases are all true.
	 */
	default boolean isValidFile(File file, String fileName) {
		return (file != null) && file.exists() && !file.isDirectory() && file.getName().equals(fileName);
	}
	
	/**
	 * Convert the formatting of the lines into one that DecentHolograms can understand.
	 * 
	 * @param lines Lines to convert.
	 * @return List containing the converted Lines.
	 */
	List<String> prepareLines(List<String> lines);

}

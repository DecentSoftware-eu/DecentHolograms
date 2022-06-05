package eu.decentsoftware.holograms.api.convertor;

import eu.decentsoftware.holograms.plugin.convertors.ConvertorRest;

import java.io.File;
import java.util.List;

/**
 * This interface represents a Convertor used to convert holograms from other plugins or sources.
 */
public interface IConvertor {

	/**
	 * Automatically find a file to convert the holograms from and convert them.
	 *
	 * @return IConvertorRest with the result of the operation - the success status and the number of converted holograms.
	 */
	IConvertorRest convert();

	/**
	 * Convert holograms from the given file.
	 *
	 * @param file Given file.
	 * @return IConvertorRest with the result of the operation - the success status and the number of converted holograms.
	 */
	IConvertorRest convert(File file);

	/**
	 * Convert holograms from all the given files.
	 *
	 * @param files Given files.
	 * @return IConvertorRest with the result of the operation - the success status and the number of converted holograms.
	 */
	IConvertorRest convert(File... files);
	
	/**
	 * Convert the formatting of the lines into one that DecentHolograms can understand.
	 * 
	 * @param lines Lines to convert.
	 * @return List containing the converted Lines.
	 */
	List<String> prepareLines(List<String> lines);

}

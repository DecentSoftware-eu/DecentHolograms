package eu.decentsoftware.holograms.api.convertor;

/**
 * This interface represents a ConvertorRest - conversion result to extract the success boolean and the number of holograms
 */
public interface IConvertorRest {

    /**
     * Allows you to get the success status of conversion.
     *
     * @return Boolean whether the operation was successful.
     */
    boolean getSuccess();

    /**
     * Allows you to get the number of all converted holograms.
     *
     * @return int
     */
    int getCount();

}

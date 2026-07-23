package eu.decentsoftware.holograms.platform.api.resource;

/**
 * Service for saving resources to the plugin's data folder.
 *
 * @author d0by
 * @since 2.10.1
 */
public interface SaveResourceService {

    /**
     * Saves the resource with the given name to the plugin's data folder.
     *
     * @param resourceName The name of the resource to save. Must be a relative path to a file in the plugin's resources.
     * @param overwrite    Whether to overwrite the file if it already exists.
     * @since 2.10.1
     */
    void saveResource(String resourceName, boolean overwrite);
}

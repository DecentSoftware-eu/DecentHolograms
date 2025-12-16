package eu.decentsoftware.holograms.plugin.file;

import java.io.File;

public interface FileSystemService {

    /**
     * Gets the directory where external expansion JAR files are stored.
     *
     * @return the directory for expansion JAR files
     */
    File getExpansionJarsDirectory();

    /**
     * Gets the directory for a specific expansion's data.
     *
     * @param expansionId the unique identifier of the expansion
     * @return the directory for the specified expansion's data
     */
    File getExpansionDataDirectory(String expansionId);
}

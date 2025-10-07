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
     * Gets the directory where configuration files for expansions are stored.
     *
     * @return the directory for expansion configuration files
     */
    File getExpansionConfigsDirectory();
}

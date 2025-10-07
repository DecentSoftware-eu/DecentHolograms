package eu.decentsoftware.holograms.api.expansion;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface ExpansionLoader {

    static @NotNull ExpansionLoader getDefaultLoader() {
        return new DefaultExpansionLoader();
    }

    /**
     * Load expansions using the provided class loader.
     *
     * @param classLoader the class loader to use for loading expansions
     * @return an iterable of loaded expansions
     */
    Iterable<? extends Expansion> loadExpansions(ClassLoader classLoader);

    /**
     * Load expansions from a file (e.g., a JAR file).
     *
     * @param file the file to load expansions from
     * @return an iterable of loaded expansions
     */
    Iterable<? extends Expansion> loadExpansions(File file);
}

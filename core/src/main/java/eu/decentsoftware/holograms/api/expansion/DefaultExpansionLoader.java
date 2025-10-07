package eu.decentsoftware.holograms.api.expansion;

import eu.decentsoftware.holograms.api.utils.classpath.JarFileURLClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ServiceLoader;

/**
 * Default implementation of the ExpansionLoader.
 *
 * @author ZorTik
 */
public class DefaultExpansionLoader implements ExpansionLoader {
    private final ClassLoader classLoader;

    /**
     * Creates a new DefaultExpansionLoader using the current thread's context class loader.
     */
    public DefaultExpansionLoader() {
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * Creates a new DefaultExpansionLoader with the specified class loader.
     *
     * @param classLoader the class loader to use for loading expansions
     */
    public DefaultExpansionLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Iterable<? extends Expansion> loadExpansions(ClassLoader classLoader) {
        return ServiceLoader.load(Expansion.class, classLoader);
    }

    @Override
    public Iterable<? extends Expansion> loadExpansions(File file) {
        try {
            ClassLoader fileClassLoader = new JarFileURLClassLoader(file, classLoader);

            return loadExpansions(fileClassLoader);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid file provided.", e);
        }
    }
}

package eu.decentsoftware.holograms.api.expansion.source;

import eu.decentsoftware.holograms.api.expansion.Expansion;
import eu.decentsoftware.holograms.api.expansion.ExpansionLoader;

import java.io.File;

public class JarFileExpansionSource implements ExpansionSource {
    private final File file;
    private final ExpansionLoader loader;

    public JarFileExpansionSource(File file, ExpansionLoader loader) {
        this.file = file;
        this.loader = loader;
    }

    @Override
    public String getId() {
        return file.getName();
    }

    @Override
    public Iterable<? extends Expansion> loadExpansions() {
        return loader.loadExpansions(file);
    }
}

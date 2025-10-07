package eu.decentsoftware.holograms.api.utils.classpath;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicURLClassLoader extends URLClassLoader {

    public DynamicURLClassLoader(ClassLoader parent) {
        super(new URL[]{}, parent);
    }

    public void addFile(File file) throws MalformedURLException {
        super.addURL(file.toURI().toURL());
    }
}

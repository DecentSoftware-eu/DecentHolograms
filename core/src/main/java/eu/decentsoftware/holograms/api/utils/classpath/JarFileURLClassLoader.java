package eu.decentsoftware.holograms.api.utils.classpath;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarFileURLClassLoader extends URLClassLoader {

    public JarFileURLClassLoader(File file, ClassLoader parent) throws MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, parent);
    }
}

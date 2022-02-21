package eu.decentsoftware.holograms.api.utils.file;

import eu.decentsoftware.holograms.api.utils.Common;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import java.io.File;

@UtilityClass
public class FileUtils {

    @Nonnull
    public static String[] getFileNames(String path) {
        return getFileNames(path, null);
    }

    @Nonnull
    public static String[] getFileNames(String path, String regex) {
        return getFileNames(path, regex, false);
    }

    public static String[] getFileNames(String path, String regex, boolean createDir) {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            return dir.list((dir1, name) -> regex == null || regex.trim().isEmpty() || name.matches(regex));
        } else if (createDir && dir.mkdirs()) {
            Common.log("Created directory %s", path);
        }
        return new String[0];
    }

}

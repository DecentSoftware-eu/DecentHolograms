package eu.decentsoftware.holograms.api.utils.file;

import eu.decentsoftware.holograms.api.utils.Common;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations.
 *
 * @author d0by
 */
@UtilityClass
public class FileUtils {

	/**
	 * Get the names of all files in a tree, starting at the given root.
	 *
	 * @param rootPath  The root file path.
	 * @param regex     The regex to match the file names.
	 * @param createDir Whether to create the root directory if it doesn't exist.
	 * @return The list of file names.
	 * @since 2.7.10
	 */
	@NotNull
	public static List<String> getFileNamesFromTree(@NotNull String rootPath, @Nullable String regex, boolean createDir) {
		List<String> files = new ArrayList<>();
		File root = new File(rootPath);
		if (root.exists() && root.isDirectory()) {
			File[] children = root.listFiles();
			if (children != null) {
				for (File child : children) {
					if (child.isDirectory()) {
						files.addAll(getFileNamesFromTree(child.getAbsolutePath(), regex, createDir));
					} else if (regex == null || regex.trim().isEmpty() || child.getName().matches(regex)) {
						files.add(child.getAbsolutePath());
					}
				}
			}
		} else if (createDir && root.mkdirs()) {
			Common.log("Created directory %s", rootPath);
		}
		return files;
	}

}

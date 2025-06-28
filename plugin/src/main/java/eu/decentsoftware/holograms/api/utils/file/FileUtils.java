package eu.decentsoftware.holograms.api.utils.file;

import eu.decentsoftware.holograms.api.utils.Log;
import lombok.NonNull;
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
	 * Get the files in a tree, starting at the given root.
	 *
	 * @param root      The root file.
	 * @param regex     The regex to match the file names.
	 * @param createDir Whether to create the root directory if it doesn't exist.
	 * @return The list of file.
	 * @since 2.7.10
	 */
	@NotNull
	public static List<File> getFilesFromTree(@NotNull File root, @Nullable String regex, boolean createDir) {
		List<File> files = new ArrayList<>();
		if (root.exists() && root.isDirectory()) {
			File[] children = root.listFiles();
			if (children != null) {
				for (File child : children) {
					if (child.isDirectory()) {
						files.addAll(getFilesFromTree(child, regex, createDir));
					} else if (regex == null || regex.trim().isEmpty() || child.getName().matches(regex)) {
						files.add(child);
					}
				}
			}
		} else if (createDir && root.mkdirs()) {
			            Log.info("已创建目录 %s", root.getPath());
		}
		return files;
	}

	/**
	 * Get the relative path of a file to a base directory.
	 *
	 * @param file The file.
	 * @param base The base directory.
	 * @return The relative path or null if the file is not in the base directory.
	 */
	@Nullable
	public static String getRelativePath(@NonNull File file, @NonNull File base) {
		String filePath = file.getAbsolutePath();
		String basePath = base.getAbsolutePath();
		if (filePath.startsWith(basePath)) {
			return filePath.substring(basePath.length() + 1);
		}
		return null;
	}

}

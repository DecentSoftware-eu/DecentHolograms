package eu.decentsoftware.holograms.api.utils;

import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.logging.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
public class UpdateChecker {

    private final int resourceId;

    public UpdateChecker(int resourceId) {
        this.resourceId = resourceId;
    }

    public void getVersion(Consumer<String> consumer) {
        S.async(() -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext() && consumer != null) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                Log.info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}
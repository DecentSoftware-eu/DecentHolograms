package eu.decentsoftware.holograms.api.expansion.config;

import eu.decentsoftware.holograms.api.expansion.Expansion;
import eu.decentsoftware.holograms.plugin.file.FileSystemService;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultExpansionConfigSourceTest {
    private static File mainDataDir;
    private static File expansionJarsDir;
    private static File expansionDataDir;

    private DefaultExpansionConfigSource configSource;

    @BeforeAll
    public static void init() throws IOException {
        Path tmpPath = Files.createTempDirectory("dh-test-");

        mainDataDir = tmpPath.toFile();
        Files.createDirectories(tmpPath);

        expansionJarsDir = new File(mainDataDir, "expansions");
        Files.createDirectories(expansionJarsDir.toPath());

        expansionDataDir = new File(mainDataDir, "expansions-data");
        Files.createDirectories(expansionDataDir.toPath());
    }

    @AfterAll
    public static void cleanup() throws IOException {
        FileUtils.deleteDirectory(mainDataDir);
    }

    @BeforeEach
    public void setup() throws IOException {
        FileUtils.cleanDirectory(mainDataDir);

        FileSystemService fileSystemService = new FileSystemService() {
            @Override
            public File getExpansionJarsDirectory() {
                return expansionJarsDir;
            }

            @Override
            public File getExpansionDataDirectory(String expansionId) {
                return new File(expansionDataDir, expansionId);
            }
        };
        configSource = new DefaultExpansionConfigSource(fileSystemService);
    }

    @Test
    public void testThrowsErrorsOnNulls() {
        Expansion expansion = buildTestExpansion();
        ExpansionConfig config = new ExpansionConfig(true, new MemoryConfiguration());

        assertThrows(NullPointerException.class, () -> configSource.saveConfig(null, config));
        assertThrows(NullPointerException.class, () -> configSource.saveConfig(expansion, null));
        assertThrows(NullPointerException.class, () -> configSource.loadOrCreateConfig(null));
    }

    @Test
    public void testLoadOrCreateConfigCreatesFile() {
        Expansion expansion = buildTestExpansion();

        configSource.loadOrCreateConfig(expansion);

        File configFile = new File(expansionDataDir + File.separator + expansion.getId(), "config.yml");
        assertTrue(configFile.exists());
    }

    @Test
    public void testSaveConfigPersistsData() {
        Expansion expansion = buildTestExpansion();
        ExpansionConfig config = new ExpansionConfig(false, new MemoryConfiguration());
        config.getSettings().set("key", "value");

        configSource.saveConfig(expansion, config);

        ExpansionConfig loadedConfig = configSource.loadOrCreateConfig(expansion);
        assertFalse(loadedConfig.isEnabled());
        assertEquals("value", loadedConfig.getSettings().getString("key"));
    }

    private Expansion buildTestExpansion() {
        return new Expansion() {
            @Override
            public String getId() {
                return "test-expansion";
            }

            @Override
            public String getName() {
                return "Test Expansion";
            }

            @Override
            public String getDescription() {
                return "A test expansion for unit testing.";
            }

            @Override
            public String getAuthor() {
                return "ZorTik";
            }

            @Override
            public String getVersion() {
                return "1.0.0";
            }
        };
    }
}

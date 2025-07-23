package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.utils.reflect.Version;

import eu.decentsoftware.holograms.nms.api.NmsAdapter;

import eu.decentsoftware.holograms.nms.api.renderer.NmsHologramRendererFactory;
import eu.decentsoftware.holograms.shared.reflect.ReflectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class NmsAdapterFactoryTest {

    private Version version;
    private NmsAdapterFactory factory;

    @BeforeEach
    void setUp() {
        // We need to mock the way Version gets the current version,
        // because it's called when the Version class is initialized.
        // Hopefully we will get rid of this in the future.
        // >:(
        Server server = mock(Server.class);
        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            mockedBukkit.when(Bukkit::getServer).thenReturn(server);
            when(server.getBukkitVersion()).thenReturn("1.8.8-R0.1-SNAPSHOT");

            version = Version.v1_8_R3; // Any version
        }

        factory = new NmsAdapterFactory();
    }

    @Test
    void testCreateNmsAdapter_nullVersion() {
        Exception exception = assertThrows(NullPointerException.class, () -> factory.createNmsAdapter(null));

        assertEquals("version cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(Version.class)
    void testCreateNmsAdapter_valid(Version version) {
        String className = "eu.decentsoftware.holograms.nms." + version.name() + ".NmsAdapterImpl";

        try (MockedStatic<ReflectUtil> classMock = mockStatic(ReflectUtil.class)) {
            classMock.when(() -> ReflectUtil.getClass(className)).thenReturn(ValidNmsAdapter.class);
            NmsAdapter adapter = factory.createNmsAdapter(version);
            assertNotNull(adapter);
            assertInstanceOf(ValidNmsAdapter.class, adapter);
        }
    }




    static class ValidNmsAdapter implements NmsAdapter {
        @Override
        public NmsHologramRendererFactory getHologramComponentFactory() {
            throw new UnsupportedOperationException("Test implementation");
        }

        @Override
        public void registerPacketListener(Player player) {
            throw new UnsupportedOperationException("Test implementation");
        }

        @Override
        public void unregisterPacketListener(Player player) {
            throw new UnsupportedOperationException("Test implementation");
        }
    }

    static class NotNmsAdapter {
    }

    static class NoDefaultConstructorNmsAdapter implements NmsAdapter {
        public NoDefaultConstructorNmsAdapter(String param) {
        }

        @Override
        public NmsHologramRendererFactory getHologramComponentFactory() {
            throw new UnsupportedOperationException("Test implementation");
        }

        @Override
        public void registerPacketListener(Player player) {
            throw new UnsupportedOperationException("Test implementation");
        }

        @Override
        public void unregisterPacketListener(Player player) {
            throw new UnsupportedOperationException("Test implementation");
        }
    }

    static class FailingNmsAdapter implements NmsAdapter {
        public FailingNmsAdapter() {
            throw new RuntimeException("Constructor failure");
        }

        @Override
        public NmsHologramRendererFactory getHologramComponentFactory() {
            throw new UnsupportedOperationException("Test implementation");
        }

        @Override
        public void registerPacketListener(Player player) {
            throw new UnsupportedOperationException("Test implementation");
        }

        @Override
        public void unregisterPacketListener(Player player) {
            throw new UnsupportedOperationException("Test implementation");
        }
    }

}
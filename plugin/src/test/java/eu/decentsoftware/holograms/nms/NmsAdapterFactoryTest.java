package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
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

    @Test
    void testCreateNmsAdapter_unsupportedServerVersion() {
        String className = "eu.decentsoftware.holograms.nms.v1_8_R3.NmsAdapterImpl";

        try (MockedStatic<ReflectUtil> classMock = mockStatic(ReflectUtil.class)) {
            classMock.when(() -> ReflectUtil.getClass(className)).thenThrow(new ClassNotFoundException());
            DecentHologramsNmsException exception = assertThrows(DecentHologramsNmsException.class,
                    () -> factory.createNmsAdapter(version));
            assertEquals("Unsupported server version: v1_8_R3", exception.getMessage());
        }
    }

    @Test
    void testCreateNmsAdapter_classNotImplementingNmsAdapter() {
        String className = "eu.decentsoftware.holograms.nms.v1_8_R3.NmsAdapterImpl";

        try (MockedStatic<ReflectUtil> classMock = mockStatic(ReflectUtil.class)) {
            classMock.when(() -> ReflectUtil.getClass(className)).thenReturn(NotNmsAdapter.class);
            DecentHologramsNmsException exception = assertThrows(DecentHologramsNmsException.class,
                    () -> factory.createNmsAdapter(version));
            String expectedMessage = "Nms adapter " + className + " does not implement " + NmsAdapter.class.getName();
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Test
    void testCreateNmsAdapter_noDefaultConstructor() {
        String className = "eu.decentsoftware.holograms.nms.v1_8_R3.NmsAdapterImpl";

        try (MockedStatic<ReflectUtil> classMock = mockStatic(ReflectUtil.class)) {
            classMock.when(() -> ReflectUtil.getClass(className)).thenReturn(NoDefaultConstructorNmsAdapter.class);
            DecentHologramsNmsException exception = assertThrows(DecentHologramsNmsException.class,
                    () -> factory.createNmsAdapter(version));
            String expectedMessage = "NmsAdapter implementation is missing the default constructor: " + className;
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Test
    void testCreateNmsAdapter_constructorFailure() {
        String className = "eu.decentsoftware.holograms.nms.v1_8_R3.NmsAdapterImpl";

        try (MockedStatic<ReflectUtil> classMock = mockStatic(ReflectUtil.class)) {
            classMock.when(() -> ReflectUtil.getClass(className)).thenReturn(FailingNmsAdapter.class);
            DecentHologramsNmsException exception = assertThrows(DecentHologramsNmsException.class,
                    () -> factory.createNmsAdapter(version));
            String expectedMessage = "Failed to construct a new instance of NmsAdapter implementation: " + className;
            assertEquals(expectedMessage, exception.getMessage());
            assertNotNull(exception.getCause());
        }
    }

    @Test
    void testCreateNmsAdapter_unknownException() {
        String className = "eu.decentsoftware.holograms.nms.v1_8_R3.NmsAdapterImpl";

        try (MockedStatic<ReflectUtil> classMock = mockStatic(ReflectUtil.class)) {
            // The method ReflectUtil#getClass should never throw a RuntimeException
            // this is just to simulate an unknown exception
            classMock.when(() -> ReflectUtil.getClass(className)).thenThrow(new RuntimeException("Test exception"));
            DecentHologramsNmsException exception = assertThrows(DecentHologramsNmsException.class,
                    () -> factory.createNmsAdapter(version));
            String expectedMessage = "Unknown error occurred while initializing NmsAdapter implementation: " + className;
            assertEquals(expectedMessage, exception.getMessage());
            assertNotNull(exception.getCause());
            assertEquals("Test exception", exception.getCause().getMessage());
        }
    }

    static class ValidNmsAdapter implements NmsAdapter {
        @Override
        public NmsHologramRendererFactory getHologramComponentFactory() {
            throw new UnsupportedOperationException("Test implementation");
        }

        @Override
        public void registerPacketListener(Player player, NmsPacketListener listener) {
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
        public void registerPacketListener(Player player, NmsPacketListener listener) {
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
        public void registerPacketListener(Player player, NmsPacketListener listener) {
            throw new UnsupportedOperationException("Test implementation");
        }

        @Override
        public void unregisterPacketListener(Player player) {
            throw new UnsupportedOperationException("Test implementation");
        }
    }

}
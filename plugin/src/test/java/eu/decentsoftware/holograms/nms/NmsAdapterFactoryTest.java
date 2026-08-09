package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
import eu.decentsoftware.holograms.nms.api.renderer.NmsHologramRendererFactory;
import eu.decentsoftware.holograms.shared.reflect.ReflectUtil;
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
import static org.mockito.Mockito.mockStatic;

class NmsAdapterFactoryTest {

    private static final String MODULE = Version.v1_8_R3.name();

    private NmsAdapterFactory factory;

    @BeforeEach
    void setUp() {
        factory = new NmsAdapterFactory();
    }

    @Test
    void testCreateNmsAdapter_nullModuleName() {
        Exception exception = assertThrows(NullPointerException.class, () -> factory.createNmsAdapter(null));

        assertEquals("moduleName cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(Version.class)
    void testCreateNmsAdapter_valid(Version version) {
        String className = "eu.decentsoftware.holograms.nms." + version.name() + ".NmsAdapterImpl";

        try (MockedStatic<ReflectUtil> classMock = mockStatic(ReflectUtil.class)) {
            classMock.when(() -> ReflectUtil.getClass(className)).thenReturn(ValidNmsAdapter.class);
            NmsAdapter adapter = factory.createNmsAdapter(version.name());
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
                    () -> factory.createNmsAdapter(MODULE));
            assertEquals("Unsupported server version: v1_8_R3", exception.getMessage());
        }
    }

    @Test
    void testCreateNmsAdapter_classNotImplementingNmsAdapter() {
        String className = "eu.decentsoftware.holograms.nms.v1_8_R3.NmsAdapterImpl";

        try (MockedStatic<ReflectUtil> classMock = mockStatic(ReflectUtil.class)) {
            classMock.when(() -> ReflectUtil.getClass(className)).thenReturn(NotNmsAdapter.class);
            DecentHologramsNmsException exception = assertThrows(DecentHologramsNmsException.class,
                    () -> factory.createNmsAdapter(MODULE));
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
                    () -> factory.createNmsAdapter(MODULE));
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
                    () -> factory.createNmsAdapter(MODULE));
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
                    () -> factory.createNmsAdapter(MODULE));
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
        public NoDefaultConstructorNmsAdapter(@SuppressWarnings("unused") String param) {
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
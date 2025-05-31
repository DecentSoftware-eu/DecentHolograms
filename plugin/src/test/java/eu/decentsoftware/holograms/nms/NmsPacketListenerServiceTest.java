package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NmsPacketListenerServiceTest {

    @Mock
    private JavaPlugin javaPlugin;
    @Mock
    private NmsAdapter nmsAdapter;
    @Mock
    private NmsPacketListener nmsPacketListener;
    @Mock
    private PluginManager pluginManager;

    private NmsPacketListenerService nmsPacketListenerService;

    @BeforeEach
    void setUp() {
        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            mockedBukkit.when(Bukkit::getOnlinePlayers).thenReturn(new ArrayList<>());
            mockedBukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);

            nmsPacketListenerService = new NmsPacketListenerService(javaPlugin, nmsAdapter, nmsPacketListener);
        }
    }

    @Test
    void testConstruction() {
        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            List<Player> onlinePlayers = getOnlinePlayers();
            mockedBukkit.when(Bukkit::getOnlinePlayers).thenReturn(onlinePlayers);
            PluginManager pluginManagerMock = mock(PluginManager.class);
            mockedBukkit.when(Bukkit::getPluginManager).thenReturn(pluginManagerMock);
            JavaPlugin javaPluginMock = mock(JavaPlugin.class);
            NmsAdapter nmsAdapterMock = mock(NmsAdapter.class);
            NmsPacketListener nmsPacketListenerMock = mock(NmsPacketListener.class);

            NmsPacketListenerService result = new NmsPacketListenerService(javaPluginMock, nmsAdapterMock, nmsPacketListenerMock);

            assertNotNull(result);
            verify(pluginManagerMock).registerEvents(any(NmsPlayerListener.class), eq(javaPluginMock));
            for (Player onlinePlayer : onlinePlayers) {
                verify(nmsAdapterMock).registerPacketListener(onlinePlayer, nmsPacketListenerMock);
            }
        }
    }

    @Test
    void testShutdown() {
        try (MockedStatic<HandlerList> mockedHandlerList = mockStatic(HandlerList.class);
             MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            List<Player> onlinePlayers = getOnlinePlayers();
            mockedBukkit.when(Bukkit::getOnlinePlayers).thenReturn(onlinePlayers);

            nmsPacketListenerService.shutdown();

            for (Player onlinePlayer : onlinePlayers) {
                verify(nmsAdapter).unregisterPacketListener(onlinePlayer);
            }
            mockedHandlerList.verify(() -> HandlerList.unregisterAll(any(NmsPlayerListener.class)));
        }
    }

    @Test
    void testRegisterListener() {
        Player player = mock(Player.class);

        nmsPacketListenerService.registerListener(player);

        verify(nmsAdapter).registerPacketListener(player, nmsPacketListener);
    }

    @Test
    void testUnregisterListener() {
        Player player = mock(Player.class);

        nmsPacketListenerService.unregisterListener(player);

        verify(nmsAdapter).unregisterPacketListener(player);
    }

    private List<Player> getOnlinePlayers() {
        return Arrays.asList(
                mock(Player.class),
                mock(Player.class)
        );
    }

}
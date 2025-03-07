package eu.decentsoftware.holograms.nms;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NmsPlayerListenerTest {

    @Mock
    private NmsPacketListenerService packetListenerService;
    @InjectMocks
    private NmsPlayerListener listener;

    @Test
    void testOnPlayerJoin() {
        Player player = mock(Player.class);
        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(player, null);

        listener.onPlayerJoin(playerJoinEvent);

        verify(packetListenerService).registerListener(player);
    }

    @Test
    void testOnPlayerQuit() {
        Player player = mock(Player.class);
        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(player, null);

        listener.onPlayerQuit(playerQuitEvent);

        verify(packetListenerService).unregisterListener(player);
    }

}
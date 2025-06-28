package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.nms.api.event.NmsEntityInteractAction;
import eu.decentsoftware.holograms.nms.api.event.NmsEntityInteractEvent;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecentHologramsNmsPacketListenerTest {

    @Mock
    private HologramManager hologramManager;
    @InjectMocks
    private DecentHologramsNmsPacketListener packetListener;

    private static Stream<Arguments> providerInteractionActionsWithRespectiveClickTypes() {
        return Stream.of(
                Arguments.arguments(NmsEntityInteractAction.LEFT_CLICK, ClickType.LEFT),
                Arguments.arguments(NmsEntityInteractAction.RIGHT_CLICK, ClickType.RIGHT),
                Arguments.arguments(NmsEntityInteractAction.SHIFT_LEFT_CLICK, ClickType.SHIFT_LEFT),
                Arguments.arguments(NmsEntityInteractAction.SHIFT_RIGHT_CLICK, ClickType.SHIFT_RIGHT)
        );
    }

    @Test
    void testOnEntityInteract_invalidInteractionAction() {
        Player player = mock(Player.class);
        NmsEntityInteractEvent event = new NmsEntityInteractEvent(player, 1, null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> packetListener.onEntityInteract(event));

        assertEquals("未知操作: null", exception.getMessage());
        verify(hologramManager, never()).onClick(any(), anyInt(), any());
    }

    @ParameterizedTest
    @MethodSource("providerInteractionActionsWithRespectiveClickTypes")
    void testOnEntityInteract_mapping(NmsEntityInteractAction action, ClickType clickType) {
        Player player = mock(Player.class);
        NmsEntityInteractEvent event = new NmsEntityInteractEvent(player, 1, action);

        when(hologramManager.onClick(any(), anyInt(), any())).thenReturn(false);

        packetListener.onEntityInteract(event);

        assertFalse(event.isHandled());
        verify(hologramManager).onClick(player, 1, clickType);
    }

    @Test
    void testOnEntityInteract_eventHandled() {
        Player player = mock(Player.class);
        NmsEntityInteractEvent event = new NmsEntityInteractEvent(player, 1, NmsEntityInteractAction.LEFT_CLICK);

        when(hologramManager.onClick(any(), anyInt(), any())).thenReturn(true);

        packetListener.onEntityInteract(event);

        assertTrue(event.isHandled());
        verify(hologramManager).onClick(player, 1, ClickType.LEFT);
    }

}
package online.demonzdevelopment.listeners;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Player Move Listener
 * Cancels warmup if player moves
 */
public class PlayerMoveListener implements Listener {

    private final DZQuantumTeleport plugin;

    public PlayerMoveListener(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // TODO: Implement warmup cancellation logic
    }
}

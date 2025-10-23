package online.demonzdevelopment.listeners;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Player Damage Listener
 * Cancels warmup if player takes damage
 */
public class PlayerDamageListener implements Listener {

    private final DZQuantumTeleport plugin;

    public PlayerDamageListener(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        // TODO: Implement warmup cancellation logic
    }
}

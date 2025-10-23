package online.demonzdevelopment.listeners;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Player Death Listener
 * Saves death location for /back command
 */
public class PlayerDeathListener implements Listener {

    private final DZQuantumTeleport plugin;

    public PlayerDeathListener(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Save death location
        plugin.getDatabaseManager().saveBackLocation(
            event.getPlayer().getUniqueId(),
            event.getPlayer().getLocation()
        );
    }
}

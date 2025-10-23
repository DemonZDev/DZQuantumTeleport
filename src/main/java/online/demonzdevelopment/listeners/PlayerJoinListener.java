package online.demonzdevelopment.listeners;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Player Join Listener
 * Handles player join events
 */
public class PlayerJoinListener implements Listener {

    private final DZQuantumTeleport plugin;

    public PlayerJoinListener(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load player data
        plugin.getHomeManager().loadHomes(event.getPlayer().getUniqueId());
        
        // Load toggle state
        plugin.getDatabaseManager().getTPToggleState(event.getPlayer().getUniqueId())
            .thenAccept(enabled -> {
                plugin.getTPAManager().setToggleState(event.getPlayer().getUniqueId(), enabled);
            });
    }
}

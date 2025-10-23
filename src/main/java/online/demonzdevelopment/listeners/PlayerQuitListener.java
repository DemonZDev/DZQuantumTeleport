package online.demonzdevelopment.listeners;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Player Quit Listener
 */
public class PlayerQuitListener implements Listener {

    private final DZQuantumTeleport plugin;

    public PlayerQuitListener(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Clear cache
        plugin.getHomeManager().clearCache(event.getPlayer().getUniqueId());
        plugin.getRankManager().clearCache(event.getPlayer().getUniqueId());
    }
}

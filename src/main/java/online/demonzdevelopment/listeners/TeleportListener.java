package online.demonzdevelopment.listeners;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Teleport Listener
 * Saves previous location for /back command
 */
public class TeleportListener implements Listener {

    private final DZQuantumTeleport plugin;

    public TeleportListener(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        // Save previous location
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            plugin.getDatabaseManager().saveBackLocation(
                event.getPlayer().getUniqueId(),
                event.getFrom()
            );
        }
    }
}

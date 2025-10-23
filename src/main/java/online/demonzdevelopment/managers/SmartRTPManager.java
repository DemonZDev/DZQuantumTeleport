package online.demonzdevelopment.managers;

import online.demonzdevelopment.DZQuantumTeleport;
import online.demonzdevelopment.models.RTPTier;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Smart RTP Manager
 * Handles progressive distance RTP system
 */
public class SmartRTPManager {

    private final DZQuantumTeleport plugin;
    private final RTPTier[] tiers;

    public SmartRTPManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
        this.tiers = loadTiers();
    }

    /**
     * Load tier configuration
     */
    private RTPTier[] loadTiers() {
        return new RTPTier[]{
            new RTPTier(1, "&7Beginner Explorer", 0, 10, 100, 1.0),
            new RTPTier(2, "&aExplorer", 10, 100, 500, 1.2),
            new RTPTier(3, "&bAdventurer", 25, 500, 1000, 1.5),
            new RTPTier(4, "&dWanderer", 50, 1000, 2500, 2.0),
            new RTPTier(5, "&5Voyager", 100, 2500, 5000, 2.5),
            new RTPTier(6, "&6&lMaster Explorer", 200, 5000, 10000, 3.0)
        };
    }

    /**
     * Get player's current tier
     */
    public RTPTier getPlayerTier(UUID uuid) {
        int uses = getRTPUses(uuid);
        
        for (int i = tiers.length - 1; i >= 0; i--) {
            if (uses >= tiers[i].getUsesRequired()) {
                return tiers[i];
            }
        }
        
        return tiers[0];
    }

    /**
     * Get RTP uses for player
     */
    public int getRTPUses(UUID uuid) {
        return plugin.getDatabaseManager().getRTPUses(uuid).join();
    }

    /**
     * Find safe random location
     */
    public CompletableFuture<Location> findSafeLocation(UUID uuid, World world) {
        return CompletableFuture.supplyAsync(() -> {
            RTPTier tier = getPlayerTier(uuid);
            int maxAttempts = plugin.getConfigManager().getRTPMaxAttempts();
            
            for (int attempt = 0; attempt < maxAttempts; attempt++) {
                Location location = generateRandomLocation(world, tier);
                
                if (plugin.getTeleportManager().isSafeLocation(location)) {
                    return location;
                }
            }
            
            return null;
        });
    }

    /**
     * Generate random location within tier range
     */
    private Location generateRandomLocation(World world, RTPTier tier) {
        // TODO: Implement proper random location generation with border respect
        int x = (int) (Math.random() * (tier.getMaxDistance() - tier.getMinDistance()) + tier.getMinDistance());
        int z = (int) (Math.random() * (tier.getMaxDistance() - tier.getMinDistance()) + tier.getMinDistance());
        int y = world.getHighestBlockYAt(x, z);
        
        return new Location(world, x, y, z);
    }

    /**
     * Increment RTP uses
     */
    public void incrementUses(UUID uuid) {
        plugin.getDatabaseManager().incrementRTPUses(uuid);
    }
}

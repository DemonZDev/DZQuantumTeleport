package online.demonzdevelopment.managers;

import online.demonzdevelopment.DZQuantumTeleport;
import online.demonzdevelopment.models.Home;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Home Manager
 * Handles all home-related operations
 */
public class HomeManager {

    private final DZQuantumTeleport plugin;
    private final Map<UUID, List<Home>> homeCache;

    public HomeManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
        this.homeCache = new ConcurrentHashMap<>();
    }

    /**
     * Load homes from database
     */
    public CompletableFuture<List<Home>> loadHomes(UUID uuid) {
        if (homeCache.containsKey(uuid)) {
            return CompletableFuture.completedFuture(new ArrayList<>(homeCache.get(uuid)));
        }

        return plugin.getDatabaseManager().loadHomes(uuid)
            .thenApply(homes -> {
                homeCache.put(uuid, new ArrayList<>(homes));
                return homes;
            });
    }

    /**
     * Get all homes for player
     */
    public List<Home> getHomes(UUID uuid) {
        return homeCache.getOrDefault(uuid, new ArrayList<>());
    }

    /**
     * Get specific home
     */
    public Home getHome(UUID uuid, String name) {
        return getHomes(uuid).stream()
            .filter(h -> h.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    /**
     * Get home count
     */
    public int getHomeCount(UUID uuid) {
        return getHomes(uuid).size();
    }

    /**
     * Create home
     */
    public CompletableFuture<Boolean> createHome(UUID uuid, String name, Location location) {
        Home home = new Home(uuid, name, location);
        
        return plugin.getDatabaseManager().saveHome(home)
            .thenApply(success -> {
                if (success) {
                    homeCache.computeIfAbsent(uuid, k -> new ArrayList<>()).add(home);
                }
                return success;
            });
    }

    /**
     * Delete home
     */
    public CompletableFuture<Boolean> deleteHome(UUID uuid, String name) {
        return plugin.getDatabaseManager().deleteHome(uuid, name)
            .thenApply(success -> {
                if (success) {
                    List<Home> homes = homeCache.get(uuid);
                    if (homes != null) {
                        homes.removeIf(h -> h.getName().equalsIgnoreCase(name));
                    }
                }
                return success;
            });
    }

    /**
     * Rename home
     */
    public CompletableFuture<Boolean> renameHome(UUID uuid, String oldName, String newName) {
        Home home = getHome(uuid, oldName);
        if (home == null) {
            return CompletableFuture.completedFuture(false);
        }

        // Delete old and create new
        return deleteHome(uuid, oldName)
            .thenCompose(deleted -> {
                if (deleted) {
                    return createHome(uuid, newName, home.getLocation());
                }
                return CompletableFuture.completedFuture(false);
            });
    }

    /**
     * Move home to new location
     */
    public CompletableFuture<Boolean> moveHome(UUID uuid, String name, Location location) {
        Home home = getHome(uuid, name);
        if (home == null) {
            return CompletableFuture.completedFuture(false);
        }

        home.setLocation(location);
        return plugin.getDatabaseManager().saveHome(home);
    }

    /**
     * Check if home exists
     */
    public boolean homeExists(UUID uuid, String name) {
        return getHome(uuid, name) != null;
    }

    /**
     * Check if player can create more homes
     */
    public boolean canCreateHome(Player player) {
        String rank = plugin.getRankManager().getPlayerRank(player);
        int maxHomes = plugin.getRankManager().getMaxHomes(rank);
        
        if (maxHomes == -1) {
            return true; // Unlimited
        }

        return getHomeCount(player.getUniqueId()) < maxHomes;
    }

    /**
     * Clear cache
     */
    public void clearCache() {
        homeCache.clear();
    }

    /**
     * Clear cache for specific player
     */
    public void clearCache(UUID uuid) {
        homeCache.remove(uuid);
    }
}

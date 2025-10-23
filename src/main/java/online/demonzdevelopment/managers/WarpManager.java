package online.demonzdevelopment.managers;

import online.demonzdevelopment.DZQuantumTeleport;
import online.demonzdevelopment.models.Warp;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Warp Manager
 * Handles all warp-related operations
 */
public class WarpManager {

    private final DZQuantumTeleport plugin;
    private final Map<String, Warp> warpCache;

    public WarpManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
        this.warpCache = new HashMap<>();
        loadWarps();
    }

    /**
     * Load all warps from database
     */
    public void loadWarps() {
        plugin.getDatabaseManager().loadWarps()
            .thenAccept(warps -> {
                warpCache.clear();
                for (Warp warp : warps) {
                    warpCache.put(warp.getName().toLowerCase(), warp);
                }
                plugin.getLogger().info("Loaded " + warps.size() + " warps");
            });
    }

    /**
     * Get all warps
     */
    public List<Warp> getAllWarps() {
        return new ArrayList<>(warpCache.values());
    }

    /**
     * Get all public warps
     */
    public List<Warp> getPublicWarps() {
        List<Warp> publicWarps = new ArrayList<>();
        for (Warp warp : warpCache.values()) {
            if (warp.isPublic()) {
                publicWarps.add(warp);
            }
        }
        return publicWarps;
    }

    /**
     * Get specific warp
     */
    public Warp getWarp(String name) {
        return warpCache.get(name.toLowerCase());
    }

    /**
     * Create warp
     */
    public CompletableFuture<Boolean> createWarp(String name, Location location, Warp.WarpVisibility visibility) {
        Warp warp = new Warp(name, location, visibility);
        
        return plugin.getDatabaseManager().saveWarp(warp)
            .thenApply(success -> {
                if (success) {
                    warpCache.put(name.toLowerCase(), warp);
                }
                return success;
            });
    }

    /**
     * Delete warp
     */
    public CompletableFuture<Boolean> deleteWarp(String name) {
        return plugin.getDatabaseManager().deleteWarp(name)
            .thenApply(success -> {
                if (success) {
                    warpCache.remove(name.toLowerCase());
                }
                return success;
            });
    }

    /**
     * Rename warp
     */
    public CompletableFuture<Boolean> renameWarp(String oldName, String newName) {
        Warp warp = getWarp(oldName);
        if (warp == null) {
            return CompletableFuture.completedFuture(false);
        }

        return deleteWarp(oldName)
            .thenCompose(deleted -> {
                if (deleted) {
                    return createWarp(newName, warp.getLocation(), warp.getVisibility());
                }
                return CompletableFuture.completedFuture(false);
            });
    }

    /**
     * Move warp location
     */
    public CompletableFuture<Boolean> moveWarp(String name, Location location) {
        Warp warp = getWarp(name);
        if (warp == null) {
            return CompletableFuture.completedFuture(false);
        }

        warp.setLocation(location);
        return plugin.getDatabaseManager().saveWarp(warp);
    }

    /**
     * Change warp visibility
     */
    public CompletableFuture<Boolean> changeVisibility(String name, Warp.WarpVisibility visibility) {
        Warp warp = getWarp(name);
        if (warp == null) {
            return CompletableFuture.completedFuture(false);
        }

        warp.setVisibility(visibility);
        return plugin.getDatabaseManager().saveWarp(warp);
    }

    /**
     * Check if warp exists
     */
    public boolean warpExists(String name) {
        return warpCache.containsKey(name.toLowerCase());
    }

    /**
     * Check if player can access warp
     */
    public boolean canAccess(UUID uuid, String name) {
        Warp warp = getWarp(name);
        if (warp == null) {
            return false;
        }

        // Public warps accessible to all
        if (warp.isPublic()) {
            return true;
        }

        // Private warps require admin permission
        Player player = plugin.getServer().getPlayer(uuid);
        return player != null && player.hasPermission("dzqtp.admin.warp.private");
    }

    /**
     * Clear cache
     */
    public void clearCache() {
        warpCache.clear();
        loadWarps();
    }
}

package online.demonzdevelopment.database;

import online.demonzdevelopment.models.Home;
import online.demonzdevelopment.models.Warp;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Database Manager Interface
 * All storage implementations must extend this
 */
public abstract class DatabaseManager {

    /**
     * Initialize the database
     */
    public abstract void initialize() throws Exception;

    /**
     * Close database connections
     */
    public abstract void close();

    /**
     * Save all data
     */
    public abstract void saveAllData();

    // ===== HOME METHODS =====

    /**
     * Load all homes for a player
     */
    public abstract CompletableFuture<List<Home>> loadHomes(UUID uuid);

    /**
     * Save a home
     */
    public abstract CompletableFuture<Boolean> saveHome(Home home);

    /**
     * Delete a home
     */
    public abstract CompletableFuture<Boolean> deleteHome(UUID uuid, String name);

    /**
     * Check if home exists
     */
    public abstract CompletableFuture<Boolean> homeExists(UUID uuid, String name);

    // ===== WARP METHODS =====

    /**
     * Load all warps
     */
    public abstract CompletableFuture<List<Warp>> loadWarps();

    /**
     * Save a warp
     */
    public abstract CompletableFuture<Boolean> saveWarp(Warp warp);

    /**
     * Delete a warp
     */
    public abstract CompletableFuture<Boolean> deleteWarp(String name);

    /**
     * Check if warp exists
     */
    public abstract CompletableFuture<Boolean> warpExists(String name);

    // ===== SPAWN METHODS =====

    /**
     * Save spawn location for world
     */
    public abstract CompletableFuture<Boolean> saveSpawn(String world, Location location);

    /**
     * Load spawn location for world
     */
    public abstract CompletableFuture<Location> loadSpawn(String world);

    // ===== BACK LOCATION METHODS =====

    /**
     * Save back location
     */
    public abstract CompletableFuture<Boolean> saveBackLocation(UUID uuid, Location location);

    /**
     * Load back location
     */
    public abstract CompletableFuture<Location> loadBackLocation(UUID uuid);

    // ===== RTP STATISTICS =====

    /**
     * Get RTP usage count for player
     */
    public abstract CompletableFuture<Integer> getRTPUses(UUID uuid);

    /**
     * Increment RTP usage
     */
    public abstract CompletableFuture<Boolean> incrementRTPUses(UUID uuid);

    // ===== COOLDOWN METHODS =====

    /**
     * Save cooldown
     */
    public abstract CompletableFuture<Boolean> saveCooldown(UUID uuid, String command, long expireTime);

    /**
     * Get cooldown expiry time
     */
    public abstract CompletableFuture<Long> getCooldownExpiry(UUID uuid, String command);

    /**
     * Remove expired cooldowns
     */
    public abstract CompletableFuture<Void> cleanupExpiredCooldowns();

    // ===== LIMIT METHODS =====

    /**
     * Get usage count for limit period
     */
    public abstract CompletableFuture<Integer> getUsageCount(UUID uuid, String command, String period);

    /**
     * Increment usage count
     */
    public abstract CompletableFuture<Boolean> incrementUsageCount(UUID uuid, String command, String period);

    /**
     * Reset limits for period
     */
    public abstract CompletableFuture<Boolean> resetLimits(String period);

    // ===== TOGGLE TP METHODS =====

    /**
     * Get TP toggle state
     */
    public abstract CompletableFuture<Boolean> getTPToggleState(UUID uuid);

    /**
     * Set TP toggle state
     */
    public abstract CompletableFuture<Boolean> setTPToggleState(UUID uuid, boolean enabled);
}

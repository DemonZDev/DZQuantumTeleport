package online.demonzdevelopment.managers;

import online.demonzdevelopment.DZQuantumTeleport;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cooldown Manager
 * Handles command cooldowns
 */
public class CooldownManager {

    private final DZQuantumTeleport plugin;
    private final Map<String, Long> cooldowns;

    public CooldownManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
        this.cooldowns = new ConcurrentHashMap<>();
    }

    /**
     * Set cooldown for player command
     */
    public void setCooldown(UUID uuid, String command, int seconds) {
        if (seconds <= 0) return;
        
        String key = uuid.toString() + ":" + command;
        long expireTime = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.put(key, expireTime);
        
        // Save to database
        plugin.getDatabaseManager().saveCooldown(uuid, command, expireTime);
    }

    /**
     * Check if player has cooldown
     */
    public boolean hasCooldown(UUID uuid, String command) {
        String key = uuid.toString() + ":" + command;
        Long expireTime = cooldowns.get(key);
        
        if (expireTime == null) {
            return false;
        }

        if (System.currentTimeMillis() >= expireTime) {
            cooldowns.remove(key);
            return false;
        }

        return true;
    }

    /**
     * Get remaining cooldown in seconds
     */
    public long getRemainingCooldown(UUID uuid, String command) {
        String key = uuid.toString() + ":" + command;
        Long expireTime = cooldowns.get(key);
        
        if (expireTime == null) {
            return 0;
        }

        long remaining = expireTime - System.currentTimeMillis();
        if (remaining <= 0) {
            cooldowns.remove(key);
            return 0;
        }

        return remaining / 1000;
    }

    /**
     * Remove cooldown
     */
    public void removeCooldown(UUID uuid, String command) {
        String key = uuid.toString() + ":" + command;
        cooldowns.remove(key);
    }

    /**
     * Clear all cooldowns
     */
    public void clearCache() {
        cooldowns.clear();
    }

    /**
     * Cleanup expired cooldowns
     */
    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        cooldowns.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}

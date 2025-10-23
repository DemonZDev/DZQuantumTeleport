package online.demonzdevelopment.config;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Configuration Manager
 * Handles loading and accessing config.yml values
 */
public class ConfigManager {

    private final DZQuantumTeleport plugin;
    private FileConfiguration config;

    public ConfigManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    // Rank system
    public String getRankDetectionMethod() {
        return config.getString("rank-system.detection-method", "luckperms");
    }

    public String getDefaultRank() {
        return config.getString("rank-system.default-rank", "default");
    }

    public int getRankCacheDuration() {
        return config.getInt("rank-system.cache-duration", 5);
    }

    // Storage
    public String getStorageType() {
        return config.getString("storage.type", "flatfile");
    }

    // Teleportation
    public boolean isWarmupEnabled() {
        return config.getBoolean("teleportation.warmup.enabled", true);
    }

    public int getDefaultWarmupDelay() {
        return config.getInt("teleportation.warmup.default-delay", 3);
    }

    public boolean cancelWarmupOnMove() {
        return config.getBoolean("teleportation.warmup.cancel-on-move", true);
    }

    public boolean cancelWarmupOnDamage() {
        return config.getBoolean("teleportation.warmup.cancel-on-damage", true);
    }

    public double getMovementTolerance() {
        return config.getDouble("teleportation.warmup.movement-tolerance", 0.5);
    }

    // Safety checks
    public boolean checkSuffocation() {
        return config.getBoolean("teleportation.safety-checks.check-suffocation", true);
    }

    public boolean checkFallDamage() {
        return config.getBoolean("teleportation.safety-checks.check-fall-damage", true);
    }

    public boolean checkLava() {
        return config.getBoolean("teleportation.safety-checks.check-lava", true);
    }

    public boolean checkFire() {
        return config.getBoolean("teleportation.safety-checks.check-fire", true);
    }

    public boolean checkWaterSafety() {
        return config.getBoolean("teleportation.safety-checks.check-water-safety", true);
    }

    // RTP
    public boolean isSmartRTPEnabled() {
        return config.getBoolean("smart-rtp.enabled", true);
    }

    public int getRTPMaxAttempts() {
        return config.getInt("random-teleport.max-attempts", 15);
    }

    public boolean respectWorldBorder() {
        return config.getBoolean("random-teleport.respect-border", true);
    }

    // Cost system
    public boolean isCostSystemEnabled() {
        return config.getBoolean("cost-system.enabled", true);
    }

    public boolean refundOnCancel() {
        return config.getBoolean("cost-system.refund-on-cancel", true);
    }

    public double getRefundPercentage() {
        return config.getDouble("cost-system.refund-percentage", 0.75);
    }

    // Limit system
    public boolean isLimitSystemEnabled() {
        return config.getBoolean("limit-system.enabled", true);
    }

    // Sounds
    public boolean areSoundsEnabled() {
        return config.getBoolean("sounds.enabled", true);
    }

    // Particles
    public boolean areParticlesEnabled() {
        return config.getBoolean("particles.enabled", true);
    }

    // Performance
    public boolean useAsyncDatabase() {
        return config.getBoolean("performance.async.database", true);
    }

    public boolean useAsyncTeleportChecks() {
        return config.getBoolean("performance.async.teleport-checks", true);
    }

    // Developer
    public boolean isDebugMode() {
        return config.getBoolean("developer.debug-mode", false);
    }
}

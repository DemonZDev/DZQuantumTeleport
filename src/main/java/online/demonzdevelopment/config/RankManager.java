package online.demonzdevelopment.config;

import net.luckperms.api.model.user.User;
import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rank Manager
 * Handles rank detection and rank-based configuration
 */
public class RankManager {

    private final DZQuantumTeleport plugin;
    private FileConfiguration ranksConfig;
    private final Map<UUID, CachedRank> rankCache;
    private List<String> rankPriority;

    public RankManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
        this.rankCache = new ConcurrentHashMap<>();
        loadRanks();
    }

    public void loadRanks() {
        File ranksFile = new File(plugin.getDataFolder(), "ranks.yml");
        if (!ranksFile.exists()) {
            plugin.saveResource("ranks.yml", false);
        }
        ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);
        
        // Load rank priority
        rankPriority = plugin.getConfig().getStringList("rank-system.priority-order");
        if (rankPriority.isEmpty()) {
            rankPriority = Arrays.asList("admin", "elite", "mvp", "vip", "default");
        }
    }

    public void reload() {
        loadRanks();
        rankCache.clear();
    }

    /**
     * Get player's rank
     * @param player Player
     * @return Rank name
     */
    public String getPlayerRank(Player player) {
        return getPlayerRank(player.getUniqueId());
    }

    /**
     * Get player's rank by UUID
     * @param uuid Player UUID
     * @return Rank name
     */
    public String getPlayerRank(UUID uuid) {
        // Check cache
        CachedRank cached = rankCache.get(uuid);
        if (cached != null && !cached.isExpired()) {
            return cached.getRank();
        }

        // Detect rank
        String rank = detectRank(uuid);
        
        // Cache rank
        int cacheDuration = plugin.getConfigManager().getRankCacheDuration();
        rankCache.put(uuid, new CachedRank(rank, cacheDuration));
        
        return rank;
    }

    /**
     * Detect rank using LuckPerms or permissions
     */
    private String detectRank(UUID uuid) {
        String method = plugin.getConfigManager().getRankDetectionMethod();
        
        if (method.equalsIgnoreCase("luckperms")) {
            return detectRankLuckPerms(uuid);
        } else {
            return detectRankPermission(uuid);
        }
    }

    /**
     * Detect rank using LuckPerms API
     */
    private String detectRankLuckPerms(UUID uuid) {
        try {
            User user = plugin.getLuckPerms().getUserManager().getUser(uuid);
            if (user == null) {
                return plugin.getConfigManager().getDefaultRank();
            }

            // Check ranks in priority order
            for (String rank : rankPriority) {
                if (user.getCachedData().getPermissionData()
                        .checkPermission("dzqtp.rank." + rank).asBoolean()) {
                    return rank;
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error detecting rank for " + uuid + ": " + e.getMessage());
        }

        return plugin.getConfigManager().getDefaultRank();
    }

    /**
     * Detect rank using Bukkit permissions
     */
    private String detectRankPermission(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) {
            return plugin.getConfigManager().getDefaultRank();
        }

        // Check ranks in priority order
        for (String rank : rankPriority) {
            if (player.hasPermission("dzqtp.rank." + rank)) {
                return rank;
            }
        }

        return plugin.getConfigManager().getDefaultRank();
    }

    /**
     * Get rank configuration section
     */
    public ConfigurationSection getRankConfig(String rank) {
        return ranksConfig.getConfigurationSection("ranks." + rank);
    }

    /**
     * Get command configuration for rank
     */
    public ConfigurationSection getCommandConfig(String rank, String command) {
        return ranksConfig.getConfigurationSection("ranks." + rank + "." + command);
    }

    /**
     * Check if command is enabled for rank
     */
    public boolean isCommandEnabled(String rank, String command) {
        ConfigurationSection config = getCommandConfig(rank, command);
        return config != null && config.getBoolean("enabled", true);
    }

    /**
     * Get cooldown for command
     */
    public int getCooldown(String rank, String command) {
        ConfigurationSection config = getCommandConfig(rank, command);
        return config != null ? config.getInt("cooldown", 0) : 0;
    }

    /**
     * Get max homes for rank
     */
    public int getMaxHomes(String rank) {
        ConfigurationSection config = getCommandConfig(rank, "sethome");
        return config != null ? config.getInt("max-homes", 3) : 3;
    }

    /**
     * Get allowed worlds for command
     */
    public List<String> getAllowedWorlds(String rank, String command) {
        ConfigurationSection config = getCommandConfig(rank, command);
        return config != null ? config.getStringList("allowed-worlds") : new ArrayList<>();
    }

    /**
     * Check if world is restricted for command
     */
    public boolean isWorldRestricted(String rank, String command) {
        ConfigurationSection config = getCommandConfig(rank, command);
        return config != null && config.getBoolean("world-restricted", false);
    }

    /**
     * Check if player can use command in world
     */
    public boolean canUseInWorld(Player player, String command, String world) {
        String rank = getPlayerRank(player);
        
        if (!isWorldRestricted(rank, command)) {
            return true;
        }

        List<String> allowedWorlds = getAllowedWorlds(rank, command);
        return allowedWorlds.isEmpty() || allowedWorlds.contains(world);
    }

    /**
     * Get hourly limit for command
     */
    public int getHourlyLimit(String rank, String command) {
        ConfigurationSection config = getCommandConfig(rank, command);
        return config != null ? config.getInt("hourly-limit", -1) : -1;
    }

    /**
     * Get daily limit for command
     */
    public int getDailyLimit(String rank, String command) {
        ConfigurationSection config = getCommandConfig(rank, command);
        return config != null ? config.getInt("daily-limit", -1) : -1;
    }

    /**
     * Get weekly limit for command
     */
    public int getWeeklyLimit(String rank, String command) {
        ConfigurationSection config = getCommandConfig(rank, command);
        return config != null ? config.getInt("weekly-limit", -1) : -1;
    }

    /**
     * Get monthly limit for command
     */
    public int getMonthlyLimit(String rank, String command) {
        ConfigurationSection config = getCommandConfig(rank, command);
        return config != null ? config.getInt("monthly-limit", -1) : -1;
    }

    /**
     * Check if rank bypasses all limits
     */
    public boolean bypassesAllLimits(String rank) {
        ConfigurationSection config = getRankConfig(rank);
        return config != null && config.getBoolean("bypass-all-limits", false);
    }

    /**
     * Check if rank bypasses all costs
     */
    public boolean bypassesAllCosts(String rank) {
        ConfigurationSection config = getRankConfig(rank);
        return config != null && config.getBoolean("bypass-all-costs", false);
    }

    /**
     * Get rank display name
     */
    public String getRankDisplayName(String rank) {
        ConfigurationSection config = getRankConfig(rank);
        return config != null ? config.getString("display-name", rank) : rank;
    }

    /**
     * Clear cache
     */
    public void clearCache() {
        rankCache.clear();
    }

    /**
     * Clear cache for specific player
     */
    public void clearCache(UUID uuid) {
        rankCache.remove(uuid);
    }

    /**
     * Cached rank with expiration
     */
    private static class CachedRank {
        private final String rank;
        private final long expireTime;

        public CachedRank(String rank, int durationMinutes) {
            this.rank = rank;
            this.expireTime = System.currentTimeMillis() + (durationMinutes * 60 * 1000L);
        }

        public String getRank() {
            return rank;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }
}

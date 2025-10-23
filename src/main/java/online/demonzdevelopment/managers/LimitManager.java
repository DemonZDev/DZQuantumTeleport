package online.demonzdevelopment.managers;

import online.demonzdevelopment.DZQuantumTeleport;

import java.util.UUID;

/**
 * Limit Manager  
 * Handles usage limits (hourly, daily, weekly, monthly)
 */
public class LimitManager {

    private final DZQuantumTeleport plugin;

    public LimitManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    /**
     * Check if player can use command based on limits
     */
    public boolean canUse(UUID uuid, String command, String rank) {
        // Check hourly limit
        int hourlyLimit = plugin.getRankManager().getHourlyLimit(rank, command);
        if (hourlyLimit > 0) {
            int hourlyUsage = getUsageCount(uuid, command, "hourly");
            if (hourlyUsage >= hourlyLimit) {
                return false;
            }
        }

        // Check daily limit
        int dailyLimit = plugin.getRankManager().getDailyLimit(rank, command);
        if (dailyLimit > 0) {
            int dailyUsage = getUsageCount(uuid, command, "daily");
            if (dailyUsage >= dailyLimit) {
                return false;
            }
        }

        // Check weekly limit
        int weeklyLimit = plugin.getRankManager().getWeeklyLimit(rank, command);
        if (weeklyLimit > 0) {
            int weeklyUsage = getUsageCount(uuid, command, "weekly");
            if (weeklyUsage >= weeklyLimit) {
                return false;
            }
        }

        // Check monthly limit
        int monthlyLimit = plugin.getRankManager().getMonthlyLimit(rank, command);
        if (monthlyLimit > 0) {
            int monthlyUsage = getUsageCount(uuid, command, "monthly");
            if (monthlyUsage >= monthlyLimit) {
                return false;
            }
        }

        return true;
    }

    /**
     * Increment usage count
     */
    public void incrementUsage(UUID uuid, String command) {
        plugin.getDatabaseManager().incrementUsageCount(uuid, command, "hourly");
        plugin.getDatabaseManager().incrementUsageCount(uuid, command, "daily");
        plugin.getDatabaseManager().incrementUsageCount(uuid, command, "weekly");
        plugin.getDatabaseManager().incrementUsageCount(uuid, command, "monthly");
    }

    /**
     * Get usage count for period
     */
    private int getUsageCount(UUID uuid, String command, String period) {
        return plugin.getDatabaseManager().getUsageCount(uuid, command, period).join();
    }

    /**
     * Reset limits for period
     */
    public void resetLimits(String period) {
        plugin.getDatabaseManager().resetLimits(period);
    }

    /**
     * Clear cache
     */
    public void clearCache() {
        // Limits are stored in database, no cache to clear
    }
}

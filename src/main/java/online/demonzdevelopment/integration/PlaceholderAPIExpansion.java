package online.demonzdevelopment.integration;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * PlaceholderAPI Expansion for DZQuantumTeleport
 */
public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private final DZQuantumTeleport plugin;

    public PlaceholderAPIExpansion(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "dzqtp";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        // %dzqtp_homes%
        if (params.equals("homes")) {
            return String.valueOf(plugin.getHomeManager().getHomeCount(player.getUniqueId()));
        }

        // %dzqtp_max_homes%
        if (params.equals("max_homes")) {
            String rank = plugin.getRankManager().getPlayerRank(player);
            return String.valueOf(plugin.getRankManager().getMaxHomes(rank));
        }

        // %dzqtp_rank%
        if (params.equals("rank")) {
            return plugin.getRankManager().getPlayerRank(player);
        }

        // %dzqtp_rtp_tier%
        if (params.equals("rtp_tier")) {
            return plugin.getSmartRTPManager().getPlayerTier(player.getUniqueId()).getDisplayName();
        }

        // %dzqtp_rtp_uses%
        if (params.equals("rtp_uses")) {
            return String.valueOf(plugin.getSmartRTPManager().getRTPUses(player.getUniqueId()));
        }

        // %dzqtp_cooldown_<command>%
        if (params.startsWith("cooldown_")) {
            String command = params.substring(9);
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player.getUniqueId(), command);
            if (remaining <= 0) {
                return "Ready";
            }
            return formatTime(remaining);
        }

        return null;
    }

    private String formatTime(long seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            return (seconds / 60) + "m " + (seconds % 60) + "s";
        } else {
            return (seconds / 3600) + "h " + ((seconds % 3600) / 60) + "m";
        }
    }
}

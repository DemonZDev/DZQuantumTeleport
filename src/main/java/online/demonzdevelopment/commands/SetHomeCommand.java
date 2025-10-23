package online.demonzdevelopment.commands;

import online.demonzdevelopment.DZQuantumTeleport;
import online.demonzdevelopment.models.Cost;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * SetHome Command
 * Allows players to create homes at their current location
 */
public class SetHomeCommand implements CommandExecutor {

    private final DZQuantumTeleport plugin;

    public SetHomeCommand(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageManager().getMessage("general.player-only"));
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("dzqtp.command.home")) {
            player.sendMessage(plugin.getMessageManager().getMessage("general.no-permission"));
            return true;
        }

        // Check arguments
        if (args.length == 0) {
            player.sendMessage(plugin.getMessageManager().getMessage("home.invalid-name"));
            return true;
        }

        String homeName = args[0].toLowerCase();
        String rank = plugin.getRankManager().getPlayerRank(player);

        // Check if command is enabled for rank
        if (!plugin.getRankManager().isCommandEnabled(rank, "sethome")) {
            player.sendMessage(plugin.getMessageManager().getMessage("general.no-permission"));
            return true;
        }

        // Validate home name
        if (!isValidHomeName(homeName)) {
            player.sendMessage(plugin.getMessageManager().getMessage("home.invalid-name"));
            return true;
        }

        // Check if home already exists
        if (plugin.getHomeManager().homeExists(player.getUniqueId(), homeName)) {
            player.sendMessage(plugin.getMessageManager().getMessage("home.set-already-exists"));
            return true;
        }

        // Check if player can create more homes
        if (!plugin.getHomeManager().canCreateHome(player)) {
            int current = plugin.getHomeManager().getHomeCount(player.getUniqueId());
            int max = plugin.getRankManager().getMaxHomes(rank);
            
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("current", String.valueOf(current));
            placeholders.put("max", String.valueOf(max));
            
            player.sendMessage(plugin.getMessageManager().getMessage("home.set-max-reached", placeholders));
            return true;
        }

        // Check cooldown
        if (plugin.getCooldownManager().hasCooldown(player.getUniqueId(), "sethome")) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player.getUniqueId(), "sethome");
            
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("time", plugin.getMessageManager().formatTime(remaining));
            
            player.sendMessage(plugin.getMessageManager().getMessage("cooldown.active", placeholders));
            return true;
        }

        // Check limits
        if (!plugin.getLimitManager().canUse(player.getUniqueId(), "sethome", rank)) {
            player.sendMessage(plugin.getMessageManager().getMessage("limits.daily-reached"));
            return true;
        }

        // Check world restrictions
        if (!plugin.getRankManager().canUseInWorld(player, "sethome", player.getWorld().getName())) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("world", player.getWorld().getName());
            player.sendMessage(plugin.getMessageManager().getMessage("world-restrictions.sethome-denied", placeholders));
            return true;
        }

        // Get cost
        Cost cost = plugin.getEconomyManager().getCost(player, "sethome");

        // Check if player can afford
        if (!plugin.getEconomyManager().canAfford(player, cost)) {
            player.sendMessage(plugin.getMessageManager().getMessage("costs.insufficient-funds"));
            for (String missing : plugin.getEconomyManager().getMissingAmounts(player, cost)) {
                player.sendMessage("  &c- " + missing);
            }
            return true;
        }

        // Charge cost
        if (!plugin.getEconomyManager().chargeCost(player, cost)) {
            player.sendMessage(plugin.getMessageManager().getMessage("costs.insufficient-funds"));
            return true;
        }

        // Create home
        plugin.getHomeManager().createHome(player.getUniqueId(), homeName, player.getLocation())
            .thenAccept(success -> {
                if (success) {
                    // Set cooldown
                    int cooldown = plugin.getRankManager().getCooldown(rank, "sethome");
                    plugin.getCooldownManager().setCooldown(player.getUniqueId(), "sethome", cooldown);
                    
                    // Increment usage
                    plugin.getLimitManager().incrementUsage(player.getUniqueId(), "sethome");
                    
                    // Send success message
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("name", homeName);
                    player.sendMessage(plugin.getMessageManager().getMessage("home.set-success", placeholders));
                    
                    // Play sound
                    if (plugin.getConfigManager().areSoundsEnabled()) {
                        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                    }
                } else {
                    // Refund on error
                    plugin.getEconomyManager().refundCost(player, cost, 1.0);
                    player.sendMessage(plugin.getMessageManager().getMessage("home.set-error"));
                }
            });

        return true;
    }

    /**
     * Validate home name
     */
    private boolean isValidHomeName(String name) {
        if (name.length() < 2 || name.length() > 16) {
            return false;
        }
        return name.matches("[a-zA-Z0-9_-]+");
    }
}

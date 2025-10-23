package online.demonzdevelopment.commands;

import online.demonzdevelopment.DZQuantumTeleport;
import online.demonzdevelopment.models.Cost;
import online.demonzdevelopment.models.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Home Command
 * Teleports player to their home
 */
public class HomeCommand implements CommandExecutor {

    private final DZQuantumTeleport plugin;

    public HomeCommand(DZQuantumTeleport plugin) {
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
            player.sendMessage("&cUsage: /home <name>");
            return true;
        }

        String homeName = args[0].toLowerCase();
        String rank = plugin.getRankManager().getPlayerRank(player);

        // Check if command is enabled
        if (!plugin.getRankManager().isCommandEnabled(rank, "home")) {
            player.sendMessage(plugin.getMessageManager().getMessage("general.no-permission"));
            return true;
        }

        // Get home
        Home home = plugin.getHomeManager().getHome(player.getUniqueId(), homeName);
        if (home == null) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("name", homeName);
            player.sendMessage(plugin.getMessageManager().getMessage("home.teleport-not-found", placeholders));
            return true;
        }

        // Check cooldown
        if (plugin.getCooldownManager().hasCooldown(player.getUniqueId(), "home")) {
            long remaining = plugin.getCooldownManager().getRemainingCooldown(player.getUniqueId(), "home");
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("time", plugin.getMessageManager().formatTime(remaining));
            player.sendMessage(plugin.getMessageManager().getMessage("cooldown.active", placeholders));
            return true;
        }

        // Check limits
        if (!plugin.getLimitManager().canUse(player.getUniqueId(), "home", rank)) {
            player.sendMessage(plugin.getMessageManager().getMessage("limits.daily-reached"));
            return true;
        }

        // Get cost
        Cost cost = plugin.getEconomyManager().getCost(player, "home");

        // Check if player can afford
        if (!plugin.getEconomyManager().canAfford(player, cost)) {
            player.sendMessage(plugin.getMessageManager().getMessage("costs.insufficient-funds"));
            return true;
        }

        // Charge cost
        if (!plugin.getEconomyManager().chargeCost(player, cost)) {
            return true;
        }

        // Teleport
        plugin.getTeleportManager().teleport(player, home.getLocation())
            .thenAccept(success -> {
                if (success) {
                    // Set cooldown
                    int cooldown = plugin.getRankManager().getCooldown(rank, "home");
                    plugin.getCooldownManager().setCooldown(player.getUniqueId(), "home", cooldown);
                    
                    // Increment usage
                    plugin.getLimitManager().incrementUsage(player.getUniqueId(), "home");
                    
                    // Update last used
                    home.updateLastUsed();
                    plugin.getDatabaseManager().saveHome(home);
                    
                    // Send success message
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("name", homeName);
                    player.sendMessage(plugin.getMessageManager().getMessage("home.teleport-success", placeholders));
                    
                    // Particles
                    plugin.getParticleManager().playArrivalEffect(player);
                } else {
                    // Refund
                    plugin.getEconomyManager().refundCost(player, cost, 1.0);
                    player.sendMessage(plugin.getMessageManager().getMessage("teleportation.unsafe-location"));
                }
            });

        return true;
    }
}

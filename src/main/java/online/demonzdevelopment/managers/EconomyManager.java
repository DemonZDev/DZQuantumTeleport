package online.demonzdevelopment.managers;

import online.demonzdevelopment.DZQuantumTeleport;
import online.demonzdevelopment.config.RankManager;
import online.demonzdevelopment.integration.DZEconomyAPI;
import online.demonzdevelopment.models.Cost;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Economy Manager
 * Handles all cost calculations and transactions
 */
public class EconomyManager {

    private final DZQuantumTeleport plugin;
    private final DZEconomyAPI economyAPI;

    public EconomyManager(DZQuantumTeleport plugin, DZEconomyAPI economyAPI) {
        this.plugin = plugin;
        this.economyAPI = economyAPI;
    }

    /**
     * Get cost for a command based on player's rank
     * @param player Player
     * @param command Command name
     * @return Cost object
     */
    public Cost getCost(Player player, String command) {
        String rank = plugin.getRankManager().getPlayerRank(player);
        
        // Check if rank bypasses costs
        if (plugin.getRankManager().bypassesAllCosts(rank)) {
            return new Cost();
        }

        // Check bypass permission
        if (player.hasPermission("dzqtp.bypass.cost")) {
            return new Cost();
        }

        ConfigurationSection config = plugin.getRankManager().getCommandConfig(rank, command);
        if (config == null) {
            return new Cost();
        }

        ConfigurationSection costsSection = config.getConfigurationSection("costs");
        if (costsSection == null) {
            return new Cost();
        }

        Cost cost = new Cost();
        cost.setMoney(costsSection.getDouble("money", 0));
        cost.setMobcoin(costsSection.getDouble("mobcoin", 0));
        cost.setGem(costsSection.getDouble("gem", 0));
        cost.setXpLevels(costsSection.getInt("xp-levels", 0));

        // Load items
        if (costsSection.contains("items")) {
            List<?> itemsList = costsSection.getList("items");
            if (itemsList != null) {
                for (Object obj : itemsList) {
                    if (obj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> itemMap = (Map<String, Object>) obj;
                        String material = (String) itemMap.get("material");
                        int amount = ((Number) itemMap.get("amount")).intValue();
                        cost.addItem(new Cost.ItemCost(material, amount));
                    }
                }
            }
        }

        return cost;
    }

    /**
     * Check if player can afford a cost
     * @param player Player
     * @param cost Cost to check
     * @return true if player can afford
     */
    public boolean canAfford(Player player, Cost cost) {
        // Check money
        if (cost.getMoney() > 0) {
            if (!economyAPI.hasBalance(player.getUniqueId(), DZEconomyAPI.CurrencyType.MONEY, cost.getMoney())) {
                return false;
            }
        }

        // Check mobcoin
        if (cost.getMobcoin() > 0) {
            if (!economyAPI.hasBalance(player.getUniqueId(), DZEconomyAPI.CurrencyType.MOBCOIN, cost.getMobcoin())) {
                return false;
            }
        }

        // Check gem
        if (cost.getGem() > 0) {
            if (!economyAPI.hasBalance(player.getUniqueId(), DZEconomyAPI.CurrencyType.GEM, cost.getGem())) {
                return false;
            }
        }

        // Check XP
        if (cost.getXpLevels() > 0) {
            if (player.getLevel() < cost.getXpLevels()) {
                return false;
            }
        }

        // Check items
        for (Cost.ItemCost itemCost : cost.getItems()) {
            Material material = Material.getMaterial(itemCost.getMaterial());
            if (material == null) continue;
            
            ItemStack item = new ItemStack(material, itemCost.getAmount());
            if (!player.getInventory().containsAtLeast(item, itemCost.getAmount())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Charge player the cost
     * @param player Player
     * @param cost Cost to charge
     * @return true if successful
     */
    public boolean chargeCost(Player player, Cost cost) {
        if (!canAfford(player, cost)) {
            return false;
        }

        // Remove money
        if (cost.getMoney() > 0) {
            economyAPI.removeCurrency(player.getUniqueId(), DZEconomyAPI.CurrencyType.MONEY, cost.getMoney());
        }

        // Remove mobcoin
        if (cost.getMobcoin() > 0) {
            economyAPI.removeCurrency(player.getUniqueId(), DZEconomyAPI.CurrencyType.MOBCOIN, cost.getMobcoin());
        }

        // Remove gem
        if (cost.getGem() > 0) {
            economyAPI.removeCurrency(player.getUniqueId(), DZEconomyAPI.CurrencyType.GEM, cost.getGem());
        }

        // Remove XP
        if (cost.getXpLevels() > 0) {
            player.setLevel(player.getLevel() - cost.getXpLevels());
        }

        // Remove items
        for (Cost.ItemCost itemCost : cost.getItems()) {
            Material material = Material.getMaterial(itemCost.getMaterial());
            if (material == null) continue;
            
            ItemStack item = new ItemStack(material, itemCost.getAmount());
            player.getInventory().removeItem(item);
        }

        return true;
    }

    /**
     * Refund cost to player
     * @param player Player
     * @param cost Cost to refund
     * @param percentage Refund percentage (0.0 - 1.0)
     */
    public void refundCost(Player player, Cost cost, double percentage) {
        // Refund money
        if (cost.getMoney() > 0) {
            double refund = cost.getMoney() * percentage;
            economyAPI.addCurrency(player.getUniqueId(), DZEconomyAPI.CurrencyType.MONEY, refund);
        }

        // Refund mobcoin
        if (cost.getMobcoin() > 0) {
            double refund = cost.getMobcoin() * percentage;
            economyAPI.addCurrency(player.getUniqueId(), DZEconomyAPI.CurrencyType.MOBCOIN, refund);
        }

        // Refund gem
        if (cost.getGem() > 0) {
            double refund = cost.getGem() * percentage;
            economyAPI.addCurrency(player.getUniqueId(), DZEconomyAPI.CurrencyType.GEM, refund);
        }

        // Refund XP
        if (cost.getXpLevels() > 0) {
            int refund = (int) (cost.getXpLevels() * percentage);
            player.setLevel(player.getLevel() + refund);
        }

        // Refund items
        for (Cost.ItemCost itemCost : cost.getItems()) {
            Material material = Material.getMaterial(itemCost.getMaterial());
            if (material == null) continue;
            
            int refund = (int) (itemCost.getAmount() * percentage);
            ItemStack item = new ItemStack(material, refund);
            player.getInventory().addItem(item);
        }
    }

    /**
     * Get missing amounts message
     * @param player Player
     * @param cost Cost
     * @return List of missing items
     */
    public List<String> getMissingAmounts(Player player, Cost cost) {
        List<String> missing = new ArrayList<>();

        // Check money
        if (cost.getMoney() > 0) {
            double current = economyAPI.getBalance(player.getUniqueId(), DZEconomyAPI.CurrencyType.MONEY);
            if (current < cost.getMoney()) {
                missing.add("Money: $" + (cost.getMoney() - current));
            }
        }

        // Check mobcoin
        if (cost.getMobcoin() > 0) {
            double current = economyAPI.getBalance(player.getUniqueId(), DZEconomyAPI.CurrencyType.MOBCOIN);
            if (current < cost.getMobcoin()) {
                missing.add("MobCoins: " + (cost.getMobcoin() - current) + " MC");
            }
        }

        // Check gem
        if (cost.getGem() > 0) {
            double current = economyAPI.getBalance(player.getUniqueId(), DZEconomyAPI.CurrencyType.GEM);
            if (current < cost.getGem()) {
                missing.add("Gems: " + (cost.getGem() - current) + " ◆");
            }
        }

        // Check XP
        if (cost.getXpLevels() > 0 && player.getLevel() < cost.getXpLevels()) {
            missing.add("XP Levels: " + (cost.getXpLevels() - player.getLevel()));
        }

        // Check items
        for (Cost.ItemCost itemCost : cost.getItems()) {
            Material material = Material.getMaterial(itemCost.getMaterial());
            if (material == null) continue;
            
            ItemStack item = new ItemStack(material, itemCost.getAmount());
            if (!player.getInventory().containsAtLeast(item, itemCost.getAmount())) {
                int has = 0;
                for (ItemStack invItem : player.getInventory().getContents()) {
                    if (invItem != null && invItem.getType() == material) {
                        has += invItem.getAmount();
                    }
                }
                missing.add(itemCost.getMaterial() + ": " + (itemCost.getAmount() - has));
            }
        }

        return missing;
    }

    /**
     * Format cost as string
     * @param cost Cost
     * @return Formatted string
     */
    public String formatCost(Cost cost) {
        if (!cost.hasCost()) {
            return "Free";
        }

        List<String> parts = new ArrayList<>();

        if (cost.getMoney() > 0) {
            parts.add(economyAPI.formatCurrency(cost.getMoney(), DZEconomyAPI.CurrencyType.MONEY));
        }
        if (cost.getMobcoin() > 0) {
            parts.add(economyAPI.formatCurrency(cost.getMobcoin(), DZEconomyAPI.CurrencyType.MOBCOIN));
        }
        if (cost.getGem() > 0) {
            parts.add(economyAPI.formatCurrency(cost.getGem(), DZEconomyAPI.CurrencyType.GEM));
        }
        if (cost.getXpLevels() > 0) {
            parts.add(cost.getXpLevels() + " XP Levels");
        }
        for (Cost.ItemCost item : cost.getItems()) {
            parts.add(item.getAmount() + "x " + item.getMaterial());
        }

        return String.join(", ", parts);
    }
}

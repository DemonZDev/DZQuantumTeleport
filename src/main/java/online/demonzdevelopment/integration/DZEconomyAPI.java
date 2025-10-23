package online.demonzdevelopment.integration;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * DZEconomy API Interface
 * This interface mirrors the actual DZEconomy API structure
 */
public interface DZEconomyAPI {

    /**
     * Currency types supported by DZEconomy
     */
    enum CurrencyType {
        MONEY,
        MOBCOIN,
        GEM
    }

    /**
     * Check if player has sufficient balance
     * @param uuid Player UUID
     * @param type Currency type
     * @param amount Amount to check
     * @return true if player has enough balance
     */
    boolean hasBalance(UUID uuid, CurrencyType type, double amount);

    /**
     * Get player's balance
     * @param uuid Player UUID
     * @param type Currency type
     * @return Current balance
     */
    double getBalance(UUID uuid, CurrencyType type);

    /**
     * Add currency to player
     * @param uuid Player UUID
     * @param type Currency type
     * @param amount Amount to add
     * @return true if successful
     */
    boolean addCurrency(UUID uuid, CurrencyType type, double amount);

    /**
     * Remove currency from player
     * @param uuid Player UUID
     * @param type Currency type
     * @param amount Amount to remove
     * @return true if successful
     */
    boolean removeCurrency(UUID uuid, CurrencyType type, double amount);

    /**
     * Format currency amount with symbol
     * @param amount Amount to format
     * @param type Currency type
     * @return Formatted string
     */
    String formatCurrency(double amount, CurrencyType type);
}

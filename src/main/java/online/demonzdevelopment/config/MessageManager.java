package online.demonzdevelopment.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Message Manager
 * Handles all plugin messages with color code support
 */
public class MessageManager {

    private final DZQuantumTeleport plugin;
    private FileConfiguration messages;
    private final Map<String, String> messageCache;
    private final MiniMessage miniMessage;
    private final LegacyComponentSerializer legacySerializer;

    public MessageManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
        this.messageCache = new HashMap<>();
        this.miniMessage = MiniMessage.miniMessage();
        this.legacySerializer = LegacyComponentSerializer.legacyAmpersand();
        loadMessages();
    }

    public void loadMessages() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        messageCache.clear();
    }

    public void reload() {
        loadMessages();
    }

    /**
     * Get a message from messages.yml
     * @param path Message path
     * @return Formatted message
     */
    public String getMessage(String path) {
        if (messageCache.containsKey(path)) {
            return messageCache.get(path);
        }

        String message = messages.getString(path, "&cMessage not found: " + path);
        message = applyPrefix(message);
        messageCache.put(path, message);
        return message;
    }

    /**
     * Get message with placeholders
     * @param path Message path
     * @param placeholders Key-value pairs for replacement
     * @return Formatted message
     */
    public String getMessage(String path, Map<String, String> placeholders) {
        String message = getMessage(path);
        
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        
        return message;
    }

    /**
     * Get message as Component
     * @param path Message path
     * @return Component message
     */
    public Component getComponent(String path) {
        return colorize(getMessage(path));
    }

    /**
     * Get message as Component with placeholders
     * @param path Message path
     * @param placeholders Placeholder replacements
     * @return Component message
     */
    public Component getComponent(String path, Map<String, String> placeholders) {
        return colorize(getMessage(path, placeholders));
    }

    /**
     * Colorize a string with legacy and hex color support
     * @param text Input text
     * @return Colored Component
     */
    public Component colorize(String text) {
        // Support hex colors &#RRGGBB
        text = translateHexColors(text);
        // Convert legacy & codes to Component
        return legacySerializer.deserialize(text);
    }

    /**
     * Translate hex colors from &#RRGGBB to § format
     */
    private String translateHexColors(String text) {
        // Pattern: &#RRGGBB
        return text.replaceAll("&#([0-9A-Fa-f]{6})", "§x§$1§$2§$3§$4§$5§$6")
                   .replaceAll("§x§(.)§(.)§(.)§(.)§(.)§(.)", "§x§$1§$2§$3§$4§$5§$6");
    }

    /**
     * Apply prefix to message if it contains {prefix}
     */
    private String applyPrefix(String message) {
        if (message.contains("{prefix}")) {
            String prefix = messages.getString("prefix", "&8[&5DZQuantumTeleport&8]&r ");
            message = message.replace("{prefix}", prefix);
        }
        return message;
    }

    /**
     * Format time in seconds to readable format
     * @param seconds Time in seconds
     * @return Formatted time string
     */
    public String formatTime(long seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            long secs = seconds % 60;
            return minutes + "m " + secs + "s";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            long secs = seconds % 60;
            return hours + "h " + minutes + "m " + secs + "s";
        } else {
            long days = seconds / 86400;
            long hours = (seconds % 86400) / 3600;
            long minutes = (seconds % 3600) / 60;
            return days + "d " + hours + "h " + minutes + "m";
        }
    }

    /**
     * Get the prefix
     */
    public String getPrefix() {
        return messages.getString("prefix", "&8[&5DZQuantumTeleport&8]&r ");
    }
}

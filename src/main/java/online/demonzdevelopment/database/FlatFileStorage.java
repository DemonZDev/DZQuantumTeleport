package online.demonzdevelopment.database;

import online.demonzdevelopment.DZQuantumTeleport;
import online.demonzdevelopment.models.Home;
import online.demonzdevelopment.models.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * FlatFile Storage Implementation
 * Stores data in YAML files
 */
public class FlatFileStorage extends DatabaseManager {

    private final DZQuantumTeleport plugin;
    private final File dataFolder;
    
    private File homesFile;
    private File warpsFile;
    private File spawnsFile;
    private File backLocationsFile;
    private File rtpStatsFile;
    private File cooldownsFile;
    private File limitsFile;
    private File togglesFile;
    
    private FileConfiguration homesConfig;
    private FileConfiguration warpsConfig;
    private FileConfiguration spawnsConfig;
    private FileConfiguration backLocationsConfig;
    private FileConfiguration rtpStatsConfig;
    private FileConfiguration cooldownsConfig;
    private FileConfiguration limitsConfig;
    private FileConfiguration togglesConfig;

    public FlatFileStorage(DZQuantumTeleport plugin) {
        this.plugin = plugin;
        String path = plugin.getConfig().getString("storage.flatfile.path", "plugins/DZQuantumTeleport/data/");
        this.dataFolder = new File(path);
    }

    @Override
    public void initialize() throws Exception {
        // Create data folder
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        // Initialize files
        homesFile = new File(dataFolder, "homes.yml");
        warpsFile = new File(dataFolder, "warps.yml");
        spawnsFile = new File(dataFolder, "spawns.yml");
        backLocationsFile = new File(dataFolder, "back-locations.yml");
        rtpStatsFile = new File(dataFolder, "rtp-stats.yml");
        cooldownsFile = new File(dataFolder, "cooldowns.yml");
        limitsFile = new File(dataFolder, "limits.yml");
        togglesFile = new File(dataFolder, "toggles.yml");

        // Create files if they don't exist
        createFileIfNotExists(homesFile);
        createFileIfNotExists(warpsFile);
        createFileIfNotExists(spawnsFile);
        createFileIfNotExists(backLocationsFile);
        createFileIfNotExists(rtpStatsFile);
        createFileIfNotExists(cooldownsFile);
        createFileIfNotExists(limitsFile);
        createFileIfNotExists(togglesFile);

        // Load configurations
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);
        warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);
        spawnsConfig = YamlConfiguration.loadConfiguration(spawnsFile);
        backLocationsConfig = YamlConfiguration.loadConfiguration(backLocationsFile);
        rtpStatsConfig = YamlConfiguration.loadConfiguration(rtpStatsFile);
        cooldownsConfig = YamlConfiguration.loadConfiguration(cooldownsFile);
        limitsConfig = YamlConfiguration.loadConfiguration(limitsFile);
        togglesConfig = YamlConfiguration.loadConfiguration(togglesFile);

        plugin.getLogger().info("FlatFile storage initialized!");
    }

    private void createFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @Override
    public void close() {
        saveAllData();
    }

    @Override
    public void saveAllData() {
        try {
            homesConfig.save(homesFile);
            warpsConfig.save(warpsFile);
            spawnsConfig.save(spawnsFile);
            backLocationsConfig.save(backLocationsFile);
            rtpStatsConfig.save(rtpStatsFile);
            cooldownsConfig.save(cooldownsFile);
            limitsConfig.save(limitsFile);
            togglesConfig.save(togglesFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save data!", e);
        }
    }

    // ===== HOME METHODS =====

    @Override
    public CompletableFuture<List<Home>> loadHomes(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            List<Home> homes = new ArrayList<>();
            String path = uuid.toString();
            
            if (!homesConfig.contains(path)) {
                return homes;
            }

            for (String homeName : homesConfig.getConfigurationSection(path).getKeys(false)) {
                String homePath = path + "." + homeName;
                Location location = deserializeLocation(homesConfig.getString(homePath + ".location"));
                
                if (location != null) {
                    long createdAt = homesConfig.getLong(homePath + ".created-at", System.currentTimeMillis());
                    long lastUsed = homesConfig.getLong(homePath + ".last-used", 0);
                    homes.add(new Home(uuid, homeName, location, createdAt, lastUsed));
                }
            }

            return homes;
        });
    }

    @Override
    public CompletableFuture<Boolean> saveHome(Home home) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String path = home.getOwner().toString() + "." + home.getName();
                homesConfig.set(path + ".location", serializeLocation(home.getLocation()));
                homesConfig.set(path + ".created-at", home.getCreatedAt());
                homesConfig.set(path + ".last-used", home.getLastUsed());
                homesConfig.save(homesFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save home!", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteHome(UUID uuid, String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String path = uuid.toString() + "." + name;
                homesConfig.set(path, null);
                homesConfig.save(homesFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to delete home!", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> homeExists(UUID uuid, String name) {
        return CompletableFuture.supplyAsync(() -> {
            return homesConfig.contains(uuid.toString() + "." + name);
        });
    }

    // ===== WARP METHODS =====

    @Override
    public CompletableFuture<List<Warp>> loadWarps() {
        return CompletableFuture.supplyAsync(() -> {
            List<Warp> warps = new ArrayList<>();
            
            for (String warpName : warpsConfig.getKeys(false)) {
                Location location = deserializeLocation(warpsConfig.getString(warpName + ".location"));
                
                if (location != null) {
                    Warp.WarpVisibility visibility = Warp.WarpVisibility.valueOf(
                        warpsConfig.getString(warpName + ".visibility", "PUBLIC")
                    );
                    long createdAt = warpsConfig.getLong(warpName + ".created-at", System.currentTimeMillis());
                    long lastUsed = warpsConfig.getLong(warpName + ".last-used", 0);
                    int usageCount = warpsConfig.getInt(warpName + ".usage-count", 0);
                    
                    warps.add(new Warp(warpName, location, visibility, createdAt, lastUsed, usageCount));
                }
            }

            return warps;
        });
    }

    @Override
    public CompletableFuture<Boolean> saveWarp(Warp warp) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String path = warp.getName();
                warpsConfig.set(path + ".location", serializeLocation(warp.getLocation()));
                warpsConfig.set(path + ".visibility", warp.getVisibility().name());
                warpsConfig.set(path + ".created-at", warp.getCreatedAt());
                warpsConfig.set(path + ".last-used", warp.getLastUsed());
                warpsConfig.set(path + ".usage-count", warp.getUsageCount());
                warpsConfig.save(warpsFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save warp!", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteWarp(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                warpsConfig.set(name, null);
                warpsConfig.save(warpsFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to delete warp!", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> warpExists(String name) {
        return CompletableFuture.supplyAsync(() -> {
            return warpsConfig.contains(name);
        });
    }

    // ===== SPAWN METHODS =====

    @Override
    public CompletableFuture<Boolean> saveSpawn(String world, Location location) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                spawnsConfig.set(world, serializeLocation(location));
                spawnsConfig.save(spawnsFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save spawn!", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Location> loadSpawn(String world) {
        return CompletableFuture.supplyAsync(() -> {
            return deserializeLocation(spawnsConfig.getString(world));
        });
    }

    // ===== BACK LOCATION METHODS =====

    @Override
    public CompletableFuture<Boolean> saveBackLocation(UUID uuid, Location location) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                backLocationsConfig.set(uuid.toString(), serializeLocation(location));
                backLocationsConfig.save(backLocationsFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save back location!", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Location> loadBackLocation(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            return deserializeLocation(backLocationsConfig.getString(uuid.toString()));
        });
    }

    // ===== RTP STATISTICS =====

    @Override
    public CompletableFuture<Integer> getRTPUses(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            return rtpStatsConfig.getInt(uuid.toString() + ".uses", 0);
        });
    }

    @Override
    public CompletableFuture<Boolean> incrementRTPUses(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int current = rtpStatsConfig.getInt(uuid.toString() + ".uses", 0);
                rtpStatsConfig.set(uuid.toString() + ".uses", current + 1);
                rtpStatsConfig.save(rtpStatsFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to increment RTP uses!", e);
                return false;
            }
        });
    }

    // ===== COOLDOWN METHODS =====

    @Override
    public CompletableFuture<Boolean> saveCooldown(UUID uuid, String command, long expireTime) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                cooldownsConfig.set(uuid.toString() + "." + command, expireTime);
                cooldownsConfig.save(cooldownsFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save cooldown!", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Long> getCooldownExpiry(UUID uuid, String command) {
        return CompletableFuture.supplyAsync(() -> {
            return cooldownsConfig.getLong(uuid.toString() + "." + command, 0);
        });
    }

    @Override
    public CompletableFuture<Void> cleanupExpiredCooldowns() {
        return CompletableFuture.runAsync(() -> {
            long now = System.currentTimeMillis();
            for (String uuid : cooldownsConfig.getKeys(false)) {
                for (String command : cooldownsConfig.getConfigurationSection(uuid).getKeys(false)) {
                    long expiry = cooldownsConfig.getLong(uuid + "." + command);
                    if (expiry < now) {
                        cooldownsConfig.set(uuid + "." + command, null);
                    }
                }
            }
            try {
                cooldownsConfig.save(cooldownsFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to cleanup cooldowns!", e);
            }
        });
    }

    // ===== LIMIT METHODS =====

    @Override
    public CompletableFuture<Integer> getUsageCount(UUID uuid, String command, String period) {
        return CompletableFuture.supplyAsync(() -> {
            return limitsConfig.getInt(uuid.toString() + "." + command + "." + period, 0);
        });
    }

    @Override
    public CompletableFuture<Boolean> incrementUsageCount(UUID uuid, String command, String period) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String path = uuid.toString() + "." + command + "." + period;
                int current = limitsConfig.getInt(path, 0);
                limitsConfig.set(path, current + 1);
                limitsConfig.save(limitsFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to increment usage count!", e);
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> resetLimits(String period) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                for (String uuid : limitsConfig.getKeys(false)) {
                    for (String command : limitsConfig.getConfigurationSection(uuid).getKeys(false)) {
                        limitsConfig.set(uuid + "." + command + "." + period, 0);
                    }
                }
                limitsConfig.save(limitsFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to reset limits!", e);
                return false;
            }
        });
    }

    // ===== TOGGLE TP METHODS =====

    @Override
    public CompletableFuture<Boolean> getTPToggleState(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            return togglesConfig.getBoolean(uuid.toString(), true);
        });
    }

    @Override
    public CompletableFuture<Boolean> setTPToggleState(UUID uuid, boolean enabled) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                togglesConfig.set(uuid.toString(), enabled);
                togglesConfig.save(togglesFile);
                return true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save toggle state!", e);
                return false;
            }
        });
    }

    // ===== UTILITY METHODS =====

    private String serializeLocation(Location loc) {
        if (loc == null) return null;
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }

    private Location deserializeLocation(String str) {
        if (str == null) return null;
        try {
            String[] parts = str.split(",");
            World world = Bukkit.getWorld(parts[0]);
            if (world == null) return null;
            
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            
            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            return null;
        }
    }
}

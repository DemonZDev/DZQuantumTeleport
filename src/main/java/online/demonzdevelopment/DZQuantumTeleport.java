package online.demonzdevelopment;

import com.zaxxer.hikari.HikariDataSource;
import net.luckperms.api.LuckPerms;
import online.demonzdevelopment.api.DZQuantumTeleportAPI;
import online.demonzdevelopment.commands.*;
import online.demonzdevelopment.config.ConfigManager;
import online.demonzdevelopment.config.MessageManager;
import online.demonzdevelopment.config.RankManager;
import online.demonzdevelopment.database.DatabaseManager;
import online.demonzdevelopment.database.FlatFileStorage;
import online.demonzdevelopment.database.MySQLStorage;
import online.demonzdevelopment.database.SQLiteStorage;
import online.demonzdevelopment.integration.DZEconomyAPI;
import online.demonzdevelopment.integration.PlaceholderAPIExpansion;
import online.demonzdevelopment.listeners.*;
import online.demonzdevelopment.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * DZQuantumTeleport - Professional Multi-Currency Teleportation Plugin
 * 
 * @author hixeda6441
 * @version 1.0.0
 */
public final class DZQuantumTeleport extends JavaPlugin {

    // Plugin instance
    private static DZQuantumTeleport instance;

    // Configuration managers
    private ConfigManager configManager;
    private MessageManager messageManager;
    private RankManager rankManager;

    // Core managers
    private DatabaseManager databaseManager;
    private HomeManager homeManager;
    private WarpManager warpManager;
    private TeleportManager teleportManager;
    private SmartRTPManager smartRTPManager;
    private CooldownManager cooldownManager;
    private LimitManager limitManager;
    private EconomyManager economyManager;
    private ParticleManager particleManager;
    private TPAManager tpaManager;
    private RegionManager regionManager;

    // External integrations
    private DZEconomyAPI dzEconomyAPI;
    private LuckPerms luckPerms;
    private boolean placeholderAPIEnabled = false;
    private boolean worldEditEnabled = false;
    private boolean worldGuardEnabled = false;
    private boolean playerParticlesEnabled = false;

    // API instance
    private DZQuantumTeleportAPI api;

    @Override
    public void onEnable() {
        instance = this;

        // ASCII Art
        getLogger().info("╔══════════════════════════════════════╗");
        getLogger().info("║   DZQuantumTeleport v1.0.0           ║");
        getLogger().info("║   Professional Teleportation System  ║");
        getLogger().info("║   Author: hixeda6441                 ║");
        getLogger().info("╚══════════════════════════════════════╝");

        // Check required dependencies
        if (!checkDependencies()) {
            getLogger().severe("Required dependencies not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize configurations
        initializeConfigurations();

        // Initialize database
        if (!initializeDatabase()) {
            getLogger().severe("Failed to initialize database! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize managers
        initializeManagers();

        // Register commands
        registerCommands();

        // Register listeners
        registerListeners();

        // Check optional dependencies
        checkOptionalDependencies();

        // Register PlaceholderAPI expansion
        if (placeholderAPIEnabled) {
            new PlaceholderAPIExpansion(this).register();
            getLogger().info("PlaceholderAPI expansion registered!");
        }

        // Register API
        registerAPI();

        // Enable metrics (bStats)
        // new Metrics(this, METRICS_ID);

        getLogger().info("DZQuantumTeleport has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling DZQuantumTeleport...");

        // Save all data
        if (databaseManager != null) {
            databaseManager.saveAllData();
        }

        // Close database connections
        if (databaseManager != null) {
            databaseManager.close();
        }

        // Clear caches
        if (homeManager != null) homeManager.clearCache();
        if (warpManager != null) warpManager.clearCache();
        if (cooldownManager != null) cooldownManager.clearCache();
        if (limitManager != null) limitManager.clearCache();
        if (rankManager != null) rankManager.clearCache();

        getLogger().info("DZQuantumTeleport has been disabled!");
    }

    /**
     * Check required dependencies
     */
    private boolean checkDependencies() {
        // Check DZEconomy
        if (Bukkit.getPluginManager().getPlugin("DZEconomy") == null) {
            getLogger().severe("DZEconomy not found! This plugin requires DZEconomy to function.");
            return false;
        }

        // Get DZEconomy API
        try {
            dzEconomyAPI = Bukkit.getServicesManager()
                .getRegistration(DZEconomyAPI.class)
                .getProvider();
            
            if (dzEconomyAPI == null) {
                getLogger().severe("Failed to retrieve DZEconomy API!");
                return false;
            }
            
            getLogger().info("Successfully hooked into DZEconomy!");
        } catch (Exception e) {
            getLogger().severe("Failed to hook into DZEconomy: " + e.getMessage());
            return false;
        }

        // Check LuckPerms
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") == null) {
            getLogger().severe("LuckPerms not found! This plugin requires LuckPerms to function.");
            return false;
        }

        // Get LuckPerms API
        try {
            luckPerms = Bukkit.getServicesManager()
                .getRegistration(LuckPerms.class)
                .getProvider();
            
            if (luckPerms == null) {
                getLogger().severe("Failed to retrieve LuckPerms API!");
                return false;
            }
            
            getLogger().info("Successfully hooked into LuckPerms!");
        } catch (Exception e) {
            getLogger().severe("Failed to hook into LuckPerms: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Check optional dependencies
     */
    private void checkOptionalDependencies() {
        // PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPIEnabled = true;
            getLogger().info("PlaceholderAPI found and hooked!");
        }

        // WorldEdit
        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            worldEditEnabled = true;
            getLogger().info("WorldEdit found and hooked!");
        }

        // WorldGuard
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardEnabled = true;
            getLogger().info("WorldGuard found and hooked!");
        }

        // PlayerParticles
        if (Bukkit.getPluginManager().getPlugin("PlayerParticles") != null) {
            playerParticlesEnabled = true;
            getLogger().info("PlayerParticles found and hooked!");
        }
    }

    /**
     * Initialize configuration files
     */
    private void initializeConfigurations() {
        getLogger().info("Loading configurations...");
        
        // Save default configs
        saveDefaultConfig();
        saveResource("ranks.yml", false);
        saveResource("messages.yml", false);

        // Initialize managers
        configManager = new ConfigManager(this);
        messageManager = new MessageManager(this);
        rankManager = new RankManager(this);

        getLogger().info("Configurations loaded successfully!");
    }

    /**
     * Initialize database
     */
    private boolean initializeDatabase() {
        getLogger().info("Initializing database...");

        String storageType = getConfig().getString("storage.type", "flatfile").toLowerCase();

        try {
            switch (storageType) {
                case "mysql":
                    getLogger().info("Using MySQL storage...");
                    databaseManager = new MySQLStorage(this);
                    break;
                case "sqlite":
                    getLogger().info("Using SQLite storage...");
                    databaseManager = new SQLiteStorage(this);
                    break;
                case "flatfile":
                default:
                    getLogger().info("Using FlatFile storage...");
                    databaseManager = new FlatFileStorage(this);
                    break;
            }

            databaseManager.initialize();
            getLogger().info("Database initialized successfully!");
            return true;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to initialize database!", e);
            return false;
        }
    }

    /**
     * Initialize all managers
     */
    private void initializeManagers() {
        getLogger().info("Initializing managers...");

        // Core managers
        cooldownManager = new CooldownManager(this);
        limitManager = new LimitManager(this);
        economyManager = new EconomyManager(this, dzEconomyAPI);
        particleManager = new ParticleManager(this);
        
        homeManager = new HomeManager(this);
        warpManager = new WarpManager(this);
        tpaManager = new TPAManager(this);
        regionManager = new RegionManager(this);
        
        teleportManager = new TeleportManager(this);
        smartRTPManager = new SmartRTPManager(this);

        getLogger().info("Managers initialized successfully!");
    }

    /**
     * Register all commands
     */
    private void registerCommands() {
        getLogger().info("Registering commands...");

        // Main command
        getCommand("tp").setExecutor(new TPCommand(this));
        getCommand("dzqtp").setExecutor(new DZQTPCommand(this));
        
        // Spawn commands
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        
        // Home commands
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("listhome").setExecutor(new ListHomeCommand(this));
        getCommand("delhome").setExecutor(new DelHomeCommand(this));
        getCommand("renmhome").setExecutor(new RenameHomeCommand(this));
        getCommand("movhome").setExecutor(new MoveHomeCommand(this));
        
        // Warp commands
        getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("listwarp").setExecutor(new ListWarpCommand(this));
        getCommand("delwarp").setExecutor(new DelWarpCommand(this));
        getCommand("renmwarp").setExecutor(new RenameWarpCommand(this));
        getCommand("cvwarp").setExecutor(new ChangeWarpVisibilityCommand(this));
        getCommand("movwarp").setExecutor(new MoveWarpCommand(this));
        
        // Teleport commands
        getCommand("rtp").setExecutor(new RTPCommand(this));
        getCommand("tpa").setExecutor(new TPACommand(this));
        getCommand("call").setExecutor(new CallCommand(this));
        getCommand("bring").setExecutor(new BringCommand(this));
        getCommand("toggletp").setExecutor(new ToggleTPCommand(this));
        
        // Region commands
        getCommand("rgtp").setExecutor(new RegionTPCommand(this));

        getLogger().info("Commands registered successfully!");
    }

    /**
     * Register all event listeners
     */
    private void registerListeners() {
        getLogger().info("Registering listeners...");

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(this), this);

        getLogger().info("Listeners registered successfully!");
    }

    /**
     * Register API for external plugins
     */
    private void registerAPI() {
        api = new DZQuantumTeleportAPI(this);
        getServer().getServicesManager().register(
            DZQuantumTeleportAPI.class,
            api,
            this,
            ServicePriority.Normal
        );
        getLogger().info("API registered successfully!");
    }

    // ===== GETTERS =====

    public static DZQuantumTeleport getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public SmartRTPManager getSmartRTPManager() {
        return smartRTPManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public LimitManager getLimitManager() {
        return limitManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public ParticleManager getParticleManager() {
        return particleManager;
    }

    public TPAManager getTPAManager() {
        return tpaManager;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public DZEconomyAPI getDZEconomyAPI() {
        return dzEconomyAPI;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public boolean isPlaceholderAPIEnabled() {
        return placeholderAPIEnabled;
    }

    public boolean isWorldEditEnabled() {
        return worldEditEnabled;
    }

    public boolean isWorldGuardEnabled() {
        return worldGuardEnabled;
    }

    public boolean isPlayerParticlesEnabled() {
        return playerParticlesEnabled;
    }

    public DZQuantumTeleportAPI getAPI() {
        return api;
    }
}

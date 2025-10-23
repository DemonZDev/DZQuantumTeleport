package online.demonzdevelopment.api;

import online.demonzdevelopment.DZQuantumTeleport;
import online.demonzdevelopment.managers.*;

/**
 * DZQuantumTeleport API
 * Public API for external plugins
 */
public class DZQuantumTeleportAPI {

    private final DZQuantumTeleport plugin;

    public DZQuantumTeleportAPI(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    /**
     * Get Home Manager
     * @return HomeManager instance
     */
    public HomeManager getHomeManager() {
        return plugin.getHomeManager();
    }

    /**
     * Get Warp Manager
     * @return WarpManager instance
     */
    public WarpManager getWarpManager() {
        return plugin.getWarpManager();
    }

    /**
     * Get Teleport Manager
     * @return TeleportManager instance
     */
    public TeleportManager getTeleportManager() {
        return plugin.getTeleportManager();
    }

    /**
     * Get Smart RTP Manager
     * @return SmartRTPManager instance
     */
    public SmartRTPManager getSmartRTPManager() {
        return plugin.getSmartRTPManager();
    }

    /**
     * Get Cooldown Manager
     * @return CooldownManager instance
     */
    public CooldownManager getCooldownManager() {
        return plugin.getCooldownManager();
    }

    /**
     * Get Limit Manager
     * @return LimitManager instance
     */
    public LimitManager getLimitManager() {
        return plugin.getLimitManager();
    }

    /**
     * Get Economy Manager
     * @return EconomyManager instance
     */
    public EconomyManager getEconomyManager() {
        return plugin.getEconomyManager();
    }

    /**
     * Get Particle Manager
     * @return ParticleManager instance
     */
    public ParticleManager getParticleManager() {
        return plugin.getParticleManager();
    }

    /**
     * Get TPA Manager
     * @return TPAManager instance
     */
    public TPAManager getTPAManager() {
        return plugin.getTPAManager();
    }

    /**
     * Get Region Manager
     * @return RegionManager instance
     */
    public RegionManager getRegionManager() {
        return plugin.getRegionManager();
    }
}

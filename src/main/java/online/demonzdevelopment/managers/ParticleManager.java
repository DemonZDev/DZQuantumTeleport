package online.demonzdevelopment.managers;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * Particle Manager
 * Handles particle effects for teleportation
 */
public class ParticleManager {

    private final DZQuantumTeleport plugin;

    public ParticleManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
    }

    /**
     * Play departure particles
     */
    public void playDepartureEffect(Player player) {
        if (!plugin.getConfigManager().areParticlesEnabled()) {
            return;
        }
        
        player.spawnParticle(Particle.PORTAL, player.getLocation(), 50, 0.5, 1, 0.5, 0.1);
    }

    /**
     * Play arrival particles
     */
    public void playArrivalEffect(Player player) {
        if (!plugin.getConfigManager().areParticlesEnabled()) {
            return;
        }
        
        player.spawnParticle(Particle.PORTAL, player.getLocation(), 50, 0.5, 1, 0.5, 0.1);
    }

    /**
     * Play warmup particles
     */
    public void playWarmupEffect(Player player) {
        if (!plugin.getConfigManager().areParticlesEnabled()) {
            return;
        }
        
        player.spawnParticle(Particle.ENCHANT, player.getLocation(), 10, 0.5, 1, 0.5);
    }
}

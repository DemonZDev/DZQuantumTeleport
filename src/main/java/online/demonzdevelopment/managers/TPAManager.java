package online.demonzdevelopment.managers;

import online.demonzdevelopment.DZQuantumTeleport;
import online.demonzdevelopment.models.TPARequest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TPA Manager
 * Handles teleport requests between players
 */
public class TPAManager {

    private final DZQuantumTeleport plugin;
    private final Map<UUID, List<TPARequest>> pendingRequests;
    private final Map<UUID, Boolean> toggleStates;

    public TPAManager(DZQuantumTeleport plugin) {
        this.plugin = plugin;
        this.pendingRequests = new ConcurrentHashMap<>();
        this.toggleStates = new ConcurrentHashMap<>();
    }

    /**
     * Send teleport request
     */
    public boolean sendRequest(UUID requester, UUID target, TPARequest.TPAType type) {
        int expireTime = 60; // seconds
        TPARequest request = new TPARequest(requester, target, type, expireTime);
        
        pendingRequests.computeIfAbsent(target, k -> new ArrayList<>()).add(request);
        
        // Schedule expiration
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            removeRequest(target, requester);
        }, expireTime * 20L);
        
        return true;
    }

    /**
     * Get pending requests for player
     */
    public List<TPARequest> getPendingRequests(UUID target) {
        List<TPARequest> requests = pendingRequests.getOrDefault(target, new ArrayList<>());
        // Remove expired
        requests.removeIf(TPARequest::isExpired);
        return requests;
    }

    /**
     * Remove request
     */
    public void removeRequest(UUID target, UUID requester) {
        List<TPARequest> requests = pendingRequests.get(target);
        if (requests != null) {
            requests.removeIf(r -> r.getRequester().equals(requester));
        }
    }

    /**
     * Get toggle state
     */
    public boolean isAcceptingRequests(UUID uuid) {
        return toggleStates.getOrDefault(uuid, true);
    }

    /**
     * Set toggle state
     */
    public void setToggleState(UUID uuid, boolean enabled) {
        toggleStates.put(uuid, enabled);
        plugin.getDatabaseManager().setTPToggleState(uuid, enabled);
    }
}

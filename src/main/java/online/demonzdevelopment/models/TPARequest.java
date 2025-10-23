package online.demonzdevelopment.models;

import java.util.UUID;

/**
 * TPA Request model
 */
public class TPARequest {
    
    private final UUID requester;
    private final UUID target;
    private final TPAType type;
    private final long timestamp;
    private final long expiresAt;

    public TPARequest(UUID requester, UUID target, TPAType type, int expireSeconds) {
        this.requester = requester;
        this.target = target;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
        this.expiresAt = timestamp + (expireSeconds * 1000L);
    }

    public UUID getRequester() {
        return requester;
    }

    public UUID getTarget() {
        return target;
    }

    public TPAType getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }

    public long getTimeRemaining() {
        long remaining = expiresAt - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }

    /**
     * TPA type enum
     */
    public enum TPAType {
        TPA,  // Requester wants to teleport to target
        CALL  // Requester wants target to teleport to them
    }

    @Override
    public String toString() {
        return "TPARequest{" +
                "requester=" + requester +
                ", target=" + target +
                ", type=" + type +
                ", expires=" + getTimeRemaining() + "s" +
                '}';
    }
}

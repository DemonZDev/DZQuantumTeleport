package online.demonzdevelopment.models;

/**
 * RTP Tier model
 */
public class RTPTier {
    
    private final int tierNumber;
    private final String displayName;
    private final int usesRequired;
    private final int minDistance;
    private final int maxDistance;
    private final double costMultiplier;

    public RTPTier(int tierNumber, String displayName, int usesRequired, 
                   int minDistance, int maxDistance, double costMultiplier) {
        this.tierNumber = tierNumber;
        this.displayName = displayName;
        this.usesRequired = usesRequired;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.costMultiplier = costMultiplier;
    }

    public int getTierNumber() {
        return tierNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getUsesRequired() {
        return usesRequired;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public double getCostMultiplier() {
        return costMultiplier;
    }

    @Override
    public String toString() {
        return "RTPTier{" +
                "tier=" + tierNumber +
                ", displayName='" + displayName + '\'' +
                ", usesRequired=" + usesRequired +
                ", distance=" + minDistance + "-" + maxDistance +
                ", multiplier=" + costMultiplier +
                '}';
    }
}

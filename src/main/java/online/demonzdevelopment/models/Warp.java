package online.demonzdevelopment.models;

import org.bukkit.Location;

/**
 * Warp data model
 */
public class Warp {
    
    private final String name;
    private Location location;
    private WarpVisibility visibility;
    private final long createdAt;
    private long lastUsed;
    private int usageCount;

    public Warp(String name, Location location, WarpVisibility visibility) {
        this.name = name;
        this.location = location;
        this.visibility = visibility;
        this.createdAt = System.currentTimeMillis();
        this.lastUsed = 0;
        this.usageCount = 0;
    }

    public Warp(String name, Location location, WarpVisibility visibility, long createdAt, long lastUsed, int usageCount) {
        this.name = name;
        this.location = location;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.lastUsed = lastUsed;
        this.usageCount = usageCount;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public WarpVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(WarpVisibility visibility) {
        this.visibility = visibility;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public void updateLastUsed() {
        this.lastUsed = System.currentTimeMillis();
        this.usageCount++;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void incrementUsageCount() {
        this.usageCount++;
    }

    public boolean isPublic() {
        return visibility == WarpVisibility.PUBLIC;
    }

    public boolean isPrivate() {
        return visibility == WarpVisibility.PRIVATE;
    }

    /**
     * Warp visibility enum
     */
    public enum WarpVisibility {
        PUBLIC,
        PRIVATE
    }

    @Override
    public String toString() {
        return "Warp{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", visibility=" + visibility +
                '}';
    }
}

package online.demonzdevelopment.models;

import org.bukkit.Location;

import java.util.UUID;

/**
 * Home data model
 */
public class Home {
    
    private final UUID owner;
    private final String name;
    private Location location;
    private final long createdAt;
    private long lastUsed;

    public Home(UUID owner, String name, Location location) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.createdAt = System.currentTimeMillis();
        this.lastUsed = 0;
    }

    public Home(UUID owner, String name, Location location, long createdAt, long lastUsed) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.createdAt = createdAt;
        this.lastUsed = lastUsed;
    }

    public UUID getOwner() {
        return owner;
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
    }

    @Override
    public String toString() {
        return "Home{" +
                "owner=" + owner +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}

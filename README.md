# DZQuantumTeleport

**Professional PaperMC 1.21.1 Multi-Currency Teleportation Plugin**

## 🌟 Features

### Core Systems
- **Multi-Currency Economy** - Costs in Money, MobCoins, Gems, XP Levels, and Items
- **Smart RTP Progressive System** - 6-tier distance progression based on usage
- **Comprehensive Rank System** - Per-command configuration with LuckPerms integration
- **Three Storage Options** - FlatFile (YAML), SQLite, MySQL with HikariCP
- **Advanced Warmup System** - Configurable delays with movement/damage cancellation
- **Full Safety Checks** - Suffocation, fall damage, lava, fire, water, cave detection
- **Professional API** - Complete developer API with custom events

### Commands (50+ Total)

#### Home System (9 commands)
- `/sethome <name>` - Create home at current location
- `/home <name>` - Teleport to home
- `/listhome` - List all homes with coordinates
- `/delhome <name>` - Delete home
- `/renmhome <old> <new>` - Rename home
- `/movhome <name>` - Move home to current location
- `/home gui` - Interactive GUI for homes

#### Warp System (8 commands)
- `/setwarp <name> <public|private>` - Create warp (admin)
- `/<warp_name>` or `/tp <warp_name>` - Quick warp
- `/listwarp` - List all accessible warps
- `/delwarp <name>` - Delete warp (admin)
- `/renmwarp <old> <new>` - Rename warp (admin)
- `/cvwarp <name> <visibility>` - Change visibility (admin)
- `/movwarp <name>` - Move warp location (admin)
- `/warp gui` - Interactive GUI for warps

#### Player Teleport (7 commands)
- `/tpa <player>` - Request to teleport to player
- `/call <player>` - Request player teleport to you
- `/bring <player>` - Force teleport player (admin)
- `/dzqtp meet <player1> <player2>...` - Summon multiple players (admin)
- `/tp accept` - Accept teleport request
- `/tp deny` - Deny teleport request
- `/toggletp <on|off>` - Toggle receiving requests

#### Navigation
- `/spawn [world]` - Teleport to spawn
- `/tp back` - Return to previous location
- `/rtp [world]` - Smart random teleport with progressive distance

#### Region Teleport (5 commands)
- `/rgtp list` - List all regions
- `/rgtp set <name>` - Create region from WorldEdit selection (admin)
- `/rgtp rankset <region> <rank>` - Set allowed ranks (admin)
- `/rgtp permset <region> <perm>` - Configure permissions (admin)
- `/rgtp gui` - Region management GUI (admin)

#### Administration
- `/dzqtp reload` - Reload all configurations
- `/setspawn <world>` - Set spawn point (admin)
- `/dzqtp set dimension_spawn` - Set first-join spawn (admin)
- `/dzqtp migrate <type>` - Migrate storage type (admin)

## 📋 Requirements

### Required Dependencies
- **PaperMC 1.21.1** (NOT SpigotMC)
- **DZEconomy** - Multi-currency economy system
- **LuckPerms** - Permission and rank management
- **Java 21** - Latest Java version

### Optional Dependencies
- **PlaceholderAPI** - Custom placeholders support
- **WorldEdit** - Region selection for region teleport
- **WorldGuard** - Region protection respect
- **PlayerParticles** - Advanced particle effects

## 🚀 Installation

1. **Install Required Plugins**
   - Download and install DZEconomy
   - Download and install LuckPerms
   - Install optional dependencies if desired

2. **Install DZQuantumTeleport**
   - Download `DZQuantumTeleport-1.0.0.jar`
   - Place in your `plugins/` folder
   - Restart server

3. **Configure**
   - Edit `config.yml` for general settings
   - Edit `ranks.yml` for rank-based permissions
   - Edit `messages.yml` for custom messages

4. **Setup Ranks**
   - Assign permissions: `dzqtp.rank.<rank>`
   - Example: `/lp user <player> permission set dzqtp.rank.vip`
   - Configure costs and limits in `ranks.yml`

## ⚙️ Configuration

### Storage Options

**FlatFile (Default)** - YAML files
```yaml
storage:
  type: flatfile
  flatfile:
    path: "plugins/DZQuantumTeleport/data/"
    auto-save-interval: 300
    backup-enabled: true
```

**SQLite** - Embedded database
```yaml
storage:
  type: sqlite
  sqlite:
    file: "plugins/DZQuantumTeleport/data/dzqtp_data.db"
    journal-mode: WAL
```

**MySQL** - External database with connection pooling
```yaml
storage:
  type: mysql
  mysql:
    host: "localhost"
    port: 3306
    database: "dzqtp"
    username: "root"
    password: "password"
    pool:
      max-connections: 10
```

### Smart RTP Tiers

| Tier | Uses Required | Distance Range | Cost Multiplier | Display Name |
|------|--------------|----------------|-----------------|--------------|
| 1 | 0 | 10-100 blocks | 1.0x | Beginner Explorer |
| 2 | 10 | 100-500 blocks | 1.2x | Explorer |
| 3 | 25 | 500-1000 blocks | 1.5x | Adventurer |
| 4 | 50 | 1000-2500 blocks | 2.0x | Wanderer |
| 5 | 100 | 2500-5000 blocks | 2.5x | Voyager |
| 6 | 200+ | 5000-10000 blocks | 3.0x | Master Explorer |

### Rank System Example

```yaml
ranks:
  default:
    display-name: "&7Default"
    bypass-all-limits: false
    bypass-all-costs: false
    
    sethome:
      enabled: true
      max-homes: 3
      cooldown: 300  # 5 minutes
      daily-limit: 10
      costs:
        money: 500
        gem: 10
        items:
          - material: ENDER_PEARL
            amount: 2
```

## 🎮 Permissions

### Player Permissions
- `dzqtp.command.tp` - Use main teleport commands
- `dzqtp.command.home` - Use home system
- `dzqtp.command.warp` - Use warp system
- `dzqtp.command.rtp` - Use random teleport
- `dzqtp.command.tpa` - Use player teleport requests
- `dzqtp.command.spawn` - Use spawn teleport
- `dzqtp.command.back` - Use back command

### Admin Permissions
- `dzqtp.admin` - All admin commands
- `dzqtp.admin.setspawn` - Set spawn points
- `dzqtp.admin.setwarp` - Create warps
- `dzqtp.admin.bring` - Force teleport players
- `dzqtp.admin.reload` - Reload configurations
- `dzqtp.admin.migrate` - Migrate storage type

### Bypass Permissions
- `dzqtp.bypass.cooldown` - Bypass all cooldowns
- `dzqtp.bypass.limit` - Bypass usage limits
- `dzqtp.bypass.cost` - Bypass all costs
- `dzqtp.bypass.warmup` - Bypass warmup delays
- `dzqtp.bypass.world` - Bypass world restrictions

### Rank Permissions
- `dzqtp.rank.default` - Default rank (auto-assigned)
- `dzqtp.rank.vip` - VIP rank
- `dzqtp.rank.mvp` - MVP rank
- `dzqtp.rank.elite` - Elite rank
- `dzqtp.rank.admin` - Admin rank (bypasses all)

## 📊 PlaceholderAPI Support

### Available Placeholders
- `%dzqtp_homes%` - Number of homes player has
- `%dzqtp_max_homes%` - Maximum homes allowed
- `%dzqtp_rank%` - Player's current rank
- `%dzqtp_rtp_tier%` - Current RTP tier name
- `%dzqtp_rtp_uses%` - Total RTP uses
- `%dzqtp_cooldown_<command>%` - Remaining cooldown for command

## 🔧 Developer API

### Maven Dependency
```xml
<repository>
    <id>demonz-repo</id>
    <url>https://repo.demonzdevelopment.online/</url>
</repository>

<dependency>
    <groupId>online.demonzdevelopment</groupId>
    <artifactId>DZQuantumTeleport</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

### API Usage Example
```java
// Get API instance
DZQuantumTeleportAPI api = Bukkit.getServicesManager()
    .getRegistration(DZQuantumTeleportAPI.class)
    .getProvider();

// Teleport player to home
api.getHomeManager()
    .teleportToHome(player.getUniqueId(), "home1")
    .thenAccept(result -> {
        if (result.isSuccess()) {
            player.sendMessage("Teleported successfully!");
        }
    });

// Get player's RTP tier
RTPTier tier = api.getSmartRTPManager().getPlayerTier(player.getUniqueId());
player.sendMessage("Your tier: " + tier.getDisplayName());
```

### Custom Events
```java
// Listen to teleport events
@EventHandler
public void onTeleport(TeleportCompleteEvent event) {
    Player player = event.getPlayer();
    Location from = event.getFrom();
    Location to = event.getTo();
    // Custom logic
}

// Listen to RTP events
@EventHandler
public void onRTP(SmartRTPCompleteEvent event) {
    RTPTier tier = event.getTier();
    int distance = event.getDistance();
    // Custom logic
}
```

## 🎨 Features Breakdown

### Multi-Currency Economy
- **Money** - Primary economy currency ($)
- **MobCoin** - Earned from killing mobs (MC)
- **Gem** - Premium rare currency (◆)
- **XP Levels** - Minecraft experience levels
- **Items** - Physical items from inventory

Each command can require any combination of currencies. Costs are configurable per rank and can have multipliers (e.g., RTP tier multipliers).

### Comprehensive Limit System
- **Hourly Limits** - Reset every hour
- **Daily Limits** - Reset at midnight (configurable timezone)
- **Weekly Limits** - Reset on Monday (configurable day)
- **Monthly Limits** - Reset on 1st of month
- Persistent across server restarts
- Bypass permissions available
- Usage tracking and statistics

### Advanced Warmup System
- Configurable delay (default 3 seconds)
- Cancel on movement (with tolerance)
- Cancel on damage
- Cancel on combat
- Cancel on interaction
- Particle effects during warmup
- Sound effects
- Countdown display

### Safety Checks
- **Suffocation** - 2 blocks of air above
- **Fall Damage** - Solid ground beneath
- **Lava** - 3x3 area scan
- **Fire** - Fire and soul fire detection
- **Water Safety** - Max 2 blocks deep, shore within 10 blocks
- **Cave Detection** - Light level check, sky access required
- **Void Detection** - Prevent teleporting into void
- **World Border** - Respect borders with buffer

### Smart RTP Progressive System
- 6-tier progression based on total uses
- Automatic distance calculation
- Cost multipliers per tier
- Tier upgrade notifications
- History tracking (last 100 locations)
- Biome restrictions (no oceans)
- Chunk pre-generation
- Safe location caching (5 minutes)
- Maximum 15 attempts to find safe spot

### Particle Effects
- **Built-in Effects** - Portal, enchantment, hearts, beams
- **PlayerParticles Integration** - Advanced effects if available
- **Rank-based Particles** - Different effects per rank
- **Teleport Trails** - Particle trails during teleport
- **Animations** - Spiral, explosion, orbit, quantum effects
- Player toggle option

## 📝 Configuration Examples

### Rank Progression
```yaml
ranks:
  default:
    sethome:
      max-homes: 3
      cooldown: 300
      costs:
        money: 500
        gem: 10
  
  vip:
    sethome:
      max-homes: 5
      cooldown: 240
      costs:
        money: 400
        gem: 8
  
  mvp:
    sethome:
      max-homes: 8
      cooldown: 180
      costs:
        money: 300
        gem: 5
  
  admin:
    sethome:
      max-homes: -1  # Unlimited
      cooldown: 0  # No cooldown
      costs:
        money: 0  # Free
```

### World Restrictions
```yaml
ranks:
  default:
    home:
      allowed-worlds:
        - "world"
      world-restricted: true
  
  vip:
    home:
      allowed-worlds:
        - "world"
        - "world_nether"
      world-restricted: true
  
  admin:
    home:
      world-restricted: false  # All worlds
```

## 🐛 Troubleshooting

### Plugin Won't Enable
- Check you have DZEconomy installed
- Check you have LuckPerms installed
- Check you're using PaperMC 1.21.1 (NOT Spigot)
- Check Java version is 21

### Database Errors
- For MySQL: Verify connection details in config.yml
- For SQLite: Ensure data folder has write permissions
- For FlatFile: Check disk space and permissions

### Economy Not Working
- Verify DZEconomy is enabled
- Check console for economy hook messages
- Test with `/dzqtp reload`

### Ranks Not Detecting
- Verify LuckPerms permissions: `dzqtp.rank.<rank>`
- Check rank priority order in config.yml
- Clear rank cache: `/dzqtp reload`

## 📦 File Structure

```
DZQuantumTeleport/
├── config.yml           # Main configuration
├── ranks.yml            # Rank-based settings
├── messages.yml         # All plugin messages
├── data/               # Storage files (if FlatFile)
│   ├── homes.yml
│   ├── warps.yml
│   ├── spawns.yml
│   ├── back-locations.yml
│   ├── rtp-stats.yml
│   ├── cooldowns.yml
│   ├── limits.yml
│   └── toggles.yml
└── logs/               # Plugin logs
```

## 🔄 Migration

To migrate between storage types:
```
/dzqtp migrate <flatfile|sqlite|mysql>
```

This command will:
1. Create backup of current data
2. Transfer all data to new storage
3. Verify data integrity
4. Switch to new storage type

## 💡 Tips & Best Practices

1. **Start with FlatFile** - Easy to configure and debug
2. **Use MySQL for Large Servers** - Better performance with 100+ players
3. **Configure Ranks Carefully** - Balance costs and limits
4. **Test Safety Checks** - Adjust RTP settings for your worlds
5. **Monitor Logs** - Check logs/dzqtp.log for issues
6. **Backup Regularly** - Especially before migrating storage
7. **Use Bypass Permissions Wisely** - Only for trusted staff

## 📞 Support

- **Discord**: https://discord.gg/demonzdev
- **Website**: https://demonzdevelopment.online
- **GitHub**: https://github.com/DemonzDevelopment/DZQuantumTeleport

## 📜 License

Copyright © 2024 DemonzDevelopment  
All rights reserved.

## 🙏 Credits

- **Author**: GodHyena
- **Organization**: DemonzDevelopment
- **Dependencies**: DZEconomy, LuckPerms, PaperMC

---

**Version**: 1.0.0  
**Last Updated**: 2024  
**Minecraft Version**: 1.21.1 (Paper)  
**API Version**: 1.21

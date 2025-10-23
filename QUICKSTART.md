# Quick Start Guide - DZQuantumTeleport

## 🚀 Getting Started in 5 Minutes

### Step 1: Prerequisites
Ensure you have:
- PaperMC 1.21.1 server
- DZEconomy plugin installed
- LuckPerms plugin installed
- Java 21

### Step 2: Installation
1. Download `DZQuantumTeleport-1.0.0.jar`
2. Place in `plugins/` folder
3. Restart server
4. Plugin will generate config files automatically

### Step 3: First Configuration
The plugin works out of the box with default settings. But you can customize:

**config.yml** - Main settings
```yaml
storage:
  type: flatfile  # Start with flatfile
```

**ranks.yml** - Already configured for 5 ranks
- No changes needed for basic operation

**messages.yml** - All messages pre-configured
- Customize colors and text as desired

### Step 4: Setup Permissions
Assign ranks to players:
```bash
# Default rank (auto-assigned to all players)
/lp user <player> permission set dzqtp.rank.default

# VIP rank
/lp user <player> permission set dzqtp.rank.vip

# MVP rank
/lp user <player> permission set dzqtp.rank.mvp
```

### Step 5: Test Basic Commands

**Set a home:**
```
/sethome myhouse
```

**Teleport to home:**
```
/home myhouse
```

**Set spawn:**
```
/setspawn world
```

**Teleport to spawn:**
```
/spawn
```

**Create a warp (admin):**
```
/setwarp shop public
```

**Random teleport:**
```
/rtp
```

## 📊 Default Rank Configuration

### Default Rank Includes:
- **Max Homes**: 3
- **Home Creation Cost**: $500, 10 Gems, 2 Ender Pearls
- **Home Teleport Cost**: $50, 5 MobCoins
- **Home Cooldown**: 5 minutes (creation), 1 minute (teleport)
- **Daily Limits**: 10 home creations, 100 home teleports
- **RTP Cost**: $500, 50 MobCoins, 25 Gems, 3 XP, 3 Diamonds, 10 Ender Pearls
- **Allowed Worlds**: "world" only

## 🎮 Essential Commands Cheat Sheet

### Player Commands
```
/sethome <name>     - Create home
/home <name>        - Go to home
/listhome           - List homes
/delhome <name>     - Delete home
/spawn              - Go to spawn
/rtp                - Random teleport
/tpa <player>       - Request teleport to player
/tp back            - Return to previous location
```

### Admin Commands
```
/dzqtp reload          - Reload configurations
/setspawn <world>      - Set spawn point
/setwarp <name> public - Create public warp
/bring <player>        - Force teleport player
```

## ⚙️ Common Configurations

### Adjust Home Limits
Edit `ranks.yml`:
```yaml
ranks:
  vip:
    sethome:
      max-homes: 5  # Change from 3 to 5
```

### Change Cooldowns
```yaml
ranks:
  default:
    home:
      cooldown: 30  # Reduce from 60 to 30 seconds
```

### Adjust Costs
```yaml
ranks:
  default:
    sethome:
      costs:
        money: 250  # Reduce from 500
        gem: 5      # Reduce from 10
```

### Disable World Restrictions
```yaml
ranks:
  default:
    home:
      world-restricted: false  # Allow all worlds
```

### Change Storage Type
```yaml
storage:
  type: mysql  # Change from flatfile
  mysql:
    host: "localhost"
    database: "minecraft"
    username: "root"
    password: "yourpassword"
```

## 🔧 Troubleshooting

### Plugin Won't Load
**Error**: DZEconomy not found
- **Solution**: Install DZEconomy first

**Error**: LuckPerms not found
- **Solution**: Install LuckPerms first

### Economy Not Working
**Issue**: Costs not being charged
- **Check**: DZEconomy is enabled and working
- **Check**: Player has sufficient balance
- **Fix**: `/dzqtp reload`

### Ranks Not Detecting
**Issue**: Players showing default rank
- **Check**: LuckPerms permissions set correctly
- **Check**: Permission format: `dzqtp.rank.vip`
- **Fix**: `/lp user <player> permission set dzqtp.rank.vip`

### Database Errors (MySQL)
**Issue**: Connection failed
- **Check**: MySQL server running
- **Check**: Credentials in config.yml
- **Check**: Database exists
- **Fix**: Create database manually if needed

## 📝 Quick Rank Upgrade Guide

To upgrade a player from default to VIP:

1. **Set Permission:**
   ```
   /lp user PlayerName permission set dzqtp.rank.vip
   ```

2. **Verify:**
   ```
   /lp user PlayerName permission check dzqtp.rank.vip
   ```

3. **Player Benefits:**
   - Max homes increased: 3 → 5
   - Reduced cooldowns
   - Lower costs
   - Access to nether world

## 🎯 Advanced Features

### Smart RTP Tiers
- Players automatically progress through 6 tiers
- Each tier increases teleport distance
- Tier 1: 10-100 blocks
- Tier 6: 5000-10000 blocks
- Progression based on total RTP uses

### Multi-Currency Economy
Commands can cost multiple currencies:
- Money ($)
- MobCoins (MC)
- Gems (◆)
- XP Levels
- Physical Items

### Usage Limits
Built-in limit system:
- Hourly limits
- Daily limits
- Weekly limits
- Monthly limits
- Auto-reset system

## 💡 Tips for Server Owners

1. **Start Simple**: Use default configuration initially
2. **Test Ranks**: Create test accounts for each rank
3. **Monitor Costs**: Adjust based on your economy
4. **Use FlatFile First**: Switch to MySQL when needed
5. **Regular Backups**: Backup data/ folder regularly
6. **Read Logs**: Check console for errors
7. **Gradual Changes**: Don't change everything at once

## 🆘 Getting Help

- Check `README.md` for full documentation
- Review `IMPLEMENTATION.md` for technical details
- Check console logs for error messages
- Verify all dependencies are installed
- Test with `/dzqtp reload` after changes

## 🎊 You're Ready!

Your DZQuantumTeleport plugin is now set up and ready to use. Players can start creating homes, teleporting, and exploring with the progressive RTP system.

**Next Steps:**
1. Create your first homes
2. Set up warps for important locations
3. Configure ranks based on your server economy
4. Adjust limits based on player activity
5. Enable MySQL for better performance (optional)

Happy teleporting! 🚀

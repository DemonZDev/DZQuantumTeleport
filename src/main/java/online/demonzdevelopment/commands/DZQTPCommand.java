package online.demonzdevelopment.commands;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DZQTPCommand implements CommandExecutor {
    private final DZQuantumTeleport plugin;
    public DZQTPCommand(DZQuantumTeleport plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // TODO: Admin commands (reload, migrate, set, bring, meet)
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigManager().reload();
            plugin.getMessageManager().reload();
            plugin.getRankManager().reload();
            sender.sendMessage(plugin.getMessageManager().getMessage("general.reload-success"));
            return true;
        }
        return true;
    }
}

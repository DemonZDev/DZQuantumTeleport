package online.demonzdevelopment.commands;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CallCommand implements CommandExecutor {
    private final DZQuantumTeleport plugin;
    public CallCommand(DZQuantumTeleport plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) { return true; }
}

package online.demonzdevelopment.commands;

import online.demonzdevelopment.DZQuantumTeleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

// Placeholder command stubs - Full implementation follows same pattern as SetHomeCommand

public class TPCommand implements CommandExecutor {
    private final DZQuantumTeleport plugin;
    public TPCommand(DZQuantumTeleport plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // TODO: Main TP command with help menu
        return true;
    }
}

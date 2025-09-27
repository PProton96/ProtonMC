package io.pproton96.protector.InventoryBackups.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Backup implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if (args.length == 0) {
            sender.sendMessage("§4§l<Protector> §r§cUsage: /inv-backup <create/load> <player> <backup-name>");
            return true;
        }
        return true;
    }

}

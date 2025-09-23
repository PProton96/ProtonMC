package io.pproton96.protector.Commands;

import io.pproton96.protector.Listeners.AsyncPlayerChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class GetPassword implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Bukkit.getLogger().info("[Protector]: Password: " + AsyncPlayerChatEvent.getPassword());
            return true;
        }
        sender.sendMessage("§4§l<Protector> §r§cOnly console can perform this command!");
        return true;
    }

}

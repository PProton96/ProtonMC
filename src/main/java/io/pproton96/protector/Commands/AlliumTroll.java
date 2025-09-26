package io.pproton96.protector.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class AlliumTroll implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if (args == null) {
            sender.sendMessage("§4§l<Protector> §r§cUsage: /alliumtroll <player-name>");
            return true;
        }
        Bukkit.getLogger().info("[Protector]: " + Arrays.stream(args).findFirst().orElse("ASS"));


        return true;
    }

}

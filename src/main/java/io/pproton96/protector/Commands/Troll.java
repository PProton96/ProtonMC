package io.pproton96.protector.Commands;

import io.pproton96.protector.Protector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;


public class Troll implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        final List<String> commands = Arrays.asList("allium", "dmgMultiplier", "levitate");
        final int argsCount = args.length;

        if (argsCount < 2 || !commands.contains(args[0])) {
            sender.sendMessage("§4§l<Protector> §r§cUsage: /troll <type> <player>");
            return true;
        }
        if (args[0].equals("allium")) {
            allium(sender, args);
        }


        return true;
    }

    public void allium(CommandSender sender, String args[]) {
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("§4§l<Protector> §r§cPlayer not found!");
            return;
        }
        ItemStack[] playerInventory = player.getInventory().getContents();

        ItemStack[] replaced = playerInventory.clone();
        for (ItemStack item : replaced) {
            item.setType(Material.ALLIUM);
        }
        player.getInventory().setContents(replaced); // Replace the player's inventory with alliums

        if (args[1] != null) { // If a delay is specified
            float delay = 0.25f; // Default delay

            try {
                delay = Float.parseFloat(args[1]);
            } catch (Exception e) {
                sender.sendMessage("§4§l<Protector> §r§cInvalid delay! Delay must be a decimal or whole number in seconds!");
                Bukkit.getLogger().warning("[Protector]: Encountered an error while parsing delay: \n" + e.getMessage());
                return;
            }

            long ticks = (long) (delay * 20); // Convert delay in seconds to ticks
            Bukkit.getScheduler().runTaskLater(Protector.getInstance(), () -> {
                player.getInventory().setContents(playerInventory); // Bring back the original inventory
            }, ticks);

            sender.sendMessage("§4§l<Protector> §r§aYou have replaced " + player.getName() + "'s inventory with allium for " + delay + " seconds!");
            return;
        }


        sender.sendMessage("§4§l<Protector> §r§aYou have replaced " + player.getName() + "'s inventory with allium!");
    }

}

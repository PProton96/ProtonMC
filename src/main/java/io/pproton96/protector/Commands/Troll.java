package io.pproton96.protector.Commands;

import io.pproton96.protector.Protector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;


public class Troll implements CommandExecutor {

    private static Map<Player, Float> dmgMultiplierMap = new HashMap<>();

    public static Map<Player, Float> getDmgMultiplierMap() {
        return dmgMultiplierMap;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if (!sender.hasPermission("protector.troll") && !sender.isOp() && !(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("§4§l<Protector> §r§cYou don't have permission to perform this command!");
            return true;
        }

        final List<String> commands = Arrays.asList("allium", "dmgMultiplier", "levitate");
        final int argsCount = args.length;

        if (argsCount < 2 || !commands.contains(args[0])) {
            sender.sendMessage("§4§l<Protector> §r§cGeneral Usage: /troll <type> <player>" +
                    "\n §lTypes:" +
                    "\n§r§d- allium: /troll allium <player> [delay]; If delay is not specified, player's inventory will be just replaced with allium." +
                    "\n§r§c- dmgMultiplier: /troll dmgMultiplier <player> <multiplier>; Multiplies damage player takes by <multiplier>." +
                    "\n§r§b- levitate: /troll levitate <player>; Shoots player into the cosmos.");
            return true;
        }
        // Switch case for different types of trolling
        switch (args[0]) {
            case "allium":
                allium(sender, args);
            case "dmgMultiplier":
                dmgMultiplier(sender, args);
            case "levitate":
                levitate(sender, args);
        }


        return true;
    }


    // Check if player exists and sender has permission(in this case, op)
    public Player checkPlayer(CommandSender sender, String name, String commandName) {
        if (!sender.isOp() && !(sender instanceof ConsoleCommandSender) && sender.hasPermission("protector.troll." + commandName)) {
            sender.sendMessage("§4§l<Protector> §r§cYou don't have permission to perform this command!");
            return null;
        }

        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            sender.sendMessage("§4§l<Protector> §r§cPlayer not found!");
            return null;
        }
        return player;
    }

    // Replaces player's inventory with alliums
    public void allium(CommandSender sender, String args[]) {
        Player player = checkPlayer(sender, args[0], "allium");
        if (player == null) return;

        ItemStack[] playerInventory = player.getInventory().getContents();
        ItemStack[] replaced = playerInventory.clone(); // Clone the player's inventory

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

    // Multiplies damage player takes by a specified multiplier
    public void dmgMultiplier(CommandSender sender, String args[]) {
        Player player = checkPlayer(sender, args[0], "dmgMultiplier");
        if (player == null) return;

        float multiplier;
        try {
            multiplier = Float.parseFloat(args[1]);
        } catch (Exception e) {
            sender.sendMessage("§4§l<Protector> §r§cInvalid multiplier! Multiplier must be a decimal or whole number!");
            Bukkit.getLogger().warning("[Protector]: Encountered an error while parsing multiplier: \n" + e.getMessage());
            return;
        }

        dmgMultiplierMap.put(player, multiplier);
    }

    // Shoots player into the cosmos
    public void levitate(CommandSender sender, String args[]) {
        Player player = checkPlayer(sender, args[0], "levitate");
        if (player == null) return;

        float duration;
        try {
            duration = Float.parseFloat(args[1]);
        } catch (Exception e) {
            sender.sendMessage("§4§l<Protector> §r§cInvalid duration! Duration must be a decimal or whole number in seconds!");
            Bukkit.getLogger().warning("[Protector]: Encountered an error while parsing duration: \n" + e.getMessage());
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, ((int) Math.ceil(duration*20)), 9));
    }
}

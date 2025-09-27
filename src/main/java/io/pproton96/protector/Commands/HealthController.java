package io.pproton96.protector.Commands;

import io.pproton96.protector.Protector;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.createBossBar;
import static org.bukkit.Bukkit.getServer;

public class HealthController implements CommandExecutor {
    // Player, TargetPlayer format
    private Map<Player, Player> targets = new HashMap<>();
    private BukkitTask hpMonitorTask;
    private final BukkitScheduler scheduler = getServer().getScheduler();

    public void runHpMonitorTask() {
        if (hpMonitorTask == null || hpMonitorTask.isCancelled()) {
            hpMonitorTask = scheduler.runTaskTimer(Protector.getInstance(), () -> {
                for (Map.Entry<Player, Player> entry : targets.entrySet()) {
                    // e is like entry(entry-player, entry-target)
                    Player eplayer = entry.getKey();
                    Player etarget = entry.getValue();

                    /*Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            "title " + eplayer.getName() +
                                    " actionbar " +
                                    "§eTarget's health\n§4" + etarget.getHealth() + "❤"
                    );*/
                    BossBar bar = createBossBar("Target's Health", BarColor.RED, BarStyle.SOLID);
                    bar.addPlayer(eplayer);
                    bar.setProgress(etarget.getHealth() / etarget.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
                }
            }, 0, 5);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if (args.length < 1) {
            sender.sendMessage("""
                    §4§l<Protector> §r§cUsage:
                    
                    - /health set <player> <health>
                    - /health get <player>
                    - /health monitor <player> <on/off>""");
            return true;
        }

        // Get health
        if (args[0].equalsIgnoreCase("get")) {
            // Just some checks
            if (!sender.hasPermission("protector.healthcontroller.get") && !sender.isOp()) {
                sender.sendMessage("§4§l<Protector> §r§cYou don't have permission to perform this command!");
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage("§4§l<Protector> §r§cUsage: /health get <player>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§4§l<Protector> §r§cPlayer not found!");
                return true;
            }

            // Actual command execution
            sender.sendMessage("§4§l<Protector> §r§a" + target.getName() + "'s health is " + target.getHealth() + "!");
            return true;
        }

        // Set health
        if (args[0].equalsIgnoreCase("set")) {
            // Just some checks
            if (!sender.hasPermission("protector.healthcontroller.set") && !sender.isOp()) {
                sender.sendMessage("§4§l<Protector> §r§cYou don't have permission to perform this command!");
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage("§4§l<Protector> §r§cUsage: /health set <player> <health>; (1 Heart = 2 HP)");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§4§l<Protector> §r§cPlayer not found!");
                return true;
            }

            // Actual command execution
            float health;
            try {
                health = Float.parseFloat(args[2]);
            } catch (Exception e) {
                sender.sendMessage("§4§l<Protector> §r§cInvalid health value!");
                return true;
            }

            if (health < 0) {
                sender.sendMessage("§4§l<Protector> §r§cHealth cannot be negative!(You've just killed " + target.getName() + " lol)");
                target.setHealth(0);
                return true;
            } else if (health < target.getAttribute(Attribute.MAX_HEALTH).getBaseValue()) {
                target.removePotionEffect(PotionEffectType.ABSORPTION);
                target.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
                target.setHealth(health);
                return true;
            }

            if (health > 1024.0) {
                float absorptionHealth = health - 1024.0f;
                health = 1024.0f;
                int amplifier = (int) Math.ceil(absorptionHealth / 4.0);
                target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, -1, amplifier));
            }
            target.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
            target.setHealth(health);
            return true;
        }

        // Monitor on/off
        if (args[0].equalsIgnoreCase("monitor")) {
            // Just some checks
            if (!sender.hasPermission("protector.healthcontroller.monitor") && !sender.isOp()) {
                sender.sendMessage("§4§l<Protector> §r§cYou don't have permission to perform this command!");
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage("§4§l<Protector> §r§cUsage: /health monitor <player> <on/off>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§4§l<Protector> §r§cPlayer not found!");
                return true;
            }
            Player player = Bukkit.getPlayer(sender.getName());
            if (sender instanceof ConsoleCommandSender || player == null) {
                sender.sendMessage("§4§l<Protector> §r§cYou must be a player to perform this command!");
                return true;
            }

            // Actual command execution
            if (args[2].equalsIgnoreCase("on")) {
                targets.put(player, target); // Add the player and target to the targets map so the task would send titles for the player
                runHpMonitorTask();
                player.sendMessage("§4§l<Protector> §r§aMonitoring enabled!");
                return true;
            } else if (args[2].equalsIgnoreCase("off")) {
                targets.remove(player); // Remove the player from the targets map so the task wouldn't send titles for the player
                player.sendMessage("§4§l<Protector> §r§aMonitoring disabled!");
                return true;
            }
        }

        return true;
    }


}

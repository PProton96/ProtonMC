package io.pproton96.protector;

import io.pproton96.protector.Commands.Troll;
import io.pproton96.protector.Commands.GetPassword;
import io.pproton96.protector.Listeners.AsyncPlayerChatEvent;
import io.pproton96.protector.Listeners.EntityDamageByEntityEvent;
import io.pproton96.protector.Listeners.LockEvents;
import io.pproton96.protector.Listeners.PlayerJoinEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Protector extends JavaPlugin {

    private static Protector instance;

    public static Set<String> lockList = ConcurrentHashMap.newKeySet();

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        getLogger().info("Plugin has been enabled!");

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        BukkitScheduler scheduler = getServer().getScheduler();

        // Damage prevention for non-authorized players.
        scheduler.runTaskTimer(this, () -> {
            final Set<String> snapshot = new HashSet<>(lockList);

            for (String s : snapshot) {
                Player player = Bukkit.getPlayer(s);
                if (player == null) continue;

                player.setAllowFlight(true);
                PotionEffect eff = new PotionEffect(PotionEffectType.RESISTANCE, 40, 255, true, false);
                player.addPotionEffect(eff);
            }
        }, 0L, 30L);


        pluginManager.registerEvents(new PlayerJoinEvent(), this); // Player join event.
        pluginManager.registerEvents(new AsyncPlayerChatEvent(), this); // Async player chat event.
        pluginManager.registerEvents(new LockEvents(), this); // Locks events for non-authorized players.
        pluginManager.registerEvents(new EntityDamageByEntityEvent(), this); // Entity damage by entity event.

        getCommand("getpassword").setExecutor(new GetPassword()); // Command to obtain password (Works only for console).
        getCommand("troll").setExecutor(new Troll()); // Trolling commands.
    }

    @Override
    public void onDisable() {
        instance = null;
        // Plugin shutdown logic
        getLogger().info("Plugin has been disabled!");
    }

    public static Protector getInstance() {
        return instance;
    }

}

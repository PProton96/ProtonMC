package io.pproton96.protector;

import io.pproton96.protector.Commands.AlliumTroll;
import io.pproton96.protector.Commands.GetPassword;
import io.pproton96.protector.Listeners.AsyncPlayerChatEvent;
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

public final class Protector extends JavaPlugin {

    public static Set<String> lockList = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin has been enabled!");

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.runTaskTimer(this, () -> {
            lockList.forEach(name -> {
                Player player = Bukkit.getPlayer(name);
                if (player != null) {
                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 25, 255, true, false);
                    player.addPotionEffect(potionEffect);
                }
            });
        }, 0L, 20L);

        pluginManager.registerEvents(new PlayerJoinEvent(), this);
        pluginManager.registerEvents(new AsyncPlayerChatEvent(), this);
        pluginManager.registerEvents(new LockEvents(), this);

        getCommand("getpassword").setExecutor(new GetPassword());
        getCommand("alliumtroll").setExecutor(new AlliumTroll());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin has been disabled!");
    }
}

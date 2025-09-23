package io.pproton96.protector.Listeners;

import io.pproton96.protector.Protector;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {

        Protector.lockList.add(event.getPlayer().getName());
        event.getPlayer().setAllowFlight(true);
        event.getPlayer().sendMessage("§e<Protector> Type password in chat to login!");

        Bukkit.getLogger().info("[Protector]: Player " + event.getPlayer().getName() + " has joined the server!");
    }

}

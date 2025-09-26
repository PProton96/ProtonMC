package io.pproton96.protector.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static io.pproton96.protector.Protector.lockList;

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        lockList.remove(event.getPlayer().getName());
    }

}

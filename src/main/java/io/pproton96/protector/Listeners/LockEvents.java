package io.pproton96.protector.Listeners;

import io.pproton96.protector.Protector;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LockEvents implements Listener {

    public void cancelEvent(Player player, Cancellable event) {
        if (Protector.lockList.contains(player.getName())) {
            event.setCancelled(true);
        }
    }

    public void cancelEntityEvent(Entity entity, Cancellable event) {
        if (Protector.lockList.contains(entity.getName())) {
            event.setCancelled(true);
        }
    }

    public void sendTitle(Player player) {
        if (Protector.lockList.contains(player.getName())) {
            player.sendTitle("§4<AUTHORIZATION>", "§cType password in chat!", 0, 60, 10);
        }
    }


    @EventHandler
    public void onPlayerDropItem(org.bukkit.event.player.PlayerDropItemEvent event) {
        cancelEvent(event.getPlayer(), event);
        sendTitle(event.getPlayer());
    }

    @EventHandler
    public void onPlayerPickupItem(org.bukkit.event.player.PlayerPickupItemEvent event) {
        cancelEvent(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlayerInteractEntity(org.bukkit.event.player.PlayerInteractEntityEvent event) {
        cancelEvent(event.getPlayer(), event);
        sendTitle(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        cancelEvent(event.getPlayer(), event);
        sendTitle(event.getPlayer());
    }

    @EventHandler
    public void onPlayerAttemptCommand(org.bukkit.event.player.PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        String cmd = message.split(" ")[0];
        if (message.startsWith("/say")) return;
        // Prevent players(op players especially) from stopping or restarting the server.
        if (cmd.equals("/stop") || cmd.equals("/restart")) event.setCancelled(true);
        cancelEvent(event.getPlayer(), event);
        sendTitle(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        cancelEvent(event.getPlayer(), event);
        sendTitle(event.getPlayer());
    }

    // Entity Events
    @EventHandler
    public void onPlayerTakeDmg(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER || !Protector.lockList.contains(event.getEntity().getName())) {
            return;
        }
        cancelEntityEvent(event.getEntity(), event);
    }

    // Inventory Events
    @EventHandler
    public void onInventoryAction(org.bukkit.event.inventory.InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        cancelEvent(player, event);
        sendTitle(player);
    }

    @EventHandler
    public void onItemSwap(org.bukkit.event.player.PlayerSwapHandItemsEvent event) {
        cancelEvent(event.getPlayer(), event);
        sendTitle(event.getPlayer());
    }

}

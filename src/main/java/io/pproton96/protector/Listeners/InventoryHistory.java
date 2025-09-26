package io.pproton96.protector.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public class InventoryHistory implements Listener {

    @EventHandler
    public void onPlayerItemPickUp(org.bukkit.event.entity.EntityPickupItemEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            //TODO? Is this even necessary?


        }
        return;
    }


}

class History {
    private LinkedList<ItemStack[]> history;
    private int maxHistorySize = 256;
}

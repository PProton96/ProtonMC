package io.pproton96.protector.Listeners;

import io.pproton96.protector.Commands.Troll;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class EntityDamageByEntityEvent implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(org.bukkit.event.entity.EntityDamageByEntityEvent event) {

        if (event.getEntityType() != EntityType.PLAYER) return;

        Map<Player, Float> dmgMultiplierMap = Troll.getDmgMultiplierMap();

        Player player = (Player) event.getEntity();
        if (dmgMultiplierMap.containsKey(player)) {
            event.setDamage(event.getDamage()*dmgMultiplierMap.get(player));
        }
    }

}

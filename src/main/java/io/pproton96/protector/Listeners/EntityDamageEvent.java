package io.pproton96.protector.Listeners;

import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class EntityDamageEvent implements Listener {
    public static Map<Player, Float> fallDamageMultiplierMap = new HashMap<>();

    @EventHandler
    public void onEntityDamage(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (!fallDamageMultiplierMap.containsKey(player)) return;

        if (event.getDamageSource().getDamageType() == DamageType.FALL) {
            event.setDamage(event.getDamage() * fallDamageMultiplierMap.get(player));
        }
    }

}

package io.pproton96.protector.Listeners;

import io.pproton96.protector.Protector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class AsyncPlayerChatEvent implements Listener {

    public static String getPassword() {
        return password;
    }

    private static String createKey() {
        StringBuilder passwordBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            passwordBuilder.append(random.nextInt(10));
        }
        Bukkit.getLogger().info("[Protector]: Password: " + passwordBuilder.toString());
        return passwordBuilder.toString();
    }
    private static final String password = createKey();

    @EventHandler
    public void onAsyncPlayerChat(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        String text = event.getMessage();

        if (text.equals(password)) {
            Player player = event.getPlayer();
            event.setCancelled(true);
            player.sendMessage("§4§l<Protector> §r§aYou have logged in!");

            Protector.lockList.remove(player.getName());

            player.getActivePotionEffects().stream()
                    .filter(potion -> (potion.getType() == PotionEffectType.RESISTANCE && potion.getAmplifier() == 255))
                    .findFirst()
                    .ifPresent(potion -> event.getPlayer().removePotionEffect(potion.getType()));

            player.setAllowFlight(false);

            return;
        }

        if (Protector.lockList.contains(event.getPlayer().getName()) && !(text.charAt(0) == '/')) {
            event.getPlayer().sendMessage("§4§l<Protector> §r§cType password in chat to login!");
            event.setCancelled(true);
        }

    }

}

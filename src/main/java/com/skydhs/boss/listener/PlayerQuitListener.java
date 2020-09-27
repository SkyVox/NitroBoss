package com.skydhs.boss.listener;

import com.skydhs.boss.boss.PlayerBoss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    public PlayerQuitListener() {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerBoss playerBoss = PlayerBoss.getPlayerBoss(player);

        if (playerBoss != null) {
            if (!playerBoss.isDied()) {
                playerBoss.dieAndSendSpawnEgg(null);
            }
        }
    }
}
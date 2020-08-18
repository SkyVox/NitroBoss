package com.skydhs.boss.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GeneralListener implements Listener {

    public GeneralListener() {
    }

    @EventHandler
    public void s(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.DIAMOND_BLOCK)) {
            Location l = event.getPlayer().getLocation();
//            new EntityBoss(event.getPlayer().getWorld()).spawnNMSBoss(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
    }
}
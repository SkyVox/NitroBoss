package com.skydhs.boss.listener;

import com.skydhs.boss.BossSettings;
import com.skydhs.boss.FileUtil;
import com.skydhs.boss.boss.EntityBoss;
import com.skydhs.boss.boss.PlayerBoss;
import com.skydhs.boss.manager.CustomSwordSlayer;
import com.skydhs.boss.manager.EntityManager;
import com.skydhs.boss.utils.nbt.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.skydhs.boss.manager.EntityManager.getInstance;

public class GeneralListener implements Listener {

    public GeneralListener() {
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item == null || item.getType().equals(Material.AIR)) return;

            EntityBoss boss = getInstance().getBoss(item);
            if (boss == null) return;

            Player player = event.getPlayer();
            if (PlayerBoss.getPlayerBoss(player) != null) {
                player.sendMessage(FileUtil.get().getString("Messages.has-active-boss").asString());
            } else {
                Location location = event.getClickedBlock() == null ? player.getLocation() : event.getClickedBlock().getLocation().clone().add(0.5D, 1D, 0.5D);
                new PlayerBoss(player, boss).spawnNMSBoss(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                player.sendMessage(FileUtil.get().getString("Messages.boss-spawned").asString());
                takeItem(player, NBTItem.from(item));
            }

            // Cancel this event, so the player will not be able to place the boss item on floor.
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item == null || item.getType().equals(Material.AIR)) return;

        EntityBoss boss = getInstance().getBoss(item);

        if (boss != null) {
            event.setCancelled(true);
        }
    }

    private void takeItem(Player player, NBTItem nbti) {
        final ItemStack item = nbti.getItem();
        final String id = nbti.getString(BossSettings.BOSS_SPAWN_EGG_NBT);

        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack items = player.getInventory().getContents()[i];
            if (items == null || !items.getType().equals(item.getType())) continue;
            NBTItem targetItemNbti = NBTItem.from(items);
            if (targetItemNbti.hasKey(BossSettings.BOSS_SPAWN_EGG_NBT)) {
                if (StringUtils.equalsIgnoreCase(id, targetItemNbti.getString(BossSettings.BOSS_SPAWN_EGG_NBT))) {
                    if (items.getAmount() <= 1) {
                        player.getInventory().setItem(i, new ItemStack(Material.AIR));
                    } else {
                        items.setAmount(items.getAmount() - 1);
                    }
                    player.updateInventory();
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        ArmorStand entity = event.getRightClicked();
        if (EntityManager.getInstance().isBoss(entity)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() == null || event.getDamager() == null) return;
        if (!(event.getEntity() instanceof ArmorStand)) return;
        if (event.getEntity() instanceof Player) return;
        ArmorStand entity = (ArmorStand) event.getEntity();
        if (entity == null || entity.isDead()) return;
        if (!(event.getDamager() instanceof Player)) return;

        final Player player = (Player) event.getDamager();
        PlayerBoss boss = EntityManager.getInstance().getBoss(entity);
        if (boss == null) return;

        double damage = event.getDamage();
        ItemStack item = player.getItemInHand();

        if (item != null && !item.getType().equals(Material.AIR)) {
            CustomSwordSlayer sword = getSwordSlayer(NBTItem.from(item));

            if (sword != null) {
                damage = sword.getDamage();
            }
        }

        // Damage this entity.
        boss.damage(damage, player);
    }

    private CustomSwordSlayer getSwordSlayer(NBTItem nbti) {
        if (nbti.hasKey(BossSettings.BOSS_SLAYER_SWORD_NBT)) {
            return CustomSwordSlayer.getSword(nbti.getString(BossSettings.BOSS_SLAYER_SWORD_NBT));
        }
        return null;
    }
}
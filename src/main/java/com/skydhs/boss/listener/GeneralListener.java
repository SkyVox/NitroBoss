package com.skydhs.boss.listener;

import com.skydhs.boss.BossSettings;
import com.skydhs.boss.FileUtil;
import com.skydhs.boss.boss.EntityBoss;
import com.skydhs.boss.boss.PlayerBoss;
import com.skydhs.boss.utils.nbt.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
                new PlayerBoss(player, boss);
                player.sendMessage(FileUtil.get().getString("Messages.boss-spawned").asString());
                takeItem(player, NBTItem.from(item));
            }

            // Cancel this event, so the player will not be able to place the boss item on floor.
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
    public void onEntityDamage(EntityDamageByEntityEvent event) {
    }
}
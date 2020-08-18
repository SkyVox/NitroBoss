package com.skydhs.boss;

import com.skydhs.boss.boss.EntityBoss;
import com.skydhs.boss.boss.PlayerBoss;
import com.skydhs.boss.nms.EntityBossArmorStand;
import com.skydhs.boss.nms.EntityBossSlime;
import com.skydhs.boss.utils.nbt.NBTItem;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class EntityManager {
    private static EntityManager instance;

    private Core core;

    public EntityManager(Core core) {
        EntityManager.instance = this;
        this.core = core;

        // Register custom entities.
        EntityBossArmorStand.register();
        EntityBossSlime.register();
    }

    public EntityBoss getBoss(ItemStack item) {
        return null;
    }

    public PlayerBoss getBoss(Entity entity) {
        final int id = entity.getEntityId();

        for (PlayerBoss playerBoss : PlayerBoss.getSpawnedBosses().values()) {
            if (!playerBoss.isDied()) {
                if (playerBoss.getArmorStand().getId() == id || playerBoss.getSlime().getId() == id) {
                    return playerBoss;
                }
            }
        }

        return null;
    }

    public boolean isBoss(ItemStack item) {
        NBTItem nbt = NBTItem.from(item);
        return nbt.hasKey(BossSettings.BOSS_SPAWN_EGG_NBT);
    }

    public boolean isBoss(Entity entity) {
        return entity instanceof EntityBossArmorStand || entity instanceof EntityBossSlime;
    }

    public Core getCore() {
        return core;
    }

    public static EntityManager getInstance() {
        return instance;
    }
}
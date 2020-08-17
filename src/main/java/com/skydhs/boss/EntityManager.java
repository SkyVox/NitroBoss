package com.skydhs.boss;

import com.skydhs.boss.boss.EntityBoss;
import com.skydhs.boss.boss.PlayerBoss;
import com.skydhs.boss.nms.EntityBossArmorStand;
import com.skydhs.boss.nms.EntityBossSlime;
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

    public EntityBoss getBoss(Entity entity) {
        final int id = entity.getEntityId();

        for (PlayerBoss playerBoss : PlayerBoss.getSpawnedBosses().values()) {
            if (!playerBoss.isDied()) {
                EntityBoss boss = playerBoss.getBoss();

                if (boss.getArmorStand().getId() == id || boss.getSlime().getId() == id) {
                    return boss;
                }
            }
        }

        return null;
    }

    public boolean isBoss(ItemStack item) {
        return false;
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
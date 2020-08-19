package com.skydhs.boss.manager;

import com.skydhs.boss.BossSettings;
import com.skydhs.boss.Core;
import com.skydhs.boss.FileUtil;
import com.skydhs.boss.boss.EntityBoss;
import com.skydhs.boss.boss.PlayerBoss;
import com.skydhs.boss.nms.EntityBossArmorStand;
import com.skydhs.boss.nms.EntityBossSlime;
import com.skydhs.boss.utils.nbt.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EntityManager {
    private static EntityManager instance;

    private Core core;

    public EntityManager(Core core) {
        EntityManager.instance = this;
        this.core = core;

        // Register custom entities.
        EntityBossArmorStand.register();
        EntityBossSlime.register();

        BossLoader loader = new BossLoader();
        loader.loadBosses(FileUtil.getFile("config").get());
        loader.loadSwords(FileUtil.getFile("config").get());
    }

    public EntityBoss getBoss(ItemStack item) {
        NBTItem nbti = NBTItem.from(item);
        if (!nbti.hasKey(BossSettings.BOSS_SPAWN_EGG_NBT)) return null;
        return getBossById(nbti.getString(BossSettings.BOSS_SPAWN_EGG_NBT));
    }

    public EntityBoss getBossById(final String id) {
        for (Map.Entry<String, EntityBoss> entries : EntityBoss.getRegisteredBosses().entrySet()) {
            if (StringUtils.equalsIgnoreCase(entries.getKey(), id)) {
                return entries.getValue();
            }
        }
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
        NBTItem nbti = NBTItem.from(item);
        return nbti.hasKey(BossSettings.BOSS_SPAWN_EGG_NBT);
    }

    public boolean isBoss(Entity entity) {
        final int id = entity.getEntityId();

        for (PlayerBoss bosses : PlayerBoss.getSpawnedBosses().values()) {
            if (!bosses.isDied()) {
                if (bosses.getArmorStand().getId() == id || bosses.getSlime().getId() == id) return true;
            }
        }

        return false;
    }

    public Core getCore() {
        return core;
    }

    public static EntityManager getInstance() {
        return instance;
    }
}
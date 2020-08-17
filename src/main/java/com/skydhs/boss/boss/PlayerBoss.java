package com.skydhs.boss.boss;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerBoss implements Damageable {
    private static final Map<UUID, PlayerBoss> spawned_bosses = new ConcurrentHashMap<>(256);

    private Player player;
    private EntityBoss boss;
    private double health;

    public PlayerBoss(Player player, EntityBoss boss) {
        this.player = player;
        this.boss = boss;
        this.health = boss.getMaxHealth();
    }

    public Player getOwner() {
        return player;
    }

    public EntityBoss getBoss() {
        return boss;
    }

    @Override
    public boolean damage(double damage) {
        health = damage >= health ? 0 : health - damage;
        this.updateDisplayName();

        if (health <= 0) {
            boss.die();
            return true;
        }

        return false;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public double getMaxHealth() {
        return boss.getMaxHealth();
    }

    @Override
    public void resetHealth(double percentage) {
        if (health >= getMaxHealth()) return;

        final double result = boss.getMaxHealth() * (percentage / 100.0F);
        health = health+result > getMaxHealth() ? getMaxHealth() : health+result;

        this.updateDisplayName();
    }

    public boolean isDied() {
        return getHealth() <= 0;
    }

    private void updateDisplayName() {
    }

    public static PlayerBoss getPlayerBoss(Player player) {
        return getSpawnedBosses().get(player.getUniqueId());
    }

    public static Map<UUID, PlayerBoss> getSpawnedBosses() {
        return spawned_bosses;
    }
}
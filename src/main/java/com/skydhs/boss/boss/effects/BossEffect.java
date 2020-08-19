package com.skydhs.boss.boss.effects;

import com.skydhs.boss.manager.EntityManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.Set;

public class BossEffect implements Effects {
    private final Random R = new Random();

    private EffectType type;
    private double chance;
    private double damage;
    private int duration;
    private int amplifier;
    private boolean playThunder;

    public BossEffect(EffectType type, double chance, double damage, int duration, int amplifier, boolean playThunder) {
        this.type = type;
        this.chance = chance/100;
        this.damage = damage;
        this.duration = duration;
        this.amplifier = amplifier;
        this.playThunder = playThunder;
    }

    public EffectType getType() {
        return type;
    }

    @Override
    public double getChance() {
        return chance;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public int getEffectDuration() {
        return duration;
    }

    @Override
    public int getAmplifier() {
        return amplifier;
    }

    @Override
    public boolean isPlayThunder() {
        return playThunder;
    }

    public boolean isLuck() {
        return chance >= 100 || R.nextDouble() < chance;
    }

    public void apply(Set<? extends Entity> players, @Nullable Entity damager) {
        players.forEach(entity -> {
            Player player = (Player) entity;

            switch (type) {
                case DAMAGE:
                    break;
                case SLAP:
                    player.setVelocity(player.getLocation().getDirection().multiply(getAmplifier()/100));
                    player.setVelocity(new Vector(player.getVelocity().getX(), getAmplifier(), player.getVelocity().getZ()));
                    player.playSound(player.getLocation(), Sound.SHOOT_ARROW, 5, 5);
                    break;
                case POISON:
                case BLINDNESS:
                    if (!Bukkit.isPrimaryThread()) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                apply(players, damager);
                            }
                        }.runTaskLater(EntityManager.getInstance().getCore(), 0L);
                        return;
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(type.name().toUpperCase()), 20 * (getEffectDuration()+1), getAmplifier()));
                    break;
            }

            if (playThunder) {
                World world = player.getWorld();
                world.strikeLightningEffect(player.getLocation());
            }

            applyDamage(player, damager);
        });
    }

    private void applyDamage(Player player, @Nullable Entity damager) {
        if (getDamage() > 0) {
            if (damager == null) {
                player.damage(getDamage());
            } else {
                player.damage(getDamage(), damager);
            }
        }
    }
}
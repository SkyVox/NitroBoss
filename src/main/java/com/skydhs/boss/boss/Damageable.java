package com.skydhs.boss.boss;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface Damageable {

    /**
     * Causes damage on the current boss.
     *
     * @param damage Amount of taken damage.
     * @param damager Who damaged the entity.
     * @return If entity has died.
     */
    boolean damage(double damage, @Nullable Player damager);

    /**
     * Get boss current health.
     *
     * @return Current boss health.
     */
    double getHealth();

    /**
     * Get max boss health.
     *
     * @return Max boss health.
     */
    double getMaxHealth();

    /**
     * Reset boss health.
     * This will reset a specific percentage
     *
     * @param percentage
     */
    void resetHealth(double percentage);
}
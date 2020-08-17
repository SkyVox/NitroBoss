package com.skydhs.boss.boss;

public interface Damageable {

    /**
     * Causes damage on the current boss.
     *
     * @param damage Amount of taken damage.
     * @return If entity has died.
     */
    boolean damage(double damage);

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
package com.skydhs.boss.boss;

import org.bukkit.inventory.ItemStack;

public interface Spawnable {

    /**
     * Get the representative item to
     * spawn this boss.
     *
     * @return Item to spawn this boss.
     */
    ItemStack getSpawnEgg();
}
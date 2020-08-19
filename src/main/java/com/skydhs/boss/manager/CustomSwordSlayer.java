package com.skydhs.boss.manager;

import com.skydhs.boss.BossSettings;
import com.skydhs.boss.utils.nbt.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomSwordSlayer {
    private static final Map<String, CustomSwordSlayer> swords = new ConcurrentHashMap<>(8);

    private ItemStack item;
    private double damage;

    public CustomSwordSlayer(@NotNull final String id, ItemStack item, double damage) {
        this.item = setNBT(id, item);
        this.damage = damage;

        addSword(id);
    }

    private void addSword(final String id) {
        swords.put(id.toLowerCase(), this);
    }

    public ItemStack getSword() {
        return item.clone();
    }

    public double getDamage() {
        return damage;
    }

    private static ItemStack setNBT(final String id, ItemStack item) {
        NBTItem nbti = NBTItem.from(item);
        nbti.setString(BossSettings.BOSS_SLAYER_SWORD_NBT, id.toLowerCase());
        return nbti.getItem();
    }

    public static CustomSwordSlayer getSword(final String id) {
        return getSwords().get(id.toLowerCase());
    }

    public static Map<String, CustomSwordSlayer> getSwords() {
        return swords;
    }
}
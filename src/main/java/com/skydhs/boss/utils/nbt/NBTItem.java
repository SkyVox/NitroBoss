package com.skydhs.boss.utils.nbt;

import org.bukkit.inventory.ItemStack;

public class NBTItem extends NBT {
    private ItemStack item;

    protected NBTItem(ItemStack item) {
        super(item.clone());
        this.item = item;
    }

    public static NBTItem from(ItemStack item) {
        return new NBTItem(item);
    }

    @Override
    public ItemStack getItem() {
        return this.item = super.getItem();
    }

    @Override
    public ItemStack getRawItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
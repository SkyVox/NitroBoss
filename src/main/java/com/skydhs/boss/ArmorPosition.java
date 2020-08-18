package com.skydhs.boss;

public enum ArmorPosition {
    HAND(0),
    BOOTS(1),
    LEGGINGS(2),
    CHESTPLATE(3),
    HELMET(4);

    private int index;

    ArmorPosition(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
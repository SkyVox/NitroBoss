package com.skydhs.boss;

import static com.skydhs.boss.FileUtil.get;

public class BossSettings {

    /*
     * NBT that are used on bosses
     * spawn egg.
     */
    public static final String BOSS_SPAWN_EGG_NBT = "ENTITY_BOSS_EGG";

    /*
     * Radius to get nearby entities.
     */
    public static final int APPLY_EFFECTS_RADIUS = get().getInt("Settings.apply-effect-radius");
}
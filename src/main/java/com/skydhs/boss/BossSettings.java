package com.skydhs.boss;

import static com.skydhs.boss.FileUtil.get;

public class BossSettings {

    /*
     * NBT that are used on bosses
     * spawn egg.
     */
    public static final String BOSS_SPAWN_EGG_NBT = "ENTITY_BOSS_EGG";

    /*
     * NBT that are used on sword
     * hit kill for bosses.
     */
    public static final String BOSS_SLAYER_SWORD_NBT = "BOSS_SWORD_SLAYER";

    /*
     * Radius to get nearby entities.
     */
    public static final int APPLY_EFFECTS_RADIUS = get().getInt("Settings.apply-effect-radius");

    /*
     * Regen boss health after some time
     * if boss isn't being attacked.
     */
    public static final int REGEN_HEALTH_AFTER = get().getInt("Settings.regen-health-after");
}
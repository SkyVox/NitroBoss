package com.skydhs.boss.boss.effects;

public interface Effects {

    /**
     * The chance for this effect occurs.
     *
     * @return Chance.
     */
    double getChance();

    /**
     * Damage that this effects applies
     * on boss's enemy.
     *
     * @return Damage.
     */
    default double getDamage() {
        return 0;
    }

    /**
     * The potion effect duration.
     *
     * @return Effect duration.
     */
    default int getEffectDuration() {
        return 0;
    }

    /**
     * Get the effect amplifier.
     * Such as potion effect or
     * the slap amplifier.
     *
     * @return Effect amplifier.
     */
    default int getAmplifier() {
        return 0;
    }

    /**
     * @return Whether we should to play thunder.
     */
    boolean isPlayThunder();
}
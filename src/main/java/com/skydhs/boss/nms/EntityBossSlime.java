package com.skydhs.boss.nms;

import com.skydhs.boss.utils.CustomEntityRegistry;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntitySlime;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class EntityBossSlime extends EntitySlime {
    private static final DamageSource[] INVINCIBLE_FROM = new DamageSource[] {
            DamageSource.FIRE,
            DamageSource.BURN,
            DamageSource.LAVA,
            DamageSource.STUCK,
            DamageSource.DROWN,
            DamageSource.CACTUS,
            DamageSource.FALL,
            DamageSource.ANVIL,
            DamageSource.FALLING_BLOCK,
    };

    public EntityBossSlime(org.bukkit.World world) {
        super(((CraftWorld) world).getHandle());

        super.persistent = true;
        a(0.0F, 0.0F);
    }

    @Override
    public boolean isInvulnerable(DamageSource source) {
        for (DamageSource sources : INVINCIBLE_FROM) {
            if (source.equals(sources)) return true;
        }
        return super.isInvulnerable(source);
    }

    @Override
    public void makeSound(String sound, float f1, float f2) {
        // Remove sounds.
    }

    public static void register() {
        CustomEntityRegistry.registerEntity("Boss_Slime", 55, EntityBossSlime.class);
    }
}
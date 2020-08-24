package com.skydhs.boss.nms;

import com.skydhs.boss.utils.CustomEntityRegistry;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntitySlime;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class EntityBossSlime extends EntitySlime {

    public EntityBossSlime(org.bukkit.World world) {
        super(((CraftWorld) world).getHandle());

        super.persistent = true;
        a(0.0F, 0.0F);
    }

    @Override
    public boolean isInvulnerable(DamageSource source) {
        /*
         * This Slime shouldn't be killed in any reason.
         */
        return true;
    }

    @Override
    public void makeSound(String sound, float f1, float f2) {
        // Remove sounds.
    }

    public static void register() {
        CustomEntityRegistry.registerEntity("Boss_Slime", 55, EntityBossSlime.class);
    }
}
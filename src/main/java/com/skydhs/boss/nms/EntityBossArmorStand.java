package com.skydhs.boss.nms;

import com.skydhs.boss.utils.CustomEntityRegistry;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class EntityBossArmorStand extends EntityArmorStand {

    public EntityBossArmorStand(org.bukkit.World world) {
        super(((CraftWorld) world).getHandle());
    }

    @Override
    public boolean isInvulnerable(DamageSource source) {
        /*
         * The field Entity.invulnerable is private.
         * It's only used while saving NBTTags, but since the entity would be killed
         * on chunk unload, we prefer to override isInvulnerable().
         */
        return true;
    }

    @Override
    public void makeSound(String sound, float f1, float f2) {
        // Remove sounds.
    }

    public static void register() {
        CustomEntityRegistry.registerEntity("Boss_Armor_Stand", 30, EntityBossArmorStand.class);
    }
}
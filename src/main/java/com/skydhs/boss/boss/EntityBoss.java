package com.skydhs.boss.boss;

import com.skydhs.boss.nms.EntityBossArmorStand;
import com.skydhs.boss.nms.EntityBossSlime;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashSet;
import java.util.Set;

public class EntityBoss extends Boss {
    private static final Set<EntityBoss> registered_bosses = new LinkedHashSet<>(16);

    private org.bukkit.World world;
    private EntityBossArmorStand armorStand;
    private EntityBossSlime slime;

    // Boss information.
    private double maxHealth;

    public EntityBoss(org.bukkit.World world) {
        this.world = world;
        this.armorStand = new EntityBossArmorStand(world);
        this.slime = new EntityBossSlime(world);

        addBoss();
    }

    private void addBoss() {
        EntityBoss.registered_bosses.add(this);
    }

    public org.bukkit.World getWorld() {
        return world;
    }

    public EntityBossArmorStand getArmorStand() {
        return armorStand;
    }

    public EntityBossSlime getSlime() {
        return slime;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void die() {
        // TODO. Kill this boss.
    }

    @Override
    public ItemStack getSpawnEgg() {
        return null; // TODO.
    }

    public boolean spawnNMSBoss(double x, double y, double z, float yaw, float pitch) {
        try {
            World nmsWorld = ((CraftWorld) world).getHandle();
            this.armorStand = new EntityBossArmorStand(world);
            this.slime = new EntityBossSlime(world);

            // Change some entity settings.
            armorStand.setLocation(x, y, z, yaw, pitch);
            slime.setLocation(x, y-0.25D, z, yaw, pitch);
            ((CraftLivingEntity) armorStand.getBukkitEntity()).setRemoveWhenFarAway(false);
            ((CraftLivingEntity) slime.getBukkitEntity()).setRemoveWhenFarAway(false);
            slime.setSize(1);
            slime.setInvisible(true);
            armorStand.setBasePlate(false);
            armorStand.setGravity(false);
            setNoAI();

            // Then, add those entities to the world.
            nmsWorld.addEntity(armorStand, CreatureSpawnEvent.SpawnReason.CUSTOM);
            nmsWorld.addEntity(slime, CreatureSpawnEvent.SpawnReason.CUSTOM);

            // Set armor stand as a passenger of this slime.
            slime.getBukkitEntity().setPassenger(armorStand.getBukkitEntity());

            // This Boss has been successfully spawned.
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private void setNoAI() {
        NBTTagCompound tag = slime.getNBTTag();
        if (tag == null) tag = new NBTTagCompound();

        // Change the entity AI.
        tag.setInt("NoAI", 1);
        slime.c(tag);
        slime.f(tag);
    }

    public static Set<EntityBoss> getRegisteredBosses() {
        return registered_bosses;
    }
}
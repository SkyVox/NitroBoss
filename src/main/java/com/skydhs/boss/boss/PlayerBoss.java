package com.skydhs.boss.boss;

import com.skydhs.boss.manager.EntityManager;
import com.skydhs.boss.nms.EntityBossArmorStand;
import com.skydhs.boss.nms.EntityBossSlime;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerBoss implements Damageable {
    private static final Map<UUID, PlayerBoss> spawned_bosses = new ConcurrentHashMap<>(256);

    private Player player;
    private UUID playerUniqueId;
    private EntityBoss boss;
    private double health;

    private EntityBossArmorStand armorStand;
    private EntityBossSlime slime;

    // Task information.
    private int taskId = -1;

    public PlayerBoss(Player player, EntityBoss boss) {
        this.player = player;
        this.playerUniqueId = player.getUniqueId();
        this.boss = boss;
        this.health = boss.getMaxHealth();

        addSpawnedBoss();
    }

    private void addSpawnedBoss() {
        spawned_bosses.put(playerUniqueId, this);
    }

    public Player getOwner() {
        return player;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public EntityBoss getBoss() {
        return boss;
    }

    public Location getBossLocation() {
        return armorStand.getBukkitEntity().getLocation();
    }

    @Override
    public boolean damage(double damage, @Nullable Player damager) {
        health = damage >= health ? 0 : health - damage;
        this.updateDisplayName();

        if (health <= 0) {
            die(damager);
            return true;
        }

        return false;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public double getMaxHealth() {
        return boss.getMaxHealth();
    }

    @Override
    public void resetHealth(double percentage) {
        if (health >= getMaxHealth()) return;

        final double result = boss.getMaxHealth() * (percentage / 100.0F);
        health = health+result > getMaxHealth() ? getMaxHealth() : health+result;

        this.updateDisplayName();
    }

    public boolean isDied() {
        boolean ret = getHealth() <= 0 || (armorStand != null && armorStand.dead) || (slime != null && slime.dead);
        // If task isn't equals to -1, we should stop it.
        if (ret && taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            getSpawnedBosses().remove(this.playerUniqueId);
        }
        return ret;
    }

    public void die(@Nullable Player damager) {
        if (armorStand != null) {
            armorStand.die();
            armorStand.getBukkitEntity().remove();
        }
        if (slime != null) {
            slime.die();
            slime.getBukkitEntity().remove();
        }

        // If task isn't equals to -1, we should stop it.
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        // TODO Send rewards for this player.
        getSpawnedBosses().remove(this.playerUniqueId);
    }

    public EntityBossArmorStand getArmorStand() {
        return armorStand;
    }

    public EntityBossSlime getSlime() {
        return slime;
    }

    private void updateDisplayName() {
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', StringUtils.replaceEach(getBoss().getDisplayName(), new String[] {
                "%current_health%",
                "%max_health%"
        }, new String[] {
                String.valueOf(getHealth()),
                String.valueOf(getMaxHealth())
        })));
        armorStand.setCustomNameVisible(true);
    }

    public boolean spawnNMSBoss(double x, double y, double z, float yaw, float pitch) {
        try {
            final org.bukkit.World world = player.getWorld();
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
            ((CraftLivingEntity) slime.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 5, true, false));
            ((ArmorStand) armorStand.getBukkitEntity()).setBasePlate(false);
            armorStand.setGravity(false);
            armorStand.setArms(true);
            armorStand.setSmall(getBoss().isSmall());
            setNoAI();

            // Then, update displayName.
            updateDisplayName();

            // Set armor stand equipment.
            getBoss().getArmor().forEach((key, value) -> {
                if (value != null) {
                    armorStand.setEquipment(key.getIndex(), CraftItemStack.asNMSCopy(value));
                }
            });

            // Then, add those entities to the world.
            nmsWorld.addEntity(armorStand, CreatureSpawnEvent.SpawnReason.CUSTOM);
            nmsWorld.addEntity(slime, CreatureSpawnEvent.SpawnReason.CUSTOM);

            // Set armor stand as a passenger of this slime.
            slime.getBukkitEntity().setPassenger(armorStand.getBukkitEntity());

            // Setup and start our task.
            this.startTask();

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

    private void startTask() {
        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(EntityManager.getInstance().getCore(), () -> {
            double chance = getBoss().getPlayEffectChance();

            if (chance >= 100 || getBoss().R.nextDouble() < chance) {
                getBoss().applyEffect(this);
            }
        }, 20 * 5, 20*2).getTaskId();
    }

    public static PlayerBoss getPlayerBoss(Player player) {
        return getSpawnedBosses().get(player.getUniqueId());
    }

    public static Map<UUID, PlayerBoss> getSpawnedBosses() {
        return spawned_bosses;
    }
}
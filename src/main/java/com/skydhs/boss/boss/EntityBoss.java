package com.skydhs.boss.boss;

import com.skydhs.boss.ArmorPosition;
import com.skydhs.boss.BossSettings;
import com.skydhs.boss.boss.effects.BossEffect;
import com.skydhs.boss.manager.EntityManager;
import net.minecraft.server.v1_8_R3.Vector3f;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityBoss extends Boss {
    private static final Map<String, EntityBoss> registered_bosses = new LinkedHashMap<>(16);

    // Boss information.
    private String displayName;
    private double maxHealth;
    private BossEffect[] effects;
    private Map<ArmorPosition, ItemStack> armor;
    private ItemStack spawnEgg;

    public EntityBoss(@NotNull String name, String displayName, double maxHealth, BossEffect[] effects, Map<ArmorPosition, ItemStack> armor, ItemStack spawnEgg) {
        this.displayName = displayName;
        this.maxHealth = maxHealth;
        this.effects = effects;
        this.armor = armor;
        this.spawnEgg = spawnEgg;

        addBoss(name);
    }

    private void addBoss(@NotNull final String name) {
        EntityBoss.registered_bosses.put(name, this);
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public BossEffect[] getEffects() {
        return effects;
    }

    public void applyEffect(final PlayerBoss boss) {
        if (effects == null || effects.length <= 0) return;

        final Location location = boss.getBossLocation();
        int radius = BossSettings.APPLY_EFFECTS_RADIUS;
        Set<Entity> entities = location.getWorld().getNearbyEntities(location, radius, radius, radius).stream().filter(Player.class::isInstance).collect(Collectors.toSet());

        int[] positions = new int[] { 360, 0 };
        new BukkitRunnable() {
            @Override
            public void run() {
                if (boss.isDied()) {
                    cancel();
                } else {
                    if (positions[1] >= 35) {
                        if (positions[0] >= 360) {
                            positions[0] = 0;
                            positions[1] = 0;
                            cancel();
                            return;
                        }
                        positions[0]+=1;
                    } else {
                        positions[1]++;
                        positions[0]--;
                    }

                    // Play the arm animation.
                    boss.getArmorStand().setRightArmPose(new Vector3f(positions[0], 0, 0));
                }
            }
        }.runTaskTimerAsynchronously(EntityManager.getInstance().getCore(), 1, 1);

        if (entities != null && entities.size() > 0) {
            for (BossEffect effect : getEffects()) {
                if (effect.isLuck()) {
                    effect.apply(entities, boss.getArmorStand().getBukkitEntity());
                }
            }
        }
    }

    public Map<ArmorPosition, ItemStack> getArmor() {
        return armor;
    }

    @Override
    public ItemStack getSpawnEgg() {
        return spawnEgg;
    }

    public static Map<String, EntityBoss> getRegisteredBosses() {
        return registered_bosses;
    }
}
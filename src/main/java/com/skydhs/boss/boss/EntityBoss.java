package com.skydhs.boss.boss;

import com.skydhs.boss.ArmorPosition;
import com.skydhs.boss.BossSettings;
import com.skydhs.boss.boss.effects.BossEffect;
import com.skydhs.boss.manager.EntityManager;
import com.skydhs.boss.rewards.BossRewards;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.v1_8_R3.Vector3f;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class EntityBoss extends Boss {
    private static final Map<String, EntityBoss> registered_bosses = new LinkedHashMap<>(16);
    private static final Set<UUID> animations = new ConcurrentSet<>();

    // Random.
    protected final Random R = new Random();

    // Boss information.
    private String displayName;
    private double maxHealth;
    private boolean small;
    private double playEffectChance;
    private double healthRegenPercentage;
    private BossRewards[] rewards;
    private BossEffect[] effects;
    private Map<ArmorPosition, ItemStack> armor;
    private ItemStack spawnEgg;

    // Boss killing information.
    private boolean useCustomSword;

    public EntityBoss(@NotNull String name, String displayName, double maxHealth, boolean small, double playEffectChance, double healthRegenPercentage, @Nullable BossRewards[] rewards, BossEffect[] effects, Map<ArmorPosition, ItemStack> armor, ItemStack spawnEgg, boolean useCustomSword) {
        this.displayName = displayName;
        this.maxHealth = maxHealth;
        this.small = small;
        this.playEffectChance = playEffectChance/100;
        this.healthRegenPercentage = healthRegenPercentage;
        this.rewards = rewards;
        this.effects = effects;
        this.armor = armor;
        this.spawnEgg = spawnEgg;
        this.useCustomSword = useCustomSword;

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

    public boolean isSmall() {
        return small;
    }

    public double getPlayEffectChance() {
        return playEffectChance;
    }

    public double getHealthRegenPercentage() {
        return healthRegenPercentage;
    }

    public BossRewards[] getRewards() {
        return rewards;
    }

    public BossEffect[] getEffects() {
        return effects;
    }

    public void applyEffect(final PlayerBoss boss) {
        if (effects == null || effects.length <= 0) return;

        final Location location = boss.getBossLocation();
        int radius = BossSettings.APPLY_EFFECTS_RADIUS;
        Set<Entity> entities = location.getWorld().getNearbyEntities(location, radius, radius, radius).stream().filter(Player.class::isInstance).collect(Collectors.toSet());
        boolean animate = false;

        if (entities != null && entities.size() > 0) {
            for (BossEffect effect : getEffects()) {
                if (effect.isLuck()) {
                    if (!animate) animate = true;
                    effect.apply(entities, boss.getArmorStand().getBukkitEntity());
                }
            }
        }

        if (animate && (animations.stream().filter(uuid -> uuid.equals(boss.getPlayerUniqueId())).findFirst().orElse(null) == null)) {
            animations.add(boss.getPlayerUniqueId());

            int[] positions = new int[] { 360, 0 };
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (boss.isDied()) {
                        animations.remove(boss.getPlayerUniqueId());
                        cancel();
                    } else {
                        if (positions[1] >= 35) {
                            if (positions[0] >= 360) {
                                positions[0] = 0;
                                positions[1] = 0;
                                animations.remove(boss.getPlayerUniqueId());
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
        }
    }

    public Map<ArmorPosition, ItemStack> getArmor() {
        return armor;
    }

    @Override
    public ItemStack getSpawnEgg() {
        return spawnEgg.clone();
    }

    public boolean isUseCustomSword() {
        return useCustomSword;
    }

    public static Map<String, EntityBoss> getRegisteredBosses() {
        return registered_bosses;
    }
}
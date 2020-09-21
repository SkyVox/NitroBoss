package com.skydhs.boss.manager;

import com.skydhs.boss.ArmorPosition;
import com.skydhs.boss.BossSettings;
import com.skydhs.boss.FileUtil;
import com.skydhs.boss.boss.EntityBoss;
import com.skydhs.boss.boss.effects.BossEffect;
import com.skydhs.boss.boss.effects.EffectType;
import com.skydhs.boss.rewards.BossRewards;
import com.skydhs.boss.utils.ItemBuilder;
import com.skydhs.boss.utils.nbt.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BossLoader {

    public BossLoader() {
    }

    protected void loadBosses(final FileConfiguration file) {
        for (String str : file.getConfigurationSection("Bosses").getKeys(false)) {
            String displayName = ChatColor.translateAlternateColorCodes('&', file.getString("Bosses." + str + ".display-name"));
            double maxHealth = file.getDouble("Bosses." + str + ".health");
            boolean small = file.contains("Bosses." + str + ".small") && file.getString("Bosses." + str + ".small").equalsIgnoreCase("true");
            double playEffectChance = file.getDouble("Bosses." + str + ".chance-to-play-effects");
            double healthRegenPercentage = file.getDouble("Bosses." + str + ".health-regen-per-sec");
            BossRewards[] rewards = getRewards(file, "Bosses." + str + ".rewards");
            BossEffect[] effects = getEffects(file, "Bosses." + str + ".effects", file.getConfigurationSection("Bosses." + str + ".effects").getKeys(false));
            Map<ArmorPosition, ItemStack> armor = getArmor(file, "Bosses." + str + ".boss-armor", file.getConfigurationSection("Bosses." + str + ".boss-armor").getKeys(false));
            ItemBuilder builder = ItemBuilder.get(file, "Bosses." + str + ".spawn-egg");
            boolean useCustomSword = file.contains("Bosses." + str + ".use-custom-sword") && file.getString("Bosses." + str + ".use-custom-sword").equalsIgnoreCase("true");

            NBTItem nbti = NBTItem.from(builder.build());
            nbti.setString(BossSettings.BOSS_SPAWN_EGG_NBT, str.toLowerCase());

            // All load, then create new boss.
            new EntityBoss(str.toLowerCase(), displayName, maxHealth, small, playEffectChance, healthRegenPercentage, rewards, effects, armor, nbti.getItem(), useCustomSword);
        }
    }

    protected void loadSwords(final FileConfiguration file) {
        for (String str : file.getConfigurationSection("Boss-Sword-Slayer").getKeys(false)) {
            ItemBuilder builder = ItemBuilder.get(file, "Boss-Sword-Slayer." + str);
            double damage = file.getDouble("Boss-Sword-Slayer." + str + ".damage-cause");

            // All load, then create new sword slayer.
            new CustomSwordSlayer(str, builder.build(), damage);
        }
    }

    private BossEffect[] getEffects(final FileConfiguration file, String where, Set<String> section) {
        BossEffect[] ret = new BossEffect[section.size()];

        int index = 0;
        for (String str : section) {
            try {
                EffectType type = EffectType.valueOf(str.toUpperCase());
                double chance = file.contains(where + '.' + str + ".chance") ? file.getDouble(where + '.' + str + ".chance") : 100;
                double damage = file.contains(where + '.' + str + ".damage") ? file.getDouble(where + '.' + str + ".damage") : 0;
                int duration = file.contains(where + '.' + str + ".duration") ? file.getInt(where + '.' + str + ".duration") : 0;
                int amplifier = file.contains(where + '.' + str + ".amplifier") ? file.getInt(where + '.' + str + ".amplifier") : 0;
                boolean playThunder = file.contains(where + '.' + str + ".play-thunder") && file.getString(where + '.' + str + ".play-thunder").equalsIgnoreCase("true");

                // Create new Boss effect obj.
                ret[index++] = new BossEffect(type, chance, damage, duration, amplifier, playThunder);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }

        return ret;
    }

    private Map<ArmorPosition, ItemStack> getArmor(final FileConfiguration file, String where, Set<String> section) {
        Map<ArmorPosition, ItemStack> ret = new ConcurrentHashMap<>(ArmorPosition.values().length);

        for (String str : section) {
            try {
                ArmorPosition position = ArmorPosition.valueOf(file.getString(where + '.' + str + ".armor-position").toUpperCase());
                ItemBuilder builder = ItemBuilder.get(file, where + '.' + str);
                ret.put(position, builder.build());
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }

        // Fill up this map.
        for (ArmorPosition positions : ArmorPosition.values()) {
            if (!ret.containsKey(positions)) {
                ret.put(positions, null);
            }
        }

        return ret;
    }

    private BossRewards[] getRewards(final FileConfiguration file, String where) {
        if (!file.contains(where)) return null;
        Set<String> section = file.getConfigurationSection(where).getKeys(false);
        BossRewards[] ret = new BossRewards[section.size()];

        int index = 0;
        for (String str : section) {
            double chance = file.contains(where + '.' + str + ".chance") ? file.getDouble(where + '.' + str + ".chance") : 100;
            String[] commands = FileUtil.get().getList(where + '.' + str + ".commands").getRaw();
            String[] messages = FileUtil.get().getList(where + '.' + str + ".messages").getRaw();
            List<ItemStack> items = new ArrayList<>(8);

            for (String item : file.getConfigurationSection(where + '.' + str + ".items").getKeys(false)) {
                ItemBuilder builder = ItemBuilder.get(file, where + '.' + str + ".items." + item);
                if (builder == null) {
                    System.out.println("Could not load an item. Item cannot be null.");
                    continue;
                }
                items.add(builder.build());
            }

            // Then create a new @BossRewards with the given information.
            ret[index++] = new BossRewards(chance, commands, messages, items);
        }

        return ret;
    }
}
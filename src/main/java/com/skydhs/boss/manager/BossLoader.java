package com.skydhs.boss.manager;

import com.skydhs.boss.ArmorPosition;
import com.skydhs.boss.BossSettings;
import com.skydhs.boss.boss.EntityBoss;
import com.skydhs.boss.boss.effects.BossEffect;
import com.skydhs.boss.boss.effects.EffectType;
import com.skydhs.boss.utils.ItemBuilder;
import com.skydhs.boss.utils.nbt.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

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
            BossEffect[] effects = getEffects(file, "Bosses." + str + ".effects", file.getConfigurationSection("Bosses." + str + ".effects").getKeys(false));
            Map<ArmorPosition, ItemStack> armor = getArmor(file, "Bosses." + str + ".boss-armor", file.getConfigurationSection("Bosses." + str + ".boss-armor").getKeys(false));
            ItemBuilder builder = ItemBuilder.get(file, "Bosses." + str + ".spawn-egg");

            NBTItem nbti = NBTItem.from(builder.build());
            nbti.setString(BossSettings.BOSS_SPAWN_EGG_NBT, str.toLowerCase());

            // All load, then create new boss.
            new EntityBoss(str.toLowerCase(), displayName, maxHealth, effects, armor, nbti.getItem());
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
}
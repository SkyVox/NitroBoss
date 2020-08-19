package com.skydhs.boss.rewards;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BossRewards {
    private final Random R = new Random();

    private double chance;
    private String[] commands;
    private String[] messages;
    private List<ItemStack> items;

    public BossRewards(double chance, String[] commands, String[] messages, List<ItemStack> items) {
        this.chance = chance;
        this.commands = commands;
        this.messages = messages;
        this.items = items;
    }

    public void sendReward(Player player) {
        Validate.notNull(player, "player");

        if (chance >= 100 || R.nextDouble() < (chance / 100)) {
            // Send commands reward for this player.
            sendCommand(player);
            // Send messages rewards for this player.
            sendMessage(player);
            // Send items reward for this player.
            sendItems(player);
        }
    }

    private void sendCommand(Player player) {
        if (commands == null) return;
        for (String str : commands) {
            String command = StringUtils.replaceEach(str, new String[] {
                    "[PLAYER]",
                    "[CONSOLE]",
                    "/",
                    "%player_name%"
            }, new String[] {
                    "",
                    "",
                    "",
                    player.getName()
            });

            if (StringUtils.containsIgnoreCase(str, "[CONSOLE]")) {
                // Console should execute this command.
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } else {
                // Then, Player should execute this command.
                Bukkit.dispatchCommand(player, command);
            }
        }
    }

    private void sendMessage(Player player) {
        if (messages == null) return;
        for (String str : messages) {
            player.sendMessage(str);
        }
    }

    private void sendItems(Player player) {
        if (items == null || items.isEmpty()) return;

        for (ItemStack item : items) {
            ItemStack clone = item.clone();
            ItemMeta meta = clone.getItemMeta();

            if (meta.hasDisplayName()) {
                String name = meta.getDisplayName();
                meta.setDisplayName(StringUtils.replaceEach(name, new String[] {
                        "%player_name%",
                }, new String[] {
                        player.getName(),
                }));
            }

            if (meta.hasLore() && !meta.getLore().isEmpty()) {
                List<String> lore = new ArrayList<>(meta.getLore().size());
                for (String text : meta.getLore()) {
                    lore.add(StringUtils.replaceEach(text, new String[] {
                            "%player_name%",
                    }, new String[] {
                            player.getName(),
                    }));
                }
                meta.setLore(lore);
            }
            clone.setItemMeta(meta);

            // Send this item for the given player.
            Map<Integer, ItemStack> remainder = player.getInventory().addItem(clone);

            if (remainder != null && !remainder.isEmpty()) {
                remainder.values().forEach(entry -> {
                    if (entry != null) {
                        player.getWorld().dropItemNaturally(player.getLocation().add(0D, 1.5D, 0D), entry);
                    }
                });
            }
        }
    }
}
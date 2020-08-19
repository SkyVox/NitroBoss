package com.skydhs.boss.commands;

import com.skydhs.boss.FileUtil;
import com.skydhs.boss.boss.EntityBoss;
import com.skydhs.boss.manager.EntityManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossCmd implements CommandExecutor {
    private final String[] HELP = FileUtil.get().getList("Messages.boss-cmd.help").getRaw();
    private final String INSUFFICIENT_PERMISSION = FileUtil.get().getString("Messages.insufficient-permission").asString();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length <= 0) {
            for (String help : HELP) {
                sender.sendMessage(help);
            }
            return true;
        } else if (StringUtils.equalsIgnoreCase(args[0], "LISTA")) {
            if (!sender.hasPermission("boss.admin")) {
                sender.sendMessage(INSUFFICIENT_PERMISSION);
            } else {
                sender.sendMessage(FileUtil.get().getString("Messages.boss-cmd.available-bosses").replace("%available_bosses%", StringUtils.join(EntityBoss.getRegisteredBosses().keySet(), ',')).asString());
            }
        } else {
            if (!sender.hasPermission("boss.admin")) {
                sender.sendMessage(INSUFFICIENT_PERMISSION);
                return true;
            }

            if (args.length <= 1) {
                Bukkit.dispatchCommand(sender, "giveboss");
            } else {
                EntityBoss boss = getBossById(args[0]);
                if (boss == null) {
                    sender.sendMessage(FileUtil.get().getString("Messages.boss-cmd.invalid-boss").asString());
                    return true;
                }

                Player player = Bukkit.getPlayer(args[1]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage(FileUtil.get().getString("Messages.invalid-player").asString());
                    return true;
                }

                // Give boss for this player.
                player.getInventory().addItem(boss.getSpawnEgg());

                // Send message to them.
                sender.sendMessage(FileUtil.get().getString("Messages.boss-cmd.send-boss-sender").replace("%player_name%", player.getName()).asString());
                player.sendMessage(FileUtil.get().getString("Messages.boss-cmd.send-boss-target").replace("%sender_name%", sender.getName()).asString());
            }
        }
        return true;
    }

    private EntityBoss getBossById(final String id) {
        return EntityManager.getInstance().getBossById(id);
    }
}
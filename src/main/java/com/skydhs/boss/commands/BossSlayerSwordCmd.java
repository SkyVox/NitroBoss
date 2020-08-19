package com.skydhs.boss.commands;

import com.skydhs.boss.FileUtil;
import com.skydhs.boss.manager.CustomSwordSlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossSlayerSwordCmd implements CommandExecutor {
    private final String[] HELP = FileUtil.get().getList("Messages.custom-sword-slayer-cmd.help").getRaw();
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
                sender.sendMessage(FileUtil.get().getString("Messages.custom-sword-slayer-cmd.available-swords").replace("%available_swords%", StringUtils.join(CustomSwordSlayer.getSwords().keySet(), ',')).asString());
            }
        } else {
            if (!sender.hasPermission("boss.admin")) {
                sender.sendMessage(INSUFFICIENT_PERMISSION);
                return true;
            }

            if (args.length <= 1) {
                Bukkit.dispatchCommand(sender, "givematadora");
            } else {
                CustomSwordSlayer sword = getSwordSlayerById(args[0]);
                if (sword == null) {
                    sender.sendMessage(FileUtil.get().getString("Messages.custom-sword-slayer-cmd.invalid-sword").asString());
                    return true;
                }

                Player player = Bukkit.getPlayer(args[1]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage(FileUtil.get().getString("Messages.invalid-player").asString());
                    return true;
                }

                // Give sword slayer for this player.
                player.getInventory().addItem(sword.getSword());

                // Send message to them.
                sender.sendMessage(FileUtil.get().getString("Messages.custom-sword-slayer-cmd.send-sword-sender").replace("%player_name%", player.getName()).asString());
                player.sendMessage(FileUtil.get().getString("Messages.custom-sword-slayer-cmd.send-sword-target").replace("%sender_name%", sender.getName()).asString());
            }
        }
        return true;
    }

    private CustomSwordSlayer getSwordSlayerById(final String id) {
        return CustomSwordSlayer.getSword(id);
    }
}
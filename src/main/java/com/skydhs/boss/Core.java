package com.skydhs.boss;

import com.skydhs.boss.listener.GeneralListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
    public final String NAME = getDescription().getName();
    public final String VERSION = getDescription().getVersion();

    private ConsoleCommandSender console = Bukkit.getConsoleSender();

    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();
        console.sendMessage("----------");
        console.sendMessage(ChatColor.GRAY + "Enabling " + ChatColor.YELLOW +  NAME + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY + "Version: " + ChatColor.YELLOW + VERSION + ChatColor.GRAY + "!");

        // -- Generate and setup the configuration files -- \\
        new FileUtil(this, new FileUtil.FileInfo[] {
                new FileUtil.FileInfo((char) 1),
        });

        // -- Load all classes instances and the plugin dependencies -- \\
        console.sendMessage("Loading dependencies and instances...");
        new EntityManager(this);

        // -- Load the plugin commands and listeners -- \\
        console.sendMessage("Loading command and listeners...");
        getServer().getPluginManager().registerEvents(new GeneralListener(), this);

        console.sendMessage(ChatColor.YELLOW +  NAME + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY + "has been enabled! Took " + getSpentTime(time) + "ms.");
        console.sendMessage("----------");
    }

    @Override
    public void onDisable() {
        console.sendMessage("----------");
        console.sendMessage(ChatColor.GRAY + "Disabling " + ChatColor.YELLOW +  NAME + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY + "Version: " + ChatColor.YELLOW + VERSION + ChatColor.GRAY + "!");

        // TODO, Unregister bosses.

        console.sendMessage(ChatColor.YELLOW +  NAME + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY + "has been disabled!");
        console.sendMessage("----------");
    }

    private long getSpentTime(long time) {
        return System.currentTimeMillis() - time;
    }
}
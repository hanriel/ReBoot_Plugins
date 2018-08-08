package com.skyandforest.reboot_dungeons;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Dungeons extends JavaPlugin {

    public static final String CHAT_PREFIX = ChatColor.AQUA + "[" + ChatColor.GREEN + "Dungeons" + ChatColor.AQUA + "] " + ChatColor.GREEN;

    private static Dungeons instance;


    @Override
    public void onEnable() {

        if (instance != null) {
            getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/shop reload\" instead.");
            return;
        }

        instance = this;
        new _Listener();
        Bukkit.getPluginCommand("dun").setExecutor(new _Listener());
    }

    @Override
    public void onDisable() {


    }

    public static Dungeons getInstance() {
        return instance;
    }

}

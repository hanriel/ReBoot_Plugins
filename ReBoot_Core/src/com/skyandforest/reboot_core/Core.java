package com.skyandforest.reboot_core;

import com.skyandforest.reboot_core.command.CommandFramework;
import com.skyandforest.reboot_core.command.CoreCommandHandler;
import com.skyandforest.reboot_core.playerData.MySQL;
import com.skyandforest.reboot_core.playerData.PlayerDataFramework;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Core extends JavaPlugin {

    public static final String CHAT_PREFIX = "&e[&4Core&e]&r ";

    private static Core instance;

    @Override
    public void onEnable() {
        if (instance != null) {
            getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/rb reload\" instead.");
            return;
        }
        instance = this;

        new MySQL();

        CommandFramework.register(this, new CoreCommandHandler("core"));
        Core.getInstance().getServer().getPluginManager().registerEvents(new _Listener(), instance);

        load();
    }

    @Override
    public void onDisable() {

    }

    public void load() {
        File playersFolder = new File(getDataFolder(), "players");

        if (!playersFolder.isDirectory()) {
            playersFolder.mkdirs();
        }

        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerDataFramework.loadData(player);
        }

        // Register the BungeeCord plugin channel.
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }
    }

    public static Core getInstance() {
        return instance;
    }
}

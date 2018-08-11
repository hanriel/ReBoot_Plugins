package com.skyandforest.reboot_core;

import com.skyandforest.reboot_core.command.CommandFramework;
import com.skyandforest.reboot_core.command.CoreCommandHandler;
import com.skyandforest.reboot_core.playerData.PlayerDataFramework;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Core extends JavaPlugin {

    public static final String CHAT_PREFIX = ChatColor.AQUA + "[" + ChatColor.GREEN + "Core" + ChatColor.AQUA + "] " + ChatColor.GREEN;

    private static Core instance;

    private static int lastReloadErrors;

    @Override
    public void onEnable() {
        if (instance != null) {
            Log.warning("Please do not use /reload or plugin reloaders. Do \"/rb reload\" instead.");
            return;
        }
        instance = this;

        CommandFramework.register(this, new CoreCommandHandler("core"));
        Core.getInstance().getServer().getPluginManager().registerEvents(new _Listener(), instance);

        ErrorLogger errorLogger = new ErrorLogger();
        load(errorLogger);

        lastReloadErrors = errorLogger.getSize();
        if (errorLogger.hasErrors()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new ErrorLoggerTask(errorLogger), 10L);
        }
    }

    public void load(ErrorLogger errorLogger) {
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

    public static void setLastReloadErrors(int lastReloadErrors) {
        Core.lastReloadErrors = lastReloadErrors;
    }
}

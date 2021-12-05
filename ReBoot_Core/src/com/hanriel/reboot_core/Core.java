package com.hanriel.reboot_core;

import com.hanriel.reboot_core.command.CommandFramework;
import com.hanriel.reboot_core.command.CoreCommandHandler;
import com.hanriel.reboot_core.config.Settings;
import com.hanriel.reboot_core.config.YAML.PluginConfig;
import com.hanriel.reboot_core.playerData.MySQL;
import com.hanriel.reboot_core.playerData.PlayerDataFramework;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Core extends JavaPlugin {

    public static final String CHAT_PREFIX = "&e[&4Core&e]&r ";

    private static Core instance;
    private static Settings conf;

    @Override
    public void onEnable() {
        if (instance != null) return;
        instance = this;

        conf = new Settings(new PluginConfig(this, "config.yml"));

        CommandFramework.register(this, new CoreCommandHandler("core"));

        Core.getInstance().getServer().getPluginManager().registerEvents(new _Listener(), instance);
        load();
        for(Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataFramework.loadData(player);
        }
    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()){
            if (!player.hasMetadata("c"))
                continue;
            PlayerDataFramework.uploadData(player);
        }
    }

    public void load() {

        try {
            conf.load();
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().warning("I/O error while using the language file. Default values will be used.");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            getLogger().warning("The lang.yml was not a valid YAML, please look at the error above. Default values will be used.");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().warning("Unhandled error while reading the values for the configuration! Please inform the developer.");
        }

        new MySQL();

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
    public static Settings getConf() {
        return conf;
    }
}

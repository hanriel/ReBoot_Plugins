package org.hanriel.toocore;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.hanriel.toocore.command.CommandFramework;
import org.hanriel.toocore.command.CoreCommandHandler;
import org.hanriel.toocore.config.Settings;
import org.hanriel.toocore.config.YAML.PluginConfig;
import org.hanriel.toocore.playerData.MySQL;
import org.hanriel.toocore.playerData.PlayerDataFramework;

import java.io.IOException;

public final class TOO_CORE extends JavaPlugin {

    public static final String CHAT_PREFIX = "&e[&4Core&e]&r ";
    private static TOO_CORE instance;
    private static Settings conf;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (instance != null) {
            getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/too reload\" instead.");
            return;
        }
        instance = this;
        conf = new Settings(new PluginConfig(this, "config.yml"));

        CommandFramework.register(this, (CommandFramework)new CoreCommandHandler("core"));
        getInstance().getServer().getPluginManager().registerEvents(new _Listener(), instance);
        load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static TOO_CORE getInstance() {
        return instance;
    }

    public static Settings getConf() {
        return conf;
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

}

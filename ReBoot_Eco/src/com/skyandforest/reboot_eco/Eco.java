package com.skyandforest.reboot_eco;

import com.saf.reboot_core.CommandFramework;
import com.saf.reboot_core.Core;
import com.saf.reboot_core.ErrorLogger;
import com.saf.reboot_core.ErrorLoggerTask;
import com.skyandforest.reboot_eco.command.*;
import com.skyandforest.reboot_eco.config.*;
import com.skyandforest.reboot_eco.config.YAML.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Eco extends JavaPlugin {

    public static final String CHAT_PREFIX = ChatColor.AQUA + "[" + ChatColor.GREEN + "Eco" + ChatColor.AQUA + "] " + ChatColor.GREEN;

    private static Eco instance;
    private static Lang lang;

    private static int lastReloadErrors;

    @Override
    public void onEnable() {
        if (instance != null) {
            getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/eco reload\" instead.");
            return;
        }

        instance = this;
        lang = new Lang(new PluginConfig(this, "lang.yml"));

        CommandFramework.register(this, new EcoCommandHandler("eco"));
        CommandFramework.register(this, new PayCommandHandler("pay"));

        ErrorLogger errorLogger = new ErrorLogger();
        load(errorLogger);

        lastReloadErrors = errorLogger.getSize();
        if (errorLogger.hasErrors()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new ErrorLoggerTask(errorLogger), 10L);
        }
    }

    public void load(ErrorLogger errorLogger) {

        try {
            lang.load();
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

        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }
    }

    public static long getCopper(String cur, long amount) {
        if (cur.charAt(0) == 's') return amount * 100;
        if (cur.charAt(0) == 'g') return amount * 10000;
        return amount;
    }

    public static void addBalance(Player p, long amount) {
        if (p == null) return;
        long d = p.getMetadata("c").get(0).asInt() + amount;
        p.setMetadata("c", new FixedMetadataValue(Core.plugin, d));
    }

    public static void setBalance(Player player, long amount) {
        player.setMetadata("c", new FixedMetadataValue(Core.plugin, amount));
    }

    public static long[] getBalance(Player p) {
        long[] r = new long[3];
        r[0] = p.getMetadata("c").get(0).asLong();
        r[2] = r[0] / 10000;
        r[1] = r[0] / 100 % 100;
        r[0] %= 100;
        return r;
    }

    public static boolean hasMoney(Player player, String cur, long amount) {
        long i = player.getMetadata("c").get(0).asLong();
        long am = getCopper(cur, amount);
        return am <= i;
    }

    public static Eco getInstance() {
        return instance;
    }

//
//    public static Lang getLang() {
//        return lang;
//    }
//
//    public static int getLastReloadErrors() {
//        return lastReloadErrors;
//    }

    public static void setLastReloadErrors(int lastReloadErrors) {
        Eco.lastReloadErrors = lastReloadErrors;
    }

}

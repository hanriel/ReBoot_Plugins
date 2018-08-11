package com.skyandforest.reboot_core;

import com.skyandforest.reboot_core.util.Utils;
import org.bukkit.Bukkit;

public class Log {

    public static void debug(String s) {
        send(Utils.addColors("&7" + s));
    }

    public static void info(String s) {
        send(Utils.addColors("&f" + s));
    }

    public static void warning(String s) {
        send(Utils.addColors("&e" + s));
    }

    public static void error(String s) {
        send(Utils.addColors("&c" + s));
    }

    private static void send(String s){
        Bukkit.getConsoleSender().sendMessage("[Core Logger] " + s);
    }

}

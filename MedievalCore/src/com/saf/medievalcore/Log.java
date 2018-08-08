package com.saf.medievalcore;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class Log {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public Log() {
        Logger l = Core.plugin.getLogger();
        l.info(ANSI_RED + "Logger successfully loaded");
        success("Logger successfully loaded");
    }

    public static void success(String s) {
        Bukkit.getConsoleSender().sendMessage(Utils.addColors("&a" + s));
    }

    public static void info(String s) {
        Bukkit.getConsoleSender().sendMessage(Utils.addColors("&e" + s));
    }

    public static void warning(String s) {
        Bukkit.getConsoleSender().sendMessage(Utils.addColors("&c" + s));
    }

    public static void log(String s) {
        Bukkit.getConsoleSender().sendMessage(Utils.addColors("&f" + s));
    }
}

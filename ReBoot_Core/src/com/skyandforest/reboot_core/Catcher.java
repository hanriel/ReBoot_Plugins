package com.skyandforest.reboot_core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Catcher {

    public static boolean mnpc;
    public static boolean mcoins;
    public static boolean mgui;

    private static HashMap<String, Plugin> catchedPlugins = null;

    Catcher() {
        hook();
    }

    static void hook(){
        Log.info("Loading hooks...");
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

        for(Plugin pl : plugins){
            if(pl.getName().startsWith("RB_")){
                catchedPlugins.put(pl.getName(), pl);
            }

        }

        if (Bukkit.getPluginManager().isPluginEnabled("RBEconomy")) {
            mcoins = true;
            Log.info("MedievalEco hooked!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("RBNPC")) {
            mnpc = true;
            Log.info("MedievalNPC hooked!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("RBGUI")) {
            mgui = true;
            Log.info("MedievalGUI hooked!");
        }
    }

}

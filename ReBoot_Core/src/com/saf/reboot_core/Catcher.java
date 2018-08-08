package com.saf.reboot_core;

import org.bukkit.Bukkit;

class Catcher {
    static boolean mnpc;
    static boolean mcoins;
    static boolean mgui;

    static void hook(){
        Log.log("Loading hooks...");
        if (Bukkit.getPluginManager().isPluginEnabled("RBEco")) {
            mcoins = true;
            Log.success("MedievalEco hooked!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("RBNPC")) {
            mnpc = true;
            Log.success("MedievalNPC hooked!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("RBGUI")) {
            mgui = true;
            Log.success("MedievalGUI hooked!");
        }
    }

}

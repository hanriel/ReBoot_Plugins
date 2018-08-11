package com.skyandforest.reboot_npc;

import com.skyandforest.reboot_core.Log;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class NPC extends JavaPlugin {

    static Plugin plugin;
    static HashMap<String, Place> places = new HashMap<>();

    static DefNPC npc = null;
    static HashMap<Double[], EntityPlayer> npcs = new HashMap<>();

    @Override
    public void onEnable() {
        try {
            plugin = this;
            new _Listener();
            Bukkit.getPluginCommand("place").setExecutor(new _CommandExecutor());

            Log.info("The RB_NPC was successfully loaded!");
        } catch (Exception e) {
            Log.warning("Error loading plugin!!!");
            e.printStackTrace();
            super.setEnabled(false);
        }
    }
}

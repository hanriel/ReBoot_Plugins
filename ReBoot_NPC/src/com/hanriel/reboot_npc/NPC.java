package com.hanriel.reboot_npc;

import net.minecraft.server.v1_14_R1.EntityPlayer;
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

            getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/eco reload\" instead.");
        } catch (Exception e) {
            e.printStackTrace();
            super.setEnabled(false);
        }
    }
}

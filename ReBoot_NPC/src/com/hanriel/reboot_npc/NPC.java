package com.hanriel.reboot_npc;

import com.hanriel.reboot_core.command.CommandFramework;
import com.hanriel.reboot_core.config.YAML.PluginConfig;
import com.hanriel.reboot_npc.config.NpcList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.UUID;

public class NPC extends JavaPlugin {

    public static final String CHAT_PREFIX = "&e[&4NPC&e]&r ";

    private static NPC instance;
    private static NpcList npcList;

    @Override
    public void onEnable() {
        try {
            if (instance != null) return;
            instance = this;

            npcList = new NpcList(new PluginConfig(this, "npc_list.yml"));

            new _Listener();

            if (!EconomyBridge.setupEconomy()) {
                getLogger().warning("Плагин на экономику не найден!");
            }

            CommandFramework.register(this, new _CommandExecutor("npc"));

            load();

            if(npcList.getConfig().contains("data")){
                System.out.println("LOADING NPCS...");
                loadNPC();
            }
        } catch (Exception e) {
            e.printStackTrace();
            super.setEnabled(false);
        }
    }

    public void load() {
        try {
            npcList.load();
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().warning("I/O error while using the language file. Default values will be used.");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            getLogger().warning("The npc_list.yml was not a valid YAML, please look at the error above. Default values will be used.");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().warning("Unhandled error while reading the values for the configuration! Please inform the developer.");
        }
    }

    public static FileConfiguration getNPCList() {
        return npcList.getConfig();
    }

    public static void saveNPCList() {
        npcList.saveConfig();
    }

    public void loadNPC() {
        FileConfiguration file = npcList.getConfig();
        npcList.getConfig().getConfigurationSection("data").getKeys(false).forEach(npc -> {
            Location location = new Location(Bukkit.getWorld(file.getString("data." + npc + ".world")),
                    file.getDouble("data." + npc + ".x"),
                    file.getDouble("data." + npc + ".y"),
                    file.getDouble("data." + npc + ".z"));
            location.setPitch((float) file.getDouble("data." + npc + ".pitch"));
            location.setYaw((float) file.getDouble("data." + npc + ".yaw"));

            String name = file.getString("data." + npc + ".name");
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
            gameProfile.getProperties().put("textures", new Property("textures",
                    file.getString("data." + npc + ".text"), file.getString("data." + npc + ".sign")));

            DefNPC.loadNPC(location, gameProfile);
        });
    }

    public static NPC getInstance() {
        return instance;
    }
}

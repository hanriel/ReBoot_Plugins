package com.hanriel.theobsidianorder;

import com.hanriel.theobsidianorder.command.*;
import com.hanriel.theobsidianorder.config.Lang;
import com.hanriel.theobsidianorder.config.NpcList;
import com.hanriel.theobsidianorder.config.Settings;
import com.hanriel.theobsidianorder.config.YAML.PluginConfig;
import com.hanriel.theobsidianorder.util.MySQL;
import com.hanriel.theobsidianorder.playerData.PlayerDataFramework;
import com.hanriel.theobsidianorder.util.SerializableItemStack;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TheObsidianOrder extends JavaPlugin {

    public TabManager tab;

    private static TheObsidianOrder instance;
    private static Settings conf;
    private static Lang lang;
    private static NpcList npcList;

    public static List<ItemStack> iList;

    @Override
    public void onEnable() {
        if (instance != null) return;
        instance = this;

        conf = new Settings(new PluginConfig(this, "config.yml"));
        lang = new Lang(new PluginConfig(this, "lang.yml"));
        npcList = new NpcList(new PluginConfig(this, "npc_list.yml"));

//        this.tab = new TabManager(this);
//        tab.showTab();

        CommandFramework.register(this, new CoreCommandHandler("core"));
        CommandFramework.register(this, new EcoCommandHandler("eco"));
        CommandFramework.register(this, new PayCommandHandler("pay"));
        CommandFramework.register(this, new ShopCommandHandler("shop"));
        CommandFramework.register(this, new NpcCommandHandler("npc"));
        CommandFramework.register(this, new LinkCommandHandler("link"));

        getServer().getPluginManager().registerEvents(new EventListen(), instance);

        Bukkit.getServicesManager().register(TheObsidianOrder.class, this, this, ServicePriority.Normal);

        load();

        //new MySQL();

        if(npcList.getConfig().contains("data")){
            System.out.println("LOADING NPCS...");
            loadNPC();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerDataFramework.loadData(player);
        }
    }

    @Override
    public void onDisable() {
        try {
            saveObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasMetadata("c"))
                continue;
            PlayerDataFramework.uploadData(player);
        }
    }

    public void load() {

        List<SerializableItemStack> result = loadObject();
        iList = new ArrayList<>();
        if (result != null) {
            for (SerializableItemStack is : result) {
                iList.add(is.toItemStack());
            }
        }

        try {
            conf.load();
            lang.load();
            npcList.load();
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
    }

    public static TheObsidianOrder getInstance() {
        return instance;
    }

    public static Settings getConf() {
        return conf;
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

            NPC.loadNPC(location, gameProfile);
        });
    }

    public List<SerializableItemStack> loadObject() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("shopList.ser"));
            Object result = ois.readObject();
            ois.close();
            return (List<SerializableItemStack>) result;
        } catch (Exception e) {
            return null;
        }
    }

    public void saveObject() throws Exception {
        List<SerializableItemStack> isList = new ArrayList<>();
        for (ItemStack is : iList) {
            isList.add(new SerializableItemStack(is));
        }

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("shopList.ser"));
        oos.writeObject(isList);
        oos.flush();
        oos.close();
    }

    public static void closeAllSessions() {
//        for(Player player : Bukkit.getOnlinePlayers()){
//            player.kickPlayer(Shop.CHAT_PREFIX + "Shop system is reloading...");
//        }
    }

    public static Lang getLang() {
        return lang;
    }

    public static List<ItemStack> getiList() {
        return iList;
    }
}

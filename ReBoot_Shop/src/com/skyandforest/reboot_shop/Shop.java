package com.skyandforest.reboot_shop;

import com.skyandforest.reboot_shop.command.CommandFramework;
import com.skyandforest.reboot_shop.command.CommandHandler;
import com.skyandforest.reboot_shop.config.AsciiPlaceholders;
import com.skyandforest.reboot_shop.config.Lang;
import com.skyandforest.reboot_shop.config.Settings;
import com.skyandforest.reboot_shop.config.yaml.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shop extends JavaPlugin {

    public static final String CHAT_PREFIX = ChatColor.AQUA + "[" + ChatColor.GREEN + "Shop" + ChatColor.AQUA + "] " + ChatColor.GREEN;

    private static Shop instance;

    private static Settings settings;
    private static Lang lang;

    public static List<ItemStack> iList;
    public static HashMap<List, Character> playerSort = new HashMap<List, Character>();

    private static int lastReloadErrors;


    @Override
    public void onEnable() {

        if (instance != null) {
            getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/shop reload\" instead.");
            return;
        }

        instance = this;
        settings = new Settings(new PluginConfig(this, "config.yml"));
        lang = new Lang(new PluginConfig(this, "lang.yml"));

        List<SerializableItemStack> result = loadObject();
        iList = new ArrayList<>();
        if (result != null) {
            for (SerializableItemStack is : result) {
                iList.add(is.toItemStack());
            }
        }

        if (!EconomyBridge.setupEconomy()) {
            getLogger().warning("Vault with a compatible economy plugin was not found! Icons with a PRICE or commands that give money will not work.");
        }

        CommandFramework.register(this, new CommandHandler("shop"));
    }

    @Override
    public void onDisable() {

        try {
            saveObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public static Shop getInstance() {
        return instance;
    }

    public static int getLastReloadErrors() {
        return lastReloadErrors;
    }

    public static void setLastReloadErrors(int lastReloadErrors) {
        Shop.lastReloadErrors = lastReloadErrors;
    }

    public void load(ErrorLogger errorLogger) {
        //fileNameToMenuMap.clear();
        //commandsToMenuMap.clear();
        //boundItems.clear();

        try {
            settings.load();
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().warning("I/O error while using the configuration. Default values will be used.");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            getLogger().warning("The config.yml was not a valid YAML, please look at the error above. Default values will be used.");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().warning("Unhandled error while reading the values for the configuration! Please inform the developer.");
        }

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

        try {
            AsciiPlaceholders.load(errorLogger);
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().warning("I/O error while reading the placeholders. They will not work.");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().warning("Unhandled error while reading the placeholders! Please inform the developer.");
        }

        // Load the menus.
        File menusFolder = new File(getDataFolder(), "menu");

        if (!menusFolder.isDirectory()) {
            // Create the directory with the default menu.
            menusFolder.mkdirs();
            Utils.saveResourceSafe(this, "menu" + File.separator + "example.yml");
        }

//        List<PluginConfig> menusList = loadMenus(menusFolder);
//        for (PluginConfig menuConfig : menusList) {
//            try {
//                menuConfig.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//                errorLogger.addError("I/O error while loading the menu \"" + menuConfig.getFileName() + "\". Is the file in use?");
//                continue;
//            } catch (InvalidConfigurationException e) {
//                e.printStackTrace();
//                errorLogger.addError("Invalid YAML configuration for the menu \"" + menuConfig.getFileName() + "\". Please look at the error above, or use an online YAML parser (google is your friend).");
//                continue;
//            }
//
//            MenuData data = MenuSerializer.loadMenuData(menuConfig, errorLogger);
//            ExtendedIconMenu iconMenu = MenuSerializer.loadMenu(menuConfig, data.getTitle(), data.getRows(), errorLogger);
//
//            if (fileNameToMenuMap.containsKey(menuConfig.getFileName())) {
//                errorLogger.addError("Two menus have the same file name \"" + menuConfig.getFileName() + "\" with different cases. There will be problems opening one of these two menus.");
//            }
//            fileNameToMenuMap.put(menuConfig.getFileName(), iconMenu);
//
//            if (data.hasCommands()) {
//                for (String command : data.getCommands()) {
//                    if (!command.isEmpty()) {
//                        if (commandsToMenuMap.containsKey(command)) {
//                            errorLogger.addError("The menus \"" + commandsToMenuMap.get(command).getFileName() + "\" and \"" + menuConfig.getFileName() + "\" have the same command \"" + command + "\". Only one will be opened.");
//                        }
//                        commandsToMenuMap.put(command, iconMenu);
//                    }
//                }
//            }
//
//            iconMenu.setRefreshTicks(data.getRefreshTenths());
//
//            if (data.getOpenActions() != null) {
//                iconMenu.setOpenActions(data.getOpenActions());
//            }
//
//            if (data.hasBoundMaterial() && data.getClickType() != null) {
//                BoundItem boundItem = new BoundItem(iconMenu, data.getBoundMaterial(), data.getClickType());
//                if (data.hasBoundDataValue()) {
//                    boundItem.setRestrictiveData(data.getBoundDataValue());
//                }
//                boundItems.add(boundItem);
//            }
//        }

        // Register the BungeeCord plugin channel.
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }
    }

    public static Settings getSettings() {
        return settings;
    }

    public static Lang getLang() {
        return lang;
    }

    public static List<ItemStack> getiList() {
        return iList;
    }
}

//package com.skyandforest.reboot_auth;
//
//import com.skyandforest.reboot_auth.bridge.EconomyBridge;
//import com.skyandforest.reboot_auth.command.CommandFramework;
//import com.skyandforest.reboot_auth.command.CommandHandler;
//import com.skyandforest.reboot_auth.config.AsciiPlaceholders;
//import com.skyandforest.reboot_auth.config.Lang;
//import com.skyandforest.reboot_auth.config.yaml.PluginConfig;
//import com.skyandforest.reboot_auth.config.Settings;
//import com.skyandforest.reboot_auth.internal.ExtendedIconMenu;
//import com.skyandforest.reboot_auth.listener.JoinListener;
//import com.skyandforest.reboot_auth.task.ErrorLoggerTask;
//import com.skyandforest.reboot_auth.util.ErrorLogger;
//import com.skyandforest.reboot_auth.util.Utils;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.configuration.InvalidConfigurationException;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//import org.bukkit.plugin.java.JavaPlugin;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Auth extends JavaPlugin {
//
//    public static final String CHAT_PREFIX = ChatColor.AQUA + "[" + ChatColor.GREEN + "RBShop" + ChatColor.AQUA + "] " + ChatColor.GREEN;
//
//    private static Auth instance;
//    private static Settings settings;
//    private static Lang lang;
//
//    public static List<ItemStack> iList;
//
//    private static Map<String, ExtendedIconMenu> fileNameToMenuMap;
//    private static Map<String, ExtendedIconMenu> commandsToMenuMap;
//
//    private static int lastReloadErrors;
//
//    @Override
//    public void onEnable() {
//        if (instance != null) {
//            getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/Shop reload\" instead.");
//            return;
//        }
//
//        instance = this;
//        settings = new Settings(new PluginConfig(this, "config.yml"));
//        lang = new Lang(new PluginConfig(this, "lang.yml"));
//
//        List<SerializableItemStack> result = null;
//
//        try {
//            result =  (List<SerializableItemStack>) loadObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if(result == null) result = new ArrayList<>();
//
//        for(SerializableItemStack is : result){
//            iList.add(is.toItemStack());
//        }
//
//        if (!EconomyBridge.setupEconomy()) {
//            getLogger().warning("Vault with a compatible economy plugin was not found! Icons with a PRICE or commands that give money will not work.");
//        }
//
////        if (BarAPIBridge.setupPlugin()) {
////            getLogger().info("Hooked BarAPI");
////        }
////        if (PlayerPointsBridge.setupPlugin()) {
////            getLogger().info("Hooked PlayerPoints");
////        }
////        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
////        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
////        Bukkit.getPluginManager().registerEvents(new SignListener(), this);
//
//        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
//
//        CommandFramework.register(this, new CommandHandler("shop"));
//
//        ErrorLogger errorLogger = new ErrorLogger();
//        load(errorLogger);
//
//        lastReloadErrors = errorLogger.getSize();
//        if (errorLogger.hasErrors()) {
//            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new ErrorLoggerTask(errorLogger), 10L);
//        }
//    }
//
//    @Override
//    public void onDisable() {
//        try {
//            saveObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        closeAllSessions();
//    }
//
//    public Object loadObject() throws Exception {
//        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("shopList.ser"));
//        Object result = ois.readObject();
//        ois.close();
//        return result;
//    }
//
//    public void saveObject() throws Exception {
//        List<SerializableItemStack> isList = null;
//        for(ItemStack is : iList){
//            isList.add(new SerializableItemStack(is));
//        }
//
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("shopList.ser"));
//        oos.writeObject((Object)isList);
//        oos.flush();
//        oos.close();
//    }
//
//    public void load(ErrorLogger errorLogger) {
//        //fileNameToMenuMap.clear();
//        //commandsToMenuMap.clear();
//        //boundItems.clear();
//
////        try {
////            settings.load();
////        } catch (IOException e) {
////            e.printStackTrace();
////            getLogger().warning("I/O error while using the configuration. Default values will be used.");
////        } catch (InvalidConfigurationException e) {
////            e.printStackTrace();
////            getLogger().warning("The config.yml was not a valid YAML, please look at the error above. Default values will be used.");
////        } catch (Exception e) {
////            e.printStackTrace();
////            getLogger().warning("Unhandled error while reading the values for the configuration! Please inform the developer.");
////        }
////
////        try {
////            lang.load();
////        } catch (IOException e) {
////            e.printStackTrace();
////            getLogger().warning("I/O error while using the language file. Default values will be used.");
////        } catch (InvalidConfigurationException e) {
////            e.printStackTrace();
////            getLogger().warning("The lang.yml was not a valid YAML, please look at the error above. Default values will be used.");
////        } catch (Exception e) {
////            e.printStackTrace();
////            getLogger().warning("Unhandled error while reading the values for the configuration! Please inform the developer.");
////        }
////
////        try {
////            AsciiPlaceholders.load(errorLogger);
////        } catch (IOException e) {
////            e.printStackTrace();
////            getLogger().warning("I/O error while reading the placeholders. They will not work.");
////        } catch (Exception e) {
////            e.printStackTrace();
////            getLogger().warning("Unhandled error while reading the placeholders! Please inform the developer.");
////        }
//
//        // Load the menus.
//        File menusFolder = new File(getDataFolder(), "menu");
//
//        if (!menusFolder.isDirectory()) {
//            // Create the directory with the default menu.
//            menusFolder.mkdirs();
//            Utils.saveResourceSafe(this, "menu" + File.separator + "example.yml");
//        }
//
////        List<PluginConfig> menusList = loadMenus(menusFolder);
////        for (PluginConfig menuConfig : menusList) {
////            try {
////                menuConfig.load();
////            } catch (IOException e) {
////                e.printStackTrace();
////                errorLogger.addError("I/O error while loading the menu \"" + menuConfig.getFileName() + "\". Is the file in use?");
////                continue;
////            } catch (InvalidConfigurationException e) {
////                e.printStackTrace();
////                errorLogger.addError("Invalid YAML configuration for the menu \"" + menuConfig.getFileName() + "\". Please look at the error above, or use an online YAML parser (google is your friend).");
////                continue;
////            }
////
////            MenuData data = MenuSerializer.loadMenuData(menuConfig, errorLogger);
////            ExtendedIconMenu iconMenu = MenuSerializer.loadMenu(menuConfig, data.getTitle(), data.getRows(), errorLogger);
////
////            if (fileNameToMenuMap.containsKey(menuConfig.getFileName())) {
////                errorLogger.addError("Two menus have the same file name \"" + menuConfig.getFileName() + "\" with different cases. There will be problems opening one of these two menus.");
////            }
////            fileNameToMenuMap.put(menuConfig.getFileName(), iconMenu);
////
////            if (data.hasCommands()) {
////                for (String command : data.getCommands()) {
////                    if (!command.isEmpty()) {
////                        if (commandsToMenuMap.containsKey(command)) {
////                            errorLogger.addError("The menus \"" + commandsToMenuMap.get(command).getFileName() + "\" and \"" + menuConfig.getFileName() + "\" have the same command \"" + command + "\". Only one will be opened.");
////                        }
////                        commandsToMenuMap.put(command, iconMenu);
////                    }
////                }
////            }
////
////            iconMenu.setRefreshTicks(data.getRefreshTenths());
////
////            if (data.getOpenActions() != null) {
////                iconMenu.setOpenActions(data.getOpenActions());
////            }
////
////            if (data.hasBoundMaterial() && data.getClickType() != null) {
////                BoundItem boundItem = new BoundItem(iconMenu, data.getBoundMaterial(), data.getClickType());
////                if (data.hasBoundDataValue()) {
////                    boundItem.setRestrictiveData(data.getBoundDataValue());
////                }
////                boundItems.add(boundItem);
////            }
////        }
//
//        // Register the BungeeCord plugin channel.
//        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
//            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
//        }
//    }
//
//    public static void closeAllSessions() {
//        for(Player player : Bukkit.getOnlinePlayers()){
//            player.kickPlayer(Shop.CHAT_PREFIX + "Shop system is reloading...");
//        }
//    }
//
//    public static Map<String, ExtendedIconMenu> getFileNameToMenuMap() {
//        return fileNameToMenuMap;
//    }
//
//    public static Map<String, ExtendedIconMenu> getCommandToMenuMap() {
//        return commandsToMenuMap;
//    }
//
//
//    public static Shop getInstance() {
//        return instance;
//    }
//
//    public static Settings getSettings() {
//        return settings;
//    }
//
//    public static Lang getLang() {
//        return lang;
//    }
//
//    public static int getLastReloadErrors() {
//        return lastReloadErrors;
//    }
//
//    public static void setLastReloadErrors(int lastReloadErrors) {
//        Shop.lastReloadErrors = lastReloadErrors;
//    }
//
//    public static List<ItemStack> getItemsList() {
//        return iList;
//    }
//
//    public static void setItemsList(List<ItemStack> a) {
//        iList = a;
//    }
//
//
//}

package com.skyandforest.reboot_core;

import com.skyandforest.reboot_core.command.CommandFramework;
import com.skyandforest.reboot_core.command.CoreCommandHandler;
import com.skyandforest.reboot_core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Core extends JavaPlugin {

    public static final String CHAT_PREFIX = ChatColor.AQUA + "[" + ChatColor.GREEN + "Core" + ChatColor.AQUA + "] " + ChatColor.GREEN;

    private static Core instance;

    private static int lastReloadErrors;

    @Override
    public void onEnable() {
        if (instance != null) {
            Log.warning("Please do not use /reload or plugin reloaders. Do \"/rb reload\" instead.");
            return;
        }
        instance = this;

        CommandFramework.register(this, new CoreCommandHandler("core"));
        Core.getInstance().getServer().getPluginManager().registerEvents(new _Listener(), Core.getInstance());

        new Catcher();


        File f = new File(instance.getDataFolder() + "\\login.log");
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
        try {
            Files.write(
                    Paths.get(instance.getDataFolder() + "\\login.log"),
                    ("\n=====ServerStarted===@" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy@hh.mm.ss"))).getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ErrorLogger errorLogger = new ErrorLogger();
        load(errorLogger);

        lastReloadErrors = errorLogger.getSize();
        if (errorLogger.hasErrors()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new ErrorLoggerTask(errorLogger), 10L);
        }
    }

    @Override
    public void onDisable() {
        try {
            Files.write(Paths.get(instance.getDataFolder() + "\\login.log"), ("\n=====ServerClosed====@" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy@hh.mm.ss"))).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Log.warning("Error writing \"loginStatistic.log\"!!!");
            e.printStackTrace();
        }
    }

    public void load(ErrorLogger errorLogger) {
        // Load the menus.
        File playersFolder = new File(getDataFolder(), "players");

        if (!playersFolder.isDirectory()) {
            playersFolder.mkdirs();
        }

        List<PluginConfig> menusList = loadMenus(menusFolder);
        for (PluginConfig menuConfig : menusList) {
            try {
                menuConfig.load();
            } catch (IOException e) {
                e.printStackTrace();
                errorLogger.addError("I/O error while loading the menu \"" + menuConfig.getFileName() + "\". Is the file in use?");
                continue;
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
                errorLogger.addError("Invalid YAML configuration for the menu \"" + menuConfig.getFileName() + "\". Please look at the error above, or use an online YAML parser (google is your friend).");
                continue;
            }

            MenuData data = MenuSerializer.loadMenuData(menuConfig, errorLogger);
            ExtendedIconMenu iconMenu = MenuSerializer.loadMenu(menuConfig, data.getTitle(), data.getRows(), errorLogger);

            if (fileNameToMenuMap.containsKey(menuConfig.getFileName())) {
                errorLogger.addError("Two menus have the same file name \"" + menuConfig.getFileName() + "\" with different cases. There will be problems opening one of these two menus.");
            }
            fileNameToMenuMap.put(menuConfig.getFileName(), iconMenu);

            if (data.hasCommands()) {
                for (String command : data.getCommands()) {
                    if (!command.isEmpty()) {
                        if (commandsToMenuMap.containsKey(command)) {
                            errorLogger.addError("The menus \"" + commandsToMenuMap.get(command).getFileName() + "\" and \"" + menuConfig.getFileName() + "\" have the same command \"" + command + "\". Only one will be opened.");
                        }
                        commandsToMenuMap.put(command, iconMenu);
                    }
                }
            }

            iconMenu.setRefreshTicks(data.getRefreshTenths());

            if (data.getOpenActions() != null) {
                iconMenu.setOpenActions(data.getOpenActions());
            }

            if (data.hasBoundMaterial() && data.getClickType() != null) {
                BoundItem boundItem = new BoundItem(iconMenu, data.getBoundMaterial(), data.getClickType());
                if (data.hasBoundDataValue()) {
                    boundItem.setRestrictiveData(data.getBoundDataValue());
                }
                boundItems.add(boundItem);
            }
        }

        // Register the BungeeCord plugin channel.
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }
    }


//    Future loadPlayerData()
//    private List<PluginConfig> loadMenus(File file) {
//        List<PluginConfig> list = Utils.newArrayList();
//        if (file.isDirectory()) {
//            for (File subFile : file.listFiles()) {
//                list.addAll(loadMenus(subFile));
//            }
//        } else if (file.isFile()) {
//            if (file.getName().endsWith(".yml")) {
//                list.add(new PluginConfig(this, file));
//            }
//        }
//        return list;
//    }

    static JSONObject loadPlayerData(String uuid) {
        try {
            Scanner sc = new Scanner(new File(instance.getDataFolder() + "\\players\\" + uuid + ".json"));
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) sb.append(sc.next());
            sc.close();
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(sb.toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void savePlayerData(HashMap<String, Object> a, String b) {
        JSONObject c = new JSONObject();
        LocalDateTime currentTime = LocalDateTime.now();
        c.put("last date", currentTime.toString());
        for (Map.Entry<String, Object> entry : a.entrySet()) c.put(entry.getKey(), entry.getValue());
        try (FileOutputStream d = new FileOutputStream(instance.getDataFolder() + "\\players\\" + b + ".json")) {
            d.write(c.toJSONString().getBytes());
            d.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void createPlayerData(String uuid) {
        HashMap<String, Object> saveData = new HashMap<>();
        saveData.put("h", 20);//Health
        saveData.put("m", 20);//Max health
        if (Catcher.mcoins) {
            saveData.put("d", 0);//Diamond
            saveData.put("c", 0);//Copper
            saveData.put("lvl", 1); //Current lvl
            saveData.put("exp", 0); //Current exp
        }
        if (Catcher.mgui) {

        }
        if (Catcher.mnpc) {

        }

        savePlayerData(saveData, uuid);
    }

    static boolean hasPlayerData(String uuid) {
        return new File(instance.getDataFolder() + "\\players\\" + uuid + ".json").exists();
    }

    static void writeDataLogin(String displayname, Character s) {
        try {
            Files.write(Paths.get(instance.getDataFolder() + "\\login.log"), ("\r\n" + s + "@" + displayname + "@" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy@hh.mm.ss"))).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Log.warning("Error writing \"loginStatistic.log\"!!!");
            e.printStackTrace();
        }
    }

    public static Core getInstance() {
        return instance;
    }

    public static void setLastReloadErrors(int lastReloadErrors) {
        Core.lastReloadErrors = lastReloadErrors;
    }
}

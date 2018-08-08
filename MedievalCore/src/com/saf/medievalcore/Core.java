package com.saf.medievalcore;

import org.bukkit.Bukkit;
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

    public static Core plugin;

    @Override
    public void onEnable() {
        try {
            plugin = this;

            new _Listener();
            iniCmds();
            Catcher.hook();
            File f =  new File(plugin.getDataFolder() + "\\login.log");
            if(!f.exists()){
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            try {
                Files.write(Paths.get(plugin.getDataFolder() + "\\login.log"), ("\n=====ServerStarted===@" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy@hh.mm.ss"))).getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e) {
                Log.warning("Error writing \"loginStatistic.log\"!!!");
                e.printStackTrace();
            }
            f =  new File(plugin.getDataFolder() + "\\players");
            if (!f.exists()) { f.mkdir(); }

            Log.success("The " + getName() + " was successfully loaded!" + plugin.getDescription().getVersion());
        } catch (Exception e) {
            Log.warning("Error loading plugin!!!");
            e.printStackTrace();
            super.setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        try {
            Files.write(Paths.get(plugin.getDataFolder() + "\\login.log"), ("\n=====ServerClosed====@" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy@hh.mm.ss"))).getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            Log.warning("Error writing \"loginStatistic.log\"!!!");
            e.printStackTrace();
        }
    }

    static JSONObject loadPlayerData(String uuid) {
        try {
            Scanner sc = new Scanner(new File(plugin.getDataFolder() + "\\players\\" + uuid + ".json"));
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
        try (FileOutputStream d = new FileOutputStream(plugin.getDataFolder() + "\\players\\" + b + ".json")) {
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
        }        /*if (Catcher.minv) {
            Log.warning("КАКОГО ХУЯ ИНВЕНТАРЬ ПОДГРУЖЕН?");
        }
        if (Catcher.mnpc) {
            //Log.warning("КАКОГО ХУЯ ИНВЕНТАРЬ ПОДГРУЖЕН?");
        }*/

        savePlayerData(saveData, uuid);
    }

    static boolean hasPlayerData(String uuid) {
        return new File(plugin.getDataFolder() + "\\players\\" + uuid + ".json").exists();
    }

    private void iniCmds() {
        //HashMap<Integer, String> cmd = new HashMap<>();
        //cmd.put(1, "medieval");
        //_CommandExecutor c = new _CommandExecutor();
        //for (HashMap.Entry cm : cmd.entrySet()) Bukkit.getPluginCommand(cm.getValue().toString()).setExecutor(c);
        Bukkit.getPluginCommand("medieval").setExecutor(new _CommandExecutor());
    }

    static void writeDataLogin(String displayname, Character s){
        try {
            Files.write(Paths.get(plugin.getDataFolder() + "\\login.log"), ("\r\n"+s+"@" + displayname + "@" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy@hh.mm.ss"))).getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            Log.warning("Error writing \"loginStatistic.log\"!!!");
            e.printStackTrace();
        }
    }
}

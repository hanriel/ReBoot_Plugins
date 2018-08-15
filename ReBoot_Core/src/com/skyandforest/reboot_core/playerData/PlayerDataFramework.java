package com.skyandforest.reboot_core.playerData;

import com.skyandforest.reboot_core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PlayerDataFramework {

    public static void loadData(Player player) {
        File file = new File(Core.getInstance().getDataFolder() + "\\players\\" + player.getUniqueId().toString());
        JSONObject jsonObject;
        try {
            Scanner sc = new Scanner(file);
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) sb.append(sc.next());
            sc.close();
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(sb.toString());

            player.setMetadata("c", new FixedMetadataValue(Core.getInstance(), jsonObject.get("c")));
            player.setMetadata("d", new FixedMetadataValue(Core.getInstance(), jsonObject.get("d")));
        } catch (IOException | ParseException ignored) {
        }

        MySQL mySQL = new MySQL();
        mySQL.connect();
        ResultSet resultSet = mySQL.query(SQLQuerys.GET_USER_DATA + player.getUniqueId().toString());
        try {
            if(resultSet.next()){
                player.setMetadata("c", new FixedMetadataValue(Core.getInstance(), resultSet.getInt(1)));
                player.setMetadata("d", new FixedMetadataValue(Core.getInstance(), resultSet.getInt(2)));
            }
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    public static void saveData(HashMap<String, Object> a, Player player) {
        JSONObject jsonObject = new JSONObject();
        LocalDateTime currentTime = LocalDateTime.now();
        jsonObject.put("last date", currentTime.toString());

        for (Map.Entry<String, Object> entry : a.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }

        String path = Core.getInstance().getDataFolder() + "\\players\\" + player.getUniqueId().toString();
        try (FileOutputStream d = new FileOutputStream(path)) {
            d.write(jsonObject.toJSONString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createData(Player player) {
        player.setMetadata("c", new FixedMetadataValue(Core.getInstance(), 0));
        player.setMetadata("d", new FixedMetadataValue(Core.getInstance(), 0));

        HashMap<String, Object> saveData = new HashMap<>();
        saveData.put("d", 0);
        saveData.put("c", 0);
        saveData(saveData, player);
    }

    public static boolean hasData(Player player) {
        return new File(Core.getInstance().getDataFolder() + "\\players\\" + player.getUniqueId().toString()).exists();
    }

}

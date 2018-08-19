package com.skyandforest.reboot_core.playerData;

import com.skyandforest.reboot_core.Core;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.sql.*;
import java.util.HashMap;

public class PlayerDataFramework {

    private static MySQL mySQL =  new MySQL();

    public static void loadData(Player player) {
        try {
            ResultSet resultSet = mySQL.query(SQLQuerys.GET_USR + player.getUniqueId().toString()+"'");
            if(resultSet != null) {
                if(resultSet.next()){
                    player.setMetadata("c", new FixedMetadataValue(Core.getInstance(), resultSet.getInt(1)));
                    player.setMetadata("d", new FixedMetadataValue(Core.getInstance(), resultSet.getInt(2)));
                } else createData(player);
            } else createData(player);
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    private static void saveData(HashMap<String, Object> a, Player player) {
        try {
            String statement = "INSERT INTO `user` (`uuid`, `name`, `units`, `bits`) VALUES ('" + player.getUniqueId().toString() + "', '" +
                    player.getDisplayName() + "', '0', '0') ON DUPLICATE KEY UPDATE `units`=" +
                    a.get("c") + ",`bits`=" + a.get("d");

            Statement st = mySQL.getConnection().createStatement();
            st.executeUpdate(statement);
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    private static void createData(Player player) {
        player.setMetadata("c", new FixedMetadataValue(Core.getInstance(), 0));
        player.setMetadata("d", new FixedMetadataValue(Core.getInstance(), 0));

        HashMap<String, Object> saveData = new HashMap<>();
        saveData.put("d", 0);
        saveData.put("c", 0);
        saveData(saveData, player);
    }

    public static void downloadData(Player player) {
        try {
            ResultSet resultSet = mySQL.query(SQLQuerys.GET_USR + player.getUniqueId().toString()+"'");
            if(resultSet != null) {
                if(resultSet.next()){
                    player.setMetadata("c", new FixedMetadataValue(Core.getInstance(), resultSet.getInt(1)));
                    player.setMetadata("d", new FixedMetadataValue(Core.getInstance(), resultSet.getInt(2)));
                }
            }
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    public static void uploadData(Player player) {
        try {
            String statement = "UPDATE `user` SET `units`=" + player.getMetadata("c").get(0).asLong() +
                    ",`bits`=" + player.getMetadata("d").get(0).asLong();
            Statement st = mySQL.getConnection().createStatement();
            st.executeUpdate(statement);
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

}

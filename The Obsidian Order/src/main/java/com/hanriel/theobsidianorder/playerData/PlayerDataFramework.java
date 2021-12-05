package com.hanriel.theobsidianorder.playerData;

import com.hanriel.theobsidianorder.TheObsidianOrder;
import com.hanriel.theobsidianorder.util.MySQL;
import com.hanriel.theobsidianorder.util.SQLQuerys;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class PlayerDataFramework {

    private static MySQL mySQL = new MySQL();

    public static void loadData(Player player) {
        try {
            ResultSet resultSet = mySQL.query(SQLQuerys.GET_USR + player.getUniqueId() + "'");
            if (resultSet != null) {
                if (resultSet.next()) {
                    player.setMetadata("c", new FixedMetadataValue(TheObsidianOrder.getInstance(), resultSet.getInt(1)));
                } else createData(player);
            } else createData(player);
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    private static void saveData(HashMap<String, Object> a, Player player) {
        try {
            String statement = "INSERT INTO `users` (`uuid`, `name`, `units`) VALUES ('" + player.getUniqueId().toString() + "', '" +
                    player.getDisplayName() + "', '0') ON DUPLICATE KEY UPDATE `units`=" +
                    a.get("c");
            Statement st = mySQL.getConnection().createStatement();
            st.executeUpdate(statement);
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    private static void createData(Player player) {
        player.setMetadata("c", new FixedMetadataValue(TheObsidianOrder.getInstance(), 0));

        HashMap<String, Object> saveData = new HashMap<>();
        saveData.put("c", 100);
        saveData(saveData, player);
    }

    public static void downloadData(Player player) {
        try {
            ResultSet resultSet = mySQL.query(SQLQuerys.GET_USR + player.getUniqueId().toString() + "'");
            if (resultSet != null) {
                if (resultSet.next()) {
                    player.setMetadata("c", new FixedMetadataValue(TheObsidianOrder.getInstance(), resultSet.getInt(1)));
                }
            }
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    public static void uploadData(Player player) {
        try {
            String statement = "UPDATE `users` SET `units`=" + player.getMetadata("c").get(0).asLong() + " WHERE `uuid`='" + player.getUniqueId().toString() + "'";
            Statement st = mySQL.getConnection().createStatement();
            st.executeUpdate(statement);
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

}

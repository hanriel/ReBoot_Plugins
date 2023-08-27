package org.hanriel.toocore.playerData;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.hanriel.toocore.TOO_CORE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class PlayerDataFramework {

    private static final MySQL mySQL =  new MySQL();

    public static void loadData(Player player) {
        try {
            ResultSet resultSet = mySQL.query(SQLQuery.GET_USR + player.getUniqueId().toString()+"'");
            if(resultSet != null) {
                if(resultSet.next()){
                    player.setMetadata("c", new FixedMetadataValue(TOO_CORE.getInstance(), resultSet.getInt(1)));
                    player.setMetadata("d", new FixedMetadataValue(TOO_CORE.getInstance(), resultSet.getInt(2)));
                } else createData(player);
            } else createData(player);
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    private static void saveData(HashMap<String, Object> a, Player player) {
        try {
            String statement = "INSERT INTO `too_statistics` (`uuid`, `seconds`) VALUES ('"
                    + player.getUniqueId().toString() + "', '0') ON DUPLICATE KEY UPDATE `seconds`=" + a.get("seconds");

            Statement st = mySQL.getConnection().createStatement();
            st.executeUpdate(statement);
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    private static void createData(Player player) {
        player.setMetadata("c", new FixedMetadataValue(TOO_CORE.getInstance(), 0));
        player.setMetadata("d", new FixedMetadataValue(TOO_CORE.getInstance(), 0));

        HashMap<String, Object> saveData = new HashMap<>();
        saveData.put("seconds", player.getStatistic(Statistic.RECORD_PLAYED));
        saveData(saveData, player);
    }

    public static void downloadData(Player player) {
        try {
            ResultSet resultSet = mySQL.query(SQLQuery.GET_USR + player.getUniqueId().toString()+"'");
            if(resultSet != null) {
                if(resultSet.next()){
                    player.setMetadata("c", new FixedMetadataValue(TOO_CORE.getInstance(), resultSet.getInt(1)));
                    player.setMetadata("d", new FixedMetadataValue(TOO_CORE.getInstance(), resultSet.getInt(2)));
                }
            }
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

    public static void uploadData(Player player) {
        try {
            //String statement = "UPDATE `users` SET `units`=" + player.getMetadata("c").get(0).asLong() + ",`bits`=" + player.getMetadata("d").get(0).asLong()+" WHERE `uuid`='"+ player.getUniqueId().toString()+"'";
            String statement = "UPDATE `too_statistics` SET `seconds`=" + player.getStatistic(Statistic.PLAY_ONE_MINUTE)+" WHERE `uuid`='"+ player.getUniqueId().toString()+"'";
            Statement st = mySQL.getConnection().createStatement();
            st.executeUpdate(statement);
        } catch (SQLException e) {
            mySQL.runException(e);
        }
    }

}

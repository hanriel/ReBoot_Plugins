package com.hanriel.toohub;

import com.hanriel.toohub.util.Utils;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.sql.Statement;

public class EventListen implements Listener {

    private static MySQL mySQL = new MySQL();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(Utils.addColors("&a" + player.getName() + " &rприсоединился к игре"));
        player.sendTitle(Utils.addColors("&6&lThe &5&lObsidian &6&lOrder"), Utils.addColors("&aванильный Minecraft сервер"), 20, 80, 20);
    }

    @EventHandler
    public boolean onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(Utils.addColors("&c" + player.getName() + " &rпокинул игру"));

        if (Main.getInstance().config.getBoolean("stats.enable")) {
            try {
                String statement = "INSERT INTO `too_stats` (`displayname`, `gameSeconds`) VALUES ('" + player.getName() + "', '0') ON DUPLICATE KEY UPDATE `gameSeconds`=" +
                        player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 1200;
                Statement st = mySQL.getConnection().createStatement();
                st.executeUpdate(statement);
            } catch (SQLException e) {
                mySQL.runException(e);
            }
        }

        return true;
    }
}

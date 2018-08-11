package com.skyandforest.reboot_core;

import com.skyandforest.reboot_core.playerData.PlayerDataFramework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class _Listener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!PlayerDataFramework.hasData(player)) {
            PlayerDataFramework.createData(player);
        } else {
            PlayerDataFramework.loadData(player);
        }
    }

    @EventHandler
    public boolean onPlayerQuit(PlayerQuitEvent e) {
        if (!e.getPlayer().hasMetadata("c")) {
            return false;
        }

        Player player = e.getPlayer();
        HashMap<String, Object> data = new HashMap<>();

        data.put("c", player.getMetadata("c").get(0).asLong());
        data.put("d", player.getMetadata("d").get(0).asLong());

        PlayerDataFramework.saveData(data, player);
        return true;
    }
}

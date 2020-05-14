package com.hanriel.reboot_core;

import com.hanriel.reboot_core.playerData.PlayerDataFramework;
import com.hanriel.reboot_core.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class _Listener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(Utils.addColors("&a" + player.getName() + " &rприсоединился к игре"));
        PlayerDataFramework.loadData(player);
    }

    @EventHandler
    public boolean onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(Utils.addColors("&c" + player.getName() + " &rпокинул игру"));
        if (!player.hasMetadata("c")) {
            return false;
        }

        PlayerDataFramework.uploadData(player);
        return true;
    }
}

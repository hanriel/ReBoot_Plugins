package com.skyandforest.reboot_core;

import com.skyandforest.reboot_core.playerData.PlayerDataFramework;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class _Listener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(player.getDisplayName() + " присоеденился к игре");
        PlayerDataFramework.loadData(player);
    }

    @EventHandler
    public boolean onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(player.getDisplayName() + " присоеденился к игре");
        if (!player.hasMetadata("c")) {
            return false;
        }

        PlayerDataFramework.uploadData(player);
        return true;
    }
}

package com.hanriel.theobsidianorder;

import com.hanriel.theobsidianorder.playerData.PlayerDataFramework;
import com.hanriel.theobsidianorder.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListen implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(Utils.addColors("&a" + player.getName() + " &rприсоединился к игре"));
        player.sendTitle(Utils.addColors("&6&lThe &5&lObsidian &6&lOrder"), Utils.addColors("&aванильный Minecraft сервер"), 20, 80, 20);
        PlayerDataFramework.loadData(player);

        if (NPC.getNPCs() == null)
            return;
        if (NPC.getNPCs().isEmpty())
            return;
        NPC.addJoinPacket(player);

        PacketReader reader = new PacketReader();
        reader.inject(player);
    }

    @EventHandler
    public boolean onPlayerQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();
        e.setQuitMessage(Utils.addColors("&c" + player.getName() + " &rпокинул игру"));
        if (!player.hasMetadata("c")) {
            return false;
        }

        PlayerDataFramework.uploadData(player);

        PacketReader reader = new PacketReader();
        reader.uninject(player);

        return true;
    }

    @EventHandler
    public void onPlayerRcNPC(RightClickNPC e) {
        Exchanger ex = new Exchanger();
        ex.showExchanger(e.getPlayer());
    }

}
